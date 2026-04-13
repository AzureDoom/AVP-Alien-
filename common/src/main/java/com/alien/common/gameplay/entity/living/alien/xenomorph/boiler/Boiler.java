package com.alien.common.gameplay.entity.living.alien.xenomorph.boiler;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.ai.BoilerGOAP;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.tag.AlienBlockTags;
import com.alien.common.util.AcidBleedUtil;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.entity.v1.vibration.VibrationSystemManager;
import com.blib.api.common.goap.v1.GOAPUser;
import com.blib.api.common.pathfinding.v1.cache.TerrainCacheRegistry;
import com.blib.api.common.pathfinding.v1.evaluator.TerrainEvaluatorConfig;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigator;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorConfig;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorUser;
import com.blib.api.common.pathfinding.v1.search.SearchConfig;
import com.blib.api.common.pathfinding.v1.terrain.BlockBreakabilityEvaluators;
import com.blib.api.common.pathfinding.v1.terrain.TerrainClassifiers;
import com.blib.api.common.pathfinding.v1.terrain.TerrainType;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Boiler extends Xenomorph implements GOAPUser<Boiler>, PathNavigatorUser {

    public static AttributeSupplier.Builder createBoilerAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 2.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 0f)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 0.25F)
            .add(Attributes.FOLLOW_RANGE, 16F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.3f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 2F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1F);
    }

    private static final float MAX_BREAKABLE_DESTROY_TIME = 6.0F;

    private final BoilerAnimationDispatcher animationDispatcher;

    private final VibrationSystemManager vibrationSystemManager;

    private final BoilerData boilerData;

    private final PathNavigator pathNavigator;

    public Boiler(EntityType<? extends Boiler> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new BoilerAnimationDispatcher(this);
        this.vibrationSystemManager = new VibrationSystemManager(this, 2.5F, 32);
        this.boilerData = new BoilerData();
        this.pathNavigator = createPathNavigator(level);
    }

    private PathNavigator createPathNavigator(Level level) {
        var evaluatorConfig = TerrainEvaluatorConfig.builder()
            .addTerrain(TerrainType.GROUND, 1.0f)
            .addTerrain(TerrainType.WATER, 4.0f)
            .addTerrain(TerrainType.BREAKABLE, 8.0f)
            .withTerrainClassifier(TerrainClassifiers.GROUND_AND_WATER)
            .withBreakabilityEvaluator(
                BlockBreakabilityEvaluators.withExcludedTag(
                    BlockBreakabilityEvaluators.defaultEvaluator(MAX_BREAKABLE_DESTROY_TIME),
                    AlienBlockTags.XENOMORPH_IMMUNE
                )
            )
            .withEntitySize(1, 2)
            .withMaxFallDistance(14)
            .withCanOpenDoors(true)
            .build();

        var followRange = (float) getAttributeValue(Attributes.FOLLOW_RANGE);

        var navigatorConfig = PathNavigatorConfig.builder(evaluatorConfig)
            .withSearchConfig(SearchConfig.fromFollowRange(followRange))
            .build();

        var classificationCache = TerrainCacheRegistry.getOrCreate(level, evaluatorConfig.getTerrainClassifier());

        return new PathNavigator(level, navigatorConfig, classificationCache);
    }

    @Override
    public Agent.Builder<Boiler> blib$applyGOAPAgentProperties(Agent.Builder<Boiler> agentBuilder) {
        return BoilerGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<Boiler> blib$getGOAPGraphOrNull() {
        return BoilerGOAP.GRAPH;
    }

    @Override
    public PathNavigator getPathNavigator() {
        return pathNavigator;
    }

    @Override
    public void tick() {
        super.tick();
        vibrationSystemManager.tick();
        boilerData.tick();
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumer) {
        vibrationSystemManager.updateDynamicGameEventListener(biConsumer);
    }

    @Override
    public @Nullable EntityType<? extends Alien> getTypeForVariant(AlienVariant alienVariant) {
        return getType(alienVariant);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        var radius = 2F;
        level().explode(this, getX(), getY(), getZ(), radius, Level.ExplosionInteraction.MOB);
        triggerOnDeathMobEffects(RemovalReason.KILLED);
        discard();

        getBlockArea(blockPosition(), (int) radius, (int) radius, (int) radius)
            .stream()
            .filter(blockPos -> {
                var blockState = level().getBlockState(blockPos);
                return blockState.isAir() || blockState.canBeReplaced();
            })
            .forEach(blockPos -> AcidBleedUtil.spawnAcid(this, 3, blockPos.getCenter()));

        return true;
    }

    private List<BlockPos> getBlockArea(BlockPos center, int radiusX, int radiusY, int radiusZ) {
        var positions = new ArrayList<BlockPos>();

        for (var dx = -radiusX; dx <= radiusX; dx++) {
            for (var dy = -radiusY; dy <= radiusY; dy++) {
                for (var dz = -radiusZ; dz <= radiusZ; dz++) {
                    positions.add(center.offset(dx, dy, dz));
                }
            }
        }

        return positions;
    }

    @Override
    public boolean isAttacking() {
        return false;
    }

    @Override
    protected void resetAttackType() {}

    @Override
    public void runAttackAnimations() {}

    @Override
    protected float getHealthRegenPerSecond() {
        return 0.5F;
    }

    public BoilerAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public VibrationSystemManager getVibrationSystemManager() {
        return vibrationSystemManager;
    }

    public BoilerData getBoilerData() {
        return boilerData;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.BOILER.get();
            case NETHER -> AlienEntityTypes.NETHER_BOILER.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_BOILER.get();
            case IRRADIATED -> null;
        };
    }
}

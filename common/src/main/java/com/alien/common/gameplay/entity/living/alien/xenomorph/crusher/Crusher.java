package com.alien.common.gameplay.entity.living.alien.xenomorph.crusher;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.crusher.ai.CrusherGOAP;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienBlockTags;
import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.entity.v1.PlayerStatConstants;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Crusher extends Xenomorph implements GOAPUser<Crusher>, PathNavigatorUser {

    public static AttributeSupplier.Builder createCrusherAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 12.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 12.0F)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 0.75F)
            .add(Attributes.FOLLOW_RANGE, 35F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.7f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 5F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1.2F);
    }

    private static final float MAX_BREAKABLE_DESTROY_TIME = 6.0F;

    public final DataAccessor<CrusherAttackType> attackType;

    private final CrusherAnimationDispatcher animationDispatcher;

    private final PathNavigator pathNavigator;

    public Crusher(EntityType<? extends Crusher> entityType, Level level) {
        super(entityType, level);
        this.attackType = new DataAccessor<>(this, AlienDataSyncKeys.CRUSHER_ATTACK_TYPE.get());
        this.animationDispatcher = new CrusherAnimationDispatcher(this);
        this.pathNavigator = createPathNavigator(level);
        getXenomorphData().setParallelDigCount(2);
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
            .withEntitySize(2, 2)
            .withMaxFallDistance(14)
            .withCanOpenDoors(false)
            .build();

        var followRange = (float) getAttributeValue(Attributes.FOLLOW_RANGE);

        var navigatorConfig = PathNavigatorConfig.builder(evaluatorConfig)
            .withSearchConfig(SearchConfig.fromFollowRange(followRange))
            .build();

        var classificationCache = TerrainCacheRegistry.getOrCreate(level, evaluatorConfig.getTerrainClassifier());

        return new PathNavigator(level, navigatorConfig, classificationCache);
    }

    @Override
    public Agent.Builder<Crusher> blib$applyGOAPAgentProperties(Agent.Builder<Crusher> agentBuilder) {
        return CrusherGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<Crusher> blib$getGOAPGraphOrNull() {
        return getActiveGOAPGraph(CrusherGOAP.GRAPH);
    }

    @Override
    public PathNavigator getPathNavigator() {
        return pathNavigator;
    }

    @Override
    public @Nullable EntityType<? extends Alien> getTypeForVariant(AlienVariant alienVariant) {
        return getType(alienVariant);
    }

    @Override
    public boolean isAttacking() {
        return attackType.get() != CrusherAttackType.NONE;
    }

    @Override
    protected void resetAttackType() {
        attackType.set(CrusherAttackType.NONE);
    }

    @Override
    public void runAttackAnimations() {
        var isClawAttack = random.nextBoolean();

        playSound(
            AlienSoundEvents.ENTITY_XENOMORPH_ATTACK.get(),
            getSoundVolume(),
            (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F
        );

        var attack = isClawAttack ? CrusherAttackType.BITE : CrusherAttackType.TAIL;

        attackType.set(attack);
        beginAttack(attack.defaultDurationInTicks());
    }

    @Override
    protected float getHealthRegenPerSecond() {
        return 0.5F;
    }

    public CrusherAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.CRUSHER.get();
            case NETHER -> AlienEntityTypes.NETHER_CRUSHER.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_CRUSHER.get();
            case IRRADIATED -> AlienEntityTypes.IRRADIATED_CRUSHER.get();
        };
    }
}

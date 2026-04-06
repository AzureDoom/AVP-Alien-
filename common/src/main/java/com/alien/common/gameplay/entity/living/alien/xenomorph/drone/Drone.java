package com.alien.common.gameplay.entity.living.alien.xenomorph.drone;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.EggCarrier;
import com.alien.common.gameplay.entity.living.alien.xenomorph.EggPickupManager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.VentBuilder;
import com.alien.common.gameplay.entity.living.alien.xenomorph.VentData;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphNavigationManager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.drone.ai.DroneGOAP;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienBlockTags;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.entity.v1.EntityUtil;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.goap.v1.GOAPUser;
import com.blib.api.common.pathfinding.v1.cache.TerrainCacheRegistry;
import com.blib.api.common.pathfinding.v1.evaluator.Posture;
import com.blib.api.common.pathfinding.v1.evaluator.TerrainEvaluatorConfig;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigator;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorConfig;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorUser;
import com.blib.api.common.pathfinding.v1.physics.ClimbingMoveControl;
import com.blib.api.common.pathfinding.v1.search.SearchConfig;
import com.blib.api.common.pathfinding.v1.terrain.BlockBreakabilityEvaluators;
import com.blib.api.common.pathfinding.v1.terrain.TerrainClassifiers;
import com.blib.api.common.pathfinding.v1.terrain.TerrainType;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class Drone extends Xenomorph implements EggCarrier, GOAPUser<Drone>, PathNavigatorUser, VentBuilder {

    public static AttributeSupplier.Builder createDroneAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 4.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 0f)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 0.25F)
            .add(Attributes.FOLLOW_RANGE, 35F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.3f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 2F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1F);
    }

    private static final float MAX_BREAKABLE_DESTROY_TIME = 6.0F;

    private static final Posture STANDING = new Posture("default", 1, 2);

    private static final Posture CRAWLING = new Posture("crawling", 1, 1);

    private static final float CRAWL_TRANSITION_COST = 0.5f;

    public final DataAccessor<XenomorphAttackType> attackType;

    private final DroneAnimationDispatcher animationDispatcher;

    private final EggPickupManager eggPickupManager;

    private final VentData ventData;

    private final PathNavigator pathNavigator;

    public Drone(EntityType<? extends Drone> entityType, Level level) {
        super(entityType, level);
        this.attackType = new DataAccessor<>(this, AlienDataSyncKeys.XENOMORPH_ATTACK_TYPE.get());
        this.animationDispatcher = new DroneAnimationDispatcher(this);
        this.eggPickupManager = new EggPickupManager(this);
        this.ventData = new VentData();
        this.pathNavigator = createPathNavigator(level);
        getXenomorphData().setParallelDigCount(1);
    }

    private PathNavigator createPathNavigator(Level level) {
        var evaluatorConfig = TerrainEvaluatorConfig.builder()
            .addTerrain(TerrainType.GROUND, 1.0f)
            .addTerrain(TerrainType.CLIMBABLE, 1.0f)
            .addTerrain(TerrainType.WATER, 4.0f)
            .addTerrain(TerrainType.BREAKABLE, 8.0f)
            .withTerrainClassifier(TerrainClassifiers.GROUND_AND_WATER)
            .withBreakabilityEvaluator(
                BlockBreakabilityEvaluators.withExcludedTag(
                    BlockBreakabilityEvaluators.defaultEvaluator(MAX_BREAKABLE_DESTROY_TIME),
                    AlienBlockTags.XENOMORPH_IMMUNE
                )
            )
            .addPosture(STANDING)
            .addPosture(CRAWLING, CRAWL_TRANSITION_COST)
            .withClimbingPostureIndex(1)
            .withMaxFallDistance(14)
            .withCanOpenDoors(true)
            .build();

        var followRange = (float) getAttributeValue(Attributes.FOLLOW_RANGE);

        var navigatorConfig = PathNavigatorConfig.builder(evaluatorConfig)
            .withSearchConfig(SearchConfig.fromFollowRange(followRange))
            .onPostureEnter(0, () -> isCrawling.set(false))
            .onPostureEnter(1, () -> isCrawling.set(true))
            .build();

        var classificationCache = TerrainCacheRegistry.getOrCreate(level, evaluatorConfig.getTerrainClassifier());

        return new PathNavigator(level, navigatorConfig, classificationCache);
    }

    @Override
    protected @NotNull XenomorphNavigationManager createNavigationManager() {
        return new XenomorphNavigationManager(this, new ClimbingMoveControl(this));
    }

    @Override
    public Agent.Builder<Drone> blib$applyGOAPAgentProperties(Agent.Builder<Drone> agentBuilder) {
        return DroneGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<Drone> blib$getGOAPGraphOrNull() {
        return DroneGOAP.GRAPH;
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
    protected void registerGoals() {
        // GOAP handles all AI for the drone.
    }

    @Override
    public void tick() {
        super.tick();
        eggPickupManager.tick();
    }

    @Override
    protected boolean canEntityRideAlien(@NotNull Entity passenger) {
        return super.canEntityRideAlien(passenger)
            || passenger.getType().is(AlienEntityTypeTags.OVOMORPHS);
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        if (passenger.getType().is(AlienEntityTypeTags.OVOMORPHS)) {
            var relativePos = EntityUtil.getRelativePosition(this, 0, 0.8, -1);
            callback.accept(passenger, relativePos.x, relativePos.y, relativePos.z);
            return;
        }

        super.positionRider(passenger, callback);
    }

    @Override
    public boolean isAttacking() {
        return attackType.get() != XenomorphAttackType.NONE;
    }

    @Override
    protected void resetAttackType() {
        attackType.set(XenomorphAttackType.NONE);
    }

    @Override
    public void runAttackAnimations() {
        var attackVariant = random.nextInt(0, 3);

        playSound(
            AlienSoundEvents.ENTITY_XENOMORPH_ATTACK.get(),
            getSoundVolume(),
            (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F
        );

        var attack = switch (attackVariant) {
            case 0 -> XenomorphAttackType.CLAW;
            case 1 -> XenomorphAttackType.BITE;
            default -> XenomorphAttackType.TAIL;
        };

        attackType.set(attack);
        beginAttack(attack.defaultDurationInTicks());
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumer) {
        super.updateDynamicGameEventListener(biConsumer);
        eggPickupManager.updateDynamicGameEventListener(biConsumer);
    }

    @Override
    protected float getHealthRegenPerSecond() {
        return 0.5F;
    }

    @Override
    public EggPickupManager getEggPickupManager() {
        return eggPickupManager;
    }

    @Override
    public VentData getVentData() {
        return ventData;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        ventData.load(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ventData.save(compoundTag);
    }

    public DroneAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.DRONE.get();
            case NETHER -> AlienEntityTypes.NETHER_DRONE.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_DRONE.get();
            case IRRADIATED -> AlienEntityTypes.IRRADIATED_DRONE.get();
        };
    }
}

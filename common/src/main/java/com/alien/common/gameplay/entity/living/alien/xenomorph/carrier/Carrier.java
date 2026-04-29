package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai.CarrierGOAP;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Carrier extends Xenomorph implements GOAPUser<Carrier>, PathNavigatorUser {

    private static final float MAX_BREAKABLE_DESTROY_TIME = 6.0F;

    public static AttributeSupplier.Builder createCarrierAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 12.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 12.0F)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 0.75F)
            .add(Attributes.FOLLOW_RANGE, 35F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.7f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 5F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1.2F);
    }

    public final DataAccessor<XenomorphAttackType> attackType;

    private final CarrierAnimationDispatcher animationDispatcher;

    private final CarrierData carrierData;

    private final PathNavigator pathNavigator;

    public Carrier(EntityType<? extends Carrier> entityType, Level level) {
        super(entityType, level);
        this.attackType = new DataAccessor<>(this, AlienDataSyncKeys.XENOMORPH_ATTACK_TYPE.get());
        this.animationDispatcher = new CarrierAnimationDispatcher(this);
        this.carrierData = new CarrierData();
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
            .withEntitySize(1, 4)
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
    public Agent.Builder<Carrier> blib$applyGOAPAgentProperties(Agent.Builder<Carrier> agentBuilder) {
        return CarrierGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<Carrier> blib$getGOAPGraphOrNull() {
        return CarrierGOAP.GRAPH;
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
    protected float getHealthRegenPerSecond() {
        return 0.5F;
    }

    @Override
    public void tick() {
        super.tick();
        carrierData.tick();
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
    protected int getMaxPassengerCount() {
        return CarrierSpine.COUNT;
    }

    @Override
    protected boolean canEntityRideAlien(@NotNull Entity passenger) {
        if (passenger.getType().is(AlienEntityTypeTags.FACEHUGGERS)) {
            if (passenger.getVehicle() == this) {
                return true;
            }
            return getRidingFacehuggerCount() < CarrierSpine.COUNT;
        }
        return super.canEntityRideAlien(passenger);
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        if (passenger.getType().is(AlienEntityTypeTags.FACEHUGGERS)) {
            callback.accept(passenger, getX(), getY(), getZ());
            return;
        }
        super.positionRider(passenger, callback);
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
        ejectAllFacehuggers();
        super.die(damageSource);
    }

    private void ejectAllFacehuggers() {
        var facehuggers = getPassengers().stream()
            .filter(p -> p.getType().is(AlienEntityTypeTags.FACEHUGGERS))
            .toList();

        var count = facehuggers.size();
        for (int i = 0; i < count; i++) {
            var facehugger = facehuggers.get(i);
            facehugger.stopRiding();

            var angle = ((float) i / count) * (float) (Math.PI * 2) + (random.nextFloat() - 0.5F) * 0.5F;
            var horizontalSpeed = 0.5 + random.nextFloat() * 0.5;
            var verticalSpeed = 0.3 + random.nextFloat() * 0.4;

            facehugger.setDeltaMovement(
                Math.cos(angle) * horizontalSpeed,
                verticalSpeed,
                Math.sin(angle) * horizontalSpeed
            );
        }
    }

    public void throwFacehugger() {
        attackType.set(XenomorphAttackType.THROW);
        beginAttack(XenomorphAttackType.THROW.defaultDurationInTicks());
    }

    public int getRidingFacehuggerCount() {
        return (int) getPassengers().stream()
            .filter(p -> p.getType().is(AlienEntityTypeTags.FACEHUGGERS))
            .count();
    }

    public CarrierData getCarrierData() {
        return carrierData;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        carrierData.load(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        carrierData.save(compoundTag);
    }

    @Override
    protected boolean isImmobile() {
        return isDeadOrDying();
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    public CarrierAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.CARRIER.get();
            case NETHER -> AlienEntityTypes.NETHER_CARRIER.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_CARRIER.get();
            case IRRADIATED -> AlienEntityTypes.IRRADIATED_CARRIER.get();
        };
    }
}

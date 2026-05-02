package com.alien.common.gameplay.entity.living.alien.xenomorph.queen;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying.EggLayer;
import com.alien.common.gameplay.entity.living.alien.xenomorph.drone.Drone;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai.QueenGOAP;
import com.alien.common.gameplay.level.saveddata.QueenSpawnChunkData;
import com.alien.common.gameplay.level.saveddata.StrainLeakData;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienBlockTags;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.entity.v1.PlayerUtil;
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
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Queen extends Xenomorph implements GOAPUser<Queen>, PathNavigatorUser, EggLayer {

    public static AttributeSupplier.Builder createQueenAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 16.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 16.0F)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 2.5F)
            .add(Attributes.FOLLOW_RANGE, 35F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 10F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 0.9F);
    }

    private static final float MAX_BREAKABLE_DESTROY_TIME = 6.0F;

    public final DataAccessor<QueenAttackType> attackType;

    private final QueenAnimationDispatcher animationDispatcher;

    private final OvipositorManager ovipositorManager;

    private final QueenData queenData;

    private final PathNavigator pathNavigator;

    public Queen(EntityType<? extends Queen> entityType, Level level) {
        super(entityType, level);
        this.attackType = new DataAccessor<>(this, AlienDataSyncKeys.QUEEN_ATTACK_TYPE.get());
        this.animationDispatcher = new QueenAnimationDispatcher(this);
        this.ovipositorManager = new OvipositorManager(this);
        this.queenData = new QueenData();
        this.pathNavigator = createPathNavigator(level);
        getXenomorphData().setParallelDigCount(4);
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
            .withEntitySize(2, 4)
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
    public Agent.Builder<Queen> blib$applyGOAPAgentProperties(Agent.Builder<Queen> agentBuilder) {
        return QueenGOAP.applyAgentProperties(agentBuilder);
    }

    @Override
    public @Nullable Graph<Queen> blib$getGOAPGraphOrNull() {
        return getActiveGOAPGraph(QueenGOAP.GRAPH);
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
    public void tick() {
        super.tick();
        ovipositorManager.tick();
        queenData.tick();
    }

    @Override
    protected boolean canEntityRideAlien(@NotNull Entity passenger) {
        return Objects.equals(passenger.getType(), AlienEntityTypes.OVIPOSITOR.get());
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, @NotNull MoveFunction callback) {
        if (passenger.getType() == AlienEntityTypes.OVIPOSITOR.get()) {
            var relativePos = com.blib.api.common.entity.v1.EntityUtil.getRelativePosition(this, 3, 0.01, 5.25);
            callback.accept(passenger, relativePos.x, relativePos.y, relativePos.z);
            return;
        }

        super.positionRider(passenger, callback);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor serverLevelAccessor,
        @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnType,
        @Nullable SpawnGroupData spawnGroupData
    ) {
        if (spawnType == MobSpawnType.NATURAL) {
            applyNaturalSpawnEffects();
        }

        return super.finalizeSpawn(serverLevelAccessor, difficulty, spawnType, spawnGroupData);
    }

    private void applyNaturalSpawnEffects() {
        var level = level();

        if (level.isClientSide) {
            return;
        }

        alertPlayersOfSpawn();
        spawnGuards();
        resetQueenSpawnCooldown();

        StrainLeakData.getOrCreate(level)
            .ifSome(strainLeakData -> strainLeakData.add(getVariant(), -1));
    }

    private void alertPlayersOfSpawn() {
        for (var player : PlayerUtil.getTrackingPlayers(this)) {
            player.playNotifySound(AlienSoundEvents.ENTITY_QUEEN_SCREAM.get(), SoundSource.MASTER, 1, 1);
            player.sendSystemMessage(
                Component.literal("A scream from the depths sends chills down your spine...")
                    .withStyle(AlienVariantTypes.getFor(this).chatColor(), ChatFormatting.ITALIC)
            );
        }
    }

    private void spawnGuards() {
        var droneType = Drone.getType(getVariant());

        for (var i = 0; i < 4; i++) {
            var drone = droneType.spawn((ServerLevel) level(), blockPosition(), MobSpawnType.NATURAL);

            if (drone != null) {
                drone.setPersistenceRequired();
            }
        }
    }

    private void resetQueenSpawnCooldown() {
        QueenSpawnChunkData.getOrCreate(level())
            .ifSome(queenSpawnChunkData -> queenSpawnChunkData.getSpawnCooldown().reset());
    }

    @Override
    public float maxUpStep() {
        return 2.5F;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return AlienSoundEvents.ENTITY_QUEEN_IDLE.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return AlienSoundEvents.ENTITY_QUEEN_DEATH.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return AlienSoundEvents.ENTITY_QUEEN_HURT.get();
    }

    @Override
    protected float getHealthRegenPerSecond() {
        return 0.5F;
    }

    @Override
    public boolean isAttacking() {
        return attackType.get() != QueenAttackType.NONE;
    }

    @Override
    protected void resetAttackType() {
        attackType.set(QueenAttackType.NONE);
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
            case 0 -> QueenAttackType.SWIPE_DOWN;
            case 1 -> QueenAttackType.BACKHAND;
            default -> QueenAttackType.TAIL_STRIKE;
        };

        attackType.set(attack);
        beginAttack(attack.defaultDurationInTicks());
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
        if (
            !ovipositorManager.hasOvipositor()
                || !entity.getType().is(AlienEntityTypeTags.ALIENS)
        ) {
            super.doPush(entity);
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    public QueenAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public OvipositorManager getOvipositorManager() {
        return ovipositorManager;
    }

    public QueenData getQueenData() {
        return queenData;
    }

    @Override
    public Entity asEntity() {
        return this;
    }

    @Override
    public boolean isEggLayCooldownReady() {
        return queenData.isEggLayCooldownReady();
    }

    @Override
    public void resetEggLayCooldown() {
        queenData.resetEggLayCooldown();
    }

    @Override
    public boolean hasOvipositor() {
        return ovipositorManager.hasOvipositor();
    }

    @Override
    public Vec3 getEggLayingPosition() {
        return ovipositorManager.getEggLayingPosition();
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        ovipositorManager.load(compoundTag);
        queenData.load(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ovipositorManager.save(compoundTag);
        queenData.save(compoundTag);
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.QUEEN.get();
            case NETHER -> AlienEntityTypes.NETHER_QUEEN.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_QUEEN.get();
            case IRRADIATED -> AlienEntityTypes.IRRADIATED_QUEEN.get();
        };
    }
}

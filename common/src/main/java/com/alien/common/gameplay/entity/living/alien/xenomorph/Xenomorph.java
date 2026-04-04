package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.alien.common.gameplay.ai.goal.DigToTargetGoal;
import com.alien.common.gameplay.ai.goal.XenoFloatGoal;
import com.alien.common.gameplay.entity.CrawlingManager;
import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.GrowthManager;
import com.alien.common.gameplay.entity.living.alien.ResinManager;
import com.alien.common.model.resin.ResinData;
import com.alien.common.model.resin.ResinProducer;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.alien.common.util.AlienPredicates;
import com.alien.common.util.XenomorphGrowthUtil;
import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.entity.v1.EntitySenseCache;
import com.blib.api.common.entity.v1.EntitySenseCacheUser;
import com.blib.api.common.entity.v1.ai.goal.StrollAroundInWaterGoal;
import com.blib.api.common.goap.v1.GOAPUser;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorUser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;

public abstract class Xenomorph extends Alien implements ResinProducer, EntitySenseCacheUser {

    public final DataAccessor<Integer> attackDurationInTicks;

    public final DataAccessor<Boolean> isLunging;

    public final DataAccessor<Boolean> isCrawling;

    protected final CrawlingManager crawlingManager;

    private final XenomorphNavigationManager navigationManager;

    private final GrowthManager growthManager;

    private final ResinManager resinManager;

    private final XenomorphData xenomorphData;

    private final EntitySenseCache entitySenseCache;

    private int remainingAttackTicks;

    private boolean wasUnderwaterLastTick;

    public Xenomorph(EntityType<? extends Xenomorph> entityType, Level level) {
        super(entityType, level);

        this.attackDurationInTicks = new DataAccessor<>(this, AlienDataSyncKeys.XENOMORPH_ATTACK_DURATION_IN_TICKS.get());
        this.isLunging = new DataAccessor<>(this, AlienDataSyncKeys.XENOMORPH_IS_LUNGING.get());
        this.isCrawling = new DataAccessor<>(this, AlienDataSyncKeys.XENOMORPH_IS_CRAWLING.get());

        this.crawlingManager = new CrawlingManager(this, isCrawling);
        this.growthManager = new GrowthManager(this, XenomorphGrowthUtil.GROW_UP_CALLBACK)
            .setGrowOverTime(false);
        this.navigationManager = createNavigationManager();
        this.resinManager = new ResinManager(this, createResinData());
        this.xenomorphData = new XenomorphData(getRandom());
        this.entitySenseCache = EntitySenseCache.builder(this)
            .withScanRadius(40)
            .addTrackedTag(AlienEntityTypeTags.XENOMORPHS)
            .withRefreshPolicy(cache -> {
                var ticksSinceRefresh = cache.getEntity().tickCount - cache.getLastSenseTick();
                var wasRecentlyHurt = getLastHurtByMobTimestamp() > 0
                    && tickCount - getLastHurtByMobTimestamp() < 10;

                return ticksSinceRefresh > 20 || (wasRecentlyHurt && ticksSinceRefresh > 10);
            })
            .build();
        this.wasUnderwaterLastTick = false;

        isCrawling.onChange($ -> refreshDimensions());
    }

    protected @NotNull XenomorphNavigationManager createNavigationManager() {
        return new XenomorphNavigationManager(this, moveControl);
    }

    protected boolean canTargetInitially(LivingEntity target) {
        return true;
    }

    protected abstract @Nullable ResinData createResinData();

    public abstract void runAttackAnimations();

    public void runDigAnimation() {
        runAttackAnimations();
    }

    public abstract boolean isAttacking();

    protected abstract void resetAttackType();

    protected void beginAttack(int durationInTicks) {
        attackDurationInTicks.set(durationInTicks);
        remainingAttackTicks = durationInTicks;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new XenoFloatGoal(this));
        addDigToTargetGoal();
        goalSelector.addGoal(7, new StrollAroundInWaterGoal(this, 0.5));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.5));
        targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Xenomorph.class));
        targetSelector.addGoal(
            2,
            new NearestAttackableTargetGoal<>(
                this,
                LivingEntity.class,
                false,
                target -> AlienPredicates.canTarget(this, target)
                    && canTargetInitially(target)
            )
        );
    }

    protected void addDigToTargetGoal() {
        goalSelector.addGoal(5, new DigToTargetGoal(this, 32, 2, () -> true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!(this instanceof PathNavigatorUser)) {
            crawlingManager.tick();
        }

        growthManager.tick();
        resinManager.tick();
        xenomorphData.tick();

        updateDimensionsBasedOnWaterState();

        if (!level().isClientSide && isLunging.get() && onGround()) {
            isLunging.set(false);
        }

        if (!level().isClientSide && remainingAttackTicks > 0) {
            remainingAttackTicks--;

            if (remainingAttackTicks <= 0) {
                resetAttackType();
                attackDurationInTicks.set(0);
            }
        }

        if (!level().isClientSide) {
            var target = getTarget();

            if (target != null && !AlienPredicates.canContinueTargeting(this, target)) {
                // If the target is no longer valid, stop targeting them.
                setTarget(null);
            }

            if (this instanceof GOAPUser<?>) {
                tryAlertNearbyXenomorphs();
            }
        }
    }

    private void updateDimensionsBasedOnWaterState() {
        if (wasUnderwaterLastTick != isUnderWater()) {
            refreshDimensions();
        }

        this.wasUnderwaterLastTick = isUnderWater();
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (isControlledByLocalInstance() && isUnderWater()) {
            moveRelative(0.01F, vec3);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.9));
        } else {
            super.travel(vec3);
        }
    }

    @Override
    public void updateSwimming() {
        if (!level().isClientSide) {
            if (isEffectiveAi() && isUnderWater()) {
                navigationManager.switchToWater(this, 4, goalSelector);
                setSwimming(true);
            } else {
                navigationManager.switchToGround(this, 4, goalSelector);
                setSwimming(false);
            }
        }
    }

    @Override
    public boolean startRiding(@NotNull Entity entity, boolean force) {
        if (entity instanceof Boat || entity instanceof Minecart) {
            return false;
        }

        return super.startRiding(entity, force);
    }

    @Override
    public @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        var defaultDimensions = getType().getDimensions();
        var shouldBeSmall = crawlingManager.isCrawling() || isUnderWater();
        return defaultDimensions.scale(1, shouldBeSmall ? 0.4f : 1);
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> biConsumer) {
        super.updateDynamicGameEventListener(biConsumer);
        resinManager.updateDynamicGameEventListener(biConsumer);
    }

    // Allows the xenomorph to disable shields on attack.
    @Override
    public boolean canDisableShield() {
        return true;
    }

    // Prevents the xenomorph from having a bias towards pathing in darker areas.
    @Override
    public float getWalkTargetValue(@NotNull BlockPos blockPos, @NotNull LevelReader levelReader) {
        return 0.0F;
    }

    // Reduces how much FLOWING water slows down xenomorphs.
    @Override
    public boolean updateFluidHeightAndDoFluidPushing(@NotNull TagKey<Fluid> tagKey, double d) {
        var modifier = d;

        if (Objects.equals(tagKey, FluidTags.WATER)) {
            modifier = 0;
        }

        if (isNetherAfflicted() && Objects.equals(tagKey, FluidTags.LAVA)) {
            modifier = 0;
        }

        return super.updateFluidHeightAndDoFluidPushing(tagKey, modifier);
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity) {
        if (livingEntity != null && !livingEntity.equals(getTarget()) && ambientSoundTime > getAmbientSoundInterval()) {
            playSound(
                AlienSoundEvents.ENTITY_XENOMORPH_HISS.get(),
                getSoundVolume(),
                (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F
            );
        }

        super.setTarget(livingEntity);
    }

    private void tryAlertNearbyXenomorphs() {
        var attacker = getLastHurtByMob();
        var hurtTimestamp = getLastHurtByMobTimestamp();

        if (attacker == null || hurtTimestamp == xenomorphData.getLastAlertedHurtTimestamp()) {
            return;
        }

        xenomorphData.setLastAlertedHurtTimestamp(hurtTimestamp);

        var nearbyXenomorphs = entitySenseCache.getByTag(AlienEntityTypeTags.XENOMORPHS);

        for (var entity : nearbyXenomorphs) {
            if (entity instanceof Xenomorph xenomorph && xenomorph != this && xenomorph.getTarget() == null) {
                xenomorph.setTarget(attacker);
            }
        }
    }

    // Fixes a bug where xenomorphs would try to retaliate attack infected hosts that hurt them.
    // TODO: Remove this once GOAP AI is introduced and this edge case has been handled in the new AI.
    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return super.canAttack(target) && AlienPredicates.canContinueTargeting(this, target);
    }

    @Override
    protected void doPush(Entity entity) {
        if (
            !entity.getType().is(AlienEntityTypeTags.FACEHUGGERS)
                && !entity.getType().is(AlienEntityTypeTags.CHESTBURSTERS)
                && !entity.getType().is(AlienEntityTypeTags.ADOLESCENTS)
        ) {
            // Xenomorphs should not collide with smaller aliens.
            super.doPush(entity);
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 6 * 20;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return getTarget() != null ? AlienSoundEvents.ENTITY_XENOMORPH_HISS.get() : AlienSoundEvents.ENTITY_XENOMORPH_IDLE.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return AlienSoundEvents.ENTITY_XENOMORPH_DEATH.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return AlienSoundEvents.ENTITY_XENOMORPH_HURT.get();
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        crawlingManager.load(compoundTag);
        growthManager.load(compoundTag);
        resinManager.load(compoundTag);
        xenomorphData.load(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        crawlingManager.save(compoundTag);
        growthManager.save(compoundTag);
        resinManager.save(compoundTag);
        xenomorphData.save(compoundTag);
    }

    public GrowthManager getGrowthManager() {
        return growthManager;
    }

    @Override
    public ResinManager getResinManager() {
        return resinManager;
    }

    public XenomorphData getXenomorphData() {
        return xenomorphData;
    }

    @Override
    public EntitySenseCache getEntitySenseCache() {
        return entitySenseCache;
    }

    public XenomorphNavigationManager getNavigationManager() {
        return navigationManager;
    }

    public CrawlingManager getCrawlingManager() {
        return crawlingManager;
    }

    public void setMoveControl(MoveControl moveControl) {
        this.moveControl = moveControl;
    }

    public void setNavigation(PathNavigation navigation) {
        this.navigation = navigation;
    }
}

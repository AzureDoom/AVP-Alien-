package com.alien.common.gameplay.entity.living.alien.xenomorph.runner;

import com.alien.common.gameplay.ai.CreateVentGoal;
import com.alien.common.gameplay.ai.DropOffEggGoal;
import com.alien.common.gameplay.ai.PickUpEggGoal;
import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.EggCarrier;
import com.alien.common.gameplay.entity.living.alien.xenomorph.EggPickupManager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.QuadrupedAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphNavigationManager;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.model.resin.ResinData;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.data_sync.v1.DataAccessor;
import com.blib.api.common.entity.v1.EntityUtil;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import com.blib.api.common.entity.v1.ai.goal.combat.LungeAtTargetGoal;
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

public class Runner extends Xenomorph implements EggCarrier {

    public static AttributeSupplier.Builder createRunnerAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 4.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 0f)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 0.25F)
            .add(Attributes.FOLLOW_RANGE, 35F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.3f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 2F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1F);
    }

    public final DataAccessor<QuadrupedAttackType> attackType;

    private final RunnerAnimationDispatcher animationDispatcher;

    private final EggPickupManager eggPickupManager;

    public Runner(EntityType<? extends Runner> entityType, Level level) {
        super(entityType, level);
        this.attackType = new DataAccessor<>(this, AlienDataSyncKeys.QUADRUPED_ATTACK_TYPE.get());
        this.animationDispatcher = new RunnerAnimationDispatcher(this);
        this.eggPickupManager = new EggPickupManager(this);
    }

    @Override
    public @Nullable EntityType<? extends Alien> getTypeForVariant(AlienVariant alienVariant) {
        return getType(alienVariant);
    }

    @Override
    protected @Nullable ResinData createResinData() {
        return new ResinData(0, 16, 1, 20);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new LungeAtTargetGoal(this, 0.05F, 20 * 7, 6, 12).setOnLungeCallback(this::runLungeAnimation));
        goalSelector.addGoal(4, new PickUpEggGoal<>(this));
        goalSelector.addGoal(5, new DropOffEggGoal<>(this));
        goalSelector.addGoal(6, new CreateVentGoal(this));
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
        return attackType.get() != QuadrupedAttackType.NONE;
    }

    @Override
    protected void resetAttackType() {
        attackType.set(QuadrupedAttackType.NONE);
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
            case 0 -> QuadrupedAttackType.CLAW;
            case 1 -> QuadrupedAttackType.BITE;
            default -> QuadrupedAttackType.TAIL_QUAD;
        };

        attackType.set(attack);
        beginAttack(attack.defaultDurationInTicks());
    }

    private void runLungeAnimation() {
        playSound(AlienSoundEvents.ENTITY_XENOMORPH_LUNGE.get(), getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        isLunging.set(true);
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
    protected @NotNull XenomorphNavigationManager createNavigationManager() {
        return new XenomorphNavigationManager(this, moveControl, 1.2, 2);
    }

    public RunnerAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.RUNNER.get();
            case NETHER -> AlienEntityTypes.NETHER_RUNNER.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_RUNNER.get();
            case IRRADIATED -> AlienEntityTypes.IRRADIATED_RUNNER.get();
        };
    }

    @Override
    public EggPickupManager getEggPickupManager() {
        return eggPickupManager;
    }
}

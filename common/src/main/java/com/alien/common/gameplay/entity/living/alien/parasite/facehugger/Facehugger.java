package com.alien.common.gameplay.entity.living.alien.parasite.facehugger;

import com.alien.common.constant.FollowRangeConstants;
import com.alien.common.constant.HealthConstants;
import com.alien.common.constant.KnockbackResistanceConstants;
import com.alien.common.constant.MoveSpeedConstants;
import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.parasite.Parasite;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.entity.v1.ai.goal.combat.LungeAtTargetGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Facehugger extends Parasite {

    public static AttributeSupplier.Builder createFacehuggerAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 0f)
            .add(Attributes.ARMOR_TOUGHNESS, 0f)
            .add(Attributes.ATTACK_DAMAGE, 0f)
            .add(Attributes.FOLLOW_RANGE, FollowRangeConstants.FACEHUGGER_FOLLOW_RANGE)
            .add(Attributes.KNOCKBACK_RESISTANCE, KnockbackResistanceConstants.FACEHUGGER_KNOCKBACK_RESISTANCE)
            .add(Attributes.MAX_HEALTH, HealthConstants.FACEHUGGER_HEALTH)
            .add(Attributes.MOVEMENT_SPEED, MoveSpeedConstants.FACEHUGGER_SPEED);
    }

    private final FacehuggerAnimationDispatcher animationDispatcher;

    public Facehugger(EntityType<? extends Facehugger> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new FacehuggerAnimationDispatcher(this);
    }

    @Override
    public @Nullable EntityType<? extends Alien> getTypeForVariant(AlienVariant alienVariant) {
        return getType(alienVariant, isRoyal());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new LungeAtTargetGoal(this, 0.75F, 20 * 3, 1, 12).setOnLungeCallback(this::runLungeAnimation));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.1, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.5));
        this.targetSelector.addGoal(
            1,
            new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, this::isValidHost)
        );
    }

    private void runLungeAnimation() {
        animationDispatcher.lunge();
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
        if (!entity.getType().is(AlienEntityTypeTags.ALIENS)) {
            // Facehuggers shouldn't collide with any other aliens (especially not other small aliens or ovomorphs).
            super.doPush(entity);
        }
    }

    @Override
    protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.015F, 2F);
    }

    @Override
    public boolean isPersistenceRequired() {
        return super.isPersistenceRequired() || attachmentManager.isAttachedToHost();
    }

    @Override
    protected float getHealthRegenPerSecond() {
        return 0;
    }

    public FacehuggerAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public static @Nullable EntityType<? extends Alien> getType(AlienVariant alienVariant, boolean isRoyal) {
        if (isRoyal) {
            return switch (alienVariant) {
                case NORMAL -> AlienEntityTypes.ROYAL_FACEHUGGER.get();
                case NETHER -> AlienEntityTypes.ROYAL_NETHER_FACEHUGGER.get();
                case ABERRANT -> AlienEntityTypes.ROYAL_ABERRANT_FACEHUGGER.get();
                case IRRADIATED -> null;
            };
        }

        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.FACEHUGGER.get();
            case NETHER -> AlienEntityTypes.NETHER_FACEHUGGER.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_FACEHUGGER.get();
            case IRRADIATED -> null;
        };
    }
}

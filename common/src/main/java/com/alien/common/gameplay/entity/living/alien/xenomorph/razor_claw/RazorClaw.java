package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.model.resin.ResinData;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienMobEffects;
import com.alien.common.registry.init.AlienSoundEvents;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RazorClaw extends Xenomorph {

    private static final int BLOOD_LOSS_DURATION_IN_TICKS = 20 * 15;

    public static AttributeSupplier.Builder createRazorClawAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 12.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 12.0F)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 0.75F)
            .add(Attributes.FOLLOW_RANGE, 35F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.7f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 5F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1.2F);
    }

    private final RazorClawAnimationDispatcher animationDispatcher;

    public RazorClaw(EntityType<? extends RazorClaw> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new RazorClawAnimationDispatcher(this);
    }

    @Override
    public @Nullable EntityType<? extends Alien> getTypeForVariant(AlienVariant alienVariant) {
        return getType(alienVariant);
    }

    @Override
    protected @Nullable ResinData createResinData() {
        return new ResinData(0, 64, 1, 20);
    }

    @Override
    protected float getHealthRegenPerSecond() {
        return 0.5F;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        var result = super.doHurtTarget(entity);

        if (result && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(
                new MobEffectInstance(
                    AlienMobEffects.getBloodLossHolder(),
                    BLOOD_LOSS_DURATION_IN_TICKS,
                    0
                )
            );
        }

        return result;
    }

    @Override
    public void runAttackAnimations() {
        var attackType = random.nextInt(0, 3);

        playSound(
            AlienSoundEvents.ENTITY_XENOMORPH_ATTACK.get(),
            getSoundVolume(),
            (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F
        );

        switch (attackType) {
            case 0 -> animationDispatcher.rightClawAttack();
            case 1 -> animationDispatcher.biteAttack();
            default -> animationDispatcher.tailAttack();
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public Integer getMaxJellyToGrowth() {
        return 9;
    }

    public RazorClawAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.RAZOR_CLAW.get();
            case NETHER -> AlienEntityTypes.NETHER_RAZOR_CLAW.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_RAZOR_CLAW.get();
            case IRRADIATED -> AlienEntityTypes.IRRADIATED_RAZOR_CLAW.get();
        };
    }
}

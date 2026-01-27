package com.alien.common.gameplay.entity.living.alien.xenomorph.spitter;

import com.alien.common.constant.ArmorConstants;
import com.alien.common.constant.AttackDamageConstants;
import com.alien.common.constant.FollowRangeConstants;
import com.alien.common.constant.HealthConstants;
import com.alien.common.constant.HealthRegenConstants;
import com.alien.common.constant.KnockbackResistanceConstants;
import com.alien.common.constant.MoveSpeedConstants;
import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.model.resin.ResinData;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.blib.api.common.entity.v1.ai.goal.combat.LungeAtTargetGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Spitter extends Xenomorph {

    public static AttributeSupplier.Builder createSpitterAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, ArmorConstants.SPITTER_ARMOR)
            .add(Attributes.ARMOR_TOUGHNESS, 0f)
            .add(Attributes.ATTACK_DAMAGE, AttackDamageConstants.SPITTER_ATTACK_DAMAGE)
            .add(Attributes.FOLLOW_RANGE, FollowRangeConstants.SPITTER_FOLLOW_RANGE)
            .add(Attributes.KNOCKBACK_RESISTANCE, KnockbackResistanceConstants.SPITTER_KNOCKBACK_RESISTANCE)
            .add(Attributes.MAX_HEALTH, HealthConstants.SPITTER_HEALTH)
            .add(Attributes.MOVEMENT_SPEED, MoveSpeedConstants.SPITTER_SPEED);
    }

    private final SpitterAnimationDispatcher animationDispatcher;

    public Spitter(EntityType<? extends Spitter> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new SpitterAnimationDispatcher(this);
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

    private void runLungeAnimation() {
        playSound(AlienSoundEvents.ENTITY_XENOMORPH_LUNGE.get(), getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        animationDispatcher.lunge();
    }

    @Override
    protected float getHealthRegenPerSecond() {
        return HealthRegenConstants.SPITTER_HEALTH_REGEN;
    }

    @Override
    public Integer getMaxJellyToGrowth() {
        return null;
    }

    public SpitterAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.SPITTER.get();
            case NETHER -> AlienEntityTypes.NETHER_SPITTER.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_SPITTER.get();
            case IRRADIATED -> null;
        };
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.predalien;

import com.alien.common.constant.ArmorConstants;
import com.alien.common.constant.ArmorToughnessConstants;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Predalien extends Xenomorph {

    public static AttributeSupplier.Builder createPredalienAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, ArmorConstants.PREDALIEN_ARMOR)
            .add(Attributes.ARMOR_TOUGHNESS, ArmorToughnessConstants.PREDALIEN_ARMOR_TOUGHNESS)
            .add(Attributes.ATTACK_DAMAGE, AttackDamageConstants.PREDALIEN_ATTACK_DAMAGE)
            .add(Attributes.FOLLOW_RANGE, FollowRangeConstants.PREDALIEN_FOLLOW_RANGE)
            .add(Attributes.KNOCKBACK_RESISTANCE, KnockbackResistanceConstants.PREDALIEN_KNOCKBACK_RESISTANCE)
            .add(Attributes.MAX_HEALTH, HealthConstants.PREDALIEN_HEALTH)
            .add(Attributes.MOVEMENT_SPEED, MoveSpeedConstants.PREDALIEN_SPEED);
    }

    private final PredalienAnimationDispatcher animationDispatcher;

    public Predalien(EntityType<? extends Predalien> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new PredalienAnimationDispatcher(this);
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
        return HealthRegenConstants.PREDALIEN_HEALTH_REGEN;
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

    // Predaliens are too large to be pushed by fluids.
    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    // Predaliens are too large to be pushed.
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public Integer getMaxJellyToGrowth() {
        return 9;
    }

    public PredalienAnimationDispatcher getAnimationDispatcher() {
        return animationDispatcher;
    }

    public static EntityType<? extends Alien> getType(AlienVariant alienVariant) {
        return switch (alienVariant) {
            case NORMAL -> AlienEntityTypes.PREDALIEN.get();
            case NETHER -> AlienEntityTypes.NETHER_PREDALIEN.get();
            case ABERRANT -> AlienEntityTypes.ABERRANT_PREDALIEN.get();
            case IRRADIATED -> AlienEntityTypes.IRRADIATED_PREDALIEN.get();
        };
    }
}

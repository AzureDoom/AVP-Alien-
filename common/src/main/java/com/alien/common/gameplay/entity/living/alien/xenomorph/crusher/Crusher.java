package com.alien.common.gameplay.entity.living.alien.xenomorph.crusher;

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
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphNavigationManager;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.model.resin.ResinData;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.blib.api.common.entity.v1.ai.goal.combat.LungeAtTargetGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Crusher extends Xenomorph {

    public static AttributeSupplier.Builder createCrusherAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, ArmorConstants.CRUSHER_ARMOR)
            .add(Attributes.ARMOR_TOUGHNESS, ArmorToughnessConstants.CRUSHER_ARMOR_TOUGHNESS)
            .add(Attributes.ATTACK_DAMAGE, AttackDamageConstants.CRUSHER_ATTACK_DAMAGE)
            .add(Attributes.FOLLOW_RANGE, FollowRangeConstants.CRUSHER_FOLLOW_RANGE)
            .add(Attributes.KNOCKBACK_RESISTANCE, KnockbackResistanceConstants.CRUSHER_KNOCKBACK_RESISTANCE)
            .add(Attributes.MAX_HEALTH, HealthConstants.CRUSHER_HEALTH)
            .add(Attributes.MOVEMENT_SPEED, MoveSpeedConstants.CRUSHER_SPEED);
    }

    private final CrusherAnimationDispatcher animationDispatcher;

    public Crusher(EntityType<? extends Crusher> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new CrusherAnimationDispatcher(this);
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
    protected @NotNull XenomorphNavigationManager createNavigationManager() {
        return new XenomorphNavigationManager(this, moveControl, 1.2, 2);
    }

    @Override
    public void runAttackAnimations() {
        var isClawAttack = random.nextBoolean();

        playSound(
            AlienSoundEvents.ENTITY_XENOMORPH_ATTACK.get(),
            getSoundVolume(),
            (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F
        );

        if (isClawAttack) {
            animationDispatcher.biteAttack();
        } else {
            animationDispatcher.tailAttack();
        }
    }

    private void runLungeAnimation() {
        playSound(AlienSoundEvents.ENTITY_XENOMORPH_LUNGE.get(), getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        animationDispatcher.lunge();
    }

    @Override
    protected float getHealthRegenPerSecond() {
        return HealthRegenConstants.CRUSHER_HEALTH_REGEN;
    }

    @Override
    public Integer getMaxJellyToGrowth() {
        return 2;
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

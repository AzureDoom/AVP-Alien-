package com.alien.common.gameplay.entity.living.alien.xenomorph.predalien;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.model.resin.ResinData;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.blib.api.common.entity.v1.PlayerStatConstants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Predalien extends Xenomorph {

    public static AttributeSupplier.Builder createPredalienAttributes() {
        return Alien.createAlienAttributes()
            .add(Attributes.ARMOR, 12.0F)
            .add(Attributes.ARMOR_TOUGHNESS, 12.0F)
            .add(Attributes.ATTACK_DAMAGE, PlayerStatConstants.BASE_HEALTH * 0.75F)
            .add(Attributes.FOLLOW_RANGE, 35F)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.7f)
            .add(Attributes.MAX_HEALTH, PlayerStatConstants.BASE_HEALTH * 10F)
            .add(Attributes.MOVEMENT_SPEED, PlayerStatConstants.BASE_WALK_SPEED * 1.2F);
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
        return 0.5F;
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

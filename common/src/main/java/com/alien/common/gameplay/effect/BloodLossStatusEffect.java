package com.alien.common.gameplay.effect;

import com.alien.AlienResources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BloodLossStatusEffect extends MobEffect {

    private static final int RED_PARTICLE_COLOR = 0xCC0000;

    private static final ResourceLocation MODIFIER_ID = AlienResources.location("blood_loss_max_health_reduction");

    public BloodLossStatusEffect() {
        super(MobEffectCategory.HARMFUL, RED_PARTICLE_COLOR);
    }

    public static void applyMaxHealthReduction(LivingEntity entity, float damageAmount) {
        var attributeInstance = entity.getAttribute(Attributes.MAX_HEALTH);

        if (attributeInstance == null) {
            return;
        }

        var currentReduction = getCurrentReduction(attributeInstance);
        var newReduction = currentReduction + damageAmount;

        attributeInstance.removeModifier(MODIFIER_ID);
        attributeInstance.addTransientModifier(
            new AttributeModifier(MODIFIER_ID, -newReduction, AttributeModifier.Operation.ADD_VALUE)
        );

        if (entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        }
    }

    public static void removeMaxHealthReduction(LivingEntity entity) {
        var attributeInstance = entity.getAttribute(Attributes.MAX_HEALTH);

        if (attributeInstance == null) {
            return;
        }

        attributeInstance.removeModifier(MODIFIER_ID);
    }

    private static float getCurrentReduction(AttributeInstance attributeInstance) {
        var modifier = attributeInstance.getModifier(MODIFIER_ID);

        if (modifier == null) {
            return 0;
        }

        return (float) -modifier.amount();
    }
}

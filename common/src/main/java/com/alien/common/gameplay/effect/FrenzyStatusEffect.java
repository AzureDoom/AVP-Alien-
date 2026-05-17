package com.alien.common.gameplay.effect;

import com.alien.AlienResources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FrenzyStatusEffect extends MobEffect {

    private static final int RED_ORANGE_PARTICLE_COLOR = 0xD63A1F;

    private static final double FRENZY_ATTRIBUTE_BOOST = 0.15;

    public FrenzyStatusEffect() {
        super(MobEffectCategory.BENEFICIAL, RED_ORANGE_PARTICLE_COLOR);
        addAttributeModifier(
            Attributes.ATTACK_DAMAGE,
            AlienResources.location("frenzy_attack_damage"),
            FRENZY_ATTRIBUTE_BOOST,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        addAttributeModifier(
            Attributes.MOVEMENT_SPEED,
            AlienResources.location("frenzy_movement_speed"),
            FRENZY_ATTRIBUTE_BOOST,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}

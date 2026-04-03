package com.alien.common.gameplay.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class MetamorphosisStatusEffect extends MobEffect {

    private static final int GREEN_PARTICLE_COLOR = 0x8BF200;

    public MetamorphosisStatusEffect() {
        super(MobEffectCategory.NEUTRAL, GREEN_PARTICLE_COLOR);
    }
}

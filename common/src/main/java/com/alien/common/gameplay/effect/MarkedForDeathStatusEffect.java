package com.alien.common.gameplay.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class MarkedForDeathStatusEffect extends MobEffect {

    private static final int DARK_RED_PARTICLE_COLOR = 0x4D0000;

    public MarkedForDeathStatusEffect() {
        super(MobEffectCategory.HARMFUL, DARK_RED_PARTICLE_COLOR);
    }
}

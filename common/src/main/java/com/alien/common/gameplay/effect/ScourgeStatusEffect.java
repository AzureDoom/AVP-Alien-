package com.alien.common.gameplay.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ScourgeStatusEffect extends MobEffect {

    private static final int RED_PARTICLE_COLOR = 0xCC0000;

    public ScourgeStatusEffect() {
        super(MobEffectCategory.NEUTRAL, RED_PARTICLE_COLOR);
    }
}

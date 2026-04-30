package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack;

public record RavagerSpecialAttackConfig(
    int windupAnimationDurationInTicks,
    int maxExtraWindupHoldTicks,
    int attackDurationInTicks,
    int cooldownInTicks,
    float damagePointPercent,
    double rangeInBlocks,
    double coneAngleInDegrees
) {

    public static final RavagerSpecialAttackConfig DEFAULT = new RavagerSpecialAttackConfig(
        20,
        5,
        20,
        30 * 20,
        0.5F,
        5.0,
        90.0
    );

    public int getRandomWindupDurationInTicks(net.minecraft.util.RandomSource random) {
        return windupAnimationDurationInTicks + random.nextInt(maxExtraWindupHoldTicks + 1);
    }
}

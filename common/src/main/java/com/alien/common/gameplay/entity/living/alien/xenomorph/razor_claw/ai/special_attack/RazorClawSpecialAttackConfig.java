package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai.special_attack;

public record RazorClawSpecialAttackConfig(
    int attackDurationInTicks,
    int cooldownInTicks,
    int damageSweepStartTick,
    int damageSweepDurationInTicks,
    double rangeInBlocks,
    double hitArcDegrees,
    double knockbackStrength,
    double knockbackVerticalBoost,
    int requiredMeleeTargetCount
) {

    public static final RazorClawSpecialAttackConfig DEFAULT = new RazorClawSpecialAttackConfig(
        22,
        10 * 20,
        3,
        10,
        3.0,
        60.0,
        0.3,
        0.0625,
        3
    );
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai.special_attack;

public record RazorClawSpecialAttackConfig(
    int attackDurationInTicks,
    int cooldownInTicks,
    int damageSweepStartTick,
    int damageSweepDurationInTicks,
    double rangeInBlocks,
    double hitArcDegrees,
    int requiredMeleeTargetCount
) {

    public static final RazorClawSpecialAttackConfig DEFAULT = new RazorClawSpecialAttackConfig(
        22,
        10 * 20,
        3,
        10,
        3.0,
        60.0,
        3
    );
}

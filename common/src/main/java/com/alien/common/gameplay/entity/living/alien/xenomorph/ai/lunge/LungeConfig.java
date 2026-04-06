package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge;

public record LungeConfig(
    int minRangeInBlocks,
    int maxRangeInBlocks,
    int cooldownInTicks
) {}

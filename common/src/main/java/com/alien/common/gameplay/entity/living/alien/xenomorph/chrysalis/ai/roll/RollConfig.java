package com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ai.roll;

public record RollConfig(
    int minRangeInBlocks,
    int maxRangeInBlocks
) {

    public static final RollConfig DEFAULT = new RollConfig(5, 16);
}

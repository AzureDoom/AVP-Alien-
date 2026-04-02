package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.blib.api.common.codec.v1.stream.impl.EnumStreamCodec;
import com.just.codec.stream.StreamCodec;

public enum QuadrupedAttackType {

    NONE(0),
    BITE(8),
    CLAW(10),
    TAIL_QUAD(10);

    public static final StreamCodec<QuadrupedAttackType> CODEC = EnumStreamCodec.of(QuadrupedAttackType.class, NONE);

    private final int defaultDurationInTicks;

    QuadrupedAttackType(int defaultDurationInTicks) {
        this.defaultDurationInTicks = defaultDurationInTicks;
    }

    public int defaultDurationInTicks() {
        return defaultDurationInTicks;
    }
}

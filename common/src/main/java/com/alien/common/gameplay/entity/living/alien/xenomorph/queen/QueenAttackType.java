package com.alien.common.gameplay.entity.living.alien.xenomorph.queen;

import com.blib.api.common.codec.v1.stream.impl.EnumStreamCodec;
import com.just.codec.stream.StreamCodec;

public enum QueenAttackType {

    NONE(0),
    SWIPE_DOWN(18),
    BACKHAND(15),
    TAIL_STRIKE(20);

    public static final StreamCodec<QueenAttackType> CODEC = EnumStreamCodec.of(QueenAttackType.class, NONE);

    private final int defaultDurationInTicks;

    QueenAttackType(int defaultDurationInTicks) {
        this.defaultDurationInTicks = defaultDurationInTicks;
    }

    public int defaultDurationInTicks() {
        return defaultDurationInTicks;
    }
}

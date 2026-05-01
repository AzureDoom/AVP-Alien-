package com.alien.common.gameplay.entity.living.alien.xenomorph.empress;

import com.blib.api.common.codec.v1.stream.impl.EnumStreamCodec;
import com.just.codec.stream.StreamCodec;

public enum EmpressAttackType {

    NONE(0),
    SWIPE_DOWN(18),
    BACKHAND(15),
    TAIL_STRIKE(20);

    public static final StreamCodec<EmpressAttackType> CODEC = EnumStreamCodec.of(EmpressAttackType.class, NONE);

    private final int defaultDurationInTicks;

    EmpressAttackType(int defaultDurationInTicks) {
        this.defaultDurationInTicks = defaultDurationInTicks;
    }

    public int defaultDurationInTicks() {
        return defaultDurationInTicks;
    }
}

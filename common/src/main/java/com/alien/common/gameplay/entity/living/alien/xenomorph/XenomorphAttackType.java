package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.blib.api.common.codec.v1.stream.impl.EnumStreamCodec;
import com.just.codec.stream.StreamCodec;

public enum XenomorphAttackType {

    NONE(0),
    BITE(8),
    CLAW(10),
    TAIL(12),
    THROW(20),
    SCREAM(35);

    public static final StreamCodec<XenomorphAttackType> CODEC = EnumStreamCodec.of(XenomorphAttackType.class, NONE);

    private final int defaultDurationInTicks;

    XenomorphAttackType(int defaultDurationInTicks) {
        this.defaultDurationInTicks = defaultDurationInTicks;
    }

    public int defaultDurationInTicks() {
        return defaultDurationInTicks;
    }
}

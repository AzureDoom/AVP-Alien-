package com.alien.common.gameplay.entity.living.alien.xenomorph.crusher;

import com.blib.api.common.codec.v1.stream.impl.EnumStreamCodec;
import com.just.codec.stream.StreamCodec;

public enum CrusherAttackType {

    NONE(0),
    BITE(12),
    TAIL(15);

    public static final StreamCodec<CrusherAttackType> CODEC = EnumStreamCodec.of(CrusherAttackType.class, NONE);

    private final int defaultDurationInTicks;

    CrusherAttackType(int defaultDurationInTicks) {
        this.defaultDurationInTicks = defaultDurationInTicks;
    }

    public int defaultDurationInTicks() {
        return defaultDurationInTicks;
    }
}

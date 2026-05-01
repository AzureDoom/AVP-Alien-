package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.blib.api.common.codec.v1.stream.impl.EnumStreamCodec;
import com.just.codec.stream.StreamCodec;
import com.mojang.serialization.Codec;

public enum CocoonState {

    NONE,
    PENDING,
    SOURCE_COCOONING,
    DESTINATION_COCOONING,
    EMERGING;

    public static final Codec<CocoonState> PERSISTENT_CODEC = Codec.STRING.xmap(CocoonState::valueOf, CocoonState::name);

    public static final StreamCodec<CocoonState> STREAM_CODEC = EnumStreamCodec.of(CocoonState.class, NONE);

    public boolean isLocked() {
        return this == SOURCE_COCOONING || this == DESTINATION_COCOONING || this == EMERGING;
    }

    public boolean shouldRunCocoonAction() {
        return this != NONE;
    }
}

package com.alien.common.model.lifecycle.growth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MoltPhase(
    int idleTicks,
    int moltTicks
) {

    public static final Codec<MoltPhase> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("idleTicks").forGetter(MoltPhase::idleTicks),
            Codec.INT.fieldOf("moltTicks").forGetter(MoltPhase::moltTicks)
        ).apply(instance, MoltPhase::new)
    );

    public int totalTicks() {
        return idleTicks + moltTicks;
    }
}

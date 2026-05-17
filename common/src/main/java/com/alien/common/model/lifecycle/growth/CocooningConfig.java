package com.alien.common.model.lifecycle.growth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CocooningConfig(
    int sourceTimeInTicks,
    int destinationTimeInTicks
) {

    public static final int DEFAULT_SOURCE_TIME_IN_TICKS = 30 * 20;

    public static final int DEFAULT_DESTINATION_TIME_IN_TICKS = 30 * 20;

    public static final CocooningConfig DEFAULT = new CocooningConfig(
        DEFAULT_SOURCE_TIME_IN_TICKS,
        DEFAULT_DESTINATION_TIME_IN_TICKS
    );

    public static final Codec<CocooningConfig> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.optionalFieldOf("sourceTimeInTicks", DEFAULT_SOURCE_TIME_IN_TICKS)
                .forGetter(CocooningConfig::sourceTimeInTicks),
            Codec.INT.optionalFieldOf("destinationTimeInTicks", DEFAULT_DESTINATION_TIME_IN_TICKS)
                .forGetter(CocooningConfig::destinationTimeInTicks)
        ).apply(instance, CocooningConfig::new)
    );
}

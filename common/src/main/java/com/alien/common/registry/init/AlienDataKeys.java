package com.alien.common.registry.init;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.blib.common.network.data.DataKey;
import com.blib.common.registry.DataKeyRegistry;
import com.just.codec.stream.impl.StreamCodecs;
import com.mojang.serialization.Codec;

import java.util.function.Function;

public class AlienDataKeys {

    public static final DataKey<Integer> ACID_MULTIPLIER = register(
        "acid_multiplier",
        builder -> builder.networkSynchronized(StreamCodecs.INT)
            .persistent("Multiplier", Codec.INT)
            .build(1)
    );

    public static final DataKey<Integer> ACID_TICK_COUNT_FOR_MULTIPLIER = register(
        "acid_tick_count_for_multiplier",
        builder -> builder.persistent("TickCountForMultiplier", Codec.INT)
            .build(0)
    );

    public static final DataKey<Boolean> ADOLESCENT_HAS_DORSAL_TUBES = register(
        "adolescent_has_dorsal_tubes",
        builder -> builder.networkSynchronized(StreamCodecs.BOOLEAN)
            .build(true)
    );

    public static final DataKey<Boolean> ALIEN_IS_POISONED = register(
        "alien_is_poisoned",
        builder -> builder.networkSynchronized(StreamCodecs.BOOLEAN)
            .persistent("isPoisoned", Codec.BOOL)
            .build(false)
    );

    public static final DataKey<Integer> OVOMORPH_DESIRE_TO_HATCH = register(
        "ovomorph_desire_to_hatch",
        builder -> builder.persistent("desireToHatch", Codec.INT)
            .build(0)
    );

    public static final DataKey<Integer> OVOMORPH_HATCH_DURATION_IN_TICKS = register(
        "ovomorph_hatch_duration_in_ticks",
        builder -> builder.persistent("hatchDurationInTicks", Codec.INT)
            .build(-1)
    );

    public static final DataKey<Byte> OVOMORPH_HATCH_STATE = register(
        "ovomorph_hatch_state",
        builder -> builder.networkSynchronized(StreamCodecs.BYTE)
            .persistent("hatchState", Codec.BYTE)
            .build((byte) Ovomorph.DEFAULT_HATCH_STATE.getId())
    );

    public static final DataKey<Boolean> OVOMORPH_IS_ROOTED = register(
        "ovomorph_is_rooted",
        builder -> builder.networkSynchronized(StreamCodecs.BOOLEAN)
            .persistent("isRooted", Codec.BOOL)
            .build(true)
    );

    public static final DataKey<Byte> OVOMORPH_MAXIMUM_SPAWN_COUNT = register(
        "ovomorph_maximum_spawn_count",
        builder -> builder.networkSynchronized(StreamCodecs.BYTE)
            .persistent("maximumSpawnCount", Codec.BYTE)
            .build((byte) 1)
    );

    public static final DataKey<Integer> OVOMORPH_REMAINING_SPAWN_DELAY_IN_TICKS = register(
        "ovomorph_remaining_spawn_delay_in_ticks",
        builder -> builder.persistent("remainingSpawnDelayInTicks", Codec.INT)
            .build(-1)
    );

    public static final DataKey<Integer> OVOMORPH_SPAWN_COUNT = register(
        "ovomorph_spawn_count",
        builder -> builder.persistent("spawnCount", Codec.INT)
            .build(0)
    );

    public static final DataKey<Boolean> PARASITE_IS_FERTILE = register(
        "parasite_is_fertile",
        builder -> builder.networkSynchronized(StreamCodecs.BOOLEAN)
            .persistent("isFertile", Codec.BOOL)
            .build(true)
    );

    public static final DataKey<Integer> PARASITE_TICKS_ATTACHED_TO_HOST = register(
        "parasite_ticks_attached_to_host",
        builder -> builder.persistent("ticksAttachedToHost", Codec.INT)
            .build(0)
    );

    public static final DataKey<Boolean> XENOMORPH_IS_CRAWLING = register(
        "xenomorph_is_crawling",
        builder -> builder.networkSynchronized(StreamCodecs.BOOLEAN)
            .build(false)
    );

    private static <T> DataKey<T> register(String id, Function<DataKey.Builder<T>, DataKey<T>> factory) {
        var resourceLocation = AlienResources.location(id);
        var dataAccessor = factory.apply(new DataKey.Builder<>(resourceLocation));
        return DataKeyRegistry.register(resourceLocation, dataAccessor);
    }

    public static void initialize() {}
}

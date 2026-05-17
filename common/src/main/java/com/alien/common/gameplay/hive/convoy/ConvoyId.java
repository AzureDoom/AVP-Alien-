package com.alien.common.gameplay.hive.convoy;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable per-convoy identifier. Wraps a {@link UUID} so callers can pass it without ambiguity vs other UUID-typed
 * fields on the convoy record (the source/destination location ids and lineage faction id are different types).
 */
public record ConvoyId(UUID value) {

    public ConvoyId {
        Objects.requireNonNull(value, "ConvoyId value");
    }

    public static ConvoyId fresh() {
        return new ConvoyId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

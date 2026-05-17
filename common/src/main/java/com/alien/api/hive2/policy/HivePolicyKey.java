package com.alien.api.hive2.policy;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record HivePolicyKey<T>(
    ResourceLocation id,
    Class<T> type
) {

    public HivePolicyKey {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(type, "type");
    }

    public T cast(Object value) {
        return type.cast(value);
    }
}

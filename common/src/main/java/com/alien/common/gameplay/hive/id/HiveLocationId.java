package com.alien.common.gameplay.hive.id;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record HiveLocationId(ResourceLocation value) {

    public HiveLocationId {
        Objects.requireNonNull(value, "HiveLocationId value");
    }

    public static HiveLocationId of(ResourceLocation value) {
        return new HiveLocationId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

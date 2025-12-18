package com.alien.client.render.entity.head;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EntityHeadDataCache {

    private static final Map<ResourceLocation, EntityHeadData> CACHE = new HashMap<>();

    public static @Nullable EntityHeadData get(EntityType<?> entityType) {
        return CACHE.get(BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
    }

    public static void put(Supplier<EntityType<?>> entityTypeSupplier, EntityHeadData entityHeadData) {
        put(entityTypeSupplier.get(), entityHeadData);
    }

    public static void put(EntityType<?> entityType, EntityHeadData entityHeadData) {
        put(BuiltInRegistries.ENTITY_TYPE.getKey(entityType), entityHeadData);
    }

    public static void put(ResourceLocation resourceLocation, EntityHeadData entityHeadData) {
        CACHE.put(resourceLocation, entityHeadData);
    }
}

package com.alien.client.render.entity.head;

import com.blib.client.BLibClientMod;
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

    public static void put(BLibClientMod mod, Supplier<EntityType<?>> entityTypeSupplier, EntityHeadData entityHeadData) {
        mod.events()
            .onClientSetup()
            .register(() -> {
                var entityType = entityTypeSupplier.get();
                var resourceLocation = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);

                put(mod, resourceLocation, entityHeadData);
            });
    }

    public static void put(BLibClientMod mod, EntityType<?> entityType, EntityHeadData entityHeadData) {
        put(mod, BuiltInRegistries.ENTITY_TYPE.getKey(entityType), entityHeadData);
    }

    public static void put(BLibClientMod mod, ResourceLocation resourceLocation, EntityHeadData entityHeadData) {
        CACHE.put(resourceLocation, entityHeadData);
    }
}

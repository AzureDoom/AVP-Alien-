package com.alien.client.render.entity.parasite.attachment;

import com.blib.client.BLibClientMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ParasiteHeadAttachmentOffsetDataCache {

    private static final Map<ResourceLocation, ParasiteHeadAttachmentOffsetData> CACHE = new HashMap<>();

    public static @Nullable ParasiteHeadAttachmentOffsetData get(EntityType<?> entityType) {
        return CACHE.get(BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
    }

    public static void put(
        BLibClientMod mod,
        Supplier<? extends EntityType<?>> entityTypeSupplier,
        ParasiteHeadAttachmentOffsetData parasiteHeadAttachmentOffsetData
    ) {
        mod.events()
            .onClientSetup()
            .register(() -> {
                var entityType = entityTypeSupplier.get();
                var resourceLocation = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);

                put(mod, resourceLocation, parasiteHeadAttachmentOffsetData);
            });
    }

    public static void put(BLibClientMod mod, EntityType<?> entityType, ParasiteHeadAttachmentOffsetData parasiteHeadAttachmentOffsetData) {
        put(mod, BuiltInRegistries.ENTITY_TYPE.getKey(entityType), parasiteHeadAttachmentOffsetData);
    }

    public static void put(
        BLibClientMod mod,
        ResourceLocation resourceLocation,
        ParasiteHeadAttachmentOffsetData parasiteHeadAttachmentOffsetData
    ) {
        CACHE.put(resourceLocation, parasiteHeadAttachmentOffsetData);
    }
}

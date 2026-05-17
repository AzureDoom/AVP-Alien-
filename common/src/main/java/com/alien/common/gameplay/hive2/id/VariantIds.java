package com.alien.common.gameplay.hive2.id;

import com.alien.Alien;
import com.alien.common.model.alien.variant.AlienVariant;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class VariantIds {

    private static final String VARIANT_PATH_PREFIX = "variant/";

    private static final Map<AlienVariant, ResourceLocation> BY_VARIANT = new EnumMap<>(AlienVariant.class);

    private static final Map<ResourceLocation, AlienVariant> BY_ID = new HashMap<>();

    static {
        for (var variant : AlienVariant.VALUES) {
            var id = ResourceLocation.fromNamespaceAndPath(
                Alien.MOD_ID,
                VARIANT_PATH_PREFIX + variant.name().toLowerCase(Locale.ROOT)
            );

            BY_VARIANT.put(variant, id);
            BY_ID.put(id, variant);
        }
    }

    public static ResourceLocation of(AlienVariant variant) {
        return BY_VARIANT.get(variant);
    }

    public static @Nullable AlienVariant variantOf(ResourceLocation id) {
        return BY_ID.get(id);
    }

    public static boolean isVariantId(ResourceLocation id) {
        return Alien.MOD_ID.equals(id.getNamespace()) && id.getPath().startsWith(VARIANT_PATH_PREFIX);
    }

    private VariantIds() {}
}

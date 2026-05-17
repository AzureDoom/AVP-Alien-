package com.alien.common.registry;

import com.alien.common.model.lifecycle.growth.FormSizeScale;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FormSizeScaleRegistry {

    private static final Map<EntityType<?>, FormSizeScale> REGISTRY = new HashMap<>();

    public static @Nullable FormSizeScale get(EntityType<?> entityType) {
        return REGISTRY.get(entityType);
    }

    public static boolean has(EntityType<?> entityType) {
        return REGISTRY.containsKey(entityType);
    }

    public static void clear() {
        REGISTRY.clear();
    }

    public static void register(FormSizeScale formSizeScale) {
        REGISTRY.put(formSizeScale.entityType(), formSizeScale);
    }
}

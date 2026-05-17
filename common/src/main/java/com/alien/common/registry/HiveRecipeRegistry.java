package com.alien.common.registry;

import com.alien.common.gameplay.hive2.economy.HiveRecipe;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Static registry of {@link HiveRecipe} keyed by concrete output entity type. Populated by
 * {@link com.alien.common.data.HiveRecipeReloadListener} on datapack reload.
 */
public final class HiveRecipeRegistry {

    private static final Map<EntityType<?>, HiveRecipe> BY_OUTPUT_ENTITY = new HashMap<>();

    private HiveRecipeRegistry() {}

    public static void clear() {
        BY_OUTPUT_ENTITY.clear();
    }

    public static void register(HiveRecipe recipe) {
        BY_OUTPUT_ENTITY.put(recipe.outputEntity(), recipe);
    }

    public static @Nullable HiveRecipe forOutputEntity(EntityType<?> entityType) {
        return BY_OUTPUT_ENTITY.get(entityType);
    }

    public static Collection<HiveRecipe> all() {
        return Collections.unmodifiableCollection(BY_OUTPUT_ENTITY.values());
    }
}

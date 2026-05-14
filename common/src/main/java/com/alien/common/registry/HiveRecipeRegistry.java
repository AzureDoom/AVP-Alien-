package com.alien.common.registry;

import com.alien.common.gameplay.hive2.economy.HiveRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Static registry of {@link HiveRecipe} keyed by output caste tag. Populated by
 * {@link com.alien.common.data.HiveRecipeReloadListener} on datapack reload.
 */
public final class HiveRecipeRegistry {

    private static final Map<TagKey<EntityType<?>>, HiveRecipe> BY_OUTPUT_CASTE = new HashMap<>();

    private HiveRecipeRegistry() {}

    public static void clear() {
        BY_OUTPUT_CASTE.clear();
    }

    public static void register(HiveRecipe recipe) {
        BY_OUTPUT_CASTE.put(recipe.outputCaste(), recipe);
    }

    public static @Nullable HiveRecipe forOutputCaste(TagKey<EntityType<?>> caste) {
        return BY_OUTPUT_CASTE.get(caste);
    }

    public static Collection<HiveRecipe> all() {
        return Collections.unmodifiableCollection(BY_OUTPUT_CASTE.values());
    }
}

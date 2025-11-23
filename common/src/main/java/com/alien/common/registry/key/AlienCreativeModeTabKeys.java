package com.alien.common.registry.key;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import com.alien.AlienResources;

public class AlienCreativeModeTabKeys {

    public static final ResourceKey<CreativeModeTab> BLOCKS_KEY = createResourceKey("blocks");

    public static final ResourceKey<CreativeModeTab> COMBAT_KEY = createResourceKey("combat");

    public static final ResourceKey<CreativeModeTab> INGREDIENTS_KEY = createResourceKey("ingredients");

    public static final ResourceKey<CreativeModeTab> SPAWN_EGGS_KEY = createResourceKey("spawn_eggs");

    public static final ResourceKey<CreativeModeTab> TOOLS_AND_UTILITIES_KEY = createResourceKey("tools_and_utilities");

    public static ResourceKey<CreativeModeTab> createResourceKey(String name) {
        return ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            AlienResources.location(name)
        );
    }

    public static void initialize() {}
}

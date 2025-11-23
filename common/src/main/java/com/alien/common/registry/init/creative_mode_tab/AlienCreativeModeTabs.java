package com.alien.common.registry.init.creative_mode_tab;

import com.alien.Alien;
import com.alien.AlienResources;
import com.alien.common.registry.init.creative_mode_tab.initializer.BlocksCreativeModeTabInitializer;
import com.alien.common.registry.init.creative_mode_tab.initializer.CombatCreativeModeTabInitializer;
import com.alien.common.registry.init.creative_mode_tab.initializer.IngredientsCreativeModeTabInitializer;
import com.alien.common.registry.init.creative_mode_tab.initializer.SpawnEggsCreativeModeTabInitializer;
import com.alien.common.registry.init.creative_mode_tab.initializer.ToolsAndUtilitiesCreativeModeTabInitializer;
import com.alien.common.registry.key.AlienCreativeModeTabKeys;
import com.avp.common.registry.AVPDeferredHolder;
import com.avp.service.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AlienCreativeModeTabs {

    private static final String BASE_PATH = "creativeModeTab";

    public static final AVPDeferredHolder<CreativeModeTab> BLOCKS = register(
        AlienCreativeModeTabKeys.BLOCKS_KEY,
        () -> new ItemStack(Blocks.CRAFTING_TABLE),
        BlocksCreativeModeTabInitializer.OUTPUT_CONSUMER
    );

    public static final AVPDeferredHolder<CreativeModeTab> COMBAT = register(
        AlienCreativeModeTabKeys.COMBAT_KEY,
        () -> new ItemStack(Blocks.CRAFTING_TABLE),
        CombatCreativeModeTabInitializer.OUTPUT_CONSUMER
    );

    public static final AVPDeferredHolder<CreativeModeTab> INGREDIENTS = register(
        AlienCreativeModeTabKeys.INGREDIENTS_KEY,
        () -> new ItemStack(Blocks.CRAFTING_TABLE),
        IngredientsCreativeModeTabInitializer.OUTPUT_CONSUMER
    );

    public static final AVPDeferredHolder<CreativeModeTab> SPAWN_EGGS = register(
        AlienCreativeModeTabKeys.SPAWN_EGGS_KEY,
        () -> new ItemStack(Blocks.CRAFTING_TABLE),
        SpawnEggsCreativeModeTabInitializer.OUTPUT_CONSUMER
    );

    public static final AVPDeferredHolder<CreativeModeTab> TOOLS_AND_UTILITIES = register(
        AlienCreativeModeTabKeys.TOOLS_AND_UTILITIES_KEY,
        () -> new ItemStack(Blocks.CRAFTING_TABLE),
        ToolsAndUtilitiesCreativeModeTabInitializer.OUTPUT_CONSUMER
    );

    public static AVPDeferredHolder<CreativeModeTab> register(
        ResourceKey<CreativeModeTab> resourceKey,
        Supplier<ItemStack> iconSupplier,
        Consumer<CreativeModeTab.Output> outputConsumer
    ) {
        var path = resourceKey.location().getPath();

        return Services.REGISTRY.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            AlienResources.location(path),
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .icon(iconSupplier)
                .title(Component.translatable(BASE_PATH + "." + Alien.MOD_ID + "." + path))
                .displayItems((itemDisplayParameters, output) -> outputConsumer.accept(output))
                .build()
        );
    }

    public static void initialize() {}
}

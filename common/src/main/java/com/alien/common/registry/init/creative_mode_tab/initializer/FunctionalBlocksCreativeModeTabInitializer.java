package com.alien.common.registry.init.creative_mode_tab.initializer;

import com.alien.common.registry.init.item.AlienXenomorphHeadItems;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Consumer;

public class FunctionalBlocksCreativeModeTabInitializer {

    public static final Consumer<CreativeModeTab.Output> OUTPUT_CONSUMER = output -> AlienXenomorphHeadItems.ALL
        .forEach(entry -> CreativeModeTabUtil.accept(output, entry.head()));
}

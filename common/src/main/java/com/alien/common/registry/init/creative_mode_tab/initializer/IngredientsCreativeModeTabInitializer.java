package com.alien.common.registry.init.creative_mode_tab.initializer;

import com.alien.common.registry.init.item.AlienItems;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Consumer;

public class IngredientsCreativeModeTabInitializer {

    public static final Consumer<CreativeModeTab.Output> OUTPUT_CONSUMER = output -> {
        // Alien materials
        CreativeModeTabUtil.accept(output, AlienItems.RESIN_BALL);
        CreativeModeTabUtil.accept(output, AlienItems.CHITIN);
        CreativeModeTabUtil.accept(output, AlienItems.PLATED_CHITIN);
        CreativeModeTabUtil.accept(output, AlienItems.NETHER_RESIN_BALL);
        CreativeModeTabUtil.accept(output, AlienItems.NETHER_CHITIN);
        CreativeModeTabUtil.accept(output, AlienItems.PLATED_NETHER_CHITIN);
        CreativeModeTabUtil.accept(output, AlienItems.ABERRANT_RESIN_BALL);
        CreativeModeTabUtil.accept(output, AlienItems.ABERRANT_CHITIN);
        CreativeModeTabUtil.accept(output, AlienItems.PLATED_ABERRANT_CHITIN);
        CreativeModeTabUtil.accept(output, AlienItems.IRRADIATED_RESIN_BALL);
        CreativeModeTabUtil.accept(output, AlienItems.IRRADIATED_CHITIN);
        CreativeModeTabUtil.accept(output, AlienItems.PLATED_IRRADIATED_CHITIN);
        CreativeModeTabUtil.accept(output, AlienItems.RAW_ROYAL_JELLY);
        CreativeModeTabUtil.accept(output, AlienItems.POISON_JELLY);

        // Decorative materials
        CreativeModeTabUtil.accept(output, AlienItems.OVOID_POTTERY_SHERD);
        CreativeModeTabUtil.accept(output, AlienItems.PARASITE_POTTERY_SHERD);
        CreativeModeTabUtil.accept(output, AlienItems.ROYALTY_POTTERY_SHERD);
        CreativeModeTabUtil.accept(output, AlienItems.VECTOR_POTTERY_SHERD);
        CreativeModeTabUtil.accept(output, AlienItems.ALIEN_MUSIC_DISC_1_FRAGMENT);
    };
}

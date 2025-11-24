package com.alien.fabric.data.lang.en_us.provider;

import com.alien.common.registry.key.AlienCreativeModeTabKeys;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.function.Consumer;

public class EnUsCreativeModeTabProvider {

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {
        builder.add(AlienCreativeModeTabKeys.BLOCKS_KEY, "Alien Blocks");
        builder.add(AlienCreativeModeTabKeys.COMBAT_KEY, "Alien Combat");
        builder.add(AlienCreativeModeTabKeys.INGREDIENTS_KEY, "Alien Ingredients");
        builder.add(AlienCreativeModeTabKeys.SPAWN_EGGS_KEY, "Alien Spawn Eggs");
        builder.add(AlienCreativeModeTabKeys.TOOLS_AND_UTILITIES_KEY, "Alien Tools & Utilities");
    };
}

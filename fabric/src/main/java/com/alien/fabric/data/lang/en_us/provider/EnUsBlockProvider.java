package com.alien.fabric.data.lang.en_us.provider;

import com.avp.common.registry.AVPRegistryValidation;
import com.alien.common.registry.init.AlienBlocks;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnUsBlockProvider {

    private static final HashSet<Block> TOUCHED_ENTRIES = new HashSet<>();

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {
        AVPRegistryValidation.throwIfMissingEntries(
            AlienBlocks.getAll(),
            TOUCHED_ENTRIES::contains,
            Block::getDescriptionId,
            "Block translation did not complete successfully - there are unhandled blocks that need to be handled."
        );
    };

    private static void addBlock(
        FabricLanguageProvider.TranslationBuilder translationBuilder,
        Supplier<? extends Block> blockSupplier,
        String value
    ) {
        addBlock(translationBuilder, blockSupplier.get(), value);
    }

    private static void addBlock(FabricLanguageProvider.TranslationBuilder translationBuilder, Block block, String value) {
        TOUCHED_ENTRIES.add(block);
        translationBuilder.add(block, value);
    }
}

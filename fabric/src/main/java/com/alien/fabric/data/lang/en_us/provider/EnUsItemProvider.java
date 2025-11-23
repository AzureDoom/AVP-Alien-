package com.alien.fabric.data.lang.en_us.provider;

import com.alien.common.registry.init.item.AlienItems;
import com.avp.common.registry.AVPRegistryValidation;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnUsItemProvider {

    private static final HashSet<Item> TOUCHED_ENTRIES = new HashSet<>();

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {

        AVPRegistryValidation.throwIfMissingEntries(
            AlienItems.getAll()
                .stream()
                .filter(deferredHolder -> !(deferredHolder.get() instanceof BlockItem))
                .toList(),
            TOUCHED_ENTRIES::contains,
            Item::getDescriptionId,
            "Item translation did not complete successfully - there are unhandled items that need to be handled."
        );
    };

    private static void addItem(
        FabricLanguageProvider.TranslationBuilder translationBuilder,
        Supplier<? extends Item> itemSupplier,
        String value
    ) {
        addItem(translationBuilder, itemSupplier.get(), value);
    }

    private static void addItem(FabricLanguageProvider.TranslationBuilder translationBuilder, Item item, String value) {
        TOUCHED_ENTRIES.add(item);
        translationBuilder.add(item, value);
    }

}

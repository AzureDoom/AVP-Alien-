package com.alien.fabric.data.lang.en_us.provider;

import com.alien.common.registry.tag.AlienItemTags;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.function.Consumer;

public class EnUsItemTagProvider {

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {
        builder.add(AlienItemTags.ABERRANT_CHITIN_ARMOR, "Aberrant Chitin Armor");
        builder.add(AlienItemTags.ACID_IMMUNE, "Acid Immune");
        builder.add(AlienItemTags.CHITIN_ARMORS, "Chitin Armors");
        builder.add(AlienItemTags.FACEHUGGER_RESISTANT_HELMETS, "Facehugger-Resistant Helmets");
        builder.add(AlienItemTags.IRRADIATED_CHITIN_ARMOR, "Irradiated Chitin Armor");
        builder.add(AlienItemTags.NETHER_CHITIN_ARMOR, "Nether Chitin Armor");
        builder.add(AlienItemTags.NORMAL_CHITIN_ARMOR, "Chitin Armor");
        builder.add(AlienItemTags.PLATED_ABERRANT_CHITIN_ARMOR, "Plated Aberrant Chitin Armor");
        builder.add(AlienItemTags.PLATED_CHITIN_ARMORS, "Plated Chitin Armors");
        builder.add(AlienItemTags.PLATED_IRRADIATED_CHITIN_ARMOR, "Plated Irradiated Chitin Armor");
        builder.add(AlienItemTags.PLATED_NETHER_CHITIN_ARMOR, "Plated Nether Chitin Armor");
        builder.add(AlienItemTags.PLATED_NORMAL_CHITIN_ARMOR, "Plated Normal Chitin Armor");
    };
}

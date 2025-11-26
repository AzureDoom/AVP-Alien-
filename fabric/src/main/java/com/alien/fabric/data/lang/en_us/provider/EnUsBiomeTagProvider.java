package com.alien.fabric.data.lang.en_us.provider;

import com.alien.common.registry.tag.AlienBiomeTags;
import com.avp.common.registry.tag.AVPBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.function.Consumer;

public class EnUsBiomeTagProvider {

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {
        builder.add(AlienBiomeTags.HAS_ALTAR, "Has Altar");
        builder.add(AlienBiomeTags.HAS_BADLANDS_ALTAR, "Has Badlands Altar");
        builder.add(AlienBiomeTags.HAS_DESERT_ALTAR, "Has Desert Altar");
        builder.add(AlienBiomeTags.HAS_DEEPSLATE_ALTAR, "Has Deepslate Altar");
        builder.add(AlienBiomeTags.HAS_JUNGLE_ALTAR, "Has Jungle Altar");
        builder.add(AlienBiomeTags.HAS_NETHER_ALTAR, "Has Nether Altar");
        builder.add(AlienBiomeTags.HAS_XENOMORPHS, "Has Xenomorphs");
    };
}

package com.alien.fabric.data.lang.en_us.provider;

import com.lib.common.data.AdvancementAccess;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.function.Consumer;

public class EnUsAdvancementProvider {

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {
    };

    private static void addAdvancement(
        FabricLanguageProvider.TranslationBuilder builder,
        AdvancementAccess advancementAccess,
        String title,
        String description
    ) {
        builder.add(advancementAccess.getTitleTranslationKey(), title);
        builder.add(advancementAccess.getDescriptionTranslationKey(), description);
    }
}

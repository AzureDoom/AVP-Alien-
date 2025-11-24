package com.alien.fabric.data.lang.en_us.provider;

import com.alien.common.registry.tag.AlienMobEffectTags;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.function.Consumer;

public class EnUsMobEffectTagProvider {

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {
        builder.add(AlienMobEffectTags.DOES_NOT_AFFECT_ALIENS, "Does Not Affect Aliens");
    };
}

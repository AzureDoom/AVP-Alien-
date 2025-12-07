package com.alien.fabric.data.lang.en_us.provider;

import com.alien.common.registry.tag.AlienDamageTypesTags;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.function.Consumer;

public class EnUsDamageTypeTagProvider {

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {
        builder.add(AlienDamageTypesTags.DOES_NOT_HURT_ALIENS, "Does Not Hurt Aliens");
    };
}

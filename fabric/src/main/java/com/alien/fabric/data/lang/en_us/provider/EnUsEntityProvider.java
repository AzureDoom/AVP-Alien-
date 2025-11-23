package com.alien.fabric.data.lang.en_us.provider;

import com.alien.common.registry.init.AlienEntityTypes;
import com.avp.common.registry.AVPRegistryValidation;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.world.entity.EntityType;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnUsEntityProvider {

    private static final HashSet<EntityType<?>> TOUCHED_ENTRIES = new HashSet<>();

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {
        AVPRegistryValidation.throwIfMissingEntries(
            AlienEntityTypes.getAll(),
            TOUCHED_ENTRIES::contains,
            EntityType::getDescriptionId,
            "Entity type translation did not complete successfully - there are unhandled entity types that need to be handled."
        );
    };

    private static void addEntity(
        FabricLanguageProvider.TranslationBuilder translationBuilder,
        Supplier<? extends EntityType<?>> entityTypeSupplier,
        String value
    ) {
        addEntity(translationBuilder, entityTypeSupplier.get(), value);
    }

    private static void addEntity(FabricLanguageProvider.TranslationBuilder translationBuilder, EntityType<?> entityType, String value) {
        TOUCHED_ENTRIES.add(entityType);
        translationBuilder.add(entityType, value);
    }
}

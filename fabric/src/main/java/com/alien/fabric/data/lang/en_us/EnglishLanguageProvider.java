package com.alien.fabric.data.lang.en_us;

import com.alien.common.gameplay.hive.HiveBossBarManager;
import com.alien.fabric.data.lang.en_us.provider.EnUsAdvancementProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsBiomeTagProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsBlockProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsBlockTagProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsConfigProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsCreativeModeTabProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsEntityProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsEntityTypeTagProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsItemProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsItemTagProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsMobEffectTagProvider;
import com.alien.fabric.data.lang.en_us.provider.EnUsSoundEventProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class EnglishLanguageProvider extends FabricLanguageProvider {

    public EnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
        // Blocks
        EnUsBlockProvider.CONSUMER.accept(builder);

        // Creative Mode Tabs
        EnUsCreativeModeTabProvider.CONSUMER.accept(builder);

        // Entities
        EnUsEntityProvider.CONSUMER.accept(builder);

        // Items
        EnUsItemProvider.CONSUMER.accept(builder);

        // Sounds
        EnUsSoundEventProvider.CONSUMER.accept(builder);

        // Jukebox Sounds
        builder.add("jukebox_song.avp.alien_music_1", "Rotch Gwylt - Silver Smile");

        // Advancements
        EnUsAdvancementProvider.CONSUMER.accept(builder);

        // Hive boss bars
        HiveBossBarManager.ALIEN_VARIANT_TO_TRANSLATABLE_STRING_MAP.forEach((alienVariant, translationKey) -> {
            var prefix = switch (alienVariant) {
                case ABERRANT -> "Aberrant ";
                case IRRADIATED -> "Irradiated ";
                case NETHER -> "Nether ";
                case NORMAL -> "";
            };

            builder.add(translationKey, prefix + "Hive");
        });

        // Configs
        EnUsConfigProvider.CONSUMER.accept(builder);

        // Tags
        EnUsBlockTagProvider.CONSUMER.accept(builder);
        EnUsItemTagProvider.CONSUMER.accept(builder);
        EnUsEntityTypeTagProvider.CONSUMER.accept(builder);
        EnUsMobEffectTagProvider.CONSUMER.accept(builder);
        EnUsBiomeTagProvider.CONSUMER.accept(builder);
    }
}

package com.alien.fabric.data;

import com.alien.compatibility.avp_human.AVPHuman;
import com.alien.fabric.data.advancement.AdvancementProvider;
import com.alien.fabric.data.damage_type.DamageTypeBootstrapper;
import com.alien.fabric.data.damage_type.DamageTypeProvider;
import com.alien.fabric.data.form_size_scale.FormSizeScaleSubProvider;
import com.alien.fabric.data.gene_bonus_data.GeneBonusDataSubProvider;
import com.alien.fabric.data.growth_stages.GrowthStageSubProvider;
import com.alien.fabric.data.infections.InfectionSubProvider;
import com.alien.fabric.data.jukebox_song.AlienJukeboxSongsProvider;
import com.alien.fabric.data.lang.en_us.EnglishLanguageProvider;
import com.alien.fabric.data.loot.BlockLootTableProvider;
import com.alien.fabric.data.loot.EntityLootTableProvider;
import com.alien.fabric.data.model.BlockModelProvider;
import com.alien.fabric.data.model.ItemModelProvider;
import com.alien.fabric.data.recipe.RecipeProvider;
import com.alien.fabric.data.tag.AlienBiomeTagProvider;
import com.alien.fabric.data.tag.AlienBlockTagProvider;
import com.alien.fabric.data.tag.AlienDamageTypeTagProvider;
import com.alien.fabric.data.tag.AlienEntityTypeTagProvider;
import com.alien.fabric.data.tag.AlienItemTagProvider;
import com.alien.fabric.data.tag.AlienMobEffectTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class AlienDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        // Advancement providers
        pack.addProvider(AdvancementProvider::new);

        // Damage Type providers
        pack.addProvider(DamageTypeProvider::new);

        // Language providers
        pack.addProvider(EnglishLanguageProvider::new);

        // Model providers
        pack.addProvider(BlockModelProvider::new);
        pack.addProvider(ItemModelProvider::new);

        // Recipe providers
        pack.addProvider(RecipeProvider::new);

        // Tag providers
        pack.addProvider(AlienBlockTagProvider::new);
        pack.addProvider(AlienBiomeTagProvider::new);
        pack.addProvider(AlienDamageTypeTagProvider::new);
        pack.addProvider(AlienEntityTypeTagProvider::new);
        pack.addProvider(AlienItemTagProvider::new);
        pack.addProvider(AlienMobEffectTagProvider::new);

        // Loot providers
        pack.addProvider(BlockLootTableProvider::new);
        pack.addProvider(EntityLootTableProvider::new);

        // Jukebox Song Providers
        pack.addProvider(AlienJukeboxSongsProvider::new);

        // Custom Providers
        if (AVPHuman.MOD.isLoaded()) {
            pack.addProvider(GeneBonusDataSubProvider::new);
        }

        pack.addProvider(FormSizeScaleSubProvider::new);
        pack.addProvider(GrowthStageSubProvider::new);
        pack.addProvider(com.alien.fabric.data.hive_recipe.HiveRecipeDataProvider::new);
        pack.addProvider(InfectionSubProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.DAMAGE_TYPE, DamageTypeBootstrapper::bootstrap);
    }
}

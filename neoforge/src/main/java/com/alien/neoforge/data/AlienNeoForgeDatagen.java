package com.alien.neoforge.data;

import com.alien.Alien;
import com.alien.AlienResources;
import com.avp.neoforge.service.NeoForgeRegistryService;
import com.avp.service.Services;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = Alien.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AlienNeoForgeDatagen {

    private static final NeoForgeRegistryService REGISTRY = (NeoForgeRegistryService) Services.REGISTRY;

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();

        generator.addProvider(
            event.includeServer(),
            (DataProvider.Factory<DatapackBuiltinEntriesProvider>) output -> new DatapackBuiltinEntriesProvider(
                event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                new RegistrySetBuilder()
                    .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {
                        var biomes = bootstrap.lookup(Registries.BIOME);
                        for (var spawnData : REGISTRY.getEntitySpawnDataEntries()) {
                            if (spawnData.isConfigDisabled()) {
                                continue;
                            }

                            var entityType = spawnData.getEntityType();
                            var entityTypePath = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath();
                            var spawnKey = ResourceKey.create(
                                NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                                AlienResources.location("add_spawns_" + entityTypePath)
                            );
                            var config = spawnData.getConfigData();
                            var spawnSettings = config.spawnSettings();

                            bootstrap.register(
                                spawnKey,
                                new BiomeModifiers.AddSpawnsBiomeModifier(
                                    biomes.getOrThrow(config.biomeTagKey()),
                                    List.of(
                                        new MobSpawnSettings.SpawnerData(
                                            entityType,
                                            spawnSettings.weight,
                                            spawnSettings.minGroupSize,
                                            spawnSettings.maxGroupSize
                                        )
                                    )
                                )
                            );
                        }
                    }),
                Set.of(Alien.MOD_ID)
            )
        );
    }

    private AlienNeoForgeDatagen() {}
}

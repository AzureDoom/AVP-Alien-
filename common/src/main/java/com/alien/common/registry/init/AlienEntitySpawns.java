package com.alien.common.registry.init;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.AlienSpawning;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.QueenSpawning;
import com.alien.common.registry.tag.AlienBiomeTags;
import com.alien.compatibility.avp_human.AVPHuman;
import com.blib.api.common.entity.v1.spawning.BLibEntitySpawnData;
import com.blib.api.common.entity.v1.spawning.SpawnSettings;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.impl.BLibEntitySpawnRegistry;
import net.minecraft.world.entity.EntityType;

public class AlienEntitySpawns {

    private static final BLibEntitySpawnRegistry REGISTRY = com.alien.Alien.MOD.registries().createEntitySpawnRegistry();

    private static final SpawnSettings SPAWN_SETTINGS = new SpawnSettings(true, 1, 1, 100);

    public static void initialize() {
        registerNetherAlienSpawns();
        registerNormalAlienSpawns();

        if (AVPHuman.MOD.isLoaded()) {
            registerAberrantAlienSpawns();
            registerIrradiatedAlienSpawns();
        }
    }

    private static void registerNormalAlienSpawns() {
        register(AlienEntityTypes.CHESTBURSTER, SPAWN_SETTINGS);
        register(AlienEntityTypes.CRUSHER, SPAWN_SETTINGS);
        register(AlienEntityTypes.DRONE, SPAWN_SETTINGS);
        register(AlienEntityTypes.WARRIOR, SPAWN_SETTINGS);
        register(AlienEntityTypes.PRAETORIAN, SPAWN_SETTINGS);
        register(AlienEntityTypes.PROWLER, SPAWN_SETTINGS);
        REGISTRY.register(
            BLibEntitySpawnData.builder(AlienEntityTypes.QUEEN)
                .withBiomeTagKey(AlienBiomeTags.HAS_XENOMORPHS)
                .withSpawnPredicate(QueenSpawning.PREDICATE)
                .withSpawnSettings(SPAWN_SETTINGS)
                .build()
        );
        register(AlienEntityTypes.RUNNER, SPAWN_SETTINGS);
        register(AlienEntityTypes.SPITTER, SPAWN_SETTINGS);
    }

    private static void registerAberrantAlienSpawns() {
        register(AlienEntityTypes.ABERRANT_CHESTBURSTER, SPAWN_SETTINGS);
        register(AlienEntityTypes.ABERRANT_CRUSHER, SPAWN_SETTINGS);
        register(AlienEntityTypes.ABERRANT_DRONE, SPAWN_SETTINGS);
        register(AlienEntityTypes.ABERRANT_WARRIOR, SPAWN_SETTINGS);
        register(AlienEntityTypes.ABERRANT_PRAETORIAN, SPAWN_SETTINGS);
        register(AlienEntityTypes.ABERRANT_PROWLER, SPAWN_SETTINGS);
        register(AlienEntityTypes.ABERRANT_RUNNER, SPAWN_SETTINGS);
        REGISTRY.register(
            BLibEntitySpawnData.builder(AlienEntityTypes.ABERRANT_QUEEN)
                .withBiomeTagKey(AlienBiomeTags.HAS_XENOMORPHS)
                .withSpawnPredicate(QueenSpawning.PREDICATE)
                .withSpawnSettings(SPAWN_SETTINGS)
                .build()
        );
        register(AlienEntityTypes.ABERRANT_SPITTER, SPAWN_SETTINGS);
    }

    private static void registerIrradiatedAlienSpawns() {
        register(AlienEntityTypes.IRRADIATED_CRUSHER, SPAWN_SETTINGS);
        register(AlienEntityTypes.IRRADIATED_DRONE, SPAWN_SETTINGS);
        register(AlienEntityTypes.IRRADIATED_WARRIOR, SPAWN_SETTINGS);
        register(AlienEntityTypes.IRRADIATED_PRAETORIAN, SPAWN_SETTINGS);
        register(AlienEntityTypes.IRRADIATED_PROWLER, SPAWN_SETTINGS);
        register(AlienEntityTypes.IRRADIATED_RUNNER, SPAWN_SETTINGS);
        REGISTRY.register(
            BLibEntitySpawnData.builder(AlienEntityTypes.IRRADIATED_QUEEN)
                .withBiomeTagKey(AlienBiomeTags.HAS_XENOMORPHS)
                .withSpawnPredicate(QueenSpawning.PREDICATE)
                .withSpawnSettings(SPAWN_SETTINGS)
                .build()
        );
    }

    private static void registerNetherAlienSpawns() {
        register(AlienEntityTypes.NETHER_CHESTBURSTER, SPAWN_SETTINGS);
        register(AlienEntityTypes.NETHER_CRUSHER, SPAWN_SETTINGS);
        register(AlienEntityTypes.NETHER_DRONE, SPAWN_SETTINGS);
        register(AlienEntityTypes.NETHER_WARRIOR, SPAWN_SETTINGS);
        register(AlienEntityTypes.NETHER_PRAETORIAN, SPAWN_SETTINGS);
        register(AlienEntityTypes.NETHER_PROWLER, SPAWN_SETTINGS);
        register(AlienEntityTypes.NETHER_RUNNER, SPAWN_SETTINGS);
        register(AlienEntityTypes.NETHER_SPITTER, SPAWN_SETTINGS);

        REGISTRY.register(
            BLibEntitySpawnData.builder(AlienEntityTypes.NETHER_QUEEN)
                .withBiomeTagKey(AlienBiomeTags.HAS_XENOMORPHS)
                .withSpawnPredicate(QueenSpawning.PREDICATE)
                .withSpawnSettings(SPAWN_SETTINGS)
                .build()
        );
    }

    private static <T extends Alien> void register(BLibHolder<EntityType<T>> entityType, SpawnSettings spawn) {
        REGISTRY.register(
            BLibEntitySpawnData.builder(entityType)
                .withBiomeTagKey(AlienBiomeTags.HAS_XENOMORPHS)
                .withSpawnPredicate(AlienSpawning.getTypedPredicate())
                .withSpawnSettings(spawn)
                .build()
        );
    }
}

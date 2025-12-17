package com.alien.common.registry.init;

import com.alien.common.config.AlienConfig;
import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.AlienSpawning;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.QueenSpawning;
import com.alien.common.registry.tag.AlienBiomeTags;
import com.alien.compatibility.avp_human.AVPHuman;
import com.blib.common.gameplay.model.spawning.BLibEntitySpawnData;
import com.blib.common.gameplay.model.spawning.SpawnSettings;
import com.blib.common.registry.BLibHolder;
import com.blib.common.registry.impl.BLibEntitySpawnRegistry;
import com.human.common.registry.tag.HumanBiomeTags;
import net.minecraft.world.entity.EntityType;

public class AlienEntitySpawns {

    private static final BLibEntitySpawnRegistry REGISTRY = com.alien.Alien.MOD.registries().createEntitySpawnRegistry();;

    public static void initialize() {
        registerNetherAlienSpawns();
        registerNormalAlienSpawns();

        if (AVPHuman.MOD.isLoaded()) {
            registerAberrantAlienSpawns();
            registerIrradiatedAlienSpawns();
        }
    }

    private static void registerNormalAlienSpawns() {
        register(AlienEntityTypes.CHESTBURSTER, AlienConfig.INSTANCE.spawnConfigs.CHESTBURSTER_SPAWN);
        register(AlienEntityTypes.CRUSHER, AlienConfig.INSTANCE.spawnConfigs.CRUSHER_SPAWN);
        register(AlienEntityTypes.DRONE, AlienConfig.INSTANCE.spawnConfigs.DRONE_SPAWN);
        register(AlienEntityTypes.WARRIOR, AlienConfig.INSTANCE.spawnConfigs.WARRIOR_SPAWN);
        register(AlienEntityTypes.PRAETORIAN, AlienConfig.INSTANCE.spawnConfigs.PRAETORIAN_SPAWN);
        register(AlienEntityTypes.PROWLER, AlienConfig.INSTANCE.spawnConfigs.PROWLER_SPAWN);
        REGISTRY.register(
            BLibEntitySpawnData.builder(AlienEntityTypes.QUEEN)
                .withBiomeTagKey(AlienBiomeTags.HAS_XENOMORPHS)
                .withSpawnPredicate(QueenSpawning.PREDICATE)
                .withSpawnSettings(convert(AlienConfig.INSTANCE.spawnConfigs.QUEEN_SPAWN))
                .build()
        );
        register(AlienEntityTypes.RUNNER, AlienConfig.INSTANCE.spawnConfigs.RUNNER_SPAWN);
        register(AlienEntityTypes.SPITTER, AlienConfig.INSTANCE.spawnConfigs.SPITTER_SPAWN);
    }

    private static void registerAberrantAlienSpawns() {
        register(AlienEntityTypes.ABERRANT_CHESTBURSTER, AlienConfig.INSTANCE.spawnConfigs.ABERRANT_CHESTBURSTER_SPAWN);
        register(AlienEntityTypes.ABERRANT_CRUSHER, AlienConfig.INSTANCE.spawnConfigs.ABERRANT_CRUSHER_SPAWN);
        register(AlienEntityTypes.ABERRANT_DRONE, AlienConfig.INSTANCE.spawnConfigs.ABERRANT_DRONE_SPAWN);
        register(AlienEntityTypes.ABERRANT_WARRIOR, AlienConfig.INSTANCE.spawnConfigs.ABERRANT_WARRIOR_SPAWN);
        register(AlienEntityTypes.ABERRANT_PRAETORIAN, AlienConfig.INSTANCE.spawnConfigs.ABERRANT_PRAETORIAN_SPAWN);
        register(AlienEntityTypes.ABERRANT_PROWLER, AlienConfig.INSTANCE.spawnConfigs.ABERRANT_PROWLER_SPAWN);
        register(AlienEntityTypes.ABERRANT_RUNNER, AlienConfig.INSTANCE.spawnConfigs.ABERRANT_RUNNER_SPAWN);
        REGISTRY.register(
            BLibEntitySpawnData.builder(AlienEntityTypes.ABERRANT_QUEEN)
                .withBiomeTagKey(AlienBiomeTags.HAS_XENOMORPHS)
                .withSpawnPredicate(QueenSpawning.PREDICATE)
                .withSpawnSettings(convert(AlienConfig.INSTANCE.spawnConfigs.ABERRANT_QUEEN_SPAWN))
                .build()
        );
        register(AlienEntityTypes.ABERRANT_SPITTER, AlienConfig.INSTANCE.spawnConfigs.ABERRANT_SPITTER_SPAWN);
    }

    private static void registerIrradiatedAlienSpawns() {
        register(AlienEntityTypes.IRRADIATED_CRUSHER, AlienConfig.INSTANCE.spawnConfigs.IRRADIATED_CRUSHER_SPAWN);
        register(AlienEntityTypes.IRRADIATED_DRONE, AlienConfig.INSTANCE.spawnConfigs.IRRADIATED_DRONE_SPAWN);
        register(AlienEntityTypes.IRRADIATED_WARRIOR, AlienConfig.INSTANCE.spawnConfigs.IRRADIATED_WARRIOR_SPAWN);
        register(AlienEntityTypes.IRRADIATED_PRAETORIAN, AlienConfig.INSTANCE.spawnConfigs.IRRADIATED_PRAETORIAN_SPAWN);
        register(AlienEntityTypes.IRRADIATED_PROWLER, AlienConfig.INSTANCE.spawnConfigs.IRRADIATED_PROWLER_SPAWN);
        register(AlienEntityTypes.IRRADIATED_RUNNER, AlienConfig.INSTANCE.spawnConfigs.IRRADIATED_RUNNER_SPAWN);
        REGISTRY.register(
            BLibEntitySpawnData.builder(AlienEntityTypes.IRRADIATED_QUEEN)
                .withBiomeTagKey(HumanBiomeTags.IS_IRRADIATED)
                .withSpawnPredicate(QueenSpawning.PREDICATE)
                .withSpawnSettings(convert(AlienConfig.INSTANCE.spawnConfigs.IRRADIATED_QUEEN_SPAWN))
                .build()
        );
    }

    private static void registerNetherAlienSpawns() {
        register(AlienEntityTypes.NETHER_CHESTBURSTER, AlienConfig.INSTANCE.spawnConfigs.NETHER_CHESTBURSTER_SPAWN);
        register(AlienEntityTypes.NETHER_CRUSHER, AlienConfig.INSTANCE.spawnConfigs.NETHER_CRUSHER_SPAWN);
        register(AlienEntityTypes.NETHER_DRONE, AlienConfig.INSTANCE.spawnConfigs.NETHER_DRONE_SPAWN);
        register(AlienEntityTypes.NETHER_WARRIOR, AlienConfig.INSTANCE.spawnConfigs.NETHER_WARRIOR_SPAWN);
        register(AlienEntityTypes.NETHER_PRAETORIAN, AlienConfig.INSTANCE.spawnConfigs.NETHER_PRAETORIAN_SPAWN);
        register(AlienEntityTypes.NETHER_PROWLER, AlienConfig.INSTANCE.spawnConfigs.NETHER_PROWLER_SPAWN);
        register(AlienEntityTypes.NETHER_RUNNER, AlienConfig.INSTANCE.spawnConfigs.NETHER_RUNNER_SPAWN);
        register(AlienEntityTypes.NETHER_SPITTER, AlienConfig.INSTANCE.spawnConfigs.NETHER_SPITTER_SPAWN);

        REGISTRY.register(
            BLibEntitySpawnData.builder(AlienEntityTypes.NETHER_QUEEN)
                .withBiomeTagKey(AlienBiomeTags.HAS_XENOMORPHS)
                .withSpawnPredicate(QueenSpawning.PREDICATE)
                .withSpawnSettings(convert(AlienConfig.INSTANCE.spawnConfigs.NETHER_QUEEN_SPAWN))
                .build()
        );
    }

    private static <T extends Alien> void register(BLibHolder<EntityType<T>> entityType, AlienConfig.SpawnConfigs.SpawnSettings spawn) {
        REGISTRY.register(
            BLibEntitySpawnData.builder(entityType)
                .withBiomeTagKey(AlienBiomeTags.HAS_XENOMORPHS)
                .withSpawnPredicate(AlienSpawning.getTypedPredicate())
                .withSpawnSettings(convert(spawn))
                .build()
        );
    }

    private static SpawnSettings convert(AlienConfig.SpawnConfigs.SpawnSettings spawnSettings) {
        return new SpawnSettings(
            spawnSettings.enabled,
            spawnSettings.minGroupSize,
            spawnSettings.maxGroupSize,
            spawnSettings.weight
        );
    }
}

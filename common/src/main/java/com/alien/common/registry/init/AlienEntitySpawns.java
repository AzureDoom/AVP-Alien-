package com.alien.common.registry.init;

import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.QueenSpawning;
import com.alien.common.registry.tag.AlienBiomeTags;
import com.blib.api.common.entity.v1.spawning.BLibEntitySpawnData;
import com.blib.api.common.entity.v1.spawning.SpawnSettings;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.impl.BLibEntitySpawnRegistry;
import net.minecraft.world.entity.EntityType;

public class AlienEntitySpawns {

    private static final BLibEntitySpawnRegistry REGISTRY = com.alien.Alien.MOD.registries().createEntitySpawnRegistry();

    private static final SpawnSettings QUEEN_SPAWN_SETTINGS = new SpawnSettings(true, 1, 1, 100);

    public static void initialize() {
        registerQueen(AlienEntityTypes.QUEEN);
        registerQueen(AlienEntityTypes.ABERRANT_QUEEN);
        registerQueen(AlienEntityTypes.IRRADIATED_QUEEN);
        registerQueen(AlienEntityTypes.NETHER_QUEEN);
    }

    private static void registerQueen(BLibHolder<EntityType<Queen>> entityType) {
        REGISTRY.register(
            BLibEntitySpawnData.builder(entityType)
                .withBiomeTagKey(AlienBiomeTags.HAS_XENOMORPHS)
                .withSpawnPredicate(QueenSpawning.PREDICATE)
                .withSpawnSettings(QUEEN_SPAWN_SETTINGS)
                .build()
        );
    }
}

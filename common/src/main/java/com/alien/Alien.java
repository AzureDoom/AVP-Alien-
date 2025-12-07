package com.alien;

import com.alien.common.AlienEvents;
import com.alien.common.config.AlienConfig;
import com.alien.common.data.AlienReloadListeners;
import com.alien.common.data.fixer.migration.AlienDataMigrations;
import com.alien.common.gameplay.level.saveddata.HiveLevelData;
import com.alien.common.gameplay.level.saveddata.QueenSpawnChunkData;
import com.alien.common.registry.init.AlienArmorMaterials;
import com.alien.common.registry.init.AlienBlockEntityTypes;
import com.alien.common.registry.init.AlienCompostingChances;
import com.alien.common.registry.init.AlienDataKeys;
import com.alien.common.registry.init.AlienDecoratedPotPatterns;
import com.alien.common.registry.init.AlienEntitySpawns;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienGameEvents;
import com.alien.common.registry.init.AlienParticleTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.init.block.AlienBlocks;
import com.alien.common.registry.init.block.AlienChitinBlocks;
import com.alien.common.registry.init.block.AlienResinBlocks;
import com.alien.common.registry.init.creative_mode_tab.AlienCreativeModeTabs;
import com.alien.common.registry.init.item.AlienArmorItems;
import com.alien.common.registry.init.item.AlienItems;
import com.alien.common.registry.init.item.AlienSpawnEggItems;
import com.alien.common.registry.init.item.block.AlienBlockItems;
import com.alien.common.registry.init.item.block.AlienChitinBlockItems;
import com.alien.common.registry.init.item.block.AlienResinBlockItems;
import com.blib.BLib;
import com.blib.BLibMod;
import com.blib.service.BLibServices;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Alien {

    public static final String MOD_ID = "avp_alien";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final BLibMod MOD = BLib.createMod(MOD_ID);

    public static void initialize() {
        LOGGER.info("Initializing AVP (Alien) for mod loader '{}'", BLibServices.MOD_LOADER.getModLoaderName());

        AlienConfig.initialize();

        // No dependencies.
        AlienBlocks.initialize();
        AlienChitinBlocks.initialize();
        AlienResinBlocks.initialize();
        AlienItems.initialize();
        AlienEntityTypes.initialize();
        AlienSoundEvents.initialize();

        // Depends on blocks.
        AlienBlockItems.initialize();
        AlienChitinBlockItems.initialize();
        AlienResinBlockItems.initialize();
        // Depends on sound events.
        AlienArmorMaterials.initialize();
        // Depends on armor materials.
        AlienArmorItems.initialize();
        // Depends on entity types.
        AlienSpawnEggItems.initialize();
        // Depends on blocks.
        AlienBlockEntityTypes.initialize();
        // Depends on blocks, items, block items, etc.
        AlienCreativeModeTabs.initialize();

        AlienGameEvents.initialize();
        AlienParticleTypes.initialize();

        // Functionality
        AlienDecoratedPotPatterns.initialize();
        AlienCompostingChances.initialize();
        AlienDataKeys.initialize();
        AlienEntitySpawns.initialize();

        // Data Migration
        AlienDataMigrations.initialize();

        // Listeners/Events
        AlienReloadListeners.initialize();

        BLibServices.EVENT.afterLevelTick().register(Alien::tickHivesInLevel);
        BLibServices.EVENT.afterLevelTick().register(Alien::tickQueenSpawnCooldown);
        BLibServices.EVENT.onTagsUpdated().register(($1, $2) -> AlienEvents.onTagsUpdated());
    }

    private static void tickHivesInLevel(Level level) {
        if (level.isClientSide) {
            return;
        }

        HiveLevelData.getOrCreate(level)
            .ifSome(HiveLevelData::tick);
    }

    private static void tickQueenSpawnCooldown(Level level) {
        if (level.isClientSide) {
            return;
        }

        QueenSpawnChunkData.getOrCreate(level)
            .ifSome(QueenSpawnChunkData::tick);
    }
}

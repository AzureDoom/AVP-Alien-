package com.alien;

import com.alien.common.config.AlienConfig;
import com.alien.common.data.AlienReloadListeners;
import com.alien.common.data.fixer.migration.AlienDataMigrations;
import com.alien.common.gameplay.level.saveddata.HiveLevelData;
import com.alien.common.gameplay.level.saveddata.QueenSpawnChunkData;
import com.alien.common.registry.GrowthStageRegistry;
import com.alien.common.registry.InfectionRegistry;
import com.alien.common.registry.init.AlienArmorMaterials;
import com.alien.common.registry.init.AlienBlockEntityTypes;
import com.alien.common.registry.init.AlienCompostingChances;
import com.alien.common.registry.init.AlienDataSyncKeys;
import com.alien.common.registry.init.AlienDecoratedPotPatterns;
import com.alien.common.registry.init.AlienEntitySpawns;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienGameEvents;
import com.alien.common.registry.init.AlienParticleTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.init.block.AberrantAlienChitinBlocks;
import com.alien.common.registry.init.block.AberrantAlienResinBlocks;
import com.alien.common.registry.init.block.AlienBlocks;
import com.alien.common.registry.init.block.AlienChitinBlocks;
import com.alien.common.registry.init.block.AlienResinBlocks;
import com.alien.common.registry.init.block.IrradiatedAlienResinBlocks;
import com.alien.common.registry.init.block.NetherAlienChitinBlocks;
import com.alien.common.registry.init.block.NetherAlienResinBlocks;
import com.alien.common.registry.init.creative_mode_tab.AlienCreativeModeTabs;
import com.alien.common.registry.init.item.AlienArmorItems;
import com.alien.common.registry.init.item.AlienItems;
import com.alien.common.registry.init.item.AlienSpawnEggItems;
import com.alien.common.registry.init.item.block.AberrantAlienChitinBlockItems;
import com.alien.common.registry.init.item.block.AberrantAlienResinBlockItems;
import com.alien.common.registry.init.item.block.AlienBlockItems;
import com.alien.common.registry.init.item.block.AlienChitinBlockItems;
import com.alien.common.registry.init.item.block.AlienResinBlockItems;
import com.alien.common.registry.init.item.block.IrradiatedAlienResinBlockItems;
import com.alien.common.registry.init.item.block.NetherAlienChitinBlockItems;
import com.alien.common.registry.init.item.block.NetherAlienResinBlockItems;
import com.blib.BLib;
import com.blib.BLibMod;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Alien {

    public static final String MOD_ID = "avp_alien";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final BLibMod MOD = BLib.createMod(MOD_ID);

    public static void initialize() {
        LOGGER.info("Initializing AVP (Alien) for mod loader '{}'", BLib.getModLoaderType());

        AlienConfig.initialize();

        MOD.initialize(() -> {
            // No dependencies.
            AlienBlocks.initialize();
            AlienChitinBlocks.initialize();
            AlienResinBlocks.initialize();
            NetherAlienChitinBlocks.initialize();
            NetherAlienResinBlocks.initialize();
            AberrantAlienChitinBlocks.initialize();
            AberrantAlienResinBlocks.initialize();
            IrradiatedAlienResinBlocks.initialize();
            AlienItems.initialize();
            AlienEntityTypes.initialize();
            AlienSoundEvents.initialize();

            // Depends on blocks.
            AlienBlockItems.initialize();
            AlienChitinBlockItems.initialize();
            AlienResinBlockItems.initialize();
            NetherAlienChitinBlockItems.initialize();
            NetherAlienResinBlockItems.initialize();
            AberrantAlienChitinBlockItems.initialize();
            AberrantAlienResinBlockItems.initialize();
            IrradiatedAlienResinBlockItems.initialize();
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
            AlienDataSyncKeys.initialize();
            AlienEntitySpawns.initialize();

            // Data Migration
            AlienDataMigrations.initialize();

            // Listeners/Events
            AlienReloadListeners.initialize();

            MOD.events().postLevelTick().register(Alien::tickHivesInLevel);
            MOD.events().postLevelTick().register(Alien::tickQueenSpawnCooldown);
            MOD.events().onTagsUpdated().register(Alien::onTagsUpdated);
        });
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

    private static void onTagsUpdated(RegistryAccess registryAccess, boolean flag) {
        GrowthStageRegistry.rebuildLookupMappings();
        InfectionRegistry.rebuildLookupMappings();
    }
}

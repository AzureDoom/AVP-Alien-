package com.alien;

import com.avp.service.Services;
import com.alien.common.registry.init.AlienArmorMaterials;
import com.alien.common.registry.init.AlienBlockEntityTypes;
import com.alien.common.registry.init.AlienBlockItems;
import com.alien.common.registry.init.AlienBlocks;
import com.alien.common.registry.init.AlienEntitySpawns;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.init.AlienSoundEvents;
import com.alien.common.registry.init.creative_mode_tab.AlienCreativeModeTabs;
import com.alien.common.registry.init.item.AlienArmorItems;
import com.alien.common.registry.init.item.AlienItems;
import com.alien.common.registry.init.item.AlienSpawnEggItems;
import com.alien.common.registry.key.AlienJukeboxSongKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Alien {

    public static final String MOD_ID = "avp_alien";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void initialize() {
        LOGGER.info("Initializing AVP (Alien) for platform '{}'", Services.PLATFORM.getPlatformName());

        AlienBlocks.initialize();
        AlienBlockItems.initialize();
        AlienItems.initialize();
        AlienArmorMaterials.initialize();
        AlienArmorItems.initialize();
        AlienSpawnEggItems.initialize();
        AlienEntityTypes.initialize();
        AlienBlockEntityTypes.initialize();
        AlienCreativeModeTabs.initialize();
        AlienSoundEvents.initialize();
        AlienJukeboxSongKeys.initialize();

        // Functionality
        AlienEntitySpawns.initialize();
    }
}

package com.alien.fabric;

import com.alien.Alien;
import com.alien.fabric.common.FlammableBlockRegistry;
import com.alien.fabric.data.loot.LootTableModifier;
import net.fabricmc.api.ModInitializer;

public class AlienFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Alien.initialize();

        // Functionality
        LootTableModifier.initialize();
        FlammableBlockRegistry.initialize();
    }
}

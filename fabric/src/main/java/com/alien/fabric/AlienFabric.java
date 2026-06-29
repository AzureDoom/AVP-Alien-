package com.alien.fabric;

import com.alien.Alien;
import com.alien.common.gameplay.capture.CaptureHoldManager;
import com.alien.fabric.common.FlammableBlockRegistry;
import com.alien.fabric.data.loot.LootTableModifier;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class AlienFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Alien.initialize();

        // Functionality
        LootTableModifier.initialize();
        FlammableBlockRegistry.initialize();

        // Capture-chain hold tether (server-side reel-in for player-held mobs).
        ServerTickEvents.END_SERVER_TICK.register(CaptureHoldManager::tick);
    }
}

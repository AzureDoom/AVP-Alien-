package com.alien.neoforge;

import com.alien.Alien;
import com.alien.common.gameplay.capture.CaptureHoldManager;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(Alien.MOD_ID)
public class AlienNeoForge {

    public AlienNeoForge() {
        Alien.initialize();
        // Capture-chain hold tether (server-side reel-in for player-held mobs).
        NeoForge.EVENT_BUS.addListener(
            (ServerTickEvent.Post event) -> CaptureHoldManager.tick(event.getServer())
        );
    }
}

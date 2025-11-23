package com.alien.neoforge.client;

import com.alien.Alien;
import com.alien.client.AlienClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Alien.MOD_ID, dist = Dist.CLIENT)
public class AlienNeoForgeClient {

    static {
        // We want this to run before any of the other events, as this sets up queues of data pairs (for example, pairs
        // of item suppliers to item renderers) prior the registration events firing.
        AlienClient.initialize();
    }

    public AlienNeoForgeClient(IEventBus modBus) {
    }
}

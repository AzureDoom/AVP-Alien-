package com.alien.neoforge.client;

import com.alien.Alien;
import com.alien.client.AlienClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Alien.MOD_ID, dist = Dist.CLIENT)
public class AlienNeoForgeClient {

    public AlienNeoForgeClient(IEventBus modBus) {
        AlienClient.initialize();
    }
}

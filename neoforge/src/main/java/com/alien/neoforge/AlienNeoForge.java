package com.alien.neoforge;

import com.alien.Alien;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Alien.MOD_ID)
public class AlienNeoForge {

    public AlienNeoForge(IEventBus modBus) {
        Alien.initialize();
    }
}

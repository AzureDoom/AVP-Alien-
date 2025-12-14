package com.alien.common.data;

import com.alien.Alien;
import com.blib.common.registry.impl.BLibReloadListenerRegistry;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class AlienReloadListeners {

    private static final BLibReloadListenerRegistry REGISTRY = Alien.MOD.createReloadListenerRegistry();

    public static final PreparableReloadListener GROWTH_STAGES_RELOAD_LISTENER = new GrowthStageReloadListener();

    public static final PreparableReloadListener INFECTIONS_RELOAD_LISTENER = new InfectionReloadListener();

    public static void initialize() {
        REGISTRY.register(GrowthStageReloadListener.DIRECTORY_NAME, GROWTH_STAGES_RELOAD_LISTENER);
        REGISTRY.register(InfectionReloadListener.DIRECTORY_NAME, INFECTIONS_RELOAD_LISTENER);
    }
}

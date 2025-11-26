package com.alien.common.data;

import com.avp.service.Services;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class AlienReloadListeners {

    public static final PreparableReloadListener GROWTH_STAGES_RELOAD_LISTENER = register(
        GrowthStageReloadListener.DIRECTORY_NAME,
        new GrowthStageReloadListener()
    );

    public static final PreparableReloadListener INFECTIONS_RELOAD_LISTENER = register(
        InfectionReloadListener.DIRECTORY_NAME,
        new InfectionReloadListener()
    );

    private static PreparableReloadListener register(String id, PreparableReloadListener listener) {
        return Services.REGISTRY.registerReloadListener(id, listener);
    }

    public static void initialize() {}
}

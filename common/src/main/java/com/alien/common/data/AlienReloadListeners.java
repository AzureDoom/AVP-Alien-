package com.alien.common.data;

import com.alien.Alien;
import com.blib.api.common.registry.v1.impl.BLibReloadListenerRegistry;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class AlienReloadListeners {

    private static final BLibReloadListenerRegistry REGISTRY = Alien.MOD.registries().createReloadListenerRegistry();

    public static final PreparableReloadListener FORM_SIZE_SCALE_RELOAD_LISTENER = new FormSizeScaleReloadListener();

    public static final PreparableReloadListener GROWTH_STAGES_RELOAD_LISTENER = new GrowthStageReloadListener();

    public static final PreparableReloadListener INFECTIONS_RELOAD_LISTENER = new InfectionReloadListener();

    public static final PreparableReloadListener HIVE_RECIPES_RELOAD_LISTENER = new HiveRecipeReloadListener();

    public static void initialize() {
        REGISTRY.register(FormSizeScaleReloadListener.DIRECTORY_NAME, FORM_SIZE_SCALE_RELOAD_LISTENER, PackType.SERVER_DATA);
        REGISTRY.register(GrowthStageReloadListener.DIRECTORY_NAME, GROWTH_STAGES_RELOAD_LISTENER, PackType.SERVER_DATA);
        REGISTRY.register(InfectionReloadListener.DIRECTORY_NAME, INFECTIONS_RELOAD_LISTENER, PackType.SERVER_DATA);
        REGISTRY.register(HiveRecipeReloadListener.DIRECTORY_NAME, HIVE_RECIPES_RELOAD_LISTENER, PackType.SERVER_DATA);
    }
}

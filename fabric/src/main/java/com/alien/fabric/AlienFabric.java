package com.alien.fabric;

import com.alien.Alien;
import com.alien.fabric.common.DispenserBlockBehaviors;
import com.avp.common.AVPEvents;
import com.avp.fabric.data.loot.LootTableModifier;
import com.avp.fabric.service.FabricRegistryService;
import com.avp.service.Services;
import com.lib.common.network.DataContainer;
import com.lib.common.network.DataUser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.world.entity.LivingEntity;

public class AlienFabric implements ModInitializer {

    private static final FabricRegistryService REGISTRY = (FabricRegistryService) Services.REGISTRY;

    @Override
    public void onInitialize() {
        Alien.initialize();

        // Functionality
        DispenserBlockBehaviors.initialize();
        LootTableModifier.initialize();

        EntityTrackingEvents.START_TRACKING.register(
            (trackedEntity, player) -> {
                if (trackedEntity instanceof LivingEntity livingEntity) {
                    ((DataUser) livingEntity).getDataContainer().syncToClient(livingEntity, DataContainer.SyncType.ALL);
                }
            }
        );

        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> REGISTRY.getLiteralArgumentBuilders()
                .forEach(dispatcher::register)
        );

        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> AVPEvents.onTagsUpdated());
    }
}

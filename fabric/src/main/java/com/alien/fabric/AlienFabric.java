package com.alien.fabric;

import com.alien.Alien;
import com.alien.fabric.common.FlammableBlockRegistry;
import com.alien.fabric.data.loot.LootTableModifier;
import com.avp.common.AVPEvents;
import com.lib.common.network.DataContainer;
import com.lib.common.network.DataUser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.world.entity.LivingEntity;

public class AlienFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Alien.initialize();

        // Functionality
        LootTableModifier.initialize();
        FlammableBlockRegistry.initialize();

        EntityTrackingEvents.START_TRACKING.register(
            (trackedEntity, player) -> {
                if (trackedEntity instanceof LivingEntity livingEntity) {
                    ((DataUser) livingEntity).getDataContainer().syncToClient(livingEntity, DataContainer.SyncType.ALL);
                }
            }
        );

        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> AVPEvents.onTagsUpdated());
    }
}

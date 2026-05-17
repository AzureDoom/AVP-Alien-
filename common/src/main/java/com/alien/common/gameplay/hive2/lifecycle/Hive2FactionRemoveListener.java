package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.resources.ResourceLocation;

/**
 * Defensive cleanup when BLib removes a lineage faction:
 * <ul>
 * <li>Drop any registry entries pointing at the dead lineage. The active lineage-removal path
 * ({@link LineageDeathHandler}) already does this, but defensive cleanup catches admin-removed factions,
 * NBT-corruption recoveries, and any removal path that bypasses the normal handler.</li>
 * </ul>
 * <p>
 * Wired from the mod entry point via {@code MOD.events().onFactionRemove().register(...)}.
 */
public final class Hive2FactionRemoveListener {

    private Hive2FactionRemoveListener() {}

    public static void onFactionRemoved(ResourceLocation factionId) {
        if (!LineageIds.isLineageId(factionId)) {
            return;
        }

        // Defensively unregister any lingering registry entries.
        var locationIds = new java.util.LinkedHashSet<>(HiveLocationRegistry.INSTANCE.byLineage(factionId));
        for (var locationId : locationIds) {
            HiveLocationRegistry.INSTANCE.unregister(locationId);
        }

        if (!locationIds.isEmpty()) {
            Alien.LOGGER.info(
                "Hive2: faction-remove cleanup for {} — unregistered {} locations",
                factionId,
                locationIds.size()
            );
        }
    }
}

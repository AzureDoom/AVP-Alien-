package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.resources.ResourceLocation;

/**
 * Defensive cleanup when BLib removes a faction. Three responsibilities:
 * <ul>
 * <li>Drop any registry entries pointing at the dead lineage. The active lineage-removal paths
 * ({@link LineageDeathHandler}, {@link LineageAbsorptionHandler}, {@link CivilWarHandler}) already do this, but
 * defensive cleanup catches admin-removed factions, NBT-corruption recoveries, and any removal path that bypasses the
 * normal handlers.</li>
 * <li>Sweep every other lineage's {@code firstAdjacentTickByLineage} map to remove entries pointing at the dead lineage
 * — keeps the absorption timer state from leaking stale references.</li>
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

        // 1. Defensively unregister any lingering registry entries.
        var locationIds = new java.util.LinkedHashSet<>(HiveLocationRegistry.INSTANCE.byLineage(factionId));
        for (var locationId : locationIds) {
            HiveLocationRegistry.INSTANCE.unregister(locationId);
        }

        // 2. Sweep adjacency timers in every other lineage.
        var sweptCount = 0;
        for (var otherId : new java.util.ArrayList<>(Alien.MOD.factions().getAllIds())) {
            if (!LineageIds.isLineageId(otherId) || otherId.equals(factionId)) {
                continue;
            }
            var otherFaction = Alien.MOD.factions().get(otherId);
            if (otherFaction == null || !(otherFaction.data() instanceof LineageFactionData otherLineage)) {
                continue;
            }
            if (otherLineage.firstAdjacentTickByLineage().remove(factionId) != null) {
                sweptCount++;
            }
        }

        if (!locationIds.isEmpty() || sweptCount > 0) {
            Alien.LOGGER.info(
                "Hive2: faction-remove cleanup for {} — unregistered {} locations, swept {} adjacency timers",
                factionId,
                locationIds.size(),
                sweptCount
            );
        }
    }
}

package com.alien.common.gameplay.hive2.faction;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.registry.init.AlienFactionDataTypes;

/**
 * Creates and backfills the BLib faction that represents a single hive location.
 */
public final class HiveLocationFactionProvisioner {

    private HiveLocationFactionProvisioner() {}

    public static void ensure(HiveLocation location, LineageFactionData lineage) {
        var locationFactionId = location.id().value();
        var locationFaction = Alien.MOD.factions().get(locationFactionId);
        var created = false;

        if (locationFaction == null) {
            locationFaction = Alien.MOD.factions().getOrCreate(locationFactionId, AlienFactionDataTypes.LOCATION);
            FactionAesthetics.applyDefaults(locationFaction, lineage.variant(), FactionAesthetics.Tier.LOCATION);
            created = true;
        }

        var allocatedNumber = false;
        if (location.locationNumber() < 0) {
            location.setLocationNumber(lineage.allocateLocationNumber());
            allocatedNumber = true;
        }

        if (locationFaction.data() instanceof LocationFactionData locationData && locationData.locationId() == null) {
            locationData.setLocationId(location.id());
        }

        if (created || allocatedNumber) {
            locationFaction.setName(
                FactionNaming.forLocation(
                    lineage.variant(),
                    lineage.lineageNumber(),
                    location.locationNumber()
                )
            );
        }
    }
}

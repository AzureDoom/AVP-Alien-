package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.growth.HiveLocationClaims;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.gameplay.hive2.location.HiveLocationRemovalReason;
import net.minecraft.server.level.ServerLevel;

import java.util.HashSet;

/**
 * Minimal primitive to remove a {@link HiveLocation} from the world. Releases every claimed chunk through BLib's
 * territory manager + the registry's index, sets the location's {@code removalReason}, and unregisters it from the
 * lineage's location map and the registry.
 * <p>
 * <b>Use this directly only when the caller has already handled the location's reserves and members</b> — for example,
 * {@code MigrationDispatch} drains reserves into the convoy composition before calling here. For natural death
 * (dormancy, contest loss, player kill, admin), use {@link LocationDeathHandler} instead — it adds reserves cascade,
 * member-to-forager conversion, and advancement firing on top of this primitive.
 */
public final class LocationRemovalHelper {

    private LocationRemovalHelper() {}

    public static void remove(
        ServerLevel level,
        HiveLocation location,
        LineageFactionData lineage,
        HiveLocationRemovalReason reason
    ) {
        // Snapshot the claimed chunks before iterating — release() mutates the set.
        var chunksToRelease = new HashSet<>(location.claimedChunks());
        for (var chunk : chunksToRelease) {
            HiveLocationClaims.release(level, location, chunk);
        }

        location.setRemovalReason(reason);
        HiveLocationRegistry.INSTANCE.unregister(location.id());
        lineage.removeLocation(location.id());

        // The BLib LocationFactionData faction is keyed on the location's id; once the HiveLocation is gone, that
        // faction has no backing data and would otherwise linger in the FactionBrowser. Deleting it here prevents
        // stale-faction inspector requests (which can't be answered because HiveLocationRegistry.get() returns null)
        // and keeps the per-location membership from outliving the location.
        var locationFactionId = location.id().value();
        if (Alien.MOD.factions().exists(locationFactionId)) {
            Alien.MOD.factions().remove(locationFactionId);
        }

        Alien.LOGGER.info(
            "Hive2: removed location {} (lineage {}); reason={}",
            location.id(),
            location.lineageFactionId(),
            reason.typeKind()
        );
    }
}

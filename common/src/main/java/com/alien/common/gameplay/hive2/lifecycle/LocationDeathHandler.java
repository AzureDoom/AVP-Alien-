package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.data.AlienAdvancements;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.gameplay.hive2.location.HiveLocationRemovalReason;
import com.alien.common.gameplay.hive2.location.HivePoolCascade;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Full {@link HiveLocation} death cascade. Per {@code HIVE_REDESIGN_03_LOCATIONS.md} § 10:
 * <ul>
 * <li>Releases every claimed chunk through BLib's territory manager.</li>
 * <li>Converts loaded members in the territory to foragers (removes them from lineage membership but leaves them in the
 * variant faction; entities themselves stay in the world).</li>
 * <li>Local reserves overflow into the lineage pool with cascade to the variant pool per
 * {@code HIVE_REDESIGN_05_RESERVES.md} § 6.</li>
 * <li>Fires the {@link AlienAdvancements#KILL_A_HIVE} advancement to nearby players, but only when the death was
 * player-caused.</li>
 * <li>Removes the location from its lineage's {@code locationsById} and from the {@link HiveLocationRegistry}.</li>
 * </ul>
 * <p>
 * Doesn't trigger lineage death directly — the {@link LineageDeathHandler}'s scan picks up 0-location lineages on the
 * next slow scan.
 */
public final class LocationDeathHandler {

    private LocationDeathHandler() {}

    /** Player-caused death. Fires the advancement to nearby players. */
    public static void killByPlayer(
        ServerLevel level,
        HiveLocation location,
        LineageFactionData lineage,
        UUID killerId
    ) {
        kill(level, location, lineage, new HiveLocationRemovalReason.KilledByPlayer(killerId), true);
    }

    /** Natural decay (dormancy/0-chunk shrink). Does NOT fire the advancement. */
    public static void killNaturalDecay(ServerLevel level, HiveLocation location, LineageFactionData lineage) {
        kill(level, location, lineage, new HiveLocationRemovalReason.NaturalDecay(), false);
    }

    /** Contest loss (lost a same-variant cross-lineage chunk fight to the point of zero territory). */
    public static void killContestLost(ServerLevel level, HiveLocation location, LineageFactionData lineage) {
        kill(level, location, lineage, new HiveLocationRemovalReason.ContestLost(), false);
    }

    /** Admin-triggered. */
    public static void killAdmin(ServerLevel level, HiveLocation location, LineageFactionData lineage, String reason) {
        kill(level, location, lineage, new HiveLocationRemovalReason.AdminRemoved(reason), false);
    }

    private static void kill(
        ServerLevel level,
        HiveLocation location,
        LineageFactionData lineage,
        HiveLocationRemovalReason reason,
        boolean fireAdvancement
    ) {
        // 1. Drain local reserves into the lineage pool with cascade (overflow → variant pool).
        drainReservesToCascade(location, lineage);

        // 2. Convert loaded members in the territory to foragers — drop them from BLib lineage membership only.
        evictTerritoryMembersFromLineage(location, lineage);

        // 3. Fire advancement BEFORE we release chunks so the territory check still works.
        if (fireAdvancement) {
            fireKillAHiveAdvancement(level, location);
        }

        // 4. Release every claimed chunk via BLib + reset local indexes (LocationRemovalHelper handles BLib release).
        LocationRemovalHelper.remove(level, location, lineage, reason);

        Alien.LOGGER.info(
            "Hive2: location {} death complete (reason={}); lineage {} now has {} location(s)",
            location.id(),
            reason.typeKind(),
            location.lineageFactionId(),
            lineage.locationsById().size()
        );
    }

    private static void drainReservesToCascade(HiveLocation location, LineageFactionData lineage) {
        var reserves = location.localReserves();
        for (var type : new ArrayList<>(reserves.getAvailableEntityTypes())) {
            var count = reserves.getCount(type);
            if (count <= 0) {
                continue;
            }
            // Pull all out of the location and push to lineage with cascade. We deliberately bypass
            // addToLocationCascading because we're killing the location.
            reserves.underlying().add(type, -count);
            HivePoolCascade.addToLineageCascading(lineage, type, count);
        }
    }

    private static void evictTerritoryMembersFromLineage(HiveLocation location, LineageFactionData lineage) {
        var faction = Alien.MOD.factions().get(location.lineageFactionId());
        if (faction == null) {
            return;
        }

        // Snapshot UUIDs before mutating membership (onMemberRemoved sweeps loadedMembersByType).
        var uuidsToEvict = new ArrayList<UUID>();
        for (var perTypeSet : location.loadedMembersByType().values()) {
            uuidsToEvict.addAll(perTypeSet);
        }

        for (var uuid : uuidsToEvict) {
            faction.membership().removeMember(FactionMember.entity(uuid));
        }
    }

    private static void fireKillAHiveAdvancement(ServerLevel level, HiveLocation location) {
        var radius = HiveLocationRegistry.INSTANCE.config().bossBarDisplayRadiusBlocks();
        var radiusSqr = (double) radius * radius;
        var centerChunks = location.claimedChunks();

        for (var player : level.players()) {
            if (!(player instanceof ServerPlayer serverPlayer)) {
                continue;
            }
            // Eligible if the player is either inside the location's claimed chunks OR within the boss bar radius
            // from center — both indicate "the player was here when it fell."
            var inChunk = centerChunks.contains(new ChunkPos(serverPlayer.blockPosition()));
            var inRadius = serverPlayer.blockPosition().distSqr(location.centerPos()) <= radiusSqr;
            if (inChunk || inRadius) {
                AlienAdvancements.KILL_A_HIVE.grant(serverPlayer);
            }
        }
    }
}

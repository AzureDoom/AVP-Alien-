package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;

/**
 * Per-tick location death check. Replaces the legacy 24h dormancy timer with three accuracy-first rules:
 * <ol>
 * <li>Zero claimed chunks → kill (preserved from the legacy behavior).</li>
 * <li>Location faction empty AND local reserves empty → kill (membership is BLib-persistent so this is reliable across
 * chunk unloads).</li>
 * <li>Parent lineage faction empty → kill (defensive backup — also fired from {@link LineageDeathHandler}).</li>
 * <li>No-contact safety net: {@link HiveLocation#noContactTicksAccrued()} accumulates while ≥1 claimed chunk is loaded
 * AND no location-faction member is currently in any claimed chunk. Pauses when nothing is loaded; resets when contact
 * is observed; triggers a kill at {@link com.alien.common.gameplay.hive2.config.HiveConfig#locationMaxNoContactTicks()}
 * (default 7 game-days).</li>
 * </ol>
 * <p>
 * Runs every server tick from {@link HiveLocationRegistry#tick}; no scan-cadence throttling. If this becomes a perf
 * hotspot, the per-location body is cheap to gate (early-return on non-loaded territory).
 */
public final class LocationDormancyTask {

    private LocationDormancyTask() {}

    public static void scanAll(MinecraftServer server) {
        var config = HiveLocationRegistry.INSTANCE.config();
        var maxNoContact = config.locationMaxNoContactTicks();

        // Snapshot ids before iteration — LocationDeathHandler.kill removes the per-location faction, which mutates
        // the underlying registry that getAllIds() returns a view of.
        for (var factionId : new ArrayList<>(Alien.MOD.factions().getAllIds())) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }

            var serverLevel = server.getLevel(lineage.dimension());
            if (serverLevel == null) {
                continue;
            }

            var lineageMemberCount = faction.membership().getMembers().size();

            // Snapshot since LocationDeathHandler.killNaturalDecay can mutate locationsById.
            var locations = new ArrayList<>(lineage.locationsById().values());
            for (var location : locations) {
                if (!location.isAlive()) {
                    continue;
                }

                if (evaluateLocation(serverLevel, location, lineage, lineageMemberCount, maxNoContact)) {
                    // Killed — skip further checks on this location.
                    continue;
                }
            }
        }
    }

    /** Returns true if the location was killed this tick. */
    private static boolean evaluateLocation(
        ServerLevel level,
        HiveLocation location,
        LineageFactionData lineage,
        int lineageMemberCount,
        long maxNoContact
    ) {
        // Rule 1: zero claimed chunks → die.
        if (location.claimedChunks().isEmpty()) {
            LocationDeathHandler.killNaturalDecay(level, location, lineage);
            return true;
        }

        // Rule 3: parent lineage faction empty. LineageDeathHandler also does this explicitly; this backup catches the
        // case if the lineage check runs after this.
        if (lineageMemberCount == 0) {
            LocationDeathHandler.killNaturalDecay(level, location, lineage);
            return true;
        }

        // Rule 2: location faction empty AND no stored population → die.
        var locationFaction = Alien.MOD.factions().get(location.id().value());
        var locationMemberCount = locationFaction != null ? locationFaction.membership().getMembers().size() : 0;
        var reservesCount = location.localReserves().getCount() + location.arrivalReserves().getCount();
        if (locationMemberCount == 0 && reservesCount == 0) {
            LocationDeathHandler.killNaturalDecay(level, location, lineage);
            return true;
        }

        // Rule 4: no-contact safety net.
        var anyChunkLoaded = false;
        var memberInTerritory = false;

        for (var chunk : location.claimedChunks()) {
            if (level.getChunkSource().hasChunk(chunk.x, chunk.z)) {
                anyChunkLoaded = true;
                break;
            }
        }

        if (anyChunkLoaded && locationFaction != null) {
            for (var member : locationFaction.membership().getMembers()) {
                if (!(member instanceof com.blib.api.common.faction.v1.FactionMember.Entity entityMember)) {
                    continue;
                }
                var entity = level.getEntity(entityMember.uuid());
                if (entity == null) {
                    continue;
                }
                if (location.claimedChunks().contains(new ChunkPos(entity.blockPosition()))) {
                    memberInTerritory = true;
                    break;
                }
            }
        }

        if (!anyChunkLoaded) {
            // Paused — neither advance nor reset.
            return false;
        }

        if (memberInTerritory) {
            if (location.noContactTicksAccrued() != 0L) {
                location.setNoContactTicksAccrued(0L);
            }
            return false;
        }

        var nextAccrued = location.noContactTicksAccrued() + 1L;
        location.setNoContactTicksAccrued(nextAccrued);
        if (nextAccrued >= maxNoContact) {
            LocationDeathHandler.killNaturalDecay(level, location, lineage);
            return true;
        }

        return false;
    }
}

package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.convoy.Convoy;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.faction.LineageRemovalReason;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.registry.init.AlienFactionDataTypes;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Empress-death civil war: shatters a multi-location lineage into N successor lineages (one per surviving location).
 * <p>
 * Triggered when {@link LineageFactionData#pendingCivilWar()} is true (set by {@code Alien.die} when an empress dies).
 * The dead lineage is removed; each location is reparented to a fresh single-location lineage; members are partitioned
 * by which location's claimed chunks they currently sit in (or become foragers if outside any claimed territory);
 * convoys are partitioned per {@code HIVE_REDESIGN_06_CONVOYS.md} § 8 (each member of a convoy follows the rules for
 * its own type, the convoy itself disbands and refunds its composition into the closest successor lineage's pool).
 * <p>
 * Lineage pool is split equally; remainder goes to the oldest location's successor.
 * <p>
 * Per {@code HIVE_REDESIGN_02_FACTION_LIFECYCLES.md} § 3.
 */
public final class CivilWarHandler {

    private CivilWarHandler() {}

    /**
     * Consumes {@code pendingCivilWar=true}. If the lineage has only 0–1 locations, no shatter is performed — the flag
     * is simply cleared (a single location doesn't need an empress).
     *
     * @return the set of successor lineage ids (empty if no shatter occurred).
     */
    public static List<ResourceLocation> handle(MinecraftServer server, ResourceLocation deadLineageId) {
        var deadFaction = Alien.MOD.factions().get(deadLineageId);
        if (deadFaction == null || !(deadFaction.data() instanceof LineageFactionData deadLineage)) {
            return List.of();
        }

        if (!deadLineage.isAlive()) {
            // Already removed by another path. Clear the flag and bail.
            deadLineage.setPendingCivilWar(false);
            return List.of();
        }

        var locations = new ArrayList<>(deadLineage.locationsById().values());
        if (locations.size() <= 1) {
            // 0 or 1 locations — no shatter needed. A 0-location lineage will be picked up by lineage death;
            // a 1-location lineage just keeps going without an empress.
            deadLineage.setPendingCivilWar(false);
            return List.of();
        }

        var serverLevel = server.getLevel(deadLineage.dimension());
        if (serverLevel == null) {
            Alien.LOGGER.warn(
                "Hive2: civil war for {} skipped — dimension {} unloaded",
                deadLineageId,
                deadLineage.dimension().location()
            );
            deadLineage.setPendingCivilWar(false);
            return List.of();
        }

        // Sort locations by ageInTicks descending so the oldest location is at index 0 — its successor gets the
        // pool remainder per § 3 step 2 last-bullet.
        locations.sort((a, b) -> Long.compare(b.ageInTicks(), a.ageInTicks()));

        // 1. Mint one successor lineage per location (faction id, faction record, and parented to same variant).
        var successorIds = new ArrayList<ResourceLocation>(locations.size());
        var successorByLocation = new LinkedHashMap<HiveLocationId, ResourceLocation>();
        var successorDataById = new LinkedHashMap<ResourceLocation, LineageFactionData>();

        for (var location : locations) {
            var successorId = LineageIds.create();
            var successorFaction = Alien.MOD.factions().getOrCreate(successorId, AlienFactionDataTypes.LINEAGE);
            var successorData = successorFaction.data();
            if (successorData == null) {
                Alien.LOGGER.warn("Hive2: civil war successor data null for {} — skipping", successorId);
                continue;
            }

            successorData.setVariant(deadLineage.variant());
            successorData.setParentVariantFactionId(deadLineage.parentVariantFactionId());
            successorData.setDimension(deadLineage.dimension());
            // Founder is the local leader of the location at the moment of war (or null if dormant).
            successorData.setFounderId(location.leadership().getLeaderIdOrNull());
            successorData.setEmpressId(null);

            successorIds.add(successorId);
            successorByLocation.put(location.id(), successorId);
            successorDataById.put(successorId, successorData);
        }

        // 2. Reparent each location to its successor lineage, then transfer claims from old → new in BLib.
        for (var location : locations) {
            var successorId = successorByLocation.get(location.id());
            if (successorId == null) {
                continue;
            }

            // Reparent the location record itself.
            location.setLineageFactionId(successorId);

            // Transfer every claimed chunk in BLib's territory manager.
            for (var chunk : new LinkedHashSet<>(location.claimedChunks())) {
                Alien.MOD.territory().transferClaim(serverLevel, chunk, deadLineageId, successorId);
            }

            // Move the location into its successor lineage's locations map and out of the dead one's.
            // (Order matters: addLocation first so the registry rebuild on next start finds the right home.)
            var successorData = successorDataById.get(successorId);
            successorData.addLocation(location);
            deadLineage.removeLocation(location.id());
        }

        // The registry's byLineage index points at the dead id. Rebuild the per-location entries to point at
        // their new lineages.
        rebuildRegistryLineageIndex(locations, deadLineageId, successorByLocation);

        // 3. Partition members. Walk the dead lineage's BLib membership (snapshot first).
        var memberSnapshot = new ArrayList<>(deadFaction.membership().getMembers());
        var membersHandled = 0;
        for (var member : memberSnapshot) {
            if (!(member instanceof FactionMember.Entity entityMember)) {
                continue;
            }
            membersHandled++;
            var uuid = entityMember.uuid();
            var entity = serverLevel.getEntity(uuid);

            // Remove from the dead lineage's membership unconditionally.
            deadFaction.membership().removeMember(member);

            if (entity == null) {
                // Unloaded — we don't know which location's chunks contain them. Leave them as forager
                // (no successor lineage gets them). They'll be rejoined by Phase 6 spawn-gate when reloaded
                // if they end up in a successor's territory.
                continue;
            }

            var owningLocation = locationContainingChunk(entity.blockPosition(), locations);
            if (owningLocation != null) {
                var successorId = successorByLocation.get(owningLocation.id());
                if (successorId != null) {
                    var successorFaction = Alien.MOD.factions().get(successorId);
                    if (successorFaction != null) {
                        successorFaction.membership().addEntity(entity);
                    }
                }
                // Also assign to the location-tier faction (preserves location ⊆ lineage). The location's
                // lineageFactionId was already reparented to the successor by rebuildRegistryLineageIndex above.
                var locationFaction = Alien.MOD.factions().get(owningLocation.id().value());
                if (locationFaction != null) {
                    locationFaction.membership().addEntity(entity);
                }
            }
            // Else: outside any claimed chunk → forager. No-op (already removed above).
        }

        // 4. Partition the lineage pool equally among successors, remainder goes to the oldest-location successor.
        partitionLineagePool(deadLineage, successorDataById, locations.get(0).id(), successorByLocation);

        // 5. Partition convoys per HIVE_REDESIGN_06_CONVOYS.md § 8 (civil war row): convoys disband where they are;
        // their composition splits among successors keyed by closest successor location to the convoy's currentPos.
        partitionConvoys(deadLineage, locations, successorDataById, successorByLocation);

        // 6. Mark the dead lineage with the civil-war removal reason and remove from BLib.
        var successorIdSet = new LinkedHashSet<>(successorIds);
        deadLineage.setRemovalReason(new LineageRemovalReason.CivilWar(successorIdSet));
        deadLineage.setPendingCivilWar(false);
        Alien.MOD.factions().remove(deadLineageId);

        Alien.LOGGER.info(
            "Hive2: civil war complete: lineage {} shattered into {} successor lineages "
                + "({} members partitioned, {} convoys redistributed)",
            deadLineageId,
            successorIds.size(),
            membersHandled,
            deadLineage.convoys().size()
        );

        return successorIds;
    }

    /**
     * Sweeps every loaded lineage for {@code pendingCivilWar=true} and dispatches each. Called from
     * {@link com.alien.common.gameplay.hive2.faction.LineageInvariantTask}.
     */
    public static void scanAndHandle(MinecraftServer server) {
        var pendingIds = new ArrayList<ResourceLocation>();
        for (var factionId : Alien.MOD.factions().getAllIds()) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }
            if (lineage.pendingCivilWar()) {
                pendingIds.add(factionId);
            }
        }

        for (var id : pendingIds) {
            handle(server, id);
        }
    }

    private static void rebuildRegistryLineageIndex(
        List<HiveLocation> locations,
        ResourceLocation deadLineageId,
        Map<HiveLocationId, ResourceLocation> successorByLocation
    ) {
        // Easiest way to keep byLineage / byId / byChunk in sync is to unregister + register each location.
        for (var location : locations) {
            var successorId = successorByLocation.get(location.id());
            if (successorId == null) {
                continue;
            }
            // The location record's lineageFactionId was already set above. Unregister/reregister rebuilds
            // the byLineage entry under the new id.
            HiveLocationRegistry.INSTANCE.unregister(location.id());
            HiveLocationRegistry.INSTANCE.register(location);
        }
        // Drop the orphan entry for the dead lineage if it lingers.
        // (HiveLocationRegistry doesn't expose direct byLineage access; the unregister loop above clears it
        // for each removed entry, and an empty set would have been dropped by unregister's internal cleanup.)
        if (Alien.LOGGER.isDebugEnabled()) {
            Alien.LOGGER.debug("Hive2: civil war reindexed {} locations from dead lineage {}", locations.size(), deadLineageId);
        }
    }

    private static @Nullable HiveLocation locationContainingChunk(BlockPos pos, List<HiveLocation> locations) {
        var chunk = new ChunkPos(pos);
        for (var location : locations) {
            if (location.claimedChunks().contains(chunk)) {
                return location;
            }
        }
        return null;
    }

    private static void partitionLineagePool(
        LineageFactionData deadLineage,
        Map<ResourceLocation, LineageFactionData> successorDataById,
        HiveLocationId oldestLocationId,
        Map<HiveLocationId, ResourceLocation> successorByLocation
    ) {
        var n = successorDataById.size();
        if (n == 0) {
            return;
        }

        var pool = deadLineage.lineagePool();
        var oldestSuccessorId = successorByLocation.get(oldestLocationId);

        for (var type : new ArrayList<>(pool.getAvailableEntityTypes())) {
            var total = pool.getCount(type);
            if (total <= 0) {
                continue;
            }

            var perSuccessor = total / n;
            var remainder = total - (perSuccessor * n);

            for (var entry : successorDataById.entrySet()) {
                var slice = perSuccessor;
                if (entry.getKey().equals(oldestSuccessorId)) {
                    slice += remainder;
                }
                if (slice > 0) {
                    entry.getValue().tryAddToLineagePool(type, slice);
                }
            }

            // Drain from the dead pool so we don't double-count if anyone reads it later.
            pool.add(type, -total);
        }
    }

    private static void partitionConvoys(
        LineageFactionData deadLineage,
        List<HiveLocation> locations,
        Map<ResourceLocation, LineageFactionData> successorDataById,
        Map<HiveLocationId, ResourceLocation> successorByLocation
    ) {
        var convoys = new ArrayList<>(deadLineage.convoys());
        for (var convoy : convoys) {
            var nearestLocation = nearestLocationToConvoy(convoy, locations);
            if (nearestLocation == null) {
                continue;
            }
            var successorId = successorByLocation.get(nearestLocation.id());
            if (successorId == null) {
                continue;
            }
            var successorData = successorDataById.get(successorId);
            if (successorData == null) {
                continue;
            }

            // Refund composition into the successor's pool. Manifested entities become foragers — but Phase 11
            // doesn't manifest convoys to begin with, so this is the only step needed.
            for (var type : new ArrayList<>(convoy.composition().getAvailableEntityTypes())) {
                var count = convoy.composition().getCount(type);
                if (count > 0) {
                    successorData.tryAddToLineagePool(type, count);
                    convoy.composition().add(type, -count);
                }
            }
        }
        deadLineage.convoys().clear();
    }

    private static @Nullable HiveLocation nearestLocationToConvoy(Convoy convoy, List<HiveLocation> locations) {
        HiveLocation best = null;
        var bestDistSqr = Double.MAX_VALUE;
        for (var location : locations) {
            var dx = location.centerPos().getX() - convoy.currentPos().x;
            var dz = location.centerPos().getZ() - convoy.currentPos().z;
            var distSqr = dx * dx + dz * dz;
            if (distSqr < bestDistSqr) {
                bestDistSqr = distSqr;
                best = location;
            }
        }
        return best;
    }
}

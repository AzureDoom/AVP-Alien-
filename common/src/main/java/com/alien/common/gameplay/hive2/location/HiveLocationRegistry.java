package com.alien.common.gameplay.hive2.location;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.tick.HiveLocationLoadedTickTask;
import com.alien.common.gameplay.hive2.tick.LineageConvoyTickTask;
import com.alien.common.gameplay.hive2.tick.LineageGrowthScanTask;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Server-wide registry of {@link HiveLocation}s. Builds three indexes — by id, by lineage, by (level, chunk) — plus a
 * per-dim spatial bucket index for "nearest location" queries.
 * <p>
 * None of these indexes are persisted to disk. They're rebuilt on server start by walking every
 * {@link LineageFactionData}'s nested locations.
 * <p>
 * {@link #tick(MinecraftServer)} runs once per server tick (driven from {@code Alien.tickHive2Registry}). It iterates
 * every registered location across every dimension and dispatches them to {@link HiveLocationLoadedTickTask}; every
 * {@link HiveConfig#lineageScanIntervalTicks()} ticks it also fires {@link LineageGrowthScanTask}. Both tasks are empty
 * in Phase 2 — later phases attach the actual work.
 * <p>
 * See {@code HIVE_REDESIGN_03_LOCATIONS.md} § 1 and {@code HIVE_REDESIGN_12_PERFORMANCE.md} § 3.
 */
public final class HiveLocationRegistry {

    public static final HiveLocationRegistry INSTANCE = new HiveLocationRegistry();

    /** 32-chunk buckets — coarse enough that nearest-by-dim only scans a handful of buckets. */
    private static final int SPATIAL_BUCKET_CHUNKS = 32;

    private final Map<HiveLocationId, HiveLocation> byId = new HashMap<>();

    private final Map<ResourceLocation, Set<HiveLocationId>> byLineage = new HashMap<>();

    private final Map<ResourceKey<Level>, Map<ChunkPos, HiveLocationId>> byChunk = new HashMap<>();

    private final Map<ResourceKey<Level>, Map<Long, Set<HiveLocationId>>> byCenterDim = new HashMap<>();

    private HiveConfig config = HiveConfig.defaults();

    private long ticksSinceLastScan = 0L;

    /** Reinforcement dispatcher fires on a coarser-than-tick cadence — every 5 seconds is plenty. */
    private static final long REINFORCEMENT_DISPATCH_INTERVAL_TICKS = 20L * 5L;

    private long ticksSinceLastDispatch = 0L;

    private long ticksSinceLastReservePromotion = 0L;

    private HiveLocationRegistry() {}

    /**
     * Replaces the in-use config object. Phase 1 ships {@link HiveConfig#defaults()} — call this from a setup hook if
     * you want to pin custom tunables.
     */
    public void setConfig(HiveConfig config) {
        this.config = config;
    }

    public HiveConfig config() {
        return config;
    }

    /**
     * Inserts a location into all four indexes. Caller is responsible for persisting the location into its owning
     * {@link LineageFactionData}; this registry only maintains in-memory views.
     */
    public void register(HiveLocation location) {
        if (byId.containsKey(location.id())) {
            Alien.LOGGER.warn("HiveLocationRegistry.register called for already-registered id {}", location.id());
            return;
        }

        byId.put(location.id(), location);

        byLineage
            .computeIfAbsent(location.lineageFactionId(), $ -> new LinkedHashSet<>())
            .add(location.id());

        var byChunkForDim = byChunk.computeIfAbsent(location.dimension(), $ -> new HashMap<>());
        for (var chunk : location.claimedChunks()) {
            byChunkForDim.put(chunk, location.id());
        }

        addToSpatialIndex(location);
    }

    /**
     * Removes a location from every index. Doesn't touch the owning {@link LineageFactionData}.
     */
    public void unregister(HiveLocationId id) {
        var location = byId.remove(id);

        if (location == null) {
            return;
        }

        location.onUnregistered();

        var siblings = byLineage.get(location.lineageFactionId());
        if (siblings != null) {
            siblings.remove(id);
            if (siblings.isEmpty()) {
                byLineage.remove(location.lineageFactionId());
            }
        }

        var byChunkForDim = byChunk.get(location.dimension());
        if (byChunkForDim != null) {
            for (var chunk : location.claimedChunks()) {
                if (id.equals(byChunkForDim.get(chunk))) {
                    byChunkForDim.remove(chunk);
                }
            }
            if (byChunkForDim.isEmpty()) {
                byChunk.remove(location.dimension());
            }
        }

        removeFromSpatialIndex(location);
    }

    public @Nullable HiveLocation get(HiveLocationId id) {
        return byId.get(id);
    }

    public Collection<HiveLocation> all() {
        return Collections.unmodifiableCollection(byId.values());
    }

    public Set<HiveLocationId> byLineage(ResourceLocation lineageFactionId) {
        return Collections.unmodifiableSet(
            byLineage.getOrDefault(lineageFactionId, Set.of())
        );
    }

    public @Nullable HiveLocation getByChunk(ResourceKey<Level> dimension, ChunkPos pos) {
        var byChunkForDim = byChunk.get(dimension);
        if (byChunkForDim == null) {
            return null;
        }

        var id = byChunkForDim.get(pos);
        return id == null ? null : byId.get(id);
    }

    /**
     * Linear-over-spatial-bucket nearest lookup. With 32-chunk buckets and ~10000 locations expected at v1 max, this
     * scans at most a few dozen candidates per call.
     */
    public @Nullable HiveLocation findNearestInDim(ResourceKey<Level> dimension, BlockPos pos) {
        var dimBuckets = byCenterDim.get(dimension);
        if (dimBuckets == null || dimBuckets.isEmpty()) {
            return null;
        }

        var queryChunk = new ChunkPos(pos);
        var queryBucketX = bucketCoord(queryChunk.x);
        var queryBucketZ = bucketCoord(queryChunk.z);

        HiveLocation nearest = null;
        var nearestDistanceSqr = Double.MAX_VALUE;

        // Expand outward by bucket rings until a candidate is found, then check
        // the next ring too (a closer center might live in an adjacent bucket).
        for (var ring = 0; ring <= 64; ring++) {
            var foundInThisRing = false;

            for (var dx = -ring; dx <= ring; dx++) {
                for (var dz = -ring; dz <= ring; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != ring) {
                        continue;
                    }

                    var bucketKey = bucketKey(queryBucketX + dx, queryBucketZ + dz);
                    var bucket = dimBuckets.get(bucketKey);
                    if (bucket == null) {
                        continue;
                    }

                    for (var locationId : bucket) {
                        var location = byId.get(locationId);
                        if (location == null) {
                            continue;
                        }

                        var distSqr = location.centerPos().distSqr(pos);
                        if (distSqr < nearestDistanceSqr) {
                            nearestDistanceSqr = distSqr;
                            nearest = location;
                            foundInThisRing = true;
                        }
                    }
                }
            }

            // Stop one ring after first hit so a closer candidate in an
            // adjacent bucket isn't missed.
            if (nearest != null && !foundInThisRing) {
                break;
            }
        }

        return nearest;
    }

    public void onChunkClaimed(HiveLocation location, ChunkPos chunk) {
        byChunk
            .computeIfAbsent(location.dimension(), $ -> new HashMap<>())
            .put(chunk, location.id());
    }

    public void onChunkReleased(HiveLocation location, ChunkPos chunk) {
        var byChunkForDim = byChunk.get(location.dimension());
        if (byChunkForDim != null && location.id().equals(byChunkForDim.get(chunk))) {
            byChunkForDim.remove(chunk);
        }
    }

    /**
     * Walk every loaded lineage's nested locations and (re)build all four indexes from scratch. Intended for the
     * server-started callback.
     */
    public void rebuildFromFactions() {
        byId.clear();
        byLineage.clear();
        byChunk.clear();
        byCenterDim.clear();
        ticksSinceLastScan = 0L;
        ticksSinceLastDispatch = 0L;
        ticksSinceLastReservePromotion = 0L;

        var allIds = Alien.MOD.factions().getAllIds();
        var lineageIdCount = 0;
        var locationCount = 0;

        for (var factionId : allIds) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }

            lineageIdCount++;
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null) {
                Alien.LOGGER.warn("HiveLocationRegistry rebuild: lineage id {} resolves to null faction", factionId);
                continue;
            }

            var data = faction.data();
            if (data == null) {
                Alien.LOGGER.warn(
                    "HiveLocationRegistry rebuild: lineage id {} has null data — BLib may not have loaded yet",
                    factionId
                );
                continue;
            }

            if (!(data instanceof LineageFactionData lineageData)) {
                Alien.LOGGER.warn(
                    "HiveLocationRegistry rebuild: lineage id {} has data type {}, expected LineageFactionData",
                    factionId,
                    data.getClass().getName()
                );
                continue;
            }

            // Backfill the own-faction-id so reactive variant guards work on saves that predate the field.
            if (lineageData.factionId() == null) {
                lineageData.setFactionId(factionId);
            }

            // Backfill lineage path name + monotonic number.
            if (lineageData.lineageNumber() < 0) {
                var variantFaction = lineageData.parentVariantFactionId() != null
                    ? Alien.MOD.factions().get(lineageData.parentVariantFactionId())
                    : null;
                var allocated = variantFaction != null
                    && variantFaction.data() instanceof com.alien.common.gameplay.hive2.faction.VariantFactionData variantData
                        ? variantData.allocateLineageNumber()
                        : 0L;
                lineageData.setLineageNumber(allocated);
                faction.setName(
                    com.alien.common.gameplay.hive2.faction.FactionNaming.forLineage(lineageData.variant(), allocated)
                );
            }

            var locations = lineageData.locationsById();
            Alien.LOGGER.info(
                "HiveLocationRegistry rebuild: lineage {} has {} nested locations to register",
                factionId,
                locations.size()
            );

            for (var location : locations.values()) {
                register(location);
                locationCount++;

                // Backfill the location-faction's own location-id (for the reactive variant + lineage-membership
                // guard in LocationFactionData.onMemberAdded).
                var locationFactionId = location.id().value();
                var locationFaction = Alien.MOD.factions().get(locationFactionId);
                if (
                    locationFaction != null && locationFaction
                        .data() instanceof com.alien.common.gameplay.hive2.faction.LocationFactionData locationData
                ) {
                    if (locationData.locationId() == null) {
                        locationData.setLocationId(location.id());
                    }
                }

                // Backfill location path name + monotonic number under this lineage.
                if (location.locationNumber() < 0) {
                    var allocated = lineageData.allocateLocationNumber();
                    location.setLocationNumber(allocated);
                    if (locationFaction != null) {
                        locationFaction.setName(
                            com.alien.common.gameplay.hive2.faction.FactionNaming.forLocation(
                                lineageData.variant(),
                                lineageData.lineageNumber(),
                                allocated
                            )
                        );
                    }
                }
            }
        }

        // Backfill variant faction names for saves predating FactionNaming.
        for (var variant : com.alien.common.model.alien.variant.AlienVariant.VALUES) {
            var variantId = com.alien.common.gameplay.hive2.id.VariantIds.of(variant);
            var variantFaction = Alien.MOD.factions().get(variantId);
            if (variantFaction == null) {
                continue;
            }
            var expectedName = com.alien.common.gameplay.hive2.faction.FactionNaming.forVariant(variant);
            if (!expectedName.equals(variantFaction.name())) {
                variantFaction.setName(expectedName);
            }
        }

        Alien.LOGGER.info(
            "HiveLocationRegistry rebuilt: scanned {} factions, found {} lineage ids, registered {} locations across {} lineages",
            allIds.size(),
            lineageIdCount,
            locationCount,
            byLineage.size()
        );
    }

    /**
     * Per-server-tick entry point. Iterates every registered location across every dimension (fixes the legacy
     * Overworld-only bug, see {@code HIVE_SYSTEM_ANALYSIS.md} § 9.1.2) and dispatches them to the fast-path task. Every
     * {@link HiveConfig#lineageScanIntervalTicks()} ticks also invokes the slow-path scan.
     * <p>
     * Phase 2 attaches no real behavior — both tasks are no-ops. Later phases fill them in.
     */
    public void tick(MinecraftServer server) {
        if (!byId.isEmpty()) {
            for (var location : byId.values()) {
                if (!location.isAlive()) {
                    continue;
                }

                location.incrementAge();
                HiveLocationLoadedTickTask.run(server, location);
            }
        }

        // Phase 8: convoy travel + arrival every tick. Sparse — most lineages have zero convoys.
        LineageConvoyTickTask.run(server);

        // Phase 10: empress emergence per-tick advancement. Cheap when no queens are emerging.
        com.alien.common.gameplay.hive2.empress.EmpressEmergenceRitual.tick(server);

        // Location + lineage death checks run every tick — no throttling. See HIVE_REDESIGN_02_FACTION_LIFECYCLES.
        // Order matters: location dormancy first so per-location rules fire before the lineage-empty cascade picks
        // up newly-zero-location lineages this tick.
        com.alien.common.gameplay.hive2.lifecycle.LocationDormancyTask.scanAll(server);
        com.alien.common.gameplay.hive2.lifecycle.LineageDeathHandler.scanAndKill(server);

        // § 13 economy: balance buys + jelly production. Per-tick, no throttling.
        com.alien.common.gameplay.hive2.economy.HiveBalanceTask.scanAll(server);

        ticksSinceLastDispatch++;
        if (ticksSinceLastDispatch >= REINFORCEMENT_DISPATCH_INTERVAL_TICKS) {
            ticksSinceLastDispatch = 0L;
            com.alien.common.gameplay.hive2.convoy.ReinforcementDispatcher.scanAndDispatch(server);
            com.alien.common.gameplay.hive2.convoy.MigrationDispatch.scanAndDispatch(server);
            com.alien.common.gameplay.hive2.convoy.RaidDispatch.scanAndDispatch(server);
            com.alien.common.gameplay.hive2.empress.EmpressEmergenceTask.scanAndStart(server);
        }

        ticksSinceLastReservePromotion++;
        if (ticksSinceLastReservePromotion >= config.lineageReservePromotionInterval()) {
            ticksSinceLastReservePromotion = 0L;
            com.alien.common.gameplay.hive2.lifecycle.LineageReservePromotionTask.scanAndPromote(server);
        }

        ticksSinceLastScan++;
        if (ticksSinceLastScan >= config.lineageScanIntervalTicks()) {
            ticksSinceLastScan = 0L;
            LineageGrowthScanTask.run(server);
            // Phase 11: full lifecycle dispatch (civil war, dormancy, absorption, contests, lineage death) layered
            // on top of variant-mismatch invariants.
            com.alien.common.gameplay.hive2.faction.LineageInvariantTask.scanAllWithLifecycle(server);
        }
    }

    /**
     * Cross-tier sanity check. Logs (does not crash) any inconsistency:
     * <ul>
     * <li>Orphan lineages — registered locations whose owning lineage faction no longer exists in BLib. Their locations
     * are unregistered.</li>
     * <li>{@code byChunk} drift — every chunk in every location's {@code claimedChunks} should be reflected in the
     * per-dim chunk index. Missing entries are repaired.</li>
     * <li>{@code byCenterDim} drift — every location should be in the per-dim spatial bucket. Missing entries are
     * repaired.</li>
     * </ul>
     * <p>
     * Per {@code HIVE_REDESIGN_11_IMPLEMENTATION.md} § 2.
     */
    public void validate() {
        var orphanLineages = new HashSet<ResourceLocation>();

        for (var entry : byLineage.entrySet()) {
            var lineageId = entry.getKey();
            if (!Alien.MOD.factions().exists(lineageId)) {
                orphanLineages.add(lineageId);
            }
        }

        for (var lineageId : orphanLineages) {
            Alien.LOGGER.warn(
                "HiveLocationRegistry.validate: lineage {} has {} orphaned locations; cleaning up.",
                lineageId,
                byLineage.get(lineageId).size()
            );
            for (var orphan : Set.copyOf(byLineage.get(lineageId))) {
                unregister(orphan);
            }
        }

        var chunkRepairs = 0;
        var spatialRepairs = 0;

        for (var location : byId.values()) {
            var byChunkForDim = byChunk.get(location.dimension());
            for (var chunk : location.claimedChunks()) {
                if (byChunkForDim == null || !location.id().equals(byChunkForDim.get(chunk))) {
                    if (byChunkForDim == null) {
                        byChunkForDim = byChunk.computeIfAbsent(location.dimension(), $ -> new HashMap<>());
                    }
                    byChunkForDim.put(chunk, location.id());
                    chunkRepairs++;
                }
            }

            var dimBuckets = byCenterDim.get(location.dimension());
            var bucketX = bucketCoord(location.centerPos().getX() >> 4);
            var bucketZ = bucketCoord(location.centerPos().getZ() >> 4);
            var key = bucketKey(bucketX, bucketZ);
            var bucket = dimBuckets == null ? null : dimBuckets.get(key);

            if (bucket == null || !bucket.contains(location.id())) {
                addToSpatialIndex(location);
                spatialRepairs++;
            }
        }

        if (chunkRepairs > 0 || spatialRepairs > 0) {
            Alien.LOGGER.warn(
                "HiveLocationRegistry.validate: repaired {} byChunk entries and {} byCenterDim entries.",
                chunkRepairs,
                spatialRepairs
            );
        }

        Alien.LOGGER.info(
            "HiveLocationRegistry.validate: {} locations across {} lineages OK.",
            byId.size(),
            byLineage.size()
        );
    }

    public void clear() {
        for (var location : byId.values()) {
            location.onUnregistered();
        }
        byId.clear();
        byLineage.clear();
        byChunk.clear();
        byCenterDim.clear();
        ticksSinceLastScan = 0L;
        ticksSinceLastDispatch = 0L;
        ticksSinceLastReservePromotion = 0L;
    }

    public int locationCount() {
        return byId.size();
    }

    public int lineageCount() {
        return byLineage.size();
    }

    private void addToSpatialIndex(HiveLocation location) {
        var bucketX = bucketCoord(location.centerPos().getX() >> 4);
        var bucketZ = bucketCoord(location.centerPos().getZ() >> 4);
        var key = bucketKey(bucketX, bucketZ);

        byCenterDim
            .computeIfAbsent(location.dimension(), $ -> new HashMap<>())
            .computeIfAbsent(key, $ -> new LinkedHashSet<>())
            .add(location.id());
    }

    private void removeFromSpatialIndex(HiveLocation location) {
        var dimBuckets = byCenterDim.get(location.dimension());
        if (dimBuckets == null) {
            return;
        }

        var bucketX = bucketCoord(location.centerPos().getX() >> 4);
        var bucketZ = bucketCoord(location.centerPos().getZ() >> 4);
        var key = bucketKey(bucketX, bucketZ);

        var bucket = dimBuckets.get(key);
        if (bucket == null) {
            return;
        }

        bucket.remove(location.id());
        if (bucket.isEmpty()) {
            dimBuckets.remove(key);
        }
        if (dimBuckets.isEmpty()) {
            byCenterDim.remove(location.dimension());
        }
    }

    private static int bucketCoord(int chunkCoord) {
        return Math.floorDiv(chunkCoord, SPATIAL_BUCKET_CHUNKS);
    }

    private static long bucketKey(int x, int z) {
        return (((long) x) << 32) | (z & 0xFFFFFFFFL);
    }
}

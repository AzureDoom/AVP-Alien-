package com.alien.common.gameplay.hive2.migration;

import com.alien.Alien;
import com.alien.common.gameplay.hive.HiveFactionData;
import com.alien.common.gameplay.hive.HiveIds;
import com.alien.common.gameplay.hive2.faction.VariantFactionRegistry;
import com.alien.common.gameplay.hive2.id.HiveLocationIds;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.registry.init.AlienFactionDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * One-time migration of legacy {@code avp_alien:hive/<uuid>} factions into the new variant + lineage + location
 * structure. Runs once at server start, before {@link HiveLocationRegistry#rebuildFromFactions()}.
 * <p>
 * Per {@code HIVE_REDESIGN_11_IMPLEMENTATION.md} § 6: each legacy hive becomes a fresh single-location lineage. The old
 * hive's {@code centerPos} becomes the location's center; chunks within {@link #LEGACY_HIVE_RADIUS_CHUNKS} are claimed;
 * the legacy reserves and members transfer 1:1.
 * <p>
 * Idempotent — a server with no legacy factions is a no-op. After migration, the old factions are removed via BLib so
 * the next start has nothing to migrate. The mapping (old id → new lineage + location) is written to
 * {@link MigrationLog}.
 */
public final class OldHiveMigrator {

    /**
     * Default of legacy {@code AlienProperties.Hive.RADIUS_IN_BLOCKS} from {@code AlienPropertySchema}. Hardcoded here
     * so the property can be deleted in this same cutover. A custom server-side override of the radius is lost —
     * best-effort migration; the data is still preserved as a single-chunk-radius location after the conversion.
     */
    private static final int LEGACY_HIVE_RADIUS_BLOCKS = 64;

    private static final int LEGACY_HIVE_RADIUS_CHUNKS = LEGACY_HIVE_RADIUS_BLOCKS / 16;

    private OldHiveMigrator() {}

    /**
     * Walk every faction; for each {@code avp_alien:hive/*} create a new lineage + first location and remove the legacy
     * faction. Returns the number of hives migrated.
     */
    public static int run(MinecraftServer server) {
        var legacyHiveIds = new ArrayList<ResourceLocation>();
        for (var factionId : Alien.MOD.factions().getAllIds()) {
            if (HiveIds.isHiveId(factionId)) {
                legacyHiveIds.add(factionId);
            }
        }

        if (legacyHiveIds.isEmpty()) {
            Alien.LOGGER.info("Hive2 migrator: no legacy hive factions found — skipping");
            return 0;
        }

        Alien.LOGGER.info("Hive2 migrator: found {} legacy hive faction(s) — beginning migration", legacyHiveIds.size());

        var mapping = new LinkedHashMap<ResourceLocation, MigrationLog.MigrationEntry>();
        var migratedCount = 0;
        for (var legacyId : legacyHiveIds) {
            var entry = migrateOne(server, legacyId);
            if (entry != null) {
                mapping.put(legacyId, entry);
                migratedCount++;
            }
        }

        for (var legacyId : legacyHiveIds) {
            Alien.MOD.factions().remove(legacyId);
        }

        MigrationLog.write(server, mapping);

        Alien.LOGGER.info(
            "Hive2 migrator: migrated {}/{} legacy hives; mapping written to migration log",
            migratedCount,
            legacyHiveIds.size()
        );

        return migratedCount;
    }

    private static MigrationLog.MigrationEntry migrateOne(MinecraftServer server, ResourceLocation legacyId) {
        var legacyFaction = Alien.MOD.factions().get(legacyId);
        if (legacyFaction == null) {
            Alien.LOGGER.warn("Hive2 migrator: legacy hive {} resolves to null faction — skipping", legacyId);
            return null;
        }
        if (!(legacyFaction.data() instanceof HiveFactionData legacyData)) {
            Alien.LOGGER.warn(
                "Hive2 migrator: legacy hive {} has data type {}, expected HiveFactionData — skipping",
                legacyId,
                legacyFaction.data() == null ? "null" : legacyFaction.data().getClass().getName()
            );
            return null;
        }

        var dimension = legacyData.getDimension();
        var serverLevel = server.getLevel(dimension);
        if (serverLevel == null) {
            Alien.LOGGER.warn(
                "Hive2 migrator: legacy hive {} dimension {} not loaded — skipping",
                legacyId,
                dimension.location()
            );
            return null;
        }

        var variant = legacyData.getVariant();
        var centerPos = legacyData.getCenterPos();
        var leaderId = legacyData.getLeaderId();

        // 1. Mint variant + lineage factions.
        var variantFaction = VariantFactionRegistry.getOrCreate(variant);
        var lineageId = LineageIds.create();
        var lineageFaction = Alien.MOD.factions().getOrCreate(lineageId, AlienFactionDataTypes.LINEAGE);
        var lineageData = lineageFaction.data();
        if (lineageData == null) {
            Alien.LOGGER.warn("Hive2 migrator: lineage {} data null after creation — aborting hive {}", lineageId, legacyId);
            return null;
        }

        com.alien.common.gameplay.hive2.faction.FactionAesthetics.applyDefaults(
            lineageFaction,
            variant,
            com.alien.common.gameplay.hive2.faction.FactionAesthetics.Tier.LINEAGE
        );
        lineageData.setFactionId(lineageId);

        lineageData.setVariant(variant);
        lineageData.setParentVariantFactionId(variantFaction.id());
        lineageData.setDimension(dimension);
        lineageData.setFounderId(leaderId);

        // 2. Mint the first location at the legacy center, claim every chunk within LEGACY_HIVE_RADIUS_CHUNKS.
        var locationId = HiveLocationIds.create();
        var location = new HiveLocation(locationId, lineageId, dimension, centerPos, leaderId);

        var currentTick = server.overworld().getGameTime();
        claimChunksWithinRadius(serverLevel, location, centerPos, currentTick);

        // 3. Copy reserves verbatim — the legacy reserve manager wrapped an EntityReserves; copy each type's count.
        var legacyReserves = legacyData.getReserves();
        for (var type : legacyReserves.getAvailableEntityTypes()) {
            var count = legacyReserves.getCount(type);
            if (count > 0) {
                location.localReserves().tryAdd(type, count);
            }
        }

        lineageData.addLocation(location);
        HiveLocationRegistry.INSTANCE.register(location);

        var locationFaction = Alien.MOD.factions().getOrCreate(locationId.value(), AlienFactionDataTypes.LOCATION);
        com.alien.common.gameplay.hive2.faction.FactionAesthetics.applyDefaults(
            locationFaction,
            variant,
            com.alien.common.gameplay.hive2.faction.FactionAesthetics.Tier.LOCATION
        );
        var locationData = locationFaction.data();
        if (locationData != null) {
            locationData.setLocationId(locationId);
        }

        // 4. Transfer membership: every old member becomes a lineage + variant member.
        var memberSnapshot = new ArrayList<>(legacyFaction.membership().getMembers());
        var membersTransferred = 0;
        for (var member : memberSnapshot) {
            // Add to lineage membership directly via the FactionMember (not addEntity — entity may be unloaded).
            lineageFaction.membership().addMember(member);
            membersTransferred++;
            // The variant faction join happens automatically next time the entity loads (Alien.onEntityLoad).
        }
        // Don't bother removing from the legacy membership — we delete the faction below in run().

        Alien.LOGGER.info(
            "Hive2 migrator: migrated legacy hive {} → lineage {} + location {} ({} chunks claimed, {} members)",
            legacyId,
            lineageId,
            locationId,
            location.claimedChunks().size(),
            membersTransferred
        );

        return new MigrationLog.MigrationEntry(legacyId, lineageId, locationId, centerPos, dimension.location());
    }

    private static void claimChunksWithinRadius(
        net.minecraft.server.level.ServerLevel level,
        HiveLocation location,
        BlockPos center,
        long currentTick
    ) {
        var centerChunkX = center.getX() >> 4;
        var centerChunkZ = center.getZ() >> 4;

        // Claim every chunk whose center is within LEGACY_HIVE_RADIUS_BLOCKS of the legacy center.
        for (var dx = -LEGACY_HIVE_RADIUS_CHUNKS; dx <= LEGACY_HIVE_RADIUS_CHUNKS; dx++) {
            for (var dz = -LEGACY_HIVE_RADIUS_CHUNKS; dz <= LEGACY_HIVE_RADIUS_CHUNKS; dz++) {
                if (dx * dx + dz * dz > LEGACY_HIVE_RADIUS_CHUNKS * LEGACY_HIVE_RADIUS_CHUNKS) {
                    continue;
                }
                var chunk = new ChunkPos(centerChunkX + dx, centerChunkZ + dz);

                // Skip if some other location already owns this chunk (shouldn't happen on a fresh hive2 system,
                // but defensive).
                if (HiveLocationRegistry.INSTANCE.getByChunk(level.dimension(), chunk) != null) {
                    continue;
                }
                com.alien.common.gameplay.hive2.growth.HiveLocationClaims.claim(level, location, chunk, currentTick);
            }
        }
    }

}

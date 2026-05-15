package com.alien.common.gameplay.hive2.growth;

import com.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.drone.Drone;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.entity.living.alien.xenomorph.runner.Runner;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.HiveLocationFactionProvisioner;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.id.HiveLocationIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.gameplay.hive2.location.HiveLocationSpacing;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * Unloaded spread per {@code HIVE_REDESIGN_08_LINEAGE_SPREAD.md} § 3.
 * <p>
 * Drives "the lineage conquers an unattended dimension over real-world weeks" without forcing queen entities into
 * unloaded chunks. Folded into {@link com.alien.common.gameplay.hive2.tick.LineageGrowthScanTask}'s loop — runs once
 * per scan tick per hive location when conditions allow.
 * <p>
 * Conditions (must all hold):
 * <ul>
 * <li>Source location is old enough or past its own {@code lineageSpreadCooldownTicks} spread cooldown.</li>
 * <li>Lineage hasn't hit {@code maxLocationsPerLineage}.</li>
 * </ul>
 * <p>
 * The candidate position is picked uniformly within the spread zone of the source location. We reject if the
 * candidate's chunk is already in any existing location's claimed territory or has been previously
 * decorated. Otherwise: found a new location with bootstrap reserves and bump the source location's spread tick.
 */
public final class AbstractSpreadAttempt {

    private static final Random RANDOM = new Random();

    private static final String RESULT_COOLDOWN = "cooldown";

    private static final String RESULT_MAX_LOCATIONS = "max_locations";

    private static final String RESULT_DIMENSION_UNLOADED = "dimension_unloaded";

    private static final String RESULT_SOURCE_INACTIVE = "source_inactive";

    private static final String RESULT_SPREAD_DISABLED = "spread_disabled";

    private static final String RESULT_OCCUPIED = "occupied";

    private static final String RESULT_TOO_CLOSE = "too_close";

    private static final String RESULT_DECORATED = "decorated";

    private static final String RESULT_SUCCESS = "success";

    private AbstractSpreadAttempt() {}

    /**
     * Attempts an abstract spread for {@code lineage}. Returns the minted location id on success, or null when
     * conditions or the candidate check failed.
     */
    public static @Nullable HiveLocationId tryRun(
        MinecraftServer server,
        ResourceLocation lineageId,
        LineageFactionData lineage,
        HiveLocation sourceLocation,
        long currentTick
    ) {
        var config = HiveLocationRegistry.INSTANCE.config();

        // Cooldown.
        var remainingCooldownTicks = remainingCooldownTicks(sourceLocation, currentTick, config);
        if (remainingCooldownTicks > 0L) {
            record(
                sourceLocation,
                lineage,
                currentTick,
                RESULT_COOLDOWN,
                null,
                null,
                "Next eligible in " + remainingCooldownTicks + " ticks."
            );
            return null;
        }

        // Max locations cap.
        if (lineage.locationsById().size() >= config.maxLocationsPerLineage()) {
            record(
                sourceLocation,
                lineage,
                currentTick,
                RESULT_MAX_LOCATIONS,
                null,
                null,
                "Lineage has " + lineage.locationsById().size() + "/" + config.maxLocationsPerLineage() + " locations."
            );
            return null;
        }

        var serverLevel = server.getLevel(lineage.dimension());
        if (serverLevel == null) {
            record(
                sourceLocation,
                lineage,
                currentTick,
                RESULT_DIMENSION_UNLOADED,
                null,
                null,
                "Dimension " + lineage.dimension().location() + " is not loaded."
            );
            return null;
        }

        if (!sourceLocation.isAlive()) {
            record(sourceLocation, lineage, currentTick, RESULT_SOURCE_INACTIVE, null, null, "Source location is not alive.");
            return null;
        }

        var candidateChunk = pickCandidateInSpreadZone(sourceLocation, config);
        if (candidateChunk == null) {
            record(
                sourceLocation,
                lineage,
                currentTick,
                RESULT_SPREAD_DISABLED,
                null,
                null,
                "maxLineageSpreadChunks is " + config.maxLineageSpreadChunks() + "."
            );
            return null;
        }

        var candidateValidation = validateCandidate(lineage, candidateChunk, config);
        if (!candidateValidation.valid()) {
            record(
                sourceLocation,
                lineage,
                currentTick,
                candidateValidation.result(),
                candidateChunk,
                null,
                candidateValidation.detail()
            );
            return null;
        }

        // Found.
        var locationId = mintAbstractLocation(serverLevel, lineage, lineageId, candidateChunk, currentTick);

        sourceLocation.setLastAbstractSpreadTick(currentTick);
        record(
            sourceLocation,
            lineage,
            currentTick,
            RESULT_SUCCESS,
            candidateChunk,
            locationId,
            "Minted a location with queen and bootstrap worker reserves."
        );

        Alien.LOGGER.info(
            "Hive2: abstract spread for lineage {}: minted location {} at chunk {} (sourced from {})",
            lineageId,
            locationId,
            candidateChunk,
            sourceLocation.id()
        );

        return locationId;
    }

    private static long remainingCooldownTicks(HiveLocation sourceLocation, long currentTick, HiveConfig config) {
        var lastSpreadTick = sourceLocation.lastAbstractSpreadTick();
        if (lastSpreadTick > 0L) {
            return Math.max(0L, lastSpreadTick + config.lineageSpreadCooldownTicks() - currentTick);
        }
        return Math.max(0L, config.lineageSpreadCooldownTicks() - sourceLocation.ageInTicks());
    }

    private static @Nullable ChunkPos pickCandidateInSpreadZone(HiveLocation source, HiveConfig config) {
        var maxSpread = config.maxLineageSpreadChunks();
        if (maxSpread <= 0) {
            return null;
        }

        var sourceChunk = new ChunkPos(source.centerPos());
        // Uniform random offset within [-maxSpread, +maxSpread] in both axes.
        var dx = RANDOM.nextInt(2 * maxSpread + 1) - maxSpread;
        var dz = RANDOM.nextInt(2 * maxSpread + 1) - maxSpread;
        return new ChunkPos(sourceChunk.x + dx, sourceChunk.z + dz);
    }

    private static CandidateValidation validateCandidate(LineageFactionData lineage, ChunkPos candidate, HiveConfig config) {
        // Reject any chunk owned by any existing location.
        var occupant = HiveLocationRegistry.INSTANCE.getByChunk(lineage.dimension(), candidate);
        if (occupant != null) {
            return CandidateValidation.reject(
                RESULT_OCCUPIED,
                "Candidate chunk is already claimed by " + occupant.id().value() + "."
            );
        }

        if (
            !HiveLocationSpacing.isFarEnoughFromExistingLocations(
                lineage.dimension(),
                candidate,
                config.minimumHiveLocationDistanceChunks()
            )
        ) {
            return CandidateValidation.reject(
                RESULT_TOO_CLOSE,
                "Candidate is within " + config.minimumHiveLocationDistanceChunks() + " chunks of an existing hive location."
            );
        }

        // Reject if the chunk has the decorated_by_hive flag from any of THIS lineage's previously-killed locations.
        // (Soft cooldown — a player who clears a hive gets a brief reprieve before it tries to come back.)
        for (var location : lineage.locationsById().values()) {
            if (location.decoratedChunks().contains(candidate)) {
                return CandidateValidation.reject(
                    RESULT_DECORATED,
                    "Candidate chunk was previously decorated by " + location.id().value() + "."
                );
            }
        }

        return CandidateValidation.accept();
    }

    private static HiveLocationId mintAbstractLocation(
        ServerLevel level,
        LineageFactionData lineage,
        ResourceLocation lineageId,
        ChunkPos candidate,
        long currentTick
    ) {
        var locationId = HiveLocationIds.create();
        var centerPos = candidate.getMiddleBlockPosition(64); // Y is approximate; chunk-load corrects later
        var location = new HiveLocation(
            locationId,
            lineageId,
            lineage.dimension(),
            centerPos,
            null // No founder — minted abstractly without a queen entity.
        );
        location.setLocationNumber(lineage.allocateLocationNumber());
        HiveLocationFactionProvisioner.ensure(location, lineage);

        claimInitialCore(level, location, candidate, currentTick);

        var queenType = Queen.getType(lineage.variant());
        var droneType = Drone.getType(lineage.variant());
        var runnerType = Runner.getType(lineage.variant());
        if (queenType != null) {
            location.localReserves().tryAdd((EntityType<?>) queenType, 1);
        }
        if (droneType != null) {
            location.localReserves().tryAdd((EntityType<?>) droneType, 1);
        }
        if (runnerType != null) {
            location.localReserves().tryAdd((EntityType<?>) runnerType, 1);
        }

        location.setBiomass(0);
        location.setLastGrowthTick(currentTick);

        lineage.addLocation(location);
        HiveLocationRegistry.INSTANCE.register(location);

        return locationId;
    }

    private static void claimInitialCore(ServerLevel level, HiveLocation location, ChunkPos centerChunk, long currentTick) {
        var radius = HiveLocationRegistry.INSTANCE.config().initialHiveLocationClaimRadiusChunks();
        for (var dx = -radius; dx <= radius; dx++) {
            for (var dz = -radius; dz <= radius; dz++) {
                var chunk = new ChunkPos(centerChunk.x + dx, centerChunk.z + dz);
                if (HiveLocationRegistry.INSTANCE.getByChunk(level.dimension(), chunk) != null) {
                    continue;
                }
                HiveLocationClaims.claim(level, location, chunk, currentTick);
            }
        }
    }

    private static void record(
        HiveLocation sourceLocation,
        LineageFactionData lineage,
        long currentTick,
        String result,
        @Nullable ChunkPos candidateChunk,
        @Nullable HiveLocationId createdLocationId,
        String detail
    ) {
        sourceLocation.recordAbstractSpreadAttempt(
            currentTick,
            result,
            candidateChunk,
            createdLocationId,
            detail
        );
        lineage.markDirty();
    }

    private record CandidateValidation(
        boolean valid,
        String result,
        String detail
    ) {
        private static CandidateValidation accept() {
            return new CandidateValidation(true, "", "");
        }

        private static CandidateValidation reject(String result, String detail) {
            return new CandidateValidation(false, result, detail);
        }
    }

}

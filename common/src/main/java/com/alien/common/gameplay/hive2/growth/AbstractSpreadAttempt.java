package com.alien.common.gameplay.hive2.growth;

import com.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.drone.Drone;
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

import java.util.ArrayList;
import java.util.Random;

/**
 * Unloaded spread per {@code HIVE_REDESIGN_08_LINEAGE_SPREAD.md} § 3.
 * <p>
 * Drives "the lineage conquers an unattended dimension over real-world weeks" without forcing queen entities into
 * unloaded chunks. Folded into {@link com.alien.common.gameplay.hive2.tick.LineageGrowthScanTask}'s loop — runs once
 * per scan tick per lineage when conditions allow.
 * <p>
 * Conditions (must all hold):
 * <ul>
 * <li>{@code currentTick - lineage.lastSpreadTick >= lineageSpreadCooldownTicks} (default 30 minutes).</li>
 * <li>Lineage hasn't hit {@code maxLocationsPerLineage}.</li>
 * </ul>
 * <p>
 * The candidate position is picked uniformly within the spread zone of one of the lineage's existing locations. We
 * reject if the candidate's chunk is already in any existing location's claimed territory or has been previously
 * decorated. Otherwise: found a new location with bootstrap reserves and bump {@code lastSpreadTick}.
 */
public final class AbstractSpreadAttempt {

    private static final Random RANDOM = new Random();

    private AbstractSpreadAttempt() {}

    /**
     * Attempts an abstract spread for {@code lineage}. Returns the minted location id on success, or null when
     * conditions or the candidate check failed.
     */
    public static @Nullable HiveLocationId tryRun(
        MinecraftServer server,
        ResourceLocation lineageId,
        LineageFactionData lineage,
        long currentTick
    ) {
        var config = HiveLocationRegistry.INSTANCE.config();

        // Cooldown.
        if (currentTick - lineage.lastSpreadTick() < config.lineageSpreadCooldownTicks()) {
            return null;
        }

        // Max locations cap.
        if (lineage.locationsById().size() >= config.maxLocationsPerLineage()) {
            return null;
        }

        var serverLevel = server.getLevel(lineage.dimension());
        if (serverLevel == null) {
            return null;
        }

        var locations = new ArrayList<>(lineage.locationsById().values());
        if (locations.isEmpty()) {
            return null;
        }

        // Pick a source location uniformly to spread from.
        var sourceLocation = locations.get(RANDOM.nextInt(locations.size()));
        var candidateChunk = pickCandidateInSpreadZone(sourceLocation, config);
        if (candidateChunk == null) {
            return null;
        }

        if (!isCandidateValid(lineage, candidateChunk, config)) {
            return null;
        }

        // Found.
        var locationId = mintAbstractLocation(serverLevel, lineage, lineageId, candidateChunk, currentTick);

        lineage.setLastSpreadTick(currentTick);

        Alien.LOGGER.info(
            "Hive2: abstract spread for lineage {}: minted location {} at chunk {} (sourced from {})",
            lineageId,
            locationId,
            candidateChunk,
            sourceLocation.id()
        );

        return locationId;
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

    private static boolean isCandidateValid(LineageFactionData lineage, ChunkPos candidate, HiveConfig config) {
        // Reject any chunk owned by any existing location.
        var occupant = HiveLocationRegistry.INSTANCE.getByChunk(lineage.dimension(), candidate);
        if (occupant != null) {
            return false;
        }

        if (
            !HiveLocationSpacing.isFarEnoughFromExistingLocations(
                lineage.dimension(),
                candidate,
                config.minimumHiveLocationDistanceChunks()
            )
        ) {
            return false;
        }

        // Reject if the chunk has the decorated_by_hive flag from any of THIS lineage's previously-killed locations.
        // (Soft cooldown — a player who clears a hive gets a brief reprieve before it tries to come back.)
        for (var location : lineage.locationsById().values()) {
            if (location.decoratedChunks().contains(candidate)) {
                return false;
            }
        }

        return true;
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

        location.setPendingFounderQueen(true);

        // Bootstrap workers. The pending founder queen will pull them into the world when she materializes.
        var droneType = Drone.getType(lineage.variant());
        var runnerType = Runner.getType(lineage.variant());
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

}

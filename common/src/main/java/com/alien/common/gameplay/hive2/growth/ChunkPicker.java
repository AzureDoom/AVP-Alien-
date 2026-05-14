package com.alien.common.gameplay.hive2.growth;

import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Picks the next chunk a location should try to claim. Walks the cardinal-adjacent frontier of its current
 * {@code claimedChunks}, filters out chunks owned by any other registered location, and returns the candidate closest
 * to the location's center (Chebyshev distance). Returns {@code null} when the location is hemmed in.
 * <p>
 * See {@code HIVE_REDESIGN_09_GROWTH.md} § 3.2.
 * <p>
 * Phase 7 uses the strict "any occupant blocks" rule. Same-variant cross-lineage <em>contests</em> per
 * {@code 03_LOCATIONS} § 3 are deferred to Phase 11; until then no overlapping claims are minted.
 */
public final class ChunkPicker {

    private static final int[][] CARDINAL_OFFSETS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    private ChunkPicker() {}

    public static @Nullable ChunkPos pickNextChunk(HiveLocation location, HiveConfig config) {
        if (location.claimedChunks().size() >= config.maxChunksPerLocation()) {
            return null;
        }

        var dimension = location.dimension();
        var centerChunk = new ChunkPos(location.centerPos());
        var frontier = collectFrontier(location, dimension);

        if (frontier.isEmpty()) {
            return null;
        }

        // Tiebreak (Chebyshev, Manhattan, x, z): within a ring, axial extremes (e.g., (2,0)) come before
        // corners (e.g., (2,2)), so partial fills are rotationally symmetric — a cross filling out toward a
        // square, rather than a square with one column missing.
        return frontier
            .stream()
            .min(
                Comparator.<ChunkPos>comparingInt(c -> chebyshev(c, centerChunk))
                    .thenComparingInt(c -> manhattan(c, centerChunk))
                    .thenComparingInt(c -> c.x)
                    .thenComparingInt(c -> c.z)
            )
            .orElse(null);
    }

    private static Set<ChunkPos> collectFrontier(
        HiveLocation location,
        net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension
    ) {
        var frontier = new HashSet<ChunkPos>();

        for (var owned : location.claimedChunks()) {
            for (var offset : CARDINAL_OFFSETS) {
                var candidate = new ChunkPos(owned.x + offset[0], owned.z + offset[1]);

                if (location.claimedChunks().contains(candidate)) {
                    continue;
                }

                // Reject any chunk owned by another location (any lineage). Same-variant contests
                // are deferred to Phase 11; for now we bake in the no-overlap rule.
                var occupant = HiveLocationRegistry.INSTANCE.getByChunk(dimension, candidate);
                if (occupant != null) {
                    continue;
                }

                frontier.add(candidate);
            }
        }

        return frontier;
    }

    private static int chebyshev(ChunkPos a, ChunkPos b) {
        return Math.max(Math.abs(a.x - b.x), Math.abs(a.z - b.z));
    }

    private static int manhattan(ChunkPos a, ChunkPos b) {
        return Math.abs(a.x - b.x) + Math.abs(a.z - b.z);
    }
}

package com.alien.common.gameplay.hive2.spawning;

import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Replaces the legacy {@code HiveSpaceManager} as the spawn-gating layer for the new system. Combines three checks:
 * <ol>
 * <li><b>Containment</b> — the position is inside one of the location's claimed chunks.</li>
 * <li><b>Caste distance</b> — the entity type's chunk-distance range from the location's center allows the spawn (per
 * {@link CasteDistanceRule}).</li>
 * <li><b>Reserves</b> — the location has at least one of the requested entity type in its local reserves.</li>
 * </ol>
 * <p>
 * Returns the matching {@link HiveLocation} on success so callers can decrement reserves on the actual spawn.
 * <p>
 * See {@code HIVE_REDESIGN_03_LOCATIONS.md} § 4 and {@code HIVE_REDESIGN_05_RESERVES.md} § 4.
 */
public final class HiveLocationSpawnGate {

    private HiveLocationSpawnGate() {}

    /**
     * Full spawn-rule check. Returns the location that would accept this spawn, or null if no location applies.
     */
    public static @Nullable HiveLocation findSpawnableLocation(
        LevelAccessor level,
        EntityType<?> entityType,
        BlockPos pos
    ) {
        var location = locationContaining(level, pos);
        if (location == null) {
            return null;
        }

        var config = HiveLocationRegistry.INSTANCE.config();
        var range = CasteDistanceRule.rangeFor(entityType, config);
        if (range == null) {
            return null;
        }

        var distance = chunkDistance(location.centerPos(), pos);
        if (!range.contains(distance)) {
            return null;
        }

        if (!location.localReserves().canSpawn(entityType)) {
            return null;
        }

        return location;
    }

    /**
     * Lightweight containment check used by "is in a hive" predicates (egg laying, resin spread, despawn-into-reserves
     * routing). No reserves or caste-distance check — just "does some location own this chunk."
     */
    public static @Nullable HiveLocation locationContaining(LevelAccessor level, BlockPos pos) {
        if (!(level instanceof Level concreteLevel)) {
            return null;
        }
        var chunk = new net.minecraft.world.level.ChunkPos(pos);
        var hit = HiveLocationRegistry.INSTANCE.getByChunk(concreteLevel.dimension(), chunk);
        if (hit == null || !hit.isAlive()) {
            return null;
        }
        return hit;
    }

    private static int chunkDistance(BlockPos a, BlockPos b) {
        var dx = Math.abs((a.getX() >> 4) - (b.getX() >> 4));
        var dz = Math.abs((a.getZ() >> 4) - (b.getZ() >> 4));
        // Chebyshev — the caste range is a square band, matching how chunk distances feel in-game.
        return Math.max(dx, dz);
    }
}

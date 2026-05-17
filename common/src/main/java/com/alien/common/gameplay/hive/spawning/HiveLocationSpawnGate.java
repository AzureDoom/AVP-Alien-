package com.alien.common.gameplay.hive.spawning;

import com.alien.common.gameplay.hive.location.HiveLocation;
import com.alien.common.gameplay.hive.location.HiveLocationRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

/**
 * Replaces the legacy {@code HiveSpaceManager} as the spawn-gating layer for the new system. Combines two checks:
 * <ol>
 * <li><b>Containment</b> — the position is inside one of the location's claimed chunks.</li>
 * <li><b>Reserves</b> — the location has at least one of the requested entity type in its local reserves.</li>
 * </ol>
 * <p>
 * Caste-distance restrictions were removed because they conflicted with surface spawning expectations — castes can now
 * spawn anywhere within the location's claimed chunks subject only to reserves and vanilla monster rules.
 * <p>
 * Returns the matching {@link HiveLocation} on success so callers can decrement reserves on the actual spawn.
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

        if (!location.localReserves().canSpawn(entityType)) {
            return null;
        }

        return location;
    }

    /**
     * Lightweight containment check used by "is in a hive" predicates (egg laying, resin spread, despawn-into-reserves
     * routing). No reserves check — just "does some location own this chunk."
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
}

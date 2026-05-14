package com.alien.common.gameplay.hive2.location;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public final class HiveLocationSpacing {

    private HiveLocationSpacing() {}

    public static boolean isFarEnoughFromExistingLocations(
        ResourceKey<Level> dimension,
        ChunkPos candidate,
        int minimumDistanceChunks
    ) {
        if (minimumDistanceChunks <= 0) {
            return true;
        }

        for (var location : HiveLocationRegistry.INSTANCE.all()) {
            if (!location.isAlive() || !location.dimension().equals(dimension)) {
                continue;
            }

            if (chunkDistance(new ChunkPos(location.centerPos()), candidate) < minimumDistanceChunks) {
                return false;
            }
        }

        return true;
    }

    public static int chunkDistance(ChunkPos left, ChunkPos right) {
        var dx = Math.abs(left.x - right.x);
        var dz = Math.abs(left.z - right.z);
        return Math.max(dx, dz);
    }
}

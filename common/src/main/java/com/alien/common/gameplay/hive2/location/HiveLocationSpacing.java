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
        return HiveLocationRegistry.INSTANCE.findTooCloseToCenter(dimension, candidate, minimumDistanceChunks) == null;
    }

    public static int chunkDistance(ChunkPos left, ChunkPos right) {
        var dx = Math.abs(left.x - right.x);
        var dz = Math.abs(left.z - right.z);
        return Math.max(dx, dz);
    }
}

package com.alien.common.gameplay.hive2.convoy;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.spawning.ReserveSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

/**
 * Materializes abstract convoy members when a player gets close enough to intercept the convoy.
 */
public final class ConvoyInterception {

    private ConvoyInterception() {}

    public static boolean tryIntercept(MinecraftServer server, Convoy convoy, HiveConfig config) {
        var level = server.getLevel(convoy.dimension());
        if (level == null || convoy.composition().getCount() <= 0) {
            return false;
        }

        var player = firstInterceptingPlayer(level.players(), convoy, config);
        if (player == null) {
            return false;
        }

        var spawnPos = new BlockPos(
            (int) Math.round(convoy.currentPos().x),
            (int) Math.round(convoy.currentPos().y),
            (int) Math.round(convoy.currentPos().z)
        );

        var spawnedCount = 0;
        for (var entityType : new java.util.ArrayList<>(convoy.composition().getAvailableEntityTypes())) {
            var count = convoy.composition().getCount(entityType);
            for (var i = 0; i < count; i++) {
                var spawned = entityType.spawn(level, spawnPos, MobSpawnType.MOB_SUMMONED);
                if (spawned == null) {
                    continue;
                }

                ReserveSpawnUtil.markSpawnedFromReserves(spawned);
                if (spawned instanceof Mob mob) {
                    mob.setPersistenceRequired();
                    mob.setTarget(player);
                }
                convoy.composition().add(entityType, -1);
                spawnedCount++;
            }
        }

        if (spawnedCount <= 0) {
            return false;
        }

        Alien.LOGGER.info(
            "Convoy {} intercepted by {} at {} — spawned {} attackers",
            convoy.id(),
            player.getGameProfile().getName(),
            spawnPos,
            spawnedCount
        );
        return true;
    }

    private static ServerPlayer firstInterceptingPlayer(java.util.List<ServerPlayer> players, Convoy convoy, HiveConfig config) {
        var radius = config.convoyInterceptRadiusBlocks();
        if (radius <= 0) {
            return null;
        }

        var radiusSqr = (double) radius * radius;
        for (var player : players) {
            if (!player.isAlive() || player.isSpectator() || player.isCreative()) {
                continue;
            }
            if (player.position().distanceToSqr(convoy.currentPos()) <= radiusSqr) {
                return player;
            }
        }
        return null;
    }
}

package com.alien.common.gameplay.hive.convoy;

import com.alien.Alien;
import com.alien.common.gameplay.hive.config.HiveConfig;
import com.alien.common.gameplay.hive.faction.LineageFactionData;
import com.alien.common.registry.RaidWaveProfileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Materializes abstract convoy members when a player gets close enough to intercept the convoy.
 */
public final class ConvoyInterception {

    private ConvoyInterception() {}

    public static boolean tryIntercept(
        MinecraftServer server,
        Convoy convoy,
        LineageFactionData lineage,
        HiveConfig config
    ) {
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
        if (convoy instanceof Convoy.Raid raid) {
            var breakStartedTick = raid.waveBreakStartedTick();
            var compositionCount = raid.composition().getCount();
            var nextWaveIndex = raid.nextWaveIndex();
            spawnedCount = ConvoyMaterialization.spawnNextRaidWave(
                level,
                raid,
                RaidWaveProfileRegistry.forVariant(lineage.variant()),
                spawnPos,
                player,
                server.overworld().getGameTime()
            );
            if (
                spawnedCount > 0
                    || raid.waveBreakStartedTick() != breakStartedTick
                    || raid.composition().getCount() != compositionCount
                    || raid.nextWaveIndex() != nextWaveIndex
            ) {
                lineage.markDirty();
            }
        } else {
            spawnedCount = ConvoyMaterialization.spawnAll(level, convoy, spawnPos, player);
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
        return false;
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

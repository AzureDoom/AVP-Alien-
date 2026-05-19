package com.alien.common.gameplay.hive.tick;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.hive.location.HiveLocation;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.alien.common.util.AlienPredicates;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Retargets loaded hive xenomorphs when players trespass in the location's claimed chunks.
 */
public final class HiveTerritoryAggroTask {

    private static final long INTERVAL_TICKS = 20L;

    private HiveTerritoryAggroTask() {}

    public static boolean shouldFire(long currentTick) {
        return currentTick % INTERVAL_TICKS == 0L;
    }

    public static void run(ServerLevel level, HiveLocation location) {
        var playersInTerritory = playersInTerritory(level, location);
        if (playersInTerritory.isEmpty()) {
            return;
        }

        for (var entry : location.loadedMembersByType().entrySet()) {
            if (!entry.getKey().is(AlienEntityTypeTags.XENOMORPHS)) {
                continue;
            }

            aggroMembers(level, entry.getValue(), playersInTerritory);
        }
    }

    private static List<ServerPlayer> playersInTerritory(ServerLevel level, HiveLocation location) {
        var players = new ArrayList<ServerPlayer>();
        for (var player : level.players()) {
            if (location.claimedChunks().contains(new ChunkPos(player.blockPosition()))) {
                players.add(player);
            }
        }
        return players;
    }

    private static void aggroMembers(ServerLevel level, Set<UUID> memberIds, List<ServerPlayer> playersInTerritory) {
        for (var memberId : memberIds) {
            var entity = level.getEntity(memberId);
            if (!(entity instanceof Xenomorph xenomorph) || !xenomorph.isAlive() || xenomorph.isRemoved()) {
                continue;
            }

            var target = nearestTarget(xenomorph, playersInTerritory);
            if (target != null) {
                xenomorph.setHiveIntruderTarget(target);
            }
        }
    }

    private static @Nullable ServerPlayer nearestTarget(Xenomorph xenomorph, List<ServerPlayer> playersInTerritory) {
        ServerPlayer nearest = null;
        var nearestDistanceSqr = Double.MAX_VALUE;

        for (var player : playersInTerritory) {
            if (!AlienPredicates.canAcquireTarget(xenomorph, player)) {
                continue;
            }

            var distanceSqr = xenomorph.distanceToSqr(player);
            if (distanceSqr < nearestDistanceSqr) {
                nearest = player;
                nearestDistanceSqr = distanceSqr;
            }
        }

        return nearest;
    }
}

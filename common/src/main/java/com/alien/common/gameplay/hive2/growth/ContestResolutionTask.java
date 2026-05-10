package com.alien.common.gameplay.hive2.growth;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;

/**
 * Resolves contested chunks per {@code HIVE_REDESIGN_03_LOCATIONS.md} § 3.
 * <p>
 * Every {@code contestTickWindow} (default 60 seconds), scans every level's contested chunks via
 * {@code TerritoryManager.getAllContestedChunks(level)}. For each contested chunk, counts the number of loaded
 * xenomorphs in that chunk per claimant lineage; the side with more wins. The losers' claims on that chunk are removed.
 * <p>
 * If only one xenomorph is present (or zero), the resolution is deferred — no decisive winner. This avoids flipping an
 * empty contested chunk back and forth.
 */
public final class ContestResolutionTask {

    private ContestResolutionTask() {}

    public static void scanAll(MinecraftServer server) {
        for (var dim : server.levelKeys()) {
            var level = server.getLevel(dim);
            if (level == null) {
                continue;
            }
            scanLevel(level);
        }
    }

    private static void scanLevel(ServerLevel level) {
        var contestedChunks = Alien.MOD.territory().getAllContestedChunks(level);
        if (contestedChunks.isEmpty()) {
            return;
        }

        for (var chunk : contestedChunks) {
            resolveChunk(level, chunk);
        }
    }

    private static void resolveChunk(ServerLevel level, ChunkPos chunk) {
        var claimants = Alien.MOD.territory().getClaimants(level, chunk);
        if (claimants.size() < 2) {
            return;
        }

        // Count xenomorphs per claimant within this chunk.
        var counts = new HashMap<ResourceLocation, Integer>();
        for (var claimantId : claimants) {
            if (!LineageIds.isLineageId(claimantId)) {
                continue;
            }
            counts.put(claimantId, countXenomorphsIn(level, chunk, claimantId));
        }

        if (counts.size() < 2) {
            return;
        }

        ResourceLocation winner = null;
        var bestCount = -1;
        var tied = false;
        for (var entry : counts.entrySet()) {
            if (entry.getValue() > bestCount) {
                bestCount = entry.getValue();
                winner = entry.getKey();
                tied = false;
            } else if (entry.getValue() == bestCount) {
                tied = true;
            }
        }

        // No decisive winner — leave the contest pending.
        if (winner == null || bestCount == 0 || tied) {
            return;
        }

        // Remove every loser's claim on this chunk. Walk the loser's locations in this dim and find the one that
        // owns this chunk in claimedChunks; release it via HiveLocationClaims.
        for (var entry : counts.entrySet()) {
            var loserId = entry.getKey();
            if (loserId.equals(winner)) {
                continue;
            }
            releaseChunkFromLineage(level, loserId, chunk);
        }

        Alien.LOGGER.info(
            "Hive2: contest resolved at {} → winner {} ({} losers released)",
            chunk,
            winner,
            counts.size() - 1
        );
    }

    private static int countXenomorphsIn(ServerLevel level, ChunkPos chunk, ResourceLocation lineageId) {
        var faction = Alien.MOD.factions().get(lineageId);
        if (faction == null) {
            return 0;
        }

        var minX = chunk.getMinBlockX();
        var minZ = chunk.getMinBlockZ();
        var maxX = chunk.getMaxBlockX();
        var maxZ = chunk.getMaxBlockZ();

        var box = new AABB(minX, level.getMinBuildHeight(), minZ, maxX + 1, level.getMaxBuildHeight(), maxZ + 1);

        var xenomorphCount = 0;
        for (var entity : level.getEntitiesOfClass(com.alien.common.gameplay.entity.living.alien.Alien.class, box)) {
            if (!entity.getType().is(AlienEntityTypeTags.XENOMORPHS)) {
                continue;
            }
            // Membership check — only count entities that actually belong to this lineage.
            if (Alien.MOD.factions().getFactionIds(entity.getUUID()).contains(lineageId)) {
                xenomorphCount++;
            }
        }
        return xenomorphCount;
    }

    private static void releaseChunkFromLineage(ServerLevel level, ResourceLocation lineageId, ChunkPos chunk) {
        // The byChunk index points at one location per chunk — that's the location that "officially" owns the chunk
        // in the registry. The contest may belong to a different location in the same lineage, so scan all of the
        // lineage's locations to find which one has this chunk in its claimedChunks.
        HiveLocation owningLocation = null;
        for (var locId : HiveLocationRegistry.INSTANCE.byLineage(lineageId)) {
            var loc = HiveLocationRegistry.INSTANCE.get(locId);
            if (loc != null && loc.claimedChunks().contains(chunk)) {
                owningLocation = loc;
                break;
            }
        }

        if (owningLocation == null) {
            // The contest claim isn't tracked by any of our locations — just remove the BLib claim directly.
            Alien.MOD.territory().removeClaim(level, chunk, lineageId);
            return;
        }

        HiveLocationClaims.release(level, owningLocation, chunk);
    }
}

package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.model.alien.variant.AlienVariant;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Periodic eligibility check for lineage absorption per {@code HIVE_REDESIGN_02_FACTION_LIFECYCLES.md} § 5.4.
 * <p>
 * Walks every same-variant + same-dimension lineage pair. For each pair:
 * <ul>
 * <li>If their territories are cardinally adjacent, record the first-adjacent tick on both sides (idempotent).</li>
 * <li>If either side has any boss bar in the angry state, reset the timer.</li>
 * <li>If both sides are calm and the timer has elapsed {@code lineageAbsorptionAdjacencyTicks}, hand off to
 * {@link LineageAbsorptionHandler}.</li>
 * <li>If the territories are not adjacent, clear the timer on both sides.</li>
 * </ul>
 * <p>
 * Pair iteration is one-sided (only when {@code A.id} sorts before {@code B.id} lex) so each pair is processed once per
 * pass.
 */
public final class LineageAbsorptionTask {

    private LineageAbsorptionTask() {}

    public static void scanAll(MinecraftServer server) {
        var currentTick = server.overworld().getGameTime();
        var threshold = HiveLocationRegistry.INSTANCE.config().lineageAbsorptionAdjacencyTicks();

        // 1. Group all live lineages by (variant, dimension).
        var bucket = new HashMap<BucketKey, List<LineageInfo>>();
        for (var factionId : new ArrayList<>(Alien.MOD.factions().getAllIds())) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }
            // Skip lineages currently in pendingCivilWar — they're about to be torn apart.
            if (lineage.pendingCivilWar()) {
                continue;
            }
            bucket
                .computeIfAbsent(new BucketKey(lineage.variant(), lineage.dimension()), $ -> new ArrayList<>())
                .add(new LineageInfo(factionId, lineage, chunksOf(lineage)));
        }

        // 2. For each bucket, check every (i, j) pair where i < j by id lex.
        for (var entry : bucket.entrySet()) {
            var members = entry.getValue();
            if (members.size() < 2) {
                continue;
            }
            // Sort by id so the lex-ordering is deterministic.
            members.sort((a, b) -> a.id.compareTo(b.id));

            for (var i = 0; i < members.size(); i++) {
                var a = members.get(i);
                for (var j = i + 1; j < members.size(); j++) {
                    var b = members.get(j);
                    processPair(server, a, b, currentTick, threshold);
                }
            }
        }
    }

    private static void processPair(
        MinecraftServer server,
        LineageInfo a,
        LineageInfo b,
        long currentTick,
        long threshold
    ) {
        var adjacent = areCardinallyAdjacent(a.chunks, b.chunks);

        if (!adjacent) {
            a.lineage.firstAdjacentTickByLineage().remove(b.id);
            b.lineage.firstAdjacentTickByLineage().remove(a.id);
            return;
        }

        if (isAngry(a.lineage) || isAngry(b.lineage)) {
            a.lineage.firstAdjacentTickByLineage().remove(b.id);
            b.lineage.firstAdjacentTickByLineage().remove(a.id);
            return;
        }

        // Both calm + adjacent. Initialize timer on both sides if not set.
        var startA = a.lineage.firstAdjacentTickByLineage().get(b.id);
        var startB = b.lineage.firstAdjacentTickByLineage().get(a.id);

        if (startA == null) {
            a.lineage.firstAdjacentTickByLineage().put(b.id, currentTick);
            startA = currentTick;
        }
        if (startB == null) {
            b.lineage.firstAdjacentTickByLineage().put(a.id, currentTick);
            startB = currentTick;
        }

        var earliestStart = Math.min(startA, startB);
        if (currentTick - earliestStart >= threshold) {
            LineageAbsorptionHandler.absorb(server, a.id, a.lineage, b.id, b.lineage);
        }
    }

    private static Set<ChunkPos> chunksOf(LineageFactionData lineage) {
        var all = new HashSet<ChunkPos>();
        for (var location : lineage.locationsById().values()) {
            all.addAll(location.claimedChunks());
        }
        return all;
    }

    private static boolean areCardinallyAdjacent(Set<ChunkPos> a, Set<ChunkPos> b) {
        if (a.isEmpty() || b.isEmpty()) {
            return false;
        }
        // Iterate the smaller set for efficiency.
        var smaller = a.size() <= b.size() ? a : b;
        var larger = smaller == a ? b : a;

        for (var chunk : smaller) {
            if (
                larger.contains(new ChunkPos(chunk.x + 1, chunk.z))
                    || larger.contains(new ChunkPos(chunk.x - 1, chunk.z))
                    || larger.contains(new ChunkPos(chunk.x, chunk.z + 1))
                    || larger.contains(new ChunkPos(chunk.x, chunk.z - 1))
            ) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAngry(LineageFactionData lineage) {
        for (var location : lineage.locationsById().values()) {
            var bossBar = location.bossBar();
            if (bossBar != null && bossBar.isAngry()) {
                return true;
            }
        }
        return false;
    }

    /** Snapshot of a lineage at scan time. */
    private record LineageInfo(
        ResourceLocation id,
        LineageFactionData lineage,
        Set<ChunkPos> chunks
    ) {}

    private record BucketKey(
        AlienVariant variant,
        ResourceKey<Level> dimension
    ) {}
}

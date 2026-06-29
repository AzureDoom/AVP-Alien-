package com.alien.common.gameplay.hive.growth;

import com.alien.Alien;
import com.alien.common.gameplay.hive.faction.LineageFactionData;
import com.alien.common.gameplay.hive.location.HiveLocation;
import com.alien.common.gameplay.hive.location.HiveLocationRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

/**
 * Chunk-load decoration hook. When a player loads a chunk that's claimed by a {@link HiveLocation}, runs the location's
 * {@link CatchUpEngine} (so its claim set is up-to-date) and marks the chunk as decorated.
 * <p>
 * Phase 7 ships a minimal version: just sets the {@code decorated_by_hive} bookkeeping flag and triggers catch-up. The
 * actual world-decoration (resin block placement + ovomorph spawning per {@code HIVE_REDESIGN_09_GROWTH.md} § 6) is
 * deferred — it requires structure-template work that's out of scope for the foundation phase. The bookkeeping is in
 * place so a future visual pass can hook in cleanly.
 * <p>
 * <b>Threading / re-entrancy:</b> the chunk-load event fires from inside vanilla's chunk-future processing
 * ({@code DistanceManager.runAllUpdates} → {@code chunksToUpdateFutures.forEach(...)}). Running the catch-up + claim
 * loop synchronously here executes work inside that {@code forEach}, and any chunk-scheduling side effect mutates the
 * set mid-iteration — a {@link java.util.ConcurrentModificationException} in the server tick loop. So
 * {@link #onChunkLoad} does only a cheap registry guard synchronously and defers the actual catch-up onto the server
 * task queue, which runs after the current chunk-future pass completes. {@link #decorate} re-validates everything, so a
 * one-tick delay (or the chunk unloading again before it runs) is safe.
 */
public final class ResinDecorator {

    private ResinDecorator() {}

    public static void onChunkLoad(ServerLevel level, ChunkPos chunk) {
        // Cheap synchronous guard: chunks not owned by any location are the overwhelming majority and need no work.
        // This is a pure registry map lookup — it touches no chunk/ticket state, so it's safe to run inside the
        // CHUNK_LOAD callback. Skipping here avoids enqueuing a deferred no-op task for every loaded chunk.
        if (HiveLocationRegistry.INSTANCE.getByChunk(level.dimension(), chunk) == null) {
            return;
        }

        // Defer the catch-up + claim out of the chunk-future iteration (see class javadoc). The server task queue
        // drains after the current runAllUpdates pass returns and chunksToUpdateFutures has been cleared.
        var server = level.getServer();
        if (server != null) {
            server.execute(() -> decorate(level, chunk));
        }
    }

    private static void decorate(ServerLevel level, ChunkPos chunk) {
        var location = HiveLocationRegistry.INSTANCE.getByChunk(level.dimension(), chunk);
        if (location == null) {
            return;
        }

        if (location.decoratedChunks().contains(chunk)) {
            return;
        }

        // Bring the location's claim set up to date before decorating — the chunk we're loading might have been
        // claimed by an unloaded scan tick that hasn't been observed yet.
        var faction = Alien.MOD.factions().get(location.lineageFactionId());
        if (faction != null && faction.data() instanceof LineageFactionData lineage) {
            CatchUpEngine.catchUpTo(level, location, lineage, level.getGameTime());
        }

        // Re-check after catch-up — the location might have released this chunk in the meantime.
        if (!location.claimedChunks().contains(chunk)) {
            return;
        }

        // Phase 7: bookkeeping only. Future phase will place resin blocks + ovomorphs here based on
        // claim age (ticks elapsed since chunkClaimTicks[chunk]) and distance from center.
        location.decoratedChunks().add(chunk);
    }
}

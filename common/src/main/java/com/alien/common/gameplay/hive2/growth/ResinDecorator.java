package com.alien.common.gameplay.hive2.growth;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
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
 */
public final class ResinDecorator {

    private ResinDecorator() {}

    public static void onChunkLoad(ServerLevel level, ChunkPos chunk) {
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

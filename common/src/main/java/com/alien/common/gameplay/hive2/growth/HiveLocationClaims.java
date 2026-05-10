package com.alien.common.gameplay.hive2.growth;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

/**
 * Atomic claim/release helpers for {@link HiveLocation}. Each call:
 * <ul>
 * <li>Updates the location's {@code claimedChunks} set and {@code chunkClaimTicks} map</li>
 * <li>Calls BLib's {@link com.blib.api.common.mod.v1.model.access.BLibTerritoryAccess} to register the claim under the
 * lineage faction id</li>
 * <li>Updates the {@link HiveLocationRegistry} {@code byChunk} index</li>
 * </ul>
 * <p>
 * Use these instead of direct {@code claimedChunks().add(...)} calls so the three sources of truth (location record,
 * BLib territory, in-memory chunk index) stay synchronized.
 */
public final class HiveLocationClaims {

    private HiveLocationClaims() {}

    /**
     * Claims {@code chunk} for {@code location}. Returns true if the claim was newly added; false if the chunk was
     * already in the location's set.
     */
    public static boolean claim(ServerLevel level, HiveLocation location, ChunkPos chunk, long currentTick) {
        if (!location.claimedChunks().add(chunk)) {
            return false;
        }

        location.chunkClaimTicks().put(chunk, currentTick);
        HiveLocationRegistry.INSTANCE.onChunkClaimed(location, chunk);

        Alien.MOD.territory().addClaim(level, chunk, location.lineageFactionId());

        return true;
    }

    /**
     * Releases {@code chunk} from {@code location}. Returns true if the claim was actually removed; false if the chunk
     * wasn't in the location's set.
     */
    public static boolean release(ServerLevel level, HiveLocation location, ChunkPos chunk) {
        if (!location.claimedChunks().remove(chunk)) {
            return false;
        }

        location.chunkClaimTicks().remove(chunk);
        HiveLocationRegistry.INSTANCE.onChunkReleased(location, chunk);

        Alien.MOD.territory().removeClaim(level, chunk, location.lineageFactionId());

        return true;
    }

    /**
     * Total claimed-chunk count across every location of {@code lineage}. Used by claim attempts to enforce the
     * per-lineage chunk cap.
     */
    public static int totalChunksFor(LineageFactionData lineage) {
        var total = 0;
        for (var location : lineage.locationsById().values()) {
            total += location.claimedChunks().size();
        }
        return total;
    }
}

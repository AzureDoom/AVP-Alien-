package com.alien.common.gameplay.hive2.tick;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.growth.AbstractSpreadAttempt;
import com.alien.common.gameplay.hive2.growth.CatchUpEngine;
import com.alien.common.gameplay.hive2.growth.LoadedBiomassTicker;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

/**
 * Fast-path per-server-tick work for a single {@link HiveLocation}. Resolves the owning lineage faction (skipping the
 * tick if the lineage has been removed mid-tick) and dispatches:
 * <ul>
 * <li>Phase 3 — leader pick, boss bar progress / visibility.</li>
 * <li>Phase 7 — every-tick {@link LoadedBiomassTicker} for player-nearby locations + per-tick {@link CatchUpEngine} for
 * claim attempts (the engine is idempotent on elapsed=0, so it's free to call when biomass hasn't moved).</li>
 * <li>Phase 8 (later) — convoy manifestation interactions on this location's chunks.</li>
 * </ul>
 * <p>
 * See {@code HIVE_REDESIGN_12_PERFORMANCE.md} § 1.
 */
public final class HiveLocationLoadedTickTask {

    private HiveLocationLoadedTickTask() {}

    public static void run(MinecraftServer server, HiveLocation location) {
        var faction = Alien.MOD.factions().get(location.lineageFactionId());

        if (faction == null) {
            return;
        }

        if (!(faction.data() instanceof LineageFactionData lineage)) {
            return;
        }

        if (!lineage.isAlive()) {
            return;
        }

        location.tick(server, lineage);

        var serverLevel = server.getLevel(location.dimension());
        if (serverLevel == null) {
            return;
        }

        var currentTick = serverLevel.getGameTime();
        if (!hasLoadedClaimedChunk(serverLevel, location)) {
            return;
        }

        // Loaded biomass income — only for player-nearby locations (proxy: boss bar is showing). Cheap to call,
        // so we check every tick and let LoadedBiomassTicker decide whether this is its second.
        if (isPlayerNearby(location) && LoadedBiomassTicker.shouldFire(currentTick)) {
            LoadedBiomassTicker.run(location, lineage, currentTick);
        }

        // Per-tick claim attempts for loaded locations. CatchUpEngine is idempotent — it does its own
        // biomass-cost gating and skips if the location is angry. Calling every tick would be wasteful in the
        // limit; gate to a coarse cadence. Abstract spread follows this loaded-location cadence; unloaded locations
        // still use LineageGrowthScanTask's slower fallback.
        if (currentTick % 20L == 0L) {
            CatchUpEngine.catchUpTo(serverLevel, location, lineage, currentTick);
            AbstractSpreadAttempt.tryRun(server, location.lineageFactionId(), lineage, location, currentTick);
        }
    }

    public static boolean hasLoadedClaimedChunk(ServerLevel level, HiveLocation location) {
        for (var chunk : location.claimedChunks()) {
            if (level.getChunkSource().hasChunk(chunk.x, chunk.z)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPlayerNearby(HiveLocation location) {
        var bossBar = location.bossBar();
        return bossBar != null && bossBar.isAngry();
    }
}

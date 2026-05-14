package com.alien.common.gameplay.hive2.tick;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.growth.AbstractSpreadAttempt;
import com.alien.common.gameplay.hive2.growth.CatchUpEngine;
import com.alien.common.gameplay.hive2.id.LineageIds;
import net.minecraft.server.MinecraftServer;

/**
 * Slow-path scan that runs every {@link HiveConfig#lineageScanIntervalTicks()} server ticks (default 5 minutes). Walks
 * every loaded lineage and runs {@link CatchUpEngine#catchUpTo} on each of its locations — applying abstract biomass
 * income for the elapsed period and looping batched claim attempts.
 * <p>
 * Phase 7 wires the catch-up engine. Phase 11 layers abstract-spread (empress-gated unloaded founding of new locations)
 * on top per {@code HIVE_REDESIGN_08_LINEAGE_SPREAD.md} § 3.
 */
public final class LineageGrowthScanTask {

    private LineageGrowthScanTask() {}

    public static void run(MinecraftServer server) {
        var currentTick = server.overworld().getGameTime();

        // Abstract spread can mint location factions, so do not iterate BLib's live id view.
        for (var factionId : new java.util.ArrayList<>(Alien.MOD.factions().getAllIds())) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }

            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }

            var level = server.getLevel(lineage.dimension());
            if (level == null) {
                continue;
            }

            for (var location : new java.util.ArrayList<>(lineage.locationsById().values())) {
                if (!location.isAlive()) {
                    continue;
                }
                CatchUpEngine.catchUpTo(level, location, lineage, currentTick);
            }

            // Phase 11: empress-gated abstract spread per scan. The attempt itself is gated by cooldown +
            // empress liveness + max-locations cap, so calling unconditionally is cheap and idempotent.
            AbstractSpreadAttempt.tryRun(server, factionId, lineage, currentTick);
        }
    }
}

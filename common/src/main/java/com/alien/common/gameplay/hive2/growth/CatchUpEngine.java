package com.alien.common.gameplay.hive2.growth;

import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.server.level.ServerLevel;

/**
 * The deferred-simulation core. Catches a single {@link HiveLocation} up to {@code currentTick} by:
 * <ol>
 * <li>Adding biomass for the elapsed period using {@link BiomassIncome#unloadedPerSecond} (the "abstract" formula that
 * doesn't read entity state — see {@code HIVE_REDESIGN_09_GROWTH.md} § 5).</li>
 * <li>Looping claim attempts up to {@link com.alien.common.gameplay.hive2.config.HiveConfig#maxClaimsPerScan} while the
 * location can afford the next chunk's cost.</li>
 * <li>Bumping {@code lastGrowthTick} to {@code currentTick}.</li>
 * </ol>
 * <p>
 * Idempotent — calling twice in the same tick is a no-op (elapsed = 0 → no income, claim loop runs but cost gates
 * everything). The {@link com.alien.common.gameplay.hive2.tick.LineageGrowthScanTask} calls this for every location
 * every {@code lineageScanIntervalTicks}; the {@link com.alien.common.gameplay.hive2.tick.HiveLocationLoadedTickTask}
 * also calls it on locations with a player nearby (where the loaded ticker is bumping {@code lastGrowthTick}
 * frequently, so this just absorbs the small remainder).
 */
public final class CatchUpEngine {

    private CatchUpEngine() {}

    public static void catchUpTo(ServerLevel level, HiveLocation location, LineageFactionData lineage, long currentTick) {
        var config = HiveLocationRegistry.INSTANCE.config();
        var elapsed = currentTick - location.lastGrowthTick();

        if (elapsed > 0) {
            var perSec = BiomassIncome.unloadedPerSecond(location, lineage, config);
            var income = (int) Math.round(perSec * elapsed / 20.0);

            if (income > 0) {
                addBiomassClamped(location, income, config);
            }

            location.setLastGrowthTick(currentTick);
        }

        // Skip claim attempts on angry locations per HIVE_REDESIGN_04_BOSS_BAR § 4.
        // (Boss bar may not exist yet on a newly-minted location — treat that as "not angry".)
        var bossBar = location.bossBar();
        if (bossBar != null && bossBar.isAngry()) {
            return;
        }

        runClaimLoop(level, location, lineage, currentTick, config);
    }

    private static void addBiomassClamped(HiveLocation location, int income, com.alien.common.gameplay.hive2.config.HiveConfig config) {
        var cap = BiomassIncome.biomassCap(location, config);
        var newBiomass = Math.min(cap, location.biomass() + income);
        location.setBiomass(newBiomass);
    }

    private static void runClaimLoop(
        ServerLevel level,
        HiveLocation location,
        LineageFactionData lineage,
        long currentTick,
        com.alien.common.gameplay.hive2.config.HiveConfig config
    ) {
        var claimsThisRun = 0;
        var lineageTotal = HiveLocationClaims.totalChunksFor(lineage);

        while (
            claimsThisRun < config.maxClaimsPerScan()
                && location.claimedChunks().size() < config.maxChunksPerLocation()
                && lineageTotal < config.maxChunksPerLineage()
        ) {
            var cost = BiomassIncome.claimCost(location, config);
            if (location.biomass() < cost) {
                return;
            }

            var nextChunk = ChunkPicker.pickNextChunk(location, config);
            if (nextChunk == null) {
                return;
            }

            location.setBiomass(location.biomass() - cost);
            HiveLocationClaims.claim(level, location, nextChunk, currentTick);
            claimsThisRun++;
            lineageTotal++;
        }
    }
}

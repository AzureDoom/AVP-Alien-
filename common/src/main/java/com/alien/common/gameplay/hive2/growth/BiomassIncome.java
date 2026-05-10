package com.alien.common.gameplay.hive2.growth;

import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.registry.tag.AlienEntityTypeTags;

/**
 * Central biomass math. Pure functions over {@link HiveLocation} + {@link LineageFactionData} + {@link HiveConfig}.
 * <p>
 * See {@code HIVE_REDESIGN_10_BIOMASS.md}:
 * <ul>
 * <li>{@link #unloadedPerSecond} — § 3 abstract income, used by {@link CatchUpEngine}.</li>
 * <li>{@link #loadedPerSecond} — § 2 real-event income (per-tick portion only — discrete events like kills and resin
 * placement are hooked separately by future phases).</li>
 * <li>{@link #claimCost} — § 4 super-linear cost curve.</li>
 * <li>{@link #biomassCap} — § 5 accumulation ceiling.</li>
 * </ul>
 */
public final class BiomassIncome {

    private BiomassIncome() {}

    public static double unloadedPerSecond(HiveLocation location, LineageFactionData lineage, HiveConfig config) {
        var base = config.baseUnloadedBiomassPerChunkPerSec() * location.claimedChunks().size();
        var bonus = lineage.empressId() != null ? config.unloadedEmpressBonusPerSec() : 0.0;
        return base + bonus;
    }

    /**
     * Snapshot of the per-tick loaded income rate. Counts loaded xenomorphs + ovomorphs in the location's claimed
     * chunks (already routed into {@link HiveLocation#loadedMembersByType()} by Phase 3) and adds the empress-present
     * bonus when she's in this location's loaded set.
     * <p>
     * Discrete event income (per-kill, per-resin-block) is added by separate hooks; this method covers only the
     * per-tick portion.
     */
    public static double loadedPerSecond(HiveLocation location, LineageFactionData lineage, HiveConfig config) {
        var loadedXenoCount = 0;
        var ovomorphCount = 0;
        var empressPresent = false;
        var empressId = lineage.empressId();

        for (var entry : location.loadedMembersByType().entrySet()) {
            var size = entry.getValue().size();
            if (entry.getKey().is(AlienEntityTypeTags.XENOMORPHS)) {
                loadedXenoCount += size;
            }
            if (entry.getKey().is(AlienEntityTypeTags.OVOMORPHS)) {
                ovomorphCount += size;
            }
            if (empressId != null && !empressPresent && entry.getValue().contains(empressId)) {
                empressPresent = true;
            }
        }

        var income = config.loadedBiomassPerLoadedXenomorphPerSec() * loadedXenoCount
            + config.loadedBiomassPerOvomorphPerSec() * ovomorphCount
            + config.loadedBiomassIdleBonusPerSec();

        if (empressPresent) {
            income += config.loadedBiomassEmpressPresentBonusPerSec();
        }

        return income;
    }

    /**
     * Cost (in biomass) to claim the next chunk. Grows faster-than-linear with current size:
     *
     * <pre>
     * cost = baseChunkCost * (1 + claimedChunks * growthFactor)
     * </pre>
     * <p>
     * Returns int — fractional cost rounded up so growth always pays its way.
     */
    public static int claimCost(HiveLocation location, HiveConfig config) {
        var baseCost = config.baseChunkCost();
        var growth = config.growthFactor();
        return (int) Math.ceil(baseCost * (1.0 + location.claimedChunks().size() * growth));
    }

    /**
     * Maximum biomass the location can accumulate. Past this, additional income is discarded — the location can't
     * stockpile arbitrarily and then explode forward in growth (per the design's anti-runaway constraint in § 5).
     */
    public static int biomassCap(HiveLocation location, HiveConfig config) {
        return claimCost(location, config) * config.biomassAccumulationCapMultiplier();
    }
}

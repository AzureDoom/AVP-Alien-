package com.alien.common.gameplay.hive.convoy;

import com.alien.common.gameplay.hive.config.HiveConfig;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.entity.v1.EntityReserves;
import net.minecraft.world.entity.EntityType;

/**
 * Per-convoy speed math. The convoy moves at the speed of its slowest unit, scaled by a per-convoy-type multiplier
 * (reinforcement 1.0×; Phase 8b adds migration 0.7× and raid 1.5×).
 * <p>
 * Per-type contributions (blocks per second), per {@code HIVE_REDESIGN_06_CONVOYS.md} § 2:
 * <ul>
 * <li>Drones / runners — fast (1.5× default)</li>
 * <li>Warriors / prowlers — medium (1.0× default)</li>
 * <li>Praetorians / crushers — slow (0.6× default)</li>
 * <li>Queens / empresses — slow (0.5× default)</li>
 * </ul>
 * <p>
 * Phase 8 uses tag-based dispatch to avoid hardcoding per-EntityType maps. Speed contributions are multipliers on top
 * of {@link HiveConfig#convoySpeedBlocksPerSecond()}; the slowest contribution wins.
 */
public final class ConvoySpeedTable {

    private ConvoySpeedTable() {}

    public static double blocksPerTick(Convoy convoy, HiveConfig config) {
        var slowestMultiplier = slowestContribution(convoy.composition());
        var typeMultiplier = typeMultiplier(convoy);
        var blocksPerSecond = config.convoySpeedBlocksPerSecond() * slowestMultiplier * typeMultiplier;
        return blocksPerSecond / 20.0;
    }

    private static double slowestContribution(EntityReserves composition) {
        var slowest = Double.MAX_VALUE;
        var seenAny = false;

        for (var entityType : composition.getAvailableEntityTypes()) {
            var contribution = contributionFor(entityType);
            if (contribution < slowest) {
                slowest = contribution;
                seenAny = true;
            }
        }

        return seenAny ? slowest : 1.0;
    }

    private static double contributionFor(EntityType<?> type) {
        if (type.is(AlienEntityTypeTags.EMPRESSES) || type.is(AlienEntityTypeTags.QUEENS)) {
            return 0.5;
        }
        if (type.is(AlienEntityTypeTags.PRAETORIANS) || type.is(AlienEntityTypeTags.CRUSHERS)) {
            return 0.6;
        }
        if (type.is(AlienEntityTypeTags.WARRIORS) || type.is(AlienEntityTypeTags.PROWLERS)) {
            return 1.0;
        }
        if (type.is(AlienEntityTypeTags.DRONES) || type.is(AlienEntityTypeTags.RUNNERS)) {
            return 1.5;
        }
        return 1.0;
    }

    private static double typeMultiplier(Convoy convoy) {
        if (convoy instanceof Convoy.Reinforcement) {
            return 1.0;
        }
        if (convoy instanceof Convoy.Migration) {
            return 0.7; // hauling everything — slower
        }
        if (convoy instanceof Convoy.Raid) {
            return 1.5; // chasing — faster
        }
        return 1.0;
    }
}

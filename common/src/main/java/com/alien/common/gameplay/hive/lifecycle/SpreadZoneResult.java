package com.alien.common.gameplay.hive.lifecycle;

import net.minecraft.resources.ResourceLocation;

/**
 * What {@link SpreadZoneCheck} thinks should happen at a given (queen, position) pair. Pure data — the result is inert
 * until {@link HiveLocationFoundingService} acts on it.
 * <p>
 * See {@code HIVE_REDESIGN_08_LINEAGE_SPREAD.md} § 1 for the underlying rules.
 */
public sealed interface SpreadZoneResult {

    /**
     * The position is fresh ground for the queen — she has no lineage (or her old one is too far) and no other
     * lineage's spread zone covers this spot. She should found a brand-new lineage here.
     */
    record NewLineage() implements SpreadZoneResult {}

    /**
     * The position is inside one of the queen's existing lineage's spread zones. She should add a new location to that
     * lineage.
     */
    record NewLocation(ResourceLocation lineageFactionId) implements SpreadZoneResult {}

    /**
     * Settlement is forbidden here — typically because another lineage's territory or spread zone covers the spot, or
     * because she's still in a lineage but outside any of its spread zones. Carries a short human-readable reason for
     * debugging.
     */
    record Blocked(String reason) implements SpreadZoneResult {}
}

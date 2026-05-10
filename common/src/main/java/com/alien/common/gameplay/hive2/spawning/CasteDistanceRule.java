package com.alien.common.gameplay.hive2.spawning;

import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

/**
 * Caste-distance lookup. Replaces the legacy percentile-sphere layers from {@code HiveSpaceManager} with a flat
 * "min/max chunk distance from the location's center" range per caste, per the table in
 * {@code HIVE_REDESIGN_03_LOCATIONS.md} § 4 and {@code HIVE_REDESIGN_13_CONFIGURATION.md} § 2.
 * <p>
 * Default ranges (all values inclusive, in chunks from a location's center):
 * <ul>
 * <li>Empresses + Queens: 0–0 (center chunk only)</li>
 * <li>Praetorians + Crushers: 0–2</li>
 * <li>Drones + Runners + Ovomorphs: 0–6</li>
 * <li>Warriors + Prowlers: 2–10 (note the inner floor — warriors patrol the edge)</li>
 * </ul>
 * <p>
 * Entity types not matched by any of these tags get an "unknown" verdict and are rejected by
 * {@link HiveLocationSpawnGate}. Add the entity to the relevant tag to opt it into a band.
 */
public final class CasteDistanceRule {

    private CasteDistanceRule() {}

    public record Range(
        int minChunks,
        int maxChunks
    ) {

        public boolean contains(int chunkDistance) {
            return chunkDistance >= minChunks && chunkDistance <= maxChunks;
        }
    }

    /**
     * Resolves the allowed chunk-distance range for the given entity type. Returns {@code null} for entity types that
     * have no caste mapping — those should never spawn under the new gate.
     */
    public static @Nullable Range rangeFor(EntityType<?> entityType, HiveConfig config) {
        // Order matters: empress is also tagged QUEENS in some tag setups; check EMPRESSES first to give it the
        // tightest band. (Even if EMPRESSES isn't in QUEENS, putting it first costs nothing.)
        if (entityType.is(AlienEntityTypeTags.EMPRESSES)) {
            return new Range(config.queenRangeChunksMin(), config.queenRangeChunksMax());
        }
        if (entityType.is(AlienEntityTypeTags.QUEENS)) {
            return new Range(config.queenRangeChunksMin(), config.queenRangeChunksMax());
        }
        if (entityType.is(AlienEntityTypeTags.PRAETORIANS) || entityType.is(AlienEntityTypeTags.CRUSHERS)) {
            return new Range(config.praetorianRangeChunksMin(), config.praetorianRangeChunksMax());
        }
        if (
            entityType.is(AlienEntityTypeTags.DRONES)
                || entityType.is(AlienEntityTypeTags.RUNNERS)
                || entityType.is(AlienEntityTypeTags.OVOMORPHS)
        ) {
            return new Range(config.droneRangeChunksMin(), config.droneRangeChunksMax());
        }
        if (entityType.is(AlienEntityTypeTags.WARRIORS) || entityType.is(AlienEntityTypeTags.PROWLERS)) {
            return new Range(config.warriorRangeChunksMin(), config.warriorRangeChunksMax());
        }
        return null;
    }
}

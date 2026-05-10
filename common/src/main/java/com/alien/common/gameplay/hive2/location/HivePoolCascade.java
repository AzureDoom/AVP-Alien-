package com.alien.common.gameplay.hive2.location;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.faction.VariantFactionData;
import net.minecraft.world.entity.EntityType;

/**
 * Static helpers for the upward overflow cascade defined in {@code HIVE_REDESIGN_05_RESERVES.md} § 6:
 *
 * <pre>
 * local reserves --(overflow)--> lineage pool --(overflow)--> variant pool --(discard)
 * </pre>
 * <p>
 * Each tier has its own cap; whatever doesn't fit in tier N flows up to tier N+1. The variant pool is the top of the
 * cascade — anything that doesn't fit there is dropped (per the resolved {@code variantPoolCapPerType} default of
 * {@link Integer#MAX_VALUE}, this is a degenerate case).
 */
public final class HivePoolCascade {

    private HivePoolCascade() {}

    /**
     * Tries to add {@code count} of {@code type} starting at the location's local reserves. Cascades upward through the
     * lineage and variant pools as needed. Returns the count that was lost off the top of the cascade (zero in the
     * default-tuned case).
     */
    public static int addToLocationCascading(HiveLocation location, LineageFactionData lineage, EntityType<?> type, int count) {
        if (count <= 0) {
            return 0;
        }

        var afterLocal = location.localReserves().tryAdd(type, count);
        if (afterLocal == 0) {
            return 0;
        }

        return addToLineageCascading(lineage, type, afterLocal);
    }

    /**
     * Tries to add {@code count} of {@code type} starting at the lineage pool. Used by the shed path (which skips the
     * location tier entirely — the alien isn't despawning into a specific location).
     */
    public static int addToLineageCascading(LineageFactionData lineage, EntityType<?> type, int count) {
        if (count <= 0) {
            return 0;
        }

        var afterLineage = lineage.tryAddToLineagePool(type, count);
        if (afterLineage == 0) {
            return 0;
        }

        var variantId = lineage.parentVariantFactionId();
        if (variantId == null) {
            Alien.LOGGER.warn(
                "Lineage has no parentVariantFactionId; dropping {} overflow of {} from cascade.",
                afterLineage,
                type
            );
            return afterLineage;
        }

        var variantFaction = Alien.MOD.factions().get(variantId);
        if (variantFaction == null || !(variantFaction.data() instanceof VariantFactionData variantData)) {
            Alien.LOGGER.warn(
                "Variant faction {} missing or wrong type; dropping {} overflow of {} from cascade.",
                variantId,
                afterLineage,
                type
            );
            return afterLineage;
        }

        return variantData.tryAddToVariantPool(lineage.dimension(), type, afterLineage);
    }
}

package com.alien.common.gameplay.hive2.faction;

import com.alien.common.model.alien.variant.AlienVariant;

/**
 * Faction display names. Numbers are monotonic per parent (variant counts lineages; lineage counts locations) — dead
 * lineages/locations don't return their numbers to the sequence, so the names stay stable for the lifetime of the
 * faction.
 */
public final class FactionNaming {

    private FactionNaming() {}

    /** Lowercase pluralized variant component. {@code IRRADIATED} stays singular — no graceful plural exists. */
    public static String variantPath(AlienVariant variant) {
        return switch (variant) {
            case NORMAL -> "normals";
            case NETHER -> "nethers";
            case ABERRANT -> "aberrants";
            case IRRADIATED -> "irradiated";
        };
    }

    public static String forVariant(AlienVariant variant) {
        return "xenos/" + variantPath(variant);
    }

    public static String forLineage(AlienVariant variant, long lineageNumber) {
        return forVariant(variant) + "/lin" + lineageNumber;
    }

    public static String forLocation(AlienVariant variant, long lineageNumber, long locationNumber) {
        return "Hive " + variantCode(variant) + "_" + lineageNumber + "_" + locationNumber;
    }

    public static String variantCode(AlienVariant variant) {
        return switch (variant) {
            case NORMAL -> "NO";
            case NETHER -> "NE";
            case ABERRANT -> "AB";
            case IRRADIATED -> "IR";
        };
    }
}

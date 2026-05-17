package com.alien.common.gameplay.hive2.economy;

import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Snapshot of a location's per-caste population: known location members + local reserves, summed by entity-type tag.
 * Used by {@link HiveBalanceTask} to compute deficits and by recipe-condition checks. The known-member index is
 * persisted on each location so persistent queens, empresses, and harbingers still count after their chunks unload.
 */
public final class CastePopulation {

    /** All castes the buy task considers when computing deficits. Iterate in this order for stable tiebreaks. */
    public static final TagKey<EntityType<?>>[] TRACKED_CASTES = new TagKey[] {
        AlienEntityTypeTags.QUEENS,
        AlienEntityTypeTags.DRONES,
        AlienEntityTypeTags.RUNNERS,
        AlienEntityTypeTags.WARRIORS,
        AlienEntityTypeTags.PROWLERS,
        AlienEntityTypeTags.PRAETORIANS,
        AlienEntityTypeTags.CRUSHERS,
        AlienEntityTypeTags.RAVAGERS,
        AlienEntityTypeTags.RAZOR_CLAWS,
        AlienEntityTypeTags.BURSTERS,
        AlienEntityTypeTags.CARRIERS,
        AlienEntityTypeTags.CHRYSALISES,
        AlienEntityTypeTags.SPITTERS,
        AlienEntityTypeTags.HARBINGERS
    };

    private CastePopulation() {}

    /** Per-caste population (known location members + reserves). Insertion-ordered for stable iteration. */
    public static Map<TagKey<EntityType<?>>, Integer> popByCaste(HiveLocation location) {
        var counts = new LinkedHashMap<TagKey<EntityType<?>>, Integer>();
        for (var caste : TRACKED_CASTES) {
            counts.put(caste, countCaste(location, caste));
        }
        return counts;
    }

    /** Sum of all tracked-caste counts in the location. Used as the gate against the population cap. */
    public static int totalTrackedPopulation(HiveLocation location) {
        var total = 0;
        for (var caste : TRACKED_CASTES) {
            total += countCaste(location, caste);
        }
        return total;
    }

    /** Count of one caste (known location members + reserves) in this location. */
    public static int countCaste(HiveLocation location, TagKey<EntityType<?>> caste) {
        return countKnownCaste(location, caste) + location.localReserves().getCountMatching(type -> type.is(caste));
    }

    /** Count of one caste from persisted known members only; reserve entries are intentionally excluded. */
    public static int countKnownCaste(HiveLocation location, TagKey<EntityType<?>> caste) {
        var count = 0;
        for (var entry : location.knownMembersByType().entrySet()) {
            if (entry.getKey().is(caste)) {
                count += entry.getValue().size();
            }
        }
        return count;
    }

    /** Count of one concrete entity type (known location members + reserves) in this location. */
    public static int countEntity(HiveLocation location, EntityType<?> entityType) {
        var known = location.knownMembersByType()
            .getOrDefault(entityType, java.util.Set.of())
            .size();
        return known + location.localReserves().getCount(entityType);
    }
}

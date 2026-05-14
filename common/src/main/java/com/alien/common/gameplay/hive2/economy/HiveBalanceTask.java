package com.alien.common.gameplay.hive2.economy;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.registry.HiveRecipeRegistry;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Per-tick "balance the hive" buy task. For each living location:
 * <ol>
 * <li>Counts tracked castes (loaded + reserves) and totalsthe population.</li>
 * <li>Skips if total population is at or beyond the per-chunk cap (overencumbered — convoys may have pushed past).</li>
 * <li>Computes desired counts from policy ratios (drone↔warrior 1:1, prowler↔runner 1:4, praetorian = warriors/12,
 * crusher = runners/12, ravager = warriors/8, harbinger = 1 when pop ≥ 100, runner baseline = 1 + chunks/4). The
 * praetorian/crusher gates use the recipe-input caste so the buy stabilizes — gating praetorians on drones (recipe
 * consumes warriors) creates a slow refill cycle that doesn't violate the cap but keeps the buy task firing.</li>
 * <li>Picks the caste with the largest deficit. Tiebreak: order in {@link CastePopulation#TRACKED_CASTES}.</li>
 * <li>Resolves the recipe for that caste, checks conditions + resources + inputs, commits on success.</li>
 * </ol>
 * Runs every server tick from {@link HiveLocationRegistry#tick}. Per-location body is cheap (a few sums and a single
 * recipe lookup); no throttling per the project's correctness-over-cadence preference.
 */
public final class HiveBalanceTask {

    private HiveBalanceTask() {}

    public static void scanAll(MinecraftServer server) {
        var config = HiveLocationRegistry.INSTANCE.config();
        var populationPerChunk = config.populationPerChunk();

        for (var factionId : new ArrayList<>(Alien.MOD.factions().getAllIds())) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }
            for (var location : new ArrayList<>(lineage.locationsById().values())) {
                if (!location.isAlive()) {
                    continue;
                }
                evaluate(location, lineage, populationPerChunk);
            }
        }
    }

    private static void evaluate(HiveLocation location, LineageFactionData lineage, int populationPerChunk) {
        var pop = CastePopulation.popByCaste(location);
        var totalPop = pop.values().stream().mapToInt(Integer::intValue).sum();
        var chunks = location.claimedChunks().size();
        var cap = populationPerChunk * chunks;
        if (totalPop >= cap) {
            return;
        }
        if (CastePopulation.countCaste(location, AlienEntityTypeTags.QUEENS) <= 0) {
            return;
        }

        var deficits = computeDeficits(pop, chunks, totalPop);
        if (deficits.isEmpty()) {
            return;
        }

        TagKey<EntityType<?>> chosen = null;
        var chosenDeficit = 0;
        for (var caste : CastePopulation.TRACKED_CASTES) {
            var d = deficits.getOrDefault(caste, 0);
            if (d > chosenDeficit) {
                chosenDeficit = d;
                chosen = caste;
            }
        }
        if (chosen == null) {
            return;
        }

        var recipe = HiveRecipeRegistry.forOutputCaste(chosen);
        if (recipe == null) {
            return;
        }

        if (!conditionsHold(recipe, location, pop, totalPop)) {
            return;
        }

        if (
            location.biomass() < recipe.biomass()
                || location.royalJelly() < recipe.royalJelly()
                || location.scourgeJelly() < recipe.scourgeJelly()
        ) {
            return;
        }

        var variant = lineage.variant();

        // Resolve every input caste to a concrete entity type and confirm reserves cover it.
        var inputTypes = new ArrayList<EntityType<?>>(recipe.inputCastes().size());
        for (var input : recipe.inputCastes()) {
            var type = CasteResolver.entityTypeForCaste(variant, input.caste());
            if (type == null || location.localReserves().getCount(type) < input.count()) {
                return;
            }
            inputTypes.add(type);
        }

        var outputType = CasteResolver.entityTypeForCaste(variant, recipe.outputCaste());
        if (outputType == null) {
            return;
        }

        // All gates pass — commit.
        location.setBiomass(location.biomass() - recipe.biomass());
        location.setRoyalJelly(location.royalJelly() - recipe.royalJelly());
        location.setScourgeJelly(location.scourgeJelly() - recipe.scourgeJelly());

        for (var i = 0; i < recipe.inputCastes().size(); i++) {
            var count = recipe.inputCastes().get(i).count();
            location.localReserves().underlying().add(inputTypes.get(i), -count);
        }
        location.localReserves().tryAdd(outputType, 1);

        Alien.LOGGER.debug(
            "Hive2: balance buy at {} → +1 {} (cost: {} biomass, {} royal, {} scourge)",
            location.id(),
            outputType.builtInRegistryHolder().key().location(),
            recipe.biomass(),
            recipe.royalJelly(),
            recipe.scourgeJelly()
        );
    }

    private static Map<TagKey<EntityType<?>>, Integer> computeDeficits(
        Map<TagKey<EntityType<?>>, Integer> pop,
        int chunks,
        int totalPop
    ) {
        var drone = pop.getOrDefault(AlienEntityTypeTags.DRONES, 0);
        var runner = pop.getOrDefault(AlienEntityTypeTags.RUNNERS, 0);
        var warrior = pop.getOrDefault(AlienEntityTypeTags.WARRIORS, 0);
        var prowler = pop.getOrDefault(AlienEntityTypeTags.PROWLERS, 0);
        var praetorian = pop.getOrDefault(AlienEntityTypeTags.PRAETORIANS, 0);
        var crusher = pop.getOrDefault(AlienEntityTypeTags.CRUSHERS, 0);
        var ravager = pop.getOrDefault(AlienEntityTypeTags.RAVAGERS, 0);
        var harbinger = pop.getOrDefault(AlienEntityTypeTags.HARBINGERS, 0);

        var desired = new LinkedHashMap<TagKey<EntityType<?>>, Integer>();
        desired.put(AlienEntityTypeTags.DRONES, Math.max(drone, warrior));
        desired.put(AlienEntityTypeTags.WARRIORS, Math.max(drone, warrior));
        desired.put(AlienEntityTypeTags.RUNNERS, Math.max(runner, 1 + chunks / 4));
        desired.put(AlienEntityTypeTags.PROWLERS, Math.max(prowler, runner / 4));
        desired.put(AlienEntityTypeTags.PRAETORIANS, warrior / 12);
        desired.put(AlienEntityTypeTags.CRUSHERS, runner / 12);
        desired.put(AlienEntityTypeTags.RAVAGERS, warrior / 8);
        desired.put(AlienEntityTypeTags.HARBINGERS, totalPop >= 100 ? Math.max(1, harbinger) : 0);

        var deficits = new LinkedHashMap<TagKey<EntityType<?>>, Integer>();
        for (var entry : desired.entrySet()) {
            var current = pop.getOrDefault(entry.getKey(), 0);
            var d = entry.getValue() - current;
            if (d > 0) {
                deficits.put(entry.getKey(), d);
            }
        }
        return deficits;
    }

    private static boolean conditionsHold(
        HiveRecipe recipe,
        HiveLocation location,
        Map<TagKey<EntityType<?>>, Integer> pop,
        int totalPop
    ) {
        for (var condition : recipe.conditions()) {
            switch (condition) {
                case HiveRecipeCondition.MinPopulation min -> {
                    if (totalPop < min.value()) {
                        return false;
                    }
                }
                case HiveRecipeCondition.MaxCasteCountInLocation max -> {
                    var current = pop.getOrDefault(max.caste(), 0);
                    if (max.caste() != null && !pop.containsKey(max.caste())) {
                        // Caste not in TRACKED_CASTES — fall back to a direct count.
                        current = CastePopulation.countCaste(location, max.caste());
                    }
                    if (current >= max.value()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

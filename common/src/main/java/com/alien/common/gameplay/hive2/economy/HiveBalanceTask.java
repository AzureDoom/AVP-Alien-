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
 * Per-tick hive buy task. For each living location:
 * <ol>
 * <li>Counts tracked castes (loaded + reserves) and totals the population.</li>
 * <li>If population is below the per-chunk cap, buys a net-new basic unit when resources allow.</li>
 * <li>Once population is full, spends paid recipes on composition upgrades to move toward the policy ratios.</li>
 * </ol>
 * Runs every server tick from {@link HiveLocationRegistry#tick}. Per-location body is cheap (a few sums and bounded
 * recipe lookups); no throttling per the project's correctness-over-cadence preference.
 */
public final class HiveBalanceTask {

    private static final TagKey<EntityType<?>>[] POPULATION_FILL_CASTES = new TagKey[] {
        AlienEntityTypeTags.RUNNERS,
        AlienEntityTypeTags.DRONES
    };

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
        if (cap <= 0 || totalPop > cap) {
            return;
        }
        if (CastePopulation.countCaste(location, AlienEntityTypeTags.QUEENS) <= 0) {
            return;
        }

        if (totalPop < cap) {
            tryFillPopulation(location, lineage, pop, chunks, totalPop);
            return;
        }

        tryBalanceComposition(location, lineage, pop, chunks, totalPop);
    }

    private static boolean tryFillPopulation(
        HiveLocation location,
        LineageFactionData lineage,
        Map<TagKey<EntityType<?>>, Integer> pop,
        int chunks,
        int totalPop
    ) {
        var ordered = populationFillOrder(pop, chunks);
        for (var caste : ordered) {
            if (tryCommitCaste(location, lineage, caste, totalPop, RecipePopulationMode.NET_GAIN)) {
                return true;
            }
        }
        return false;
    }

    private static ArrayList<TagKey<EntityType<?>>> populationFillOrder(
        Map<TagKey<EntityType<?>>, Integer> pop,
        int chunks
    ) {
        var ordered = new ArrayList<TagKey<EntityType<?>>>();
        var drones = pop.getOrDefault(AlienEntityTypeTags.DRONES, 0);
        var runners = pop.getOrDefault(AlienEntityTypeTags.RUNNERS, 0);
        var runnerBaseline = 1 + chunks / 4;

        if (runners < runnerBaseline) {
            ordered.add(AlienEntityTypeTags.RUNNERS);
        }

        if (drones <= runners) {
            addIfMissing(ordered, AlienEntityTypeTags.DRONES);
            addIfMissing(ordered, AlienEntityTypeTags.RUNNERS);
        } else {
            addIfMissing(ordered, AlienEntityTypeTags.RUNNERS);
            addIfMissing(ordered, AlienEntityTypeTags.DRONES);
        }

        for (var caste : POPULATION_FILL_CASTES) {
            addIfMissing(ordered, caste);
        }
        return ordered;
    }

    private static void addIfMissing(ArrayList<TagKey<EntityType<?>>> list, TagKey<EntityType<?>> caste) {
        if (!list.contains(caste)) {
            list.add(caste);
        }
    }

    private static boolean tryBalanceComposition(
        HiveLocation location,
        LineageFactionData lineage,
        Map<TagKey<EntityType<?>>, Integer> pop,
        int chunks,
        int totalPop
    ) {
        var deficits = computeDeficits(pop, chunks, totalPop);
        if (deficits.isEmpty()) {
            return false;
        }

        var candidates = new ArrayList<TagKey<EntityType<?>>>();
        for (var caste : CastePopulation.TRACKED_CASTES) {
            if (deficits.getOrDefault(caste, 0) > 0) {
                candidates.add(caste);
            }
        }
        candidates.sort((left, right) -> Integer.compare(deficits.get(right), deficits.get(left)));

        for (var caste : candidates) {
            if (tryCommitCaste(location, lineage, caste, totalPop, RecipePopulationMode.NEUTRAL)) {
                return true;
            }
        }
        return false;
    }

    private static boolean tryCommitCaste(
        HiveLocation location,
        LineageFactionData lineage,
        TagKey<EntityType<?>> caste,
        int totalPop,
        RecipePopulationMode populationMode
    ) {
        var outputType = CasteResolver.entityTypeForCaste(lineage.variant(), caste);
        if (outputType == null) {
            return false;
        }

        var recipe = HiveRecipeRegistry.forOutputEntity(outputType);
        if (recipe == null) {
            return false;
        }

        var netPopulationChange = netPopulationChange(recipe);
        if (!populationMode.matches(netPopulationChange)) {
            return false;
        }

        if (!conditionsHold(recipe, location, totalPop)) {
            return false;
        }

        if (
            location.biomass() < recipe.biomass()
                || location.royalJelly() < recipe.royalJelly()
                || location.scourgeJelly() < recipe.scourgeJelly()
        ) {
            return false;
        }

        // Confirm the concrete input entity reserves cover the recipe.
        var inputTypes = new ArrayList<EntityType<?>>(recipe.inputEntities().size());
        for (var input : recipe.inputEntities()) {
            var type = input.entity();
            if (location.localReserves().getCount(type) < input.count()) {
                return false;
            }
            inputTypes.add(type);
        }

        if (!location.localReserves().accepts(recipe.outputEntity())) {
            return false;
        }

        // All gates pass — commit.
        location.setBiomass(location.biomass() - recipe.biomass());
        location.setRoyalJelly(location.royalJelly() - recipe.royalJelly());
        location.setScourgeJelly(location.scourgeJelly() - recipe.scourgeJelly());

        for (var i = 0; i < recipe.inputEntities().size(); i++) {
            var count = recipe.inputEntities().get(i).count();
            location.localReserves().underlying().add(inputTypes.get(i), -count);
        }
        location.localReserves().tryAdd(recipe.outputEntity(), 1);
        return true;
    }

    private static int netPopulationChange(HiveRecipe recipe) {
        var inputs = 0;
        for (var input : recipe.inputEntities()) {
            inputs += input.count();
        }
        return 1 - inputs;
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
        desired.put(AlienEntityTypeTags.HARBINGERS, totalPop >= 100 && harbinger == 0 ? 1 : 0);

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
        int totalPop
    ) {
        for (var condition : recipe.conditions()) {
            switch (condition) {
                case HiveRecipeCondition.MinPopulation min -> {
                    if (totalPop < min.value()) {
                        return false;
                    }
                }
                case HiveRecipeCondition.MinEntityCountInLocation min -> {
                    var current = CastePopulation.countEntity(location, min.entity());
                    if (current < min.value()) {
                        return false;
                    }
                }
                case HiveRecipeCondition.MaxEntityCountInLocation max -> {
                    var current = CastePopulation.countEntity(location, max.entity());
                    if (current >= max.value()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private enum RecipePopulationMode {
        NET_GAIN {
            @Override
            boolean matches(int netPopulationChange) {
                return netPopulationChange > 0;
            }
        },
        NEUTRAL {
            @Override
            boolean matches(int netPopulationChange) {
                return netPopulationChange == 0;
            }
        };

        abstract boolean matches(int netPopulationChange);
    }
}

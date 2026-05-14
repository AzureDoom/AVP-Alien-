package com.alien.fabric.data.hive_recipe;

import com.alien.AlienResources;
import com.alien.common.data.HiveRecipeReloadListener;
import com.alien.common.gameplay.hive2.economy.HiveRecipe;
import com.alien.common.gameplay.hive2.economy.HiveRecipeCondition;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HiveRecipeDataProvider implements DataProvider {

    private final FabricDataOutput output;

    private final Map<String, HiveRecipe> recipesByName = new HashMap<>();

    public HiveRecipeDataProvider(FabricDataOutput output) {
        this.output = output;
    }

    private void generate() {
        // Tier 0: primary biomass purchases.
        add("drone", new HiveRecipe(AlienEntityTypeTags.DRONES, 50, 0, 0, List.of(), List.of()));
        add("runner", new HiveRecipe(AlienEntityTypeTags.RUNNERS, 60, 0, 0, List.of(), List.of()));

        // Tier 1: jelly-fueled upgrades from drone/runner-line.
        add(
            "warrior",
            new HiveRecipe(
                AlienEntityTypeTags.WARRIORS,
                0,
                1,
                0,
                List.of(new HiveRecipe.InputCaste(AlienEntityTypeTags.DRONES, 1)),
                List.of()
            )
        );
        add(
            "praetorian",
            new HiveRecipe(
                AlienEntityTypeTags.PRAETORIANS,
                0,
                1,
                0,
                List.of(new HiveRecipe.InputCaste(AlienEntityTypeTags.WARRIORS, 1)),
                List.of()
            )
        );
        add(
            "prowler",
            new HiveRecipe(
                AlienEntityTypeTags.PROWLERS,
                0,
                1,
                0,
                List.of(new HiveRecipe.InputCaste(AlienEntityTypeTags.RUNNERS, 1)),
                List.of()
            )
        );
        add(
            "crusher",
            new HiveRecipe(
                AlienEntityTypeTags.CRUSHERS,
                0,
                1,
                0,
                List.of(new HiveRecipe.InputCaste(AlienEntityTypeTags.PROWLERS, 1)),
                List.of()
            )
        );

        // Tier 2: scourge-fueled high-cost specialists.
        add(
            "ravager",
            new HiveRecipe(
                AlienEntityTypeTags.RAVAGERS,
                0,
                0,
                1,
                List.of(new HiveRecipe.InputCaste(AlienEntityTypeTags.WARRIORS, 1)),
                List.of()
            )
        );
        add(
            "razor_claw",
            new HiveRecipe(
                AlienEntityTypeTags.RAZOR_CLAWS,
                0,
                0,
                1,
                List.of(new HiveRecipe.InputCaste(AlienEntityTypeTags.RUNNERS, 1)),
                List.of()
            )
        );
        add(
            "spitter",
            new HiveRecipe(
                AlienEntityTypeTags.SPITTERS,
                0,
                1,
                0,
                List.of(new HiveRecipe.InputCaste(AlienEntityTypeTags.DRONES, 1)),
                List.of()
            )
        );

        // Tier 3: capstone harbinger — population-gated, max one per location.
        add(
            "harbinger",
            new HiveRecipe(
                AlienEntityTypeTags.HARBINGERS,
                200,
                4,
                4,
                List.of(new HiveRecipe.InputCaste(AlienEntityTypeTags.WARRIORS, 1)),
                List.of(
                    new HiveRecipeCondition.MinPopulation(100),
                    new HiveRecipeCondition.MaxCasteCountInLocation(AlienEntityTypeTags.HARBINGERS, 1)
                )
            )
        );
    }

    private void add(String name, HiveRecipe recipe) {
        recipesByName.put(name, recipe);
    }

    @Override
    public final @NotNull CompletableFuture<?> run(CachedOutput cached) {
        generate();

        var pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, HiveRecipeReloadListener.DIRECTORY_NAME);
        var futures = new ArrayList<CompletableFuture<?>>();

        for (var entry : recipesByName.entrySet()) {
            var id = AlienResources.location(entry.getKey());
            var filePath = pathProvider.json(id);
            var jsonElement = HiveRecipe.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
            futures.add(DataProvider.saveStable(cached, jsonElement, filePath));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public final @NotNull String getName() {
        return "Hive Recipes";
    }
}

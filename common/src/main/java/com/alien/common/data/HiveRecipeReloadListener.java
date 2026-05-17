package com.alien.common.data;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.economy.HiveRecipe;
import com.alien.common.registry.HiveRecipeRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HiveRecipeReloadListener extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY_NAME = "hive_recipes";

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();

    public HiveRecipeReloadListener() {
        super(GSON, DIRECTORY_NAME);
    }

    @Override
    protected void apply(
        @NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap,
        @NotNull ResourceManager resourceManager,
        @NotNull ProfilerFiller profilerFiller
    ) {
        HiveRecipeRegistry.clear();

        for (var entry : resourceLocationJsonElementMap.entrySet()) {
            var id = entry.getKey();
            var jsonElement = entry.getValue();

            HiveRecipe.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                .resultOrPartial(err -> Alien.LOGGER.error("Failed to parse HiveRecipe {}: {}", id, err))
                .ifPresent(HiveRecipeRegistry::register);
        }
    }
}

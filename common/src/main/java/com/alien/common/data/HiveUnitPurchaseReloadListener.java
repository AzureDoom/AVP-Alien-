package com.alien.common.data;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.economy.HiveUnitPurchase;
import com.alien.common.registry.HiveUnitPurchaseRegistry;
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

public class HiveUnitPurchaseReloadListener extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY_NAME = "hive_unit_purchases";

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();

    public HiveUnitPurchaseReloadListener() {
        super(GSON, DIRECTORY_NAME);
    }

    @Override
    protected void apply(
        @NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap,
        @NotNull ResourceManager resourceManager,
        @NotNull ProfilerFiller profilerFiller
    ) {
        HiveUnitPurchaseRegistry.clear();

        for (var entry : resourceLocationJsonElementMap.entrySet()) {
            var id = entry.getKey();
            var jsonElement = entry.getValue();

            HiveUnitPurchase.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                .resultOrPartial(err -> Alien.LOGGER.error("Failed to parse HiveUnitPurchase {}: {}", id, err))
                .ifPresent(HiveUnitPurchaseRegistry::register);
        }
    }
}

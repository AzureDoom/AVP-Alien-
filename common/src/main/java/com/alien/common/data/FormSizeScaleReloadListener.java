package com.alien.common.data;

import com.alien.Alien;
import com.alien.common.model.lifecycle.growth.FormSizeScale;
import com.alien.common.registry.FormSizeScaleRegistry;
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

public class FormSizeScaleReloadListener extends SimpleJsonResourceReloadListener {

    public static final String DIRECTORY_NAME = "form_size_scale";

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();

    public FormSizeScaleReloadListener() {
        super(GSON, DIRECTORY_NAME);
    }

    @Override
    protected void apply(
        @NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap,
        @NotNull ResourceManager resourceManager,
        @NotNull ProfilerFiller profilerFiller
    ) {
        FormSizeScaleRegistry.clear();

        for (var entry : resourceLocationJsonElementMap.entrySet()) {
            var id = entry.getKey();
            var jsonElement = entry.getValue();

            FormSizeScale.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                .resultOrPartial(err -> Alien.LOGGER.error("Failed to parse FormSizeScale {}: {}", id, err))
                .ifPresent(FormSizeScaleRegistry::register);
        }
    }
}

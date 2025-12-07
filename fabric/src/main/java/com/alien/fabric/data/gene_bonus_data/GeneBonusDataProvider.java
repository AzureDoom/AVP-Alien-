package com.alien.fabric.data.gene_bonus_data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class GeneBonusDataProvider implements DataProvider {

    private final FabricDataOutput output;

    // FIXME:
    // private final Map<String, GeneBonusData> geneBonusDataByName;

    protected GeneBonusDataProvider(FabricDataOutput output) {
        this.output = output;
        // FIXME:
        // this.geneBonusDataByName = new HashMap<>();
    }

    protected abstract void generate();

    // TODO: Name shouldn't be provided by data generator.
    // FIXME:
    // public void add(String name, GeneBonusData geneBonusData) {
    // geneBonusDataByName.put(name, geneBonusData);
    // }

    @Override
    public final @NotNull CompletableFuture<?> run(CachedOutput cached) {
        // FIXME:
        // generate();
        //
        // var pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK,
        // GeneBonusDataReloadListener.DIRECTORY_NAME);
        //
        // var futures = geneBonusDataByName.entrySet()
        // .stream()
        // .map(entry -> {
        // var name = entry.getKey();
        // var geneBonusData = entry.getValue();
        // var id = AlienResources.location(name);
        //
        // var filePath = pathProvider.json(id);
        // var jsonElement = GeneBonusData.CODEC.encodeStart(JsonOps.INSTANCE, geneBonusData)
        // .getOrThrow();
        //
        // return DataProvider.saveStable(cached, jsonElement, filePath);
        // });
        //
        // return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        return CompletableFuture.allOf();
    }

    @Override
    public final @NotNull String getName() {
        return "Gene Bonus Data";
    }
}

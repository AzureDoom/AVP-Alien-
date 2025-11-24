package com.alien.fabric.data.growth_stages;

import com.alien.fabric.data.growth_stages.provider.AberrantAlienGrowthStageProvider;
import com.alien.fabric.data.growth_stages.provider.AlienGrowthStageProvider;
import com.alien.fabric.data.growth_stages.provider.NetherAlienGrowthStageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class GrowthStageSubProvider extends GrowthStageDataProvider {

    public GrowthStageSubProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate() {
        AlienGrowthStageProvider.provide(this::add);
        AberrantAlienGrowthStageProvider.provide(this::add);
        NetherAlienGrowthStageProvider.provide(this::add);
    }
}

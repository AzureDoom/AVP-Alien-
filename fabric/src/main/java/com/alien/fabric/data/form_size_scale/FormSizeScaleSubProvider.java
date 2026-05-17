package com.alien.fabric.data.form_size_scale;

import com.alien.fabric.data.form_size_scale.provider.AberrantAlienFormSizeScaleProvider;
import com.alien.fabric.data.form_size_scale.provider.AlienFormSizeScaleProvider;
import com.alien.fabric.data.form_size_scale.provider.NetherAlienFormSizeScaleProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class FormSizeScaleSubProvider extends FormSizeScaleDataProvider {

    public FormSizeScaleSubProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate() {
        AlienFormSizeScaleProvider.provide(this::add);
        AberrantAlienFormSizeScaleProvider.provide(this::add);
        NetherAlienFormSizeScaleProvider.provide(this::add);
    }
}

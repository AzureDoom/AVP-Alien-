package com.alien.client.render.armor;

import com.alien.AlienResources;
import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRendererConfig;
import mod.azure.azurelib.common.render.layer.AzAutoGlowingLayer;
import net.minecraft.resources.ResourceLocation;

public class PlatedIrradiatedChitinArmorRenderer extends AzArmorRenderer {

    private static final ResourceLocation MODEL = AlienResources.armorGeoModelLocation("plated_chitin");

    private static final ResourceLocation TEXTURE = AlienResources.armorTextureLocation("plated_irradiated_chitin");

    public PlatedIrradiatedChitinArmorRenderer() {
        super(
            AzArmorRendererConfig.builder(MODEL, TEXTURE)
                .addRenderLayer(new AzAutoGlowingLayer<>())
                .build()
        );
    }
}

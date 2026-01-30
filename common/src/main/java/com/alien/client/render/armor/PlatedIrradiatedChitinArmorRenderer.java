package com.alien.client.render.armor;

import com.alien.AlienResources;
import com.blib.api.client.render.v1.armor.AzArmorRenderer;
import com.blib.api.client.render.v1.armor.AzArmorRendererConfig;
import com.blib.api.client.render.v1.layer.AzAutoGlowingLayer;
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

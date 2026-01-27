package com.alien.client.render.armor;

import com.alien.AlienResources;
import com.blib.azurelib.common.render.armor.AzArmorRenderer;
import com.blib.azurelib.common.render.armor.AzArmorRendererConfig;
import com.blib.azurelib.common.render.layer.AzAutoGlowingLayer;
import net.minecraft.resources.ResourceLocation;

public class IrradiatedChitinArmorRenderer extends AzArmorRenderer {

    private static final String NAME = "chitin";

    private static final ResourceLocation MODEL = AlienResources.armorGeoModelLocation(NAME);

    private static final ResourceLocation TEXTURE = AlienResources.armorTextureLocation("irradiated_" + NAME);

    public IrradiatedChitinArmorRenderer() {
        super(
            AzArmorRendererConfig.builder(MODEL, TEXTURE)
                .addRenderLayer(new AzAutoGlowingLayer<>())
                .build()
        );
    }
}

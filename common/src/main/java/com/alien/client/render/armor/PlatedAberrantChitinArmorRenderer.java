package com.alien.client.render.armor;

import com.alien.AlienResources;
import com.blib.azurelib.common.render.armor.AzArmorRenderer;
import com.blib.azurelib.common.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class PlatedAberrantChitinArmorRenderer extends AzArmorRenderer {

    private static final ResourceLocation MODEL = AlienResources.armorGeoModelLocation("plated_chitin");

    private static final ResourceLocation TEXTURE = AlienResources.armorTextureLocation("plated_aberrant_chitin");

    public PlatedAberrantChitinArmorRenderer() {
        super(AzArmorRendererConfig.builder(MODEL, TEXTURE).build());
    }
}

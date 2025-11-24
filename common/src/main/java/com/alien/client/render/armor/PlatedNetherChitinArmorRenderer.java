package com.alien.client.render.armor;

import com.alien.AlienResources;
import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class PlatedNetherChitinArmorRenderer extends AzArmorRenderer {

    private static final ResourceLocation MODEL = AlienResources.armorGeoModelLocation("plated_chitin");

    private static final ResourceLocation TEXTURE = AlienResources.armorTextureLocation("plated_nether_chitin");

    public PlatedNetherChitinArmorRenderer() {
        super(AzArmorRendererConfig.builder(MODEL, TEXTURE).build());
    }
}

package com.alien.client.render.armor;

import com.alien.AlienResources;
import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class NetherChitinArmorRenderer extends AzArmorRenderer {

    private static final ResourceLocation MODEL = AlienResources.armorGeoModelLocation("chitin");

    private static final ResourceLocation TEXTURE = AlienResources.armorTextureLocation("nether_chitin");

    public NetherChitinArmorRenderer() {
        super(AzArmorRendererConfig.builder(MODEL, TEXTURE).build());
    }
}

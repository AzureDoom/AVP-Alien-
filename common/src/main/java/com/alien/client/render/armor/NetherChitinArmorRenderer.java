package com.alien.client.render.armor;

import com.alien.AlienResources;
import com.blib.api.client.render.v1.armor.AzArmorRenderer;
import com.blib.api.client.render.v1.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class NetherChitinArmorRenderer extends AzArmorRenderer {

    private static final ResourceLocation MODEL = AlienResources.armorGeoModelLocation("chitin");

    private static final ResourceLocation TEXTURE = AlienResources.armorTextureLocation("nether_chitin");

    public NetherChitinArmorRenderer() {
        super(AzArmorRendererConfig.builder(MODEL, TEXTURE).build());
    }
}

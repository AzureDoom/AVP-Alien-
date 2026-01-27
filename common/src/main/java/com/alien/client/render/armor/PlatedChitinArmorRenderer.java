package com.alien.client.render.armor;

import com.alien.AlienResources;
import com.blib.azurelib.common.render.armor.AzArmorRenderer;
import com.blib.azurelib.common.render.armor.AzArmorRendererConfig;
import net.minecraft.resources.ResourceLocation;

public class PlatedChitinArmorRenderer extends AzArmorRenderer {

    private static final String NAME = "plated_chitin";

    private static final ResourceLocation MODEL = AlienResources.armorGeoModelLocation(NAME);

    private static final ResourceLocation TEXTURE = AlienResources.armorTextureLocation(NAME);

    public PlatedChitinArmorRenderer() {
        super(AzArmorRendererConfig.builder(MODEL, TEXTURE).build());
    }
}

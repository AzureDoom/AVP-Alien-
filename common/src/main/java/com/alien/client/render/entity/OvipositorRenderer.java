package com.alien.client.render.entity;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.ovipositor.Ovipositor;
import com.blib.api.client.render.v1.entity.AzEntityRenderer;
import com.blib.api.client.render.v1.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OvipositorRenderer extends AzEntityRenderer<Ovipositor> {

    private static final String NAME = "ovipositor";

    private static final ResourceLocation MODEL = AlienResources.entityGeoModelLocation(NAME);

    private static final ResourceLocation TEXTURE = AlienResources.entityTextureLocation(NAME);

    public OvipositorRenderer(EntityRendererProvider.Context context) {
        super(AzEntityRendererConfig.<Ovipositor>builder(MODEL, TEXTURE).build(), context);
        this.shadowRadius = 0.4F;
    }
}

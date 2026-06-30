package com.alien.client.render.entity;

import com.alien.AlienResources;
import com.alien.client.render.layer.EggsackRestraintsLayer;
import com.alien.common.gameplay.entity.living.alien.ovipositor.Ovipositor;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.blib.api.client.render.v1.entity.AzEntityRenderer;
import com.blib.api.client.render.v1.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class OvipositorRenderer extends AzEntityRenderer<Ovipositor> {

    private static final String NAME = "ovipositor";

    private static final ResourceLocation MODEL = AlienResources.entityGeoModelLocation(NAME);

    /** Chained-eggsack presentation, shown when this ovipositor rides a contained, inhibited (captive) queen. */
    private static final ResourceLocation CHAINED_MODEL = AlienResources.entityGeoModelLocation("chained_eggsack");

    private static final ResourceLocation TEXTURE = AlienResources.entityTextureLocation(NAME);

    public OvipositorRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<Ovipositor>builder(OvipositorRenderer::modelFor, ovipositor -> TEXTURE)
                .addRenderLayer(new EggsackRestraintsLayer())
                .build(),
            context
        );
        this.shadowRadius = 0.4F;
    }

    /** The captive chained-eggsack geo when she is a contained, inhibited queen; the normal ovipositor otherwise. */
    private static ResourceLocation modelFor(Ovipositor ovipositor) {
        return ovipositor.getVehicle() instanceof Queen queen && queen.isInhibited() && queen.isContained()
            ? CHAINED_MODEL
            : MODEL;
    }
}

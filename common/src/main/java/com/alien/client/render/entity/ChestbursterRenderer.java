package com.alien.client.render.entity;

import com.alien.client.animation.entity.ChestbursterAnimator;
import com.alien.client.render.AlienRenderResourceCache;
import com.alien.client.render.layer.MoltLayer;
import com.alien.common.gameplay.entity.living.alien.chestburster.Chestburster;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.client.render.v1.entity.AzEntityRenderer;
import com.blib.api.client.render.v1.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ChestbursterRenderer extends AzEntityRenderer<Chestburster> {

    private static final String NAME = "chestburster";

    private static final AlienRenderResourceCache RESOURCE_CACHE = new AlienRenderResourceCache(NAME, RenderType::entityCutoutNoCull);

    public ChestbursterRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.builder(ChestbursterRenderer::modelLocation, ChestbursterRenderer::textureLocation)
                .setRenderType(ChestbursterRenderer::renderType)
                .setAnimatorProvider(ChestbursterAnimator::new)
                .addRenderLayer(new MoltLayer<>())
                .build(),
            context
        );
    }

    private static ResourceLocation modelLocation(Chestburster chestburster) {
        return RESOURCE_CACHE.getOrCreateModelLocationForVariant(AlienVariant.NORMAL, chestburster.isRoyal());
    }

    private static RenderType renderType(Chestburster chestburster) {
        return RESOURCE_CACHE.getOrCreateRenderTypeForVariant(chestburster.getVariant(), chestburster.isRoyal());
    }

    private static ResourceLocation textureLocation(Chestburster chestburster) {
        return RESOURCE_CACHE.getOrCreateTextureLocationForVariant(chestburster.getVariant(), chestburster.isRoyal());
    }
}

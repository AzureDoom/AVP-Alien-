package com.alien.client.render.entity;

import com.alien.client.animation.entity.PredalienChestbursterAnimator;
import com.alien.client.render.AlienRenderResourceCache;
import com.alien.common.gameplay.entity.living.alien.predalien_chestburster.PredalienChestburster;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.client.render.v1.entity.AzEntityRenderer;
import com.blib.api.client.render.v1.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PredalienChestbursterRenderer extends AzEntityRenderer<PredalienChestburster> {

    private static final String NAME = "predalien_chestburster";

    private static final AlienRenderResourceCache RESOURCE_CACHE = new AlienRenderResourceCache(NAME, RenderType::entityCutoutNoCull);

    public PredalienChestbursterRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.builder(PredalienChestbursterRenderer::modelLocation, PredalienChestbursterRenderer::textureLocation)
                .setRenderType(PredalienChestbursterRenderer::renderType)
                .setAnimatorProvider(PredalienChestbursterAnimator::new)
                .build(),
            context
        );
    }

    private static ResourceLocation modelLocation(PredalienChestburster predalienChestburster) {
        return RESOURCE_CACHE.getOrCreateModelLocationForVariant(AlienVariant.NORMAL);
    }

    private static RenderType renderType(PredalienChestburster predalienChestburster) {
        return RESOURCE_CACHE.getOrCreateRenderTypeForVariant(predalienChestburster.getVariant());
    }

    private static ResourceLocation textureLocation(PredalienChestburster predalienChestburster) {
        return RESOURCE_CACHE.getOrCreateTextureLocationForVariant(predalienChestburster.getVariant());
    }
}

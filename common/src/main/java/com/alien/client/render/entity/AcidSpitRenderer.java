package com.alien.client.render.entity;

import com.alien.common.gameplay.entity.projectile.AcidSpit;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AcidSpitRenderer extends EntityRenderer<AcidSpit> {

    public AcidSpitRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(
        AcidSpit entity,
        float entityYaw,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource bufferSource,
        int packedLight
    ) {
        /* Invisible — trail particles provide the visual. */
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AcidSpit acidSpit) {
        return null;
    }
}

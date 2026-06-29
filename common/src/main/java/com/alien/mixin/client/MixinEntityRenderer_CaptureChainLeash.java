package com.alien.mixin.client;

import com.alien.client.render.CaptureChainLeashRenderer;
import com.alien.client.render.CaptureHoldClientState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the capture chain for a held mob. The capture chain no longer uses vanilla leashing, so there is no vanilla
 * rope to suppress — this is a pure, additive draw: at the head of {@code EntityRenderer#render} (the same axis-aligned
 * entity-local frame vanilla uses for the leash) we check the client hold map and, if this entity is held, draw the
 * chain ribbon from the holder to it. Because the queen's BLib renderer calls {@code super.render()}, this single
 * inject covers her and every vanilla-rendered mob alike.
 */
@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer_CaptureChainLeash {

    @Inject(method = "render", at = @At("HEAD"))
    private void avp_alien$drawCaptureChain(
        Entity entity,
        float entityYaw,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        CallbackInfo ci
    ) {
        Entity holder = CaptureHoldClientState.holderOf(entity);
        if (holder != null) {
            CaptureChainLeashRenderer.render(entity, holder, partialTick, poseStack, buffer);
        }
    }
}

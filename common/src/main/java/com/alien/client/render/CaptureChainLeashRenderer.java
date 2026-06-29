package com.alien.client.render;

import com.alien.client.render.block.ChainRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * Client render for the capture-chain tether. While a player holds a mob with the capture chain -- the window before it
 * is bound to an anchor -- the same sagging chain ribbon the anchor uses is drawn from the player's hand to the mob.
 * The hold uses no vanilla leash, so there is no rope to suppress; {@code MixinEntityRenderer_CaptureChainLeash} simply
 * calls {@link #render} for any mob the client knows to be held.
 */
public final class CaptureChainLeashRenderer {

    private CaptureChainLeashRenderer() {}

    /**
     * Draw the chain from {@code holder}'s rope-hold position to {@code entity}'s leash attach point. The pose stack is
     * at the entity's axis-aligned render origin (matching vanilla {@code EntityRenderer#renderLeash}), so every
     * coordinate below is expressed in that entity-local frame.
     */
    public static void render(
        Entity entity,
        Entity holder,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource buffer
    ) {
        Vec3 entityPos = entity.getPosition(partialTick);

        // Entity leash attach point: the leash offset rotated by body yaw, mirroring vanilla renderLeash exactly.
        float bodyRot = entity.getPreciseBodyRotation(partialTick) * ((float) Math.PI / 180.0F);
        Vec3 leashOffset = entity.getLeashOffset(partialTick);
        double cos = Math.cos(bodyRot);
        double sin = Math.sin(bodyRot);
        Vec3 start = new Vec3(
            cos * leashOffset.z + sin * leashOffset.x,
            leashOffset.y,
            sin * leashOffset.z - cos * leashOffset.x
        );

        // Holder hand and camera, in the same entity-local frame.
        Vec3 end = holder.getRopeHoldPosition(partialTick).subtract(entityPos);
        Vec3 camLocal = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().subtract(entityPos);

        int light = LevelRenderer.getLightColor(entity.level(), entity.blockPosition());

        ChainRenderer.render(poseStack, buffer, start, end, camLocal, light);
    }
}

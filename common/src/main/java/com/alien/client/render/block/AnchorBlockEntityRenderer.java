package com.alien.client.render.block;

import com.alien.common.gameplay.block.capture.anchor.AnchorBlock;
import com.alien.common.gameplay.block.entity.capture.anchor.AnchorBlockEntity;
import com.alien.common.registry.init.item.AlienItems;
import com.blib.api.client.render.v1.item.BLibItemTransformOverrides;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Draws the anchor geo (as the item-as-block, the same path the head trophies use) oriented to its mount surface: floor
 * upright, ceiling flipped, wall tilted onto the surface, each yawed by FACING. The wall/ground transform mode is
 * toggled so the BLib template picks its {@code fixed_wall} vs {@code fixed} transform.
 * <p>
 * NOTE: the exact wall/ceiling rotations are a sensible first pass and may need a small in-game tuning tweak.
 */
public class AnchorBlockEntityRenderer implements BlockEntityRenderer<AnchorBlockEntity> {

    /**
     * How far each plate is pushed into its mounting surface to seat flush, in block units (~0.43 px). Applied on the
     * axis that points into the surface for that orientation: the floor pushes down (−Y), the ceiling up (+Y), and the
     * wall into its face along the facing direction (so all four wall facings seat the same). One knob for all three;
     * tune if a plate floats above or sinks into its surface.
     */
    private static final double SEAT_OFFSET = 0.027;

    @Override
    public void render(
        @NotNull AnchorBlockEntity entity,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource source,
        int packedLight,
        int packedOverlay
    ) {
        var state = entity.getBlockState();
        if (!(state.getBlock() instanceof AnchorBlock)) {
            return;
        }

        var face = state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE);
        var facing = state.getValue(HorizontalDirectionalBlock.FACING);

        poseStack.pushPose();
        boolean wall = false;

        switch (face) {
            case FLOOR -> {
                poseStack.translate(0.5, 0.5 - SEAT_OFFSET, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
            }
            case CEILING -> {
                poseStack.translate(0.5, 0.5 + SEAT_OFFSET, 0.5);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
            }
            case WALL -> {
                wall = true;
                poseStack.translate(
                    0.5 - facing.getStepX() * SEAT_OFFSET,
                    0.5,
                    0.5 - facing.getStepZ() * SEAT_OFFSET
                );
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - facing.toYRot()));
                poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            }
        }

        var mc = Minecraft.getInstance();
        var priorWall = BLibItemTransformOverrides.isRenderAsWallBlock();
        var priorGround = BLibItemTransformOverrides.isRenderAsGroundBlock();
        BLibItemTransformOverrides.setRenderAsWallBlock(wall);
        BLibItemTransformOverrides.setRenderAsGroundBlock(!wall);

        try {
            mc.getItemRenderer()
                .renderStatic(
                    new ItemStack(AlienItems.ANCHOR.get()),
                    ItemDisplayContext.FIXED,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    source,
                    entity.getLevel(),
                    0
                );
        } finally {
            BLibItemTransformOverrides.setRenderAsWallBlock(priorWall);
            BLibItemTransformOverrides.setRenderAsGroundBlock(priorGround);
        }

        poseStack.popPose();

        renderChainIfBound(entity, partialTick, poseStack, source, packedLight);
    }

    /** Draws the capture chain from the anchor's bind point to the held mob, if any. */
    private void renderChainIfBound(
        AnchorBlockEntity entity,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource source,
        int packedLight
    ) {
        int netId = entity.getBoundMobNetId();
        if (netId < 0 || entity.getLevel() == null) {
            return;
        }
        Entity bound = entity.getLevel().getEntity(netId);
        if (!(bound instanceof LivingEntity mob)) {
            return;
        }

        BlockPos pos = entity.getBlockPos();
        Vec3 origin = new Vec3(pos.getX(), pos.getY(), pos.getZ());

        Vec3 start = entity.chainAnchorPoint().subtract(origin);
        Vec3 mobPoint = mob.getPosition(partialTick).add(0.0, mob.getBbHeight() * 0.6, 0.0);
        Vec3 end = mobPoint.subtract(origin);

        Vec3 cameraLocal = Minecraft.getInstance().gameRenderer.getMainCamera()
            .getPosition()
            .subtract(origin);

        ChainRenderer.render(poseStack, source, start, end, cameraLocal, packedLight);
    }
}

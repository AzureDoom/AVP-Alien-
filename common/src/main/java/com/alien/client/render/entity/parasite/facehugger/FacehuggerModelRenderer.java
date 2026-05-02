package com.alien.client.render.entity.parasite.facehugger;

import com.alien.client.render.entity.carrier.CarrierSpineBoneCache;
import com.alien.client.render.entity.head.EntityHeadData;
import com.alien.client.render.entity.head.EntityHeadDataCache;
import com.alien.client.render.entity.parasite.attachment.ParasiteHeadAttachmentOffsetDataCache;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.client.render.v1.AzLayerRenderer;
import com.blib.api.client.render.v1.entity.model.AzEntityModelRenderer;
import com.blib.api.client.render.v1.entity.pipeline.AzEntityRendererPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

public class FacehuggerModelRenderer extends AzEntityModelRenderer<Facehugger> {

    private static final float MODEL_TO_BLOCKS = 1.0F / 16.0F;

    public FacehuggerModelRenderer(
        AzEntityRendererPipeline<Facehugger> entityRendererPipeline,
        AzLayerRenderer<UUID, Facehugger> layerRenderer
    ) {
        super(entityRendererPipeline, layerRenderer);
    }

    @Override
    protected void applyRotations(
        Facehugger facehugger,
        PoseStack poseStack,
        float ageInTicks,
        float rotationYaw,
        float partialTick,
        float nativeScale
    ) {
        if (facehugger.getVehicle() instanceof Carrier carrier) {
            applySpineRidingRotations(facehugger, poseStack, partialTick, carrier);
            return;
        }

        if (!facehugger.getAttachmentManager().isAttachedToHost()) {
            super.applyRotations(facehugger, poseStack, ageInTicks, rotationYaw, partialTick, 1);
            return;
        }

        if (facehugger.isDeadOrDying()) {
            super.applyRotations(facehugger, poseStack, ageInTicks, rotationYaw, partialTick, 1);
            return;
        }

        var host = (LivingEntity) facehugger.getVehicle();

        if (host == null) {
            return;
        }

        var data = EntityHeadDataCache.get(host.getType());

        if (data == null) {
            return;
        }

        applyHuggingRotations(facehugger, poseStack, partialTick, host, data);
    }

    private void applySpineRidingRotations(
        Facehugger facehugger,
        PoseStack poseStack,
        float partialTick,
        Carrier carrier
    ) {
        var spineData = CarrierSpineBoneCache.get(carrier.getId());

        if (spineData == null) {
            return;
        }

        var facehuggers = carrier.getPassengers()
            .stream()
            .filter(p -> p.getType().is(AlienEntityTypeTags.FACEHUGGERS))
            .toList();

        var index = facehuggers.indexOf(facehugger);

        if (index < 0 || index >= spineData.modelPositions().length) {
            return;
        }

        var offset = spineData.modelPositions()[index];

        if (offset == null) {
            return;
        }

        var carrierX = Mth.lerp(partialTick, carrier.xOld, carrier.getX());
        var carrierY = Mth.lerp(partialTick, carrier.yOld, carrier.getY());
        var carrierZ = Mth.lerp(partialTick, carrier.zOld, carrier.getZ());

        var entityX = Mth.lerp(partialTick, facehugger.xOld, facehugger.getX());
        var entityY = Mth.lerp(partialTick, facehugger.yOld, facehugger.getY());
        var entityZ = Mth.lerp(partialTick, facehugger.zOld, facehugger.getZ());

        poseStack.translate(
            carrierX + offset.x - entityX,
            carrierY + offset.y - entityY,
            carrierZ + offset.z - entityZ
        );

        var carrierYaw = Mth.rotLerp(partialTick, carrier.yBodyRotO, carrier.yBodyRot);
        poseStack.mulPose(Axis.YN.rotationDegrees(carrierYaw));

        var rotation = spineData.rotations()[index];

        if (rotation != null) {
            poseStack.mulPose(Axis.ZP.rotation(rotation.z));
            poseStack.mulPose(Axis.YP.rotation(rotation.y));
            poseStack.mulPose(Axis.XP.rotation(rotation.x));
        }
    }

    private void applyHuggingRotations(
        Facehugger facehugger,
        PoseStack poseStack,
        float partialTick,
        LivingEntity host,
        EntityHeadData data
    ) {
        var bodyYaw = Mth.rotLerp(partialTick, host.yBodyRotO, host.yBodyRot);
        var headYaw = Mth.rotLerp(partialTick, host.yHeadRotO, host.yHeadRot) - bodyYaw;
        var headPitch = Mth.rotLerp(partialTick, host.getXRot(), host.xRotO);

        var xPivot = data.pivot().x;
        var yPivot = data.pivot().y;
        var zPivot = data.pivot().z;
        var ySize = data.size().y;
        var zSize = data.size().z;

        poseStack.mulPose(Axis.YN.rotationDegrees(bodyYaw));

        poseStack.translate(xPivot, yPivot - host.getBbHeight(), -zPivot);
        poseStack.mulPose(Axis.YN.rotationDegrees(headYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));
        poseStack.translate(-xPivot, -yPivot + host.getBbHeight(), zPivot);

        var offsetSuppliers = ParasiteHeadAttachmentOffsetDataCache.get(host.getType());

        if (offsetSuppliers != null) {
            var yOffset = offsetSuppliers.verticalOffsetSupplier().apply(data, facehugger);
            var zOffset = offsetSuppliers.faceOffsetSupplier().apply(data, facehugger);
            poseStack.translate(0, yOffset, zOffset);
        } else {
            poseStack.translate(0, -ySize, zSize);
        }
    }
}

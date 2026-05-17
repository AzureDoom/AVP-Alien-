package com.alien.common.util;

import com.blib.api.client.animation.v1.animator.AzAnimationContext;
import com.blib.api.client.model.v1.AzBone;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class AzAlienHeadAnimationUtil {

    public static void applyHeadLookFromBindPose(
        LivingEntity entity,
        AzAnimationContext<?> context,
        float partialTicks,
        String headBoneName
    ) {
        var head = context.boneCache().getBakedModel().getBoneOrNull(headBoneName);

        if (head == null) {
            return;
        }

        var headPitch = -Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) * Mth.DEG_TO_RAD;
        var lerpBodyRot = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        var lerpHeadRot = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        var netHeadYaw = -(lerpHeadRot - lerpBodyRot) * Mth.DEG_TO_RAD;

        // Walk parents once, computing two accumulators in AzureLib's Rz*Ry*Rx order:
        // parentBind: artistic posture only (used in the target — what the head SHOULD look like)
        // parentCurrent: includes animation deltas (used in the inverse — cancels parent animation
        // so the head stays locked to entity look direction, vanilla-MC style)
        var parentBind = new Quaternionf();
        var parentCurrent = new Quaternionf();

        for (AzBone p = head.getParent(); p != null; p = p.getParent()) {
            var pSnap = p.getInitialAzSnapshot();
            var pqBind = new Quaternionf()
                .rotateZ(pSnap.getRotZ())
                .rotateY(pSnap.getRotY())
                .rotateX(pSnap.getRotX());
            parentBind = pqBind.mul(parentBind);

            var pqCurrent = new Quaternionf()
                .rotateZ(p.getRotZ())
                .rotateY(p.getRotY())
                .rotateX(p.getRotX());
            parentCurrent = pqCurrent.mul(parentCurrent);
        }

        // Read the head bone's pose from the animation snapshot rather than the bone itself.
        // The bone's live rotation can include our own previous-frame write (because the engine's
        // cached-reset path skips bones whose rotationChanged flag is set, and our writes set it),
        // which causes look-tracking to compound across frames into a 360° drift. The snapshot is
        // only updated by the animation pipeline, so it stays clean.
        var snapshot = context.boneCache().getBoneSnapshotsByName().get(headBoneName);
        var headPose = snapshot != null ? snapshot : head.getInitialAzSnapshot();
        var headCurrent = new Quaternionf()
            .rotateZ(headPose.getRotZ())
            .rotateY(headPose.getRotY())
            .rotateX(headPose.getRotX());

        // Target world rotation in body frame: yaw + pitch composed on top of the bone's current
        // pose plus the artistic bind chain above it.
        var target = new Quaternionf()
            .rotateY(netHeadYaw)
            .rotateX(headPitch)
            .mul(parentBind)
            .mul(headCurrent);

        // Solve for L such that engine's parentCurrent * L equals target. parentCurrent absorbs any
        // parent animation deltas so they don't leak into the head's final world orientation.
        var local = new Quaternionf(parentCurrent).invert().mul(target);

        // Decompose into Euler angles matching AzureLib's render order Rz*Ry*Rx
        // (poseStack.mulPose Z, then Y, then X; right-multiplication => Rx applied to vertex first).
        var euler = local.getEulerAnglesZYX(new Vector3f());

        head.setRotX(euler.x);
        head.setRotY(euler.y);
        head.setRotZ(euler.z);
    }

    private AzAlienHeadAnimationUtil() {
        throw new UnsupportedOperationException();
    }
}

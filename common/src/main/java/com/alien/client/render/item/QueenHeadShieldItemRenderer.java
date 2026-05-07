package com.alien.client.render.item;

import com.alien.AlienResources;
import com.blib.api.client.render.v1.item.BLibGeoBoneItemRenderer;
import com.blib.api.client.render.v1.item.BLibGeoBoneItemRendererSpec;
import com.blib.api.client.render.v1.item.BLibItemTransform;
import com.blib.api.client.render.v1.item.BLibItemTransformMode;
import com.blib.api.client.render.v1.item.BLibItemTransforms;
import com.blib.api.client.render.v1.item.BLibTunableItemTransforms;
import net.minecraft.resources.ResourceLocation;

public class QueenHeadShieldItemRenderer extends BLibGeoBoneItemRenderer {

    private static final String QUEEN = "queen";
    private static final String HEAD_BONE = "gHead";

    private static final BLibItemTransforms IDLE_TRANSFORMS = BLibItemTransforms.builder()
        .thirdPersonLeftHand(BLibItemTransform.of(0.0f, 0.0f, 0.0f, 0.0f, -90.0f, 0.0f, 0.25f))
        .thirdPersonRightHand(BLibItemTransform.of(-1.6f, -0.85f, 0.0f, -280.0f, -130.0f, 80.0f, 0.45f))
        .firstPersonLeftHand(BLibItemTransform.of(-0.35f, -0.2f, 0.4f, 0.0f, -90.0f, 0.0f, 0.25f))
        .firstPersonRightHand(BLibItemTransform.of(-0.3f, -0.75f, 1.1f, -30.0f, 0.0f, 0.0f, 0.25f))
        .head(BLibItemTransform.of(0.0f, 0.25f, 0.0f, 0.0f, 180.0f, 0.0f, 0.5f))
        .gui(BLibItemTransform.of(0.1f, -0.975f, 0.0f, 0.0f, 159.9f, 0.0f, 0.25f))
        .ground(BLibItemTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.25f))
        .fixed(BLibItemTransform.of(0.0f, 0.0f, 0.0f, 0.0f, 180.0f, 0.0f, 0.5f))
        .build();

    private static final BLibItemTransforms BLOCKING_TRANSFORMS = BLibItemTransforms.builder()
        .firstPersonRightHand(BLibItemTransform.of(-0.41f, -0.65f, 1.1f, -30.0f, 0.0f, 0.0f, 0.25f))
        .build();

    private static final ResourceLocation ITEM_ID = AlienResources.location("queen_head_shield");

    public QueenHeadShieldItemRenderer() {
        super(
            BLibGeoBoneItemRendererSpec
                .builder(
                    AlienResources.entityGeoModelLocation(QUEEN),
                    AlienResources.entityTextureLocation(QUEEN),
                    HEAD_BONE
                )
                .idleTransforms(BLibTunableItemTransforms.wrap(ITEM_ID, BLibItemTransformMode.IDLE, IDLE_TRANSFORMS))
                .blockingTransforms(BLibTunableItemTransforms.wrap(ITEM_ID, BLibItemTransformMode.BLOCKING, BLOCKING_TRANSFORMS))
                .build()
        );
    }
}

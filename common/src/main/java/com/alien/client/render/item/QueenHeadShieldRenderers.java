package com.alien.client.render.item;

import com.alien.AlienResources;
import com.blib.api.client.render.v1.BLibTransform;
import com.blib.api.client.render.v1.item.BLibGeoBoneItemRenderer;
import com.blib.api.client.render.v1.item.BLibGeoBoneItemRendererConfig;
import com.blib.api.client.render.v1.item.BLibItemTransformMode;
import com.blib.api.client.render.v1.item.BLibItemTransforms;
import com.blib.api.client.render.v1.item.BLibTunableItemTransforms;
import net.minecraft.resources.ResourceLocation;

/**
 * Factory for queen-head shield renderers. The four variants (queen / nether / aberrant / irradiated) all
 * render the same {@code gHead} bone of the queen geo at the same idle / blocking poses; only the texture
 * and the per-item tuner key differ. {@link #create} parameterizes those, so a new variant is one call site
 * away rather than a new subclass.
 */
public final class QueenHeadShieldRenderers {

    private static final String QUEEN_GEO_NAME = "queen";

    private static final String HEAD_BONE = "gHead";

    public static final BLibItemTransforms IDLE_TRANSFORMS = BLibItemTransforms.builder()
        .thirdPersonRightHand(BLibTransform.of(0.085f, -0.2967f, 0.174f, -62.458f, 79.1686f, -112.8039f, 0.5f))
        .mirrorThirdPersonRightToLeft()
        .firstPersonRightHand(BLibTransform.of(-0.25f, -0.35f, 0.1f, -135.0f, 0.0f, 0.0f, 0.3f))
        .mirrorFirstPersonRightToLeft()
        .head(BLibTransform.of(-0.0414f, 0.2145f, 0.4564f, -80.7772f, 0.0f, 0.0f, 1.3f))
        .gui(BLibTransform.of(-0.125f, -0.15f, 0.0f, 285.0f, 0.0f, 165.0f, 0.275f))
        .ground(BLibTransform.of(0.0f, 0.0f, 0.0f, -90.0f, 0.0f, 0.0f, 0.25f))
        .fixed(BLibTransform.of(0f, 0f, 0f, 0f, 0f, 0f, 1f))
        .build();

    public static final BLibItemTransforms BLOCKING_TRANSFORMS = BLibItemTransforms.builder()
        .thirdPersonRightHand(BLibTransform.of(-0.0769f, -0.5623f, -0.3255f, -124.1179f, 25.8822f, -362.5681f, 0.5f))
        .mirrorThirdPersonRightToLeft()
        .firstPersonRightHand(BLibTransform.of(-0.45f, -0.2f, 0.1f, -135.0f, 0.0f, 0.0f, 0.3f))
        .mirrorFirstPersonRightToLeft()
        .build();

    private QueenHeadShieldRenderers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Build a renderer for one queen-head variant. {@code itemId} is the item's resource id (used as the
     * tuner key so {@code /blib transform-tune} can target this variant independently of the others).
     * {@code textureEntityName} is the entity texture stem under {@code textures/entity/} — e.g.
     * {@code "queen"}, {@code "nether_queen"}.
     */
    public static BLibGeoBoneItemRenderer create(ResourceLocation itemId, String textureEntityName) {
        return new BLibGeoBoneItemRenderer(
            BLibGeoBoneItemRendererConfig
                .builder(
                    AlienResources.entityGeoModelLocation(QUEEN_GEO_NAME),
                    AlienResources.entityTextureLocation(textureEntityName),
                    HEAD_BONE
                )
                .idleTransforms(BLibTunableItemTransforms.wrap(itemId, BLibItemTransformMode.IDLE, IDLE_TRANSFORMS))
                .blockingTransforms(BLibTunableItemTransforms.wrap(itemId, BLibItemTransformMode.BLOCKING, BLOCKING_TRANSFORMS))
                .build()
        );
    }
}

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
 * Factory for crusher-head renderers. The four variants (crusher / nether / aberrant / irradiated) all render the same
 * {@code gHead} bone of the crusher geo; only the texture and the per-item tuner key differ.
 * <p>
 * The two "kinds" — wearable/placeable trophy ({@code crusher_head}, etc.) and shield ({@code crusher_head_shield},
 * etc.) — get distinct transform sets so they can be tuned independently. Default values are seeded from the queen's
 * tuned values as a starting point and are expected to be re-tuned in-game via {@code /blib transform-tune} per crusher
 * item id; the crusher head's proportions differ from the queen's, so the seeded values won't be ideal until tuned.
 */
public final class CrusherHeadRenderers {

    private static final String CRUSHER_GEO_NAME = "crusher";

    private static final String HEAD_BONE = "gHead";

    /**
     * Idle-pose transforms for the trophy / placeable variant ({@code crusher_head} and the three coloured variants).
     * No blocking-mode counterpart since the trophy doesn't implement {@code BLibShieldItem} and never enters blocking
     * state. Tune via the gizmo or {@code /blib transform-tune set/nudge} with the trophy item ids.
     */
    public static final BLibItemTransforms TROPHY_IDLE_TRANSFORMS = BLibItemTransforms.builder()
        .mirrorThirdPersonRightToLeft()
        .thirdPersonRightHand(BLibTransform.of(-0.0005f, -0.1502f, 0.0813f, -63.9815f, -0.0122f, 0.0088f, 0.6f))
        .mirrorFirstPersonRightToLeft()
        .firstPersonRightHand(BLibTransform.of(-0.1332f, 0.2564f, 0.15f, -41.8418f, 0.0f, 0.0f, 0.4f))
        .head(BLibTransform.of(-0.0414f, 0.2145f, 0.4564f, -80.7772f, 0.0f, 0.0f, 1.3f))
        .gui(BLibTransform.of(-0.125f, -0.15f, 0.0f, 285.0f, 0.0f, 165.0f, 0.275f))
        .ground(BLibTransform.of(0.0f, 0.0f, 0.0f, -90.0f, 0.0f, 0.0f, 0.25f))
        .fixed(BLibTransform.of(0.0f, 0.0991f, -0.1618f, -34.8212f, 0.0f, 0.0f, 1.0f))
        .fixedWall(BLibTransform.of(0.0f, 0.0f, 0.421f, -126.29f, 0.0f, 0.0f, 1.0f))
        .build();

    /**
     * Idle-pose transforms for the shield variant ({@code crusher_head_shield} etc.). Starts as a copy of
     * {@link #TROPHY_IDLE_TRANSFORMS} so the two shapes match before tuning — re-tune in-hand to make it look like a
     * held shield rather than a trophy in hand.
     */
    public static final BLibItemTransforms SHIELD_IDLE_TRANSFORMS = BLibItemTransforms.builder()
        .mirrorThirdPersonRightToLeft()
        .thirdPersonRightHand(BLibTransform.of(0.0459f, -0.2967f, 0.174f, -73.0463f, 45.3227f, -100.048f, 0.75f))
        .mirrorFirstPersonRightToLeft()
        .firstPersonRightHand(BLibTransform.of(0.1f, -0.5f, 0.0f, -135.0f, 0.0f, 0.0f, 0.75f))
        .head(BLibTransform.of(-0.0414f, 0.2145f, 0.4564f, -80.7772f, 0.0f, 0.0f, 1.3f))
        .gui(BLibTransform.of(-0.125f, -0.15f, 0.0f, 285.0f, 0.0f, 165.0f, 0.275f))
        .ground(BLibTransform.of(0.0f, 0.0f, 0.0f, -90.0f, 0.0f, 0.0f, 0.25f))
        .fixed(BLibTransform.of(0.0f, 0.3514f, -0.1618f, -61.2633f, 0.0f, 0.0f, 1.0f))
        .build();

    /**
     * Blocking-pose transforms for the shield variant — picked when the player is actively raising the shield. Trophy
     * doesn't have a blocking equivalent.
     */
    public static final BLibItemTransforms SHIELD_BLOCKING_TRANSFORMS = BLibItemTransforms.builder()
        .mirrorThirdPersonRightToLeft()
        .thirdPersonRightHand(BLibTransform.of(-0.0319f, -0.2192f, -0.0912f, -80.6139f, 21.0236f, -384.8028f, 0.75f))
        .mirrorFirstPersonRightToLeft()
        .firstPersonRightHand(BLibTransform.of(-0.4f, -0.1f, 0.0f, -135.0f, 0.0f, 0.0f, 0.75f))
        .build();

    private CrusherHeadRenderers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Build a renderer for one crusher-head <em>trophy</em> variant ({@code crusher_head} etc.). No blocking transforms
     * — the trophy isn't a shield and the renderer's blocking-transform path never activates because the item doesn't
     * implement {@code BLibShieldItem}.
     * <p>
     * {@code itemId} is the item's resource id (the tuner key — {@code /blib transform-tune} targets each variant
     * independently). {@code textureEntityName} is the entity texture stem under {@code textures/entity/} — e.g.
     * {@code "crusher"}, {@code "nether_crusher"}.
     */
    public static BLibGeoBoneItemRenderer createTrophy(ResourceLocation itemId, String textureEntityName) {
        return new BLibGeoBoneItemRenderer(
            BLibGeoBoneItemRendererConfig
                .builder(
                    AlienResources.entityGeoModelLocation(CRUSHER_GEO_NAME),
                    AlienResources.entityTextureLocation(textureEntityName),
                    HEAD_BONE
                )
                .idleTransforms(BLibTunableItemTransforms.wrap(itemId, BLibItemTransformMode.IDLE, TROPHY_IDLE_TRANSFORMS))
                .build()
        );
    }

    /**
     * Build a renderer for one crusher-head <em>shield</em> variant ({@code crusher_head_shield} etc.). Wraps both idle
     * and blocking transforms so the renderer's blocking-pose path activates while the player is using the item.
     */
    public static BLibGeoBoneItemRenderer createShield(ResourceLocation itemId, String textureEntityName) {
        return new BLibGeoBoneItemRenderer(
            BLibGeoBoneItemRendererConfig
                .builder(
                    AlienResources.entityGeoModelLocation(CRUSHER_GEO_NAME),
                    AlienResources.entityTextureLocation(textureEntityName),
                    HEAD_BONE
                )
                .idleTransforms(BLibTunableItemTransforms.wrap(itemId, BLibItemTransformMode.IDLE, SHIELD_IDLE_TRANSFORMS))
                .blockingTransforms(BLibTunableItemTransforms.wrap(itemId, BLibItemTransformMode.BLOCKING, SHIELD_BLOCKING_TRANSFORMS))
                .build()
        );
    }
}

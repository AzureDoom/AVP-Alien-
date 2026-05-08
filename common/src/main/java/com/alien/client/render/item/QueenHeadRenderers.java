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
 * Factory for queen-head renderers. The four variants (queen / nether / aberrant / irradiated) all render
 * the same {@code gHead} bone of the queen geo; only the texture and the per-item tuner key differ.
 * <p>
 * The two "kinds" — wearable/placeable trophy ({@code queen_head}, etc.) and shield
 * ({@code queen_head_shield}, etc.) — get distinct transform sets so they can be tuned independently.
 * They start with the same values and diverge as the user tunes each in-game; the trophy ends up looking
 * like a head sitting in the player's hand, the shield ends up looking like a held shield.
 */
public final class QueenHeadRenderers {

    private static final String QUEEN_GEO_NAME = "queen";

    private static final String HEAD_BONE = "gHead";

    /**
     * Idle-pose transforms for the trophy / placeable variant ({@code queen_head} and the three coloured
     * variants). No blocking-mode counterpart since the trophy doesn't implement {@code BLibShieldItem}
     * and never enters blocking state. Tune via the gizmo or {@code /blib transform-tune set/nudge} with
     * the trophy item ids.
     */
    public static final BLibItemTransforms TROPHY_IDLE_TRANSFORMS = BLibItemTransforms.builder()
        .thirdPersonRightHand(BLibTransform.of(-0.0149f, -0.1799f, 0.2674f, -63.9815f, -0.0122f, 0.0088f, 0.5f))
        .mirrorThirdPersonRightToLeft()
        .firstPersonRightHand(BLibTransform.of(-0.15f, 0.25f, -0.1f, -75.0f, 0.0f, 0.0f, 0.3f))
        .mirrorFirstPersonRightToLeft()
        .head(BLibTransform.of(-0.0414f, 0.2145f, 0.4564f, -80.7772f, 0.0f, 0.0f, 1.3f))
        .gui(BLibTransform.of(-0.125f, -0.15f, 0.0f, 285.0f, 0.0f, 165.0f, 0.275f))
        .ground(BLibTransform.of(0.0f, 0.0f, 0.0f, -90.0f, 0.0f, 0.0f, 0.25f))
        .fixed(BLibTransform.of(0.0f, 0.3514f, -0.1618f, -61.2633f, 0.0f, 0.0f, 1.0f))
        .fixedWall(BLibTransform.of(0.0f, 0.5f, 0.1307f, -150.3086f, 0.0f, 0.0f, 1.0f))
        .build();

    /**
     * Idle-pose transforms for the shield variant ({@code queen_head_shield} etc.). Starts as a copy of
     * {@link #TROPHY_IDLE_TRANSFORMS} so the existing values aren't lost on the split — re-tune in-hand
     * to make it look like a held shield rather than a trophy in hand.
     */
    public static final BLibItemTransforms SHIELD_IDLE_TRANSFORMS = BLibItemTransforms.builder()
        .thirdPersonRightHand(BLibTransform.of(0.085f, -0.2967f, 0.174f, -62.458f, 79.1686f, -112.8039f, 0.5f))
        .mirrorThirdPersonRightToLeft()
        .firstPersonRightHand(BLibTransform.of(-0.25f, -0.35f, 0.1f, -135.0f, 0.0f, 0.0f, 0.3f))
        .mirrorFirstPersonRightToLeft()
        .head(BLibTransform.of(-0.0414f, 0.2145f, 0.4564f, -80.7772f, 0.0f, 0.0f, 1.3f))
        .gui(BLibTransform.of(-0.125f, -0.15f, 0.0f, 285.0f, 0.0f, 165.0f, 0.275f))
        .ground(BLibTransform.of(0.0f, 0.0f, 0.0f, -90.0f, 0.0f, 0.0f, 0.25f))
        .fixed(BLibTransform.of(0.0f, 0.3514f, -0.1618f, -61.2633f, 0.0f, 0.0f, 1.0f))
        .build();

    /**
     * Blocking-pose transforms for the shield variant — picked when the player is actively raising the
     * shield. Trophy doesn't have a blocking equivalent.
     */
    public static final BLibItemTransforms SHIELD_BLOCKING_TRANSFORMS = BLibItemTransforms.builder()
        .thirdPersonRightHand(BLibTransform.of(-0.0769f, -0.5623f, -0.3255f, -124.1179f, 25.8822f, -362.5681f, 0.5f))
        .mirrorThirdPersonRightToLeft()
        .firstPersonRightHand(BLibTransform.of(-0.45f, -0.2f, 0.1f, -135.0f, 0.0f, 0.0f, 0.3f))
        .mirrorFirstPersonRightToLeft()
        .build();

    private QueenHeadRenderers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Build a renderer for one queen-head <em>trophy</em> variant ({@code queen_head} etc.). No blocking
     * transforms — the trophy isn't a shield and the renderer's blocking-transform path never activates
     * because the item doesn't implement {@code BLibShieldItem}.
     * <p>
     * {@code itemId} is the item's resource id (the tuner key — {@code /blib transform-tune} targets each
     * variant independently). {@code textureEntityName} is the entity texture stem under
     * {@code textures/entity/} — e.g. {@code "queen"}, {@code "nether_queen"}.
     */
    public static BLibGeoBoneItemRenderer createTrophy(ResourceLocation itemId, String textureEntityName) {
        return new BLibGeoBoneItemRenderer(
            BLibGeoBoneItemRendererConfig
                .builder(
                    AlienResources.entityGeoModelLocation(QUEEN_GEO_NAME),
                    AlienResources.entityTextureLocation(textureEntityName),
                    HEAD_BONE
                )
                .idleTransforms(BLibTunableItemTransforms.wrap(itemId, BLibItemTransformMode.IDLE, TROPHY_IDLE_TRANSFORMS))
                .build()
        );
    }

    /**
     * Build a renderer for one queen-head <em>shield</em> variant ({@code queen_head_shield} etc.). Wraps
     * both idle and blocking transforms so the renderer's blocking-pose path activates while the player
     * is using the item.
     */
    public static BLibGeoBoneItemRenderer createShield(ResourceLocation itemId, String textureEntityName) {
        return new BLibGeoBoneItemRenderer(
            BLibGeoBoneItemRendererConfig
                .builder(
                    AlienResources.entityGeoModelLocation(QUEEN_GEO_NAME),
                    AlienResources.entityTextureLocation(textureEntityName),
                    HEAD_BONE
                )
                .idleTransforms(BLibTunableItemTransforms.wrap(itemId, BLibItemTransformMode.IDLE, SHIELD_IDLE_TRANSFORMS))
                .blockingTransforms(BLibTunableItemTransforms.wrap(itemId, BLibItemTransformMode.BLOCKING, SHIELD_BLOCKING_TRANSFORMS))
                .build()
        );
    }
}

package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.client.animation.entity.cocoon.CocoonAnimationStateTracker;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.QueenAnimationRefs;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.QueenAttackType;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class QueenAnimator extends AzEntityAnimator<Queen> {

    private static final String NAME = "queen";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    private final CocoonAnimationStateTracker<Queen> cocoonAnimationStateTracker = new CocoonAnimationStateTracker<>();

    public QueenAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Queen> animationTrackContainer) {
        animationTrackContainer.add(
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.BODY_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.HEAD_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.LEFT_ARM_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.LEFT_LEG_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.LEFT_TITTY_ARM_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_LEG_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_TITTY_ARM_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.TAIL_TRACK_NAME)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Queen animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Queen animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        if (cocoonAnimationStateTracker.run(animatable)) {
            return;
        }

        runPassiveAnimations(animatable);

        var bakedModel = context().boneCache().getBakedModel();
        var eggSack = bakedModel.getBoneOrNull("root2");

        if (eggSack != null) {
            eggSack.setHidden(true);
        }
    }

    private void runPassiveAnimations(Queen queen) {
        var dispatcher = queen.getAnimationDispatcher();

        var attackType = queen.attackType.get();
        var attackId = queen.attackId.get();

        if (attackType != QueenAttackType.NONE) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(queen, attackType);

                switch (attackType) {
                    case SWIPE_DOWN -> dispatcher.swipeDownAttack(speed);
                    case BACKHAND -> dispatcher.backhandAttack(speed);
                    case TAIL_STRIKE -> dispatcher.tailStrikeAttack(speed);
                }

                previousAttackId = attackId;
            }
            return;
        }

        var isMovingOnGround = queen.isMovingHorizontally.get() && queen.onGround();
        Runnable animFunction;

        if (queen.getOvipositorManager().hasOvipositor()) {
            animFunction = dispatcher::sitOnOvipositor;
        } else if (queen.isUnderWater()) {
            // TODO: idle swim
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (queen.hasTarget.get()) {
                animFunction = dispatcher::run;
            } else {
                animFunction = dispatcher::walk;
            }
        } else {
            // TODO: idle crawl
            animFunction = dispatcher::idle;
        }

        animFunction.run();
    }

    private float calculateAttackSpeed(Queen queen, QueenAttackType attackType) {
        var animationName = switch (attackType) {
            case SWIPE_DOWN -> QueenAnimationRefs.SWIPEDOWN_BODY_ANIMATION_NAME;
            case BACKHAND -> QueenAnimationRefs.BACKHAND_BODY_ANIMATION_NAME;
            case TAIL_STRIKE -> QueenAnimationRefs.TAILSTRIKE_BODY_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = queen.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(queen, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

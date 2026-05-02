package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.client.animation.entity.cocoon.CocoonAnimationStateTracker;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.harbinger.Harbinger;
import com.alien.common.gameplay.entity.living.alien.xenomorph.harbinger.HarbingerAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HarbingerAnimator extends AzEntityAnimator<Harbinger> {

    private static final String NAME = "harbinger";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    private final CocoonAnimationStateTracker<Harbinger> cocoonAnimationStateTracker = new CocoonAnimationStateTracker<>();

    public HarbingerAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Harbinger> animationTrackContainer) {
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
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_LEG_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.TAIL_TRACK_NAME)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Harbinger animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Harbinger animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        if (cocoonAnimationStateTracker.run(animatable)) {
            return;
        }

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Harbinger harbinger) {
        var dispatcher = harbinger.getAnimationDispatcher();

        var attackType = harbinger.attackType.get();
        var attackId = harbinger.attackId.get();

        if (attackType != XenomorphAttackType.NONE) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(harbinger, attackType);

                switch (attackType) {
                    case BITE -> dispatcher.biteAttack(speed);
                    case CLAW -> dispatcher.rightClawAttack(speed);
                    case TAIL -> dispatcher.tailAttack(speed);
                }

                previousAttackId = attackId;
            }
            return;
        }

        var isMovingOnGround = harbinger.isMovingHorizontally.get() && harbinger.onGround();
        Runnable animFunction;

        if (harbinger.isUnderWater()) {
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (harbinger.hasTarget.get()) {
                animFunction = dispatcher::run;
            } else {
                animFunction = dispatcher::walk;
            }
        } else {
            animFunction = dispatcher::idle;
        }

        animFunction.run();
    }

    private float calculateAttackSpeed(Harbinger harbinger, XenomorphAttackType attackType) {
        var animationName = switch (attackType) {
            case BITE -> HarbingerAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME;
            case CLAW -> HarbingerAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME;
            case TAIL -> HarbingerAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = harbinger.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(harbinger, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

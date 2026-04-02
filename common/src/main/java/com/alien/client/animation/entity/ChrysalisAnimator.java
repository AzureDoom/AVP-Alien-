package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.Chrysalis;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ChrysalisAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ChrysalisAnimator extends AzEntityAnimator<Chrysalis> {

    private static final String NAME = "chrysalis";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private XenomorphAttackType previousAttackType = XenomorphAttackType.NONE;

    public ChrysalisAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Chrysalis> animationTrackContainer) {
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
    public @NotNull ResourceLocation getAnimationLocation(Chrysalis animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Chrysalis animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Chrysalis chrysalis) {
        var dispatcher = chrysalis.getAnimationDispatcher();

        var attackType = chrysalis.attackType.get();

        if (attackType != XenomorphAttackType.NONE) {
            if (attackType != previousAttackType) {
                var speed = calculateAttackSpeed(chrysalis, attackType);

                switch (attackType) {
                    case BITE -> dispatcher.biteAttack(speed);
                    case CLAW -> dispatcher.rightClawAttack(speed);
                    case TAIL -> dispatcher.tailAttack(speed);
                }

                previousAttackType = attackType;
            }
            return;
        }

        previousAttackType = XenomorphAttackType.NONE;

        var isMovingOnGround = chrysalis.isMovingHorizontally.get() && chrysalis.onGround();
        Runnable animFunction;

        if (chrysalis.isUnderWater()) {
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (chrysalis.hasTarget.get()) {
                animFunction = dispatcher::run;
            } else {
                animFunction = dispatcher::walk;
            }
        } else {
            animFunction = dispatcher::idle;
        }

        animFunction.run();
    }

    private float calculateAttackSpeed(Chrysalis chrysalis, XenomorphAttackType attackType) {
        var animationName = switch (attackType) {
            case BITE -> ChrysalisAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME;
            case CLAW -> ChrysalisAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME;
            case TAIL -> ChrysalisAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = chrysalis.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(chrysalis, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

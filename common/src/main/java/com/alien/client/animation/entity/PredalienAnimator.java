package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.predalien.Predalien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.predalien.PredalienAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PredalienAnimator extends AzEntityAnimator<Predalien> {

    private static final String NAME = "predalien";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    public PredalienAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Predalien> animationTrackContainer) {
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
    public @NotNull ResourceLocation getAnimationLocation(Predalien animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Predalien animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Predalien predalien) {
        var dispatcher = predalien.getAnimationDispatcher();

        var attackType = predalien.attackType.get();
        var attackId = predalien.attackId.get();

        if (attackType != XenomorphAttackType.NONE) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(predalien, attackType);

                switch (attackType) {
                    case BITE -> dispatcher.biteAttack(speed);
                    case CLAW -> dispatcher.rightClawAttack(speed);
                    case TAIL -> dispatcher.tailAttack(speed);
                }

                previousAttackId = attackId;
            }
            return;
        }

        var isMovingOnGround = predalien.isMovingHorizontally.get() && predalien.onGround();
        Runnable animFunction;

        if (predalien.isUnderWater()) {
            // TODO: idle swim
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (predalien.hasTarget.get()) {
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

    private float calculateAttackSpeed(Predalien predalien, XenomorphAttackType attackType) {
        var animationName = switch (attackType) {
            case BITE -> PredalienAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME;
            case CLAW -> PredalienAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME;
            case TAIL -> PredalienAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = predalien.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(predalien, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

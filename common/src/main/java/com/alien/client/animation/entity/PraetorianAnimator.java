package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.client.animation.entity.cocoon.CocoonAnimationStateTracker;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.praetorian.Praetorian;
import com.alien.common.gameplay.entity.living.alien.xenomorph.praetorian.PraetorianAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PraetorianAnimator extends AzEntityAnimator<Praetorian> {

    private static final String NAME = "praetorian";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    private final CocoonAnimationStateTracker<Praetorian> cocoonAnimationStateTracker = new CocoonAnimationStateTracker<>();

    public PraetorianAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Praetorian> animationTrackContainer) {
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
    public @NotNull ResourceLocation getAnimationLocation(Praetorian animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Praetorian animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        if (cocoonAnimationStateTracker.run(animatable)) {
            return;
        }

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Praetorian praetorian) {
        var dispatcher = praetorian.getAnimationDispatcher();

        var attackType = praetorian.attackType.get();
        var attackId = praetorian.attackId.get();

        if (attackType != XenomorphAttackType.NONE) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(praetorian, attackType);

                switch (attackType) {
                    case BITE -> dispatcher.biteAttack(speed);
                    case CLAW -> dispatcher.rightClawAttack(speed);
                    case TAIL -> dispatcher.tailAttack(speed);
                }

                previousAttackId = attackId;
            }
            return;
        }

        var isMovingOnGround = praetorian.isMovingHorizontally.get() && praetorian.onGround();
        Runnable animFunction;

        if (praetorian.isUnderWater()) {
            // TODO: idle swim
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (praetorian.hasTarget.get()) {
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

    private float calculateAttackSpeed(Praetorian praetorian, XenomorphAttackType attackType) {
        var animationName = switch (attackType) {
            case BITE -> PraetorianAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME;
            case CLAW -> PraetorianAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME;
            case TAIL -> PraetorianAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = praetorian.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(praetorian, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

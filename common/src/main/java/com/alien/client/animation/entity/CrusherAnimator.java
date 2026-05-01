package com.alien.client.animation.entity;

import com.alien.client.animation.entity.cocoon.CocoonAnimationStateTracker;
import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.xenomorph.crusher.Crusher;
import com.alien.common.gameplay.entity.living.alien.xenomorph.crusher.CrusherAnimationRefs;
import com.alien.common.gameplay.entity.living.alien.xenomorph.crusher.CrusherAttackType;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CrusherAnimator extends AzEntityAnimator<Crusher> {

    private static final String NAME = "crusher";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    private final CocoonAnimationStateTracker<Crusher> cocoonAnimationStateTracker = new CocoonAnimationStateTracker<>();

    public CrusherAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Crusher> animationTrackContainer) {
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
    public @NotNull ResourceLocation getAnimationLocation(Crusher animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Crusher animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        if (cocoonAnimationStateTracker.run(animatable)) {
            return;
        }

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Crusher crusher) {
        var dispatcher = crusher.getAnimationDispatcher();

        if (crusher.isLunging.get()) {
            dispatcher.lunge();
            return;
        }

        var attackType = crusher.attackType.get();
        var attackId = crusher.attackId.get();

        if (attackType != CrusherAttackType.NONE) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(crusher, attackType);

                switch (attackType) {
                    case BITE -> dispatcher.biteAttack(speed);
                    case TAIL -> dispatcher.tailAttack(speed);
                }

                previousAttackId = attackId;
            }
            return;
        }

        var isMovingOnGround = crusher.isMovingHorizontally.get() && crusher.onGround();
        Runnable animFunction;

        if (crusher.isUnderWater()) {
            // TODO: idle swim
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (crusher.hasTarget.get()) {
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

    private float calculateAttackSpeed(Crusher crusher, CrusherAttackType attackType) {
        var animationName = switch (attackType) {
            case BITE -> CrusherAnimationRefs.BITEATTACK_HEAD_ANIMATION_NAME;
            case TAIL -> CrusherAnimationRefs.TAILATTACK_TAIL_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = crusher.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(crusher, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

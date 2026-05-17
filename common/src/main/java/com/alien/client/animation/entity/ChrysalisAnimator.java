package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.client.animation.entity.cocoon.CocoonAnimationStateTracker;
import com.alien.common.gameplay.entity.living.alien.xenomorph.AttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.Chrysalis;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ChrysalisAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.alien.common.util.AzAlienHeadAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ChrysalisAnimator extends AzEntityAnimator<Chrysalis> {

    private static final String NAME = "chrysalis";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    private boolean wasRolling = false;

    private int rollAnimationTicks = 0;

    private final CocoonAnimationStateTracker<Chrysalis> cocoonAnimationStateTracker = new CocoonAnimationStateTracker<>();

    public ChrysalisAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Chrysalis> animationTrackContainer) {
        animationTrackContainer.add(
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.BODY)
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

        if (cocoonAnimationStateTracker.run(animatable)) {
            return;
        }

        AzAlienHeadAnimationUtil.applyHeadLookFromBindPose(animatable, context(), partialTicks, "gNeck");

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Chrysalis chrysalis) {
        var dispatcher = chrysalis.getAnimationDispatcher();

        if (chrysalis.isRolling.get()) {
            if (!wasRolling) {
                dispatcher.rollStart();
                rollAnimationTicks = 0;
                wasRolling = true;
            } else if (rollAnimationTicks > 10) {
                dispatcher.rollLoop();
            }
            rollAnimationTicks++;
            return;
        }

        if (wasRolling) {
            if (chrysalis.rollWasSmashed.get()) {
                var speed = calculateStunSpeed(chrysalis);
                dispatcher.rollSmashed(speed);
            } else {
                dispatcher.rollStop();
            }
            wasRolling = false;
            rollAnimationTicks = 0;
            return;
        }

        if (chrysalis.isStunned.get()) {
            return;
        }

        var attackType = chrysalis.attackType.get();
        var attackId = chrysalis.attackId.get();

        if (!attackType.isNone()) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(chrysalis, attackType);

                if (attackType == Chrysalis.BITE)
                    dispatcher.biteAttack(speed);
                else if (attackType == Chrysalis.CLAW)
                    dispatcher.rightClawAttack(speed);
                else if (attackType == Chrysalis.TAIL)
                    dispatcher.tailAttack(speed);

                previousAttackId = attackId;
            }
            return;
        }

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

    private float calculateStunSpeed(Chrysalis chrysalis) {
        var durationInTicks = chrysalis.stunDurationTicks.get();

        if (durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(chrysalis, ChrysalisAnimationRefs.ROLL_SMASHED_ANIMATION_NAME);

        return (float) (animation.length() / durationInTicks);
    }

    private float calculateAttackSpeed(Chrysalis chrysalis, AttackType attackType) {
        String animationName;

        if (attackType == Chrysalis.BITE)
            animationName = ChrysalisAnimationRefs.ATTACKBITE_ANIMATION_NAME;
        else if (attackType == Chrysalis.CLAW)
            animationName = ChrysalisAnimationRefs.ATTACKCLAW_ANIMATION_NAME;
        else if (attackType == Chrysalis.TAIL)
            animationName = ChrysalisAnimationRefs.ATTACKTAIL_ANIMATION_NAME;
        else
            animationName = null;

        var durationInTicks = chrysalis.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(chrysalis, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

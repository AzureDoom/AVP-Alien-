package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.client.animation.entity.cocoon.CocoonAnimationStateTracker;
import com.alien.common.gameplay.entity.living.alien.xenomorph.AttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.RazorClaw;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.RazorClawAnimationRefs;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.RazorClawSweepAttack;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RazorClawAnimator extends AzEntityAnimator<RazorClaw> {

    private static final String NAME = "razor_claw";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    private final CocoonAnimationStateTracker<RazorClaw> cocoonAnimationStateTracker = new CocoonAnimationStateTracker<>();

    public RazorClawAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<RazorClaw> animationTrackContainer) {
        animationTrackContainer.add(
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.BODY_TRACK_NAME)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(RazorClaw animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(RazorClaw animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        if (cocoonAnimationStateTracker.run(animatable)) {
            return;
        }

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(RazorClaw razorClaw) {
        var dispatcher = razorClaw.getAnimationDispatcher();

        var attackType = razorClaw.attackType.get();
        var attackId = razorClaw.attackId.get();

        if (!attackType.isNone()) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(razorClaw, attackType);

                if (attackType == RazorClaw.BITE)
                    dispatcher.biteAttack(speed);
                else if (attackType == RazorClaw.CLAW)
                    dispatcher.rightClawAttack(speed);
                else if (attackType == RazorClaw.TAIL)
                    dispatcher.tailAttack(speed);
                else if (attackType == RazorClaw.SWIM_ATTACK)
                    dispatcher.swimAttack(speed);
                else if (attackType == RazorClawSweepAttack.ATTACK)
                    dispatcher.specialAttackSpin(speed);

                previousAttackId = attackId;
            }
            return;
        }

        var isMovingOnGround = razorClaw.isMovingHorizontally.get() && razorClaw.onGround();
        Runnable animFunction;

        if (razorClaw.isUnderWater()) {
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (razorClaw.hasTarget.get()) {
                animFunction = dispatcher::run;
            } else {
                animFunction = dispatcher::walk;
            }
        } else {
            animFunction = dispatcher::idle;
        }

        animFunction.run();
    }

    private float calculateAttackSpeed(RazorClaw razorClaw, AttackType attackType) {
        String animationName = null;

        if (attackType == RazorClaw.BITE)
            animationName = RazorClawAnimationRefs.ATTACK_BITE_ANIMATION_NAME;
        else if (attackType == RazorClaw.CLAW)
            animationName = RazorClawAnimationRefs.ATTACK_CLAW_ANIMATION_NAME;
        else if (attackType == RazorClaw.TAIL)
            animationName = RazorClawAnimationRefs.ATTACK_TAIL_ANIMATION_NAME;
        else if (attackType == RazorClaw.SWIM_ATTACK)
            animationName = RazorClawAnimationRefs.SWIM_ATTACK_ANIMATION_NAME;
        else if (attackType == RazorClawSweepAttack.ATTACK)
            animationName = RazorClawAnimationRefs.SPECIAL_ATTACK_SPIN_ANIMATION_NAME;

        var durationInTicks = razorClaw.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(razorClaw, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

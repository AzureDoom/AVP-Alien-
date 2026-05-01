package com.alien.client.animation.entity;

import com.alien.client.animation.entity.cocoon.CocoonAnimationStateTracker;
import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.RavagerAnimationRefs;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack.RavagerSpecialAttackConfig;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RavagerAnimator extends AzEntityAnimator<Ravager> {

    private static final String NAME = "ravager";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    private final CocoonAnimationStateTracker<Ravager> cocoonAnimationStateTracker = new CocoonAnimationStateTracker<>();

    public RavagerAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Ravager> animationTrackContainer) {
        animationTrackContainer.add(
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.BODY_TRACK_NAME)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Ravager animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Ravager animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        if (cocoonAnimationStateTracker.run(animatable)) {
            return;
        }

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Ravager ravager) {
        var dispatcher = ravager.getAnimationDispatcher();

        var attackType = ravager.attackType.get();
        var attackId = ravager.attackId.get();

        if (attackType != XenomorphAttackType.NONE) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(ravager, attackType);

                switch (attackType) {
                    case BITE -> dispatcher.biteAttack(speed);
                    case CLAW -> dispatcher.rightClawAttack(speed);
                    case CLAW_DOUBLE -> dispatcher.doubleClawAttack(speed);
                    case TAIL -> dispatcher.tailAttack(speed);
                    case SWIM_ATTACK -> dispatcher.swimAttack(speed);
                    case SPECIAL_WINDUP -> dispatcher.specialAttackWarmup(calculateWindupAnimationSpeed(ravager));
                    case SPECIAL -> dispatcher.specialAttackActivate(speed);
                }

                previousAttackId = attackId;
            }
            return;
        }

        var isMovingOnGround = ravager.isMovingHorizontally.get() && ravager.onGround();
        Runnable animFunction;

        if (ravager.isUnderWater()) {
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (ravager.hasTarget.get()) {
                animFunction = dispatcher::run;
            } else {
                animFunction = dispatcher::walk;
            }
        } else {
            animFunction = dispatcher::idle;
        }

        animFunction.run();
    }

    private float calculateAttackSpeed(Ravager ravager, XenomorphAttackType attackType) {
        var animationName = switch (attackType) {
            case BITE -> RavagerAnimationRefs.ATTACK_BITE_ANIMATION_NAME;
            case CLAW -> RavagerAnimationRefs.ATTACK_ARM_SINGLE_ANIMATION_NAME;
            case CLAW_DOUBLE -> RavagerAnimationRefs.ATTACK_ARM_DOUBLE_ANIMATION_NAME;
            case TAIL -> RavagerAnimationRefs.ATTACK_TAIL_ANIMATION_NAME;
            case SWIM_ATTACK -> RavagerAnimationRefs.SWIM_ATTACK_ANIMATION_NAME;
            case SPECIAL_WINDUP -> RavagerAnimationRefs.SPECIAL_ATTACK_WARMUP_ANIMATION_NAME;
            case SPECIAL -> RavagerAnimationRefs.SPECIAL_ATTACK_ACTIVATE_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = ravager.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(ravager, animationName);

        return (float) (animation.length() / durationInTicks);
    }

    private float calculateWindupAnimationSpeed(Ravager ravager) {
        var animation = getAnimation(ravager, RavagerAnimationRefs.SPECIAL_ATTACK_WARMUP_ANIMATION_NAME);
        return (float) (animation.length() / RavagerSpecialAttackConfig.DEFAULT.windupAnimationDurationInTicks());
    }
}

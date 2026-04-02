package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.xenomorph.QuadrupedAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.prowler.Prowler;
import com.alien.common.gameplay.entity.living.alien.xenomorph.prowler.ProwlerAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ProwlerAnimator extends AzEntityAnimator<Prowler> {

    private static final String NAME = "prowler";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private QuadrupedAttackType previousAttackType = QuadrupedAttackType.NONE;

    public ProwlerAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Prowler> animationTrackContainer) {
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
    public @NotNull ResourceLocation getAnimationLocation(Prowler animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Prowler animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Prowler prowler) {
        var dispatcher = prowler.getAnimationDispatcher();

        if (prowler.isLunging.get()) {
            dispatcher.lunge();
            return;
        }

        var attackType = prowler.attackType.get();

        if (attackType != QuadrupedAttackType.NONE) {
            if (attackType != previousAttackType) {
                var speed = calculateAttackSpeed(prowler, attackType);

                switch (attackType) {
                    case BITE -> dispatcher.biteAttack(speed);
                    case CLAW -> dispatcher.rightClawAttack(speed);
                    case TAIL_QUAD -> dispatcher.tailAttackQuad(speed);
                }

                previousAttackType = attackType;
            }
            return;
        }

        previousAttackType = QuadrupedAttackType.NONE;

        var isMovingOnGround = prowler.isMovingHorizontally.get() && prowler.onGround();
        var isCrawling = prowler.getCrawlingManager().isCrawling();
        Runnable animFunction;

        if (prowler.isUnderWater()) {
            // TODO: idle swim
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (isCrawling) {
                animFunction = dispatcher::crawl;
            } else if (prowler.hasTarget.get()) {
                animFunction = dispatcher::run;
            } else {
                animFunction = dispatcher::walk;
            }
        } else {
            // TODO: idle crawl
            animFunction = isCrawling ? dispatcher::crawlHold : dispatcher::idle;
        }

        animFunction.run();
    }

    private float calculateAttackSpeed(Prowler prowler, QuadrupedAttackType attackType) {
        var animationName = switch (attackType) {
            case BITE -> ProwlerAnimationRefs.BITEATTACK_HEAD_ANIMATION_NAME;
            case CLAW -> ProwlerAnimationRefs.CLAWATTACKQUAD_RIGHTARM_ANIMATION_NAME;
            case TAIL_QUAD -> ProwlerAnimationRefs.TAILATTACKQUAD_TAIL_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = prowler.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(prowler, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

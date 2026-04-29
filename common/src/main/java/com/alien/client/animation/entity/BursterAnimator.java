package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.burster.Burster;
import com.alien.common.gameplay.entity.living.alien.xenomorph.burster.BursterAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BursterAnimator extends AzEntityAnimator<Burster> {

    private static final String NAME = "burster";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    public BursterAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Burster> animationTrackContainer) {
        animationTrackContainer.add(
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.BODY_TRACK_NAME)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Burster animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Burster animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Burster burster) {
        var dispatcher = burster.getAnimationDispatcher();

        if (burster.isLunging.get()) {
            dispatcher.lunge();
            return;
        }

        var attackType = burster.attackType.get();
        var attackId = burster.attackId.get();

        if (attackType != XenomorphAttackType.NONE) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(burster, attackType);

                switch (attackType) {
                    case BITE -> dispatcher.biteAttack(speed);
                    case CLAW -> dispatcher.clawAttack(speed);
                    case TAIL -> dispatcher.tailAttack(speed);
                }

                previousAttackId = attackId;
            }
            return;
        }

        var isMoving = burster.isMovingHorizontally.get() && burster.onGround();
        var isCrawling = burster.getCrawlingManager().isCrawling();
        Runnable animFunction;

        if (burster.isUnderWater()) {
            animFunction = dispatcher::swim;
        } else if (isMoving) {
            if (isCrawling) {
                animFunction = dispatcher::crawl;
            } else if (burster.hasTarget.get()) {
                animFunction = dispatcher::run;
            } else {
                animFunction = dispatcher::walk;
            }
        } else {
            animFunction = isCrawling ? dispatcher::crawlHold : dispatcher::idle;
        }

        animFunction.run();
    }

    private float calculateAttackSpeed(Burster burster, XenomorphAttackType attackType) {
        var animationName = switch (attackType) {
            case BITE -> BursterAnimationRefs.FULLATTACKBITE_ANIMATION_NAME;
            case CLAW -> BursterAnimationRefs.FULLATTACKARM_ANIMATION_NAME;
            case TAIL -> BursterAnimationRefs.FULLATTACKTAIL_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = burster.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(burster, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

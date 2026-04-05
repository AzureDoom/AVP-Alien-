package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.Warrior;
import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.WarriorAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WarriorAnimator extends AzEntityAnimator<Warrior> {

    private static final String NAME = "warrior";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private XenomorphAttackType previousAttackType = XenomorphAttackType.NONE;

    public WarriorAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Warrior> animationTrackContainer) {
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
    public @NotNull ResourceLocation getAnimationLocation(Warrior animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Warrior animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Warrior warrior) {
        var dispatcher = warrior.getAnimationDispatcher();

        if (warrior.isLunging.get()) {
            dispatcher.lunge();
            return;
        }

        var attackType = warrior.attackType.get();

        if (attackType != XenomorphAttackType.NONE) {
            if (attackType != previousAttackType) {
                var speed = calculateAttackSpeed(warrior, attackType);

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

        var isClimbing = warrior.getClimbingSurfaceDirection() > 0;
        var isMoving = warrior.isMovingHorizontally.get() && (warrior.onGround() || isClimbing);
        var isCrawling = warrior.getCrawlingManager().isCrawling();
        Runnable animFunction;

        if (warrior.isUnderWater()) {
            // TODO: idle swim
            animFunction = dispatcher::swim;
        } else if (isMoving) {
            if (isCrawling) {
                animFunction = dispatcher::crawl;
            } else if (warrior.hasTarget.get()) {
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

    private float calculateAttackSpeed(Warrior warrior, XenomorphAttackType attackType) {
        var animationName = switch (attackType) {
            case BITE -> WarriorAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME;
            case CLAW -> WarriorAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME;
            case TAIL -> WarriorAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = warrior.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(warrior, animationName);
        return (float) (animation.length() / durationInTicks);
    }
}

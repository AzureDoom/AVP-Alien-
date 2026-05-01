package com.alien.client.animation.entity;

import com.alien.client.animation.entity.cocoon.CocoonAnimationStateTracker;
import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.xenomorph.empress.Empress;
import com.alien.common.gameplay.entity.living.alien.xenomorph.empress.EmpressAnimationRefs;
import com.alien.common.gameplay.entity.living.alien.xenomorph.empress.EmpressAttackType;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class EmpressAnimator extends AzEntityAnimator<Empress> {

    private static final String NAME = "empress";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    private final CocoonAnimationStateTracker<Empress> cocoonAnimationStateTracker = new CocoonAnimationStateTracker<>();

    public EmpressAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Empress> animationTrackContainer) {
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
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.LEFT_TITTY_ARM_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_LEG_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_TITTY_ARM_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.TAIL_TRACK_NAME)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Empress animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Empress animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        if (cocoonAnimationStateTracker.run(animatable)) {
            return;
        }

        runPassiveAnimations(animatable);

        var bakedModel = context().boneCache().getBakedModel();
        var eggSack = bakedModel.getBoneOrNull("root2");

        if (eggSack != null) {
            eggSack.setHidden(true);
        }
    }

    private void runPassiveAnimations(Empress empress) {
        var dispatcher = empress.getAnimationDispatcher();

        var attackType = empress.attackType.get();
        var attackId = empress.attackId.get();

        if (attackType != EmpressAttackType.NONE) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(empress, attackType);

                switch (attackType) {
                    case SWIPE_DOWN -> dispatcher.swipeDownAttack(speed);
                    case BACKHAND -> dispatcher.backhandAttack(speed);
                    case TAIL_STRIKE -> dispatcher.tailStrikeAttack(speed);
                }

                previousAttackId = attackId;
            }
            return;
        }

        var isMovingOnGround = empress.isMovingHorizontally.get() && empress.onGround();
        Runnable animFunction;

        if (empress.getEmpressOvipositorManager().hasOvipositor()) {
            animFunction = dispatcher::sitOnOvipositor;
        } else if (empress.isUnderWater()) {
            // TODO: idle swim
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (empress.hasTarget.get()) {
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

    private float calculateAttackSpeed(Empress empress, EmpressAttackType attackType) {
        var animationName = switch (attackType) {
            case SWIPE_DOWN -> EmpressAnimationRefs.SWIPEDOWN_BODY_ANIMATION_NAME;
            case BACKHAND -> EmpressAnimationRefs.BACKHAND_BODY_ANIMATION_NAME;
            case TAIL_STRIKE -> EmpressAnimationRefs.TAILSTRIKE_BODY_ANIMATION_NAME;
            default -> null;
        };

        var durationInTicks = empress.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(empress, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

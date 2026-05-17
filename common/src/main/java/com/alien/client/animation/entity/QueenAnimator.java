package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.client.animation.entity.cocoon.CocoonAnimationStateTracker;
import com.alien.common.gameplay.entity.living.alien.xenomorph.AttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.QueenAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.alien.common.util.AzAlienHeadAnimationUtil;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class QueenAnimator extends AzEntityAnimator<Queen> {

    private static final String NAME = "queen";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    private int previousAttackId = Integer.MIN_VALUE;

    private final CocoonAnimationStateTracker<Queen> cocoonAnimationStateTracker = new CocoonAnimationStateTracker<>();

    public QueenAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Queen> animationTrackContainer) {
        animationTrackContainer.add(
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.BODY)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.HEAD)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.LEFT_ARM)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.LEFT_LEG)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.LEFT_TITTY_ARM)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_ARM)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_LEG)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.RIGHT_TITTY_ARM)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, AzAlienAnimationUtil.TAIL)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Queen animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Queen animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        if (cocoonAnimationStateTracker.run(animatable)) {
            return;
        }

        AzAlienHeadAnimationUtil.applyHeadLookFromBindPose(animatable, context(), partialTicks, "gNeck");

        runPassiveAnimations(animatable);

        var bakedModel = context().boneCache().getBakedModel();
        var eggSack = bakedModel.getBoneOrNull("root2");

        if (eggSack != null) {
            eggSack.setHidden(true);
        }
    }

    private void runPassiveAnimations(Queen queen) {
        var dispatcher = queen.getAnimationDispatcher();

        var attackType = queen.attackType.get();
        var attackId = queen.attackId.get();

        if (!attackType.isNone()) {
            if (attackId != previousAttackId) {
                var speed = calculateAttackSpeed(queen, attackType);

                if (attackType == Queen.SWIPE_DOWN)
                    dispatcher.swipeDownAttack(speed);
                else if (attackType == Queen.BACKHAND)
                    dispatcher.backhandAttack(speed);
                else if (attackType == Queen.TAIL_STRIKE)
                    dispatcher.tailStrikeAttack(speed);

                previousAttackId = attackId;
            }
            return;
        }

        var isMovingOnGround = queen.isMovingHorizontally.get() && queen.onGround();
        Runnable animFunction;

        if (queen.getOvipositorManager().hasOvipositor()) {
            animFunction = dispatcher::sitOnOvipositor;
        } else if (queen.isUnderWater()) {
            animFunction = dispatcher::swim;
        } else if (isMovingOnGround) {
            if (queen.isMovingQuickly.get()) {
                animFunction = dispatcher::run;
            } else {
                animFunction = dispatcher::walk;
            }
        } else {
            animFunction = dispatcher::idle;
        }

        animFunction.run();
    }

    private float calculateAttackSpeed(Queen queen, AttackType attackType) {
        String animationName = null;

        if (attackType == Queen.SWIPE_DOWN)
            animationName = QueenAnimationRefs.SWIPEDOWN_BODY_ANIMATION_NAME;
        else if (attackType == Queen.BACKHAND)
            animationName = QueenAnimationRefs.BACKHAND_BODY_ANIMATION_NAME;
        else if (attackType == Queen.TAIL_STRIKE)
            animationName = QueenAnimationRefs.TAILSTRIKE_BODY_ANIMATION_NAME;

        var durationInTicks = queen.attackDurationInTicks.get();

        if (animationName == null || durationInTicks <= 0) {
            return 1.0f;
        }

        var animation = getAnimation(queen, animationName);

        return (float) (animation.length() / durationInTicks);
    }
}

package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.predalien_chestburster.PredalienChestburster;
import com.alien.common.gameplay.entity.living.alien.predalien_chestburster.PredalienChestbursterAnimationRefs;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PredalienChestbursterAnimator extends AzEntityAnimator<PredalienChestburster> {

    private static final String NAME = "predalien_chestburster";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    public PredalienChestbursterAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<PredalienChestburster> animationTrackContainer) {
        animationTrackContainer.add(
            AzAnimationTrack.builder(this, PredalienChestbursterAnimationRefs.HEAD_TRACK_NAME)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, PredalienChestbursterAnimationRefs.TAIL_TRACK_NAME)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(PredalienChestburster animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(PredalienChestburster animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(PredalienChestburster chestburster) {
        var dispatcher = chestburster.getAnimationDispatcher();
        var isMovingOnGround = chestburster.isMovingHorizontally.get() && chestburster.onGround();
        Runnable animFunction;

        // if (isUnderWater()) {
        // // TODO: idle swim
        // animFunction = dispatcher::swim;
        // } else

        if (isMovingOnGround) {
            animFunction = dispatcher::slowSlither;
        } else {
            animFunction = dispatcher::idle;
        }

        animFunction.run();
    }
}

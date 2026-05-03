package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.chestburster.Chestburster;
import com.alien.common.gameplay.entity.living.alien.chestburster.ChestbursterAnimationRefs;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ChestbursterAnimator extends AzEntityAnimator<Chestburster> {

    private static final String NAME = "chestburster";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    public ChestbursterAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Chestburster> animationTrackContainer) {
        animationTrackContainer.add(
            AzAnimationTrack.builder(this, ChestbursterAnimationRefs.HEAD)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, ChestbursterAnimationRefs.TAIL)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Chestburster animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Chestburster animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Chestburster chestburster) {
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

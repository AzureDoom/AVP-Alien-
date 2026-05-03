package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.FacehuggerAnimationRefs;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FacehuggerAnimator extends AzEntityAnimator<Facehugger> {

    private static final String NAME = "facehugger";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    public FacehuggerAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Facehugger> animationTrackContainer) {
        animationTrackContainer.add(
            AzAnimationTrack.builder(this, FacehuggerAnimationRefs.LEGS)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, FacehuggerAnimationRefs.LUNGS)
                .setTransitionLength(5)
                .build(),
            AzAnimationTrack.builder(this, FacehuggerAnimationRefs.TAIL)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Facehugger animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Facehugger animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        runPassiveAnimations(animatable);
    }

    private void runPassiveAnimations(Facehugger facehugger) {
        var attachmentManager = facehugger.getAttachmentManager();
        var dispatcher = facehugger.getAnimationDispatcher();

        if ((!facehugger.isFertile.get() && !attachmentManager.isAttachedToHost()) || facehugger.isDeadOrDying()) {
            dispatcher.infertile();
            return;
        }

        if (attachmentManager.isAttachedToHost() && facehugger.isAlive()) {
            dispatcher.hug();
            return;
        }

        if (facehugger.isLunging.get()) {
            dispatcher.lunge();
            return;
        }

        var isMovingOnGround = facehugger.isMovingHorizontally.get() && facehugger.onGround();

        if (facehugger.isUnderWater()) {
            // TODO: swim
        } else if (isMovingOnGround) {
            dispatcher.run();
        } else {
            dispatcher.idle();
        }
    }
}

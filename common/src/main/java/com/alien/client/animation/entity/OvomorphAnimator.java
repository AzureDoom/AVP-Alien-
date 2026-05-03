package com.alien.client.animation.entity;

import com.alien.AlienResources;
import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.gameplay.entity.living.alien.ovomorph.OvomorphAnimationRefs;
import com.blib.api.client.animation.v1.animator.AzAnimatorConfig;
import com.blib.api.client.animation.v1.animator.AzEntityAnimator;
import com.blib.api.client.animation.v1.track.AzAnimationTrack;
import com.blib.api.client.animation.v1.track.AzAnimationTrackContainer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class OvomorphAnimator extends AzEntityAnimator<Ovomorph> {

    private static final String NAME = "ovomorph";

    private static final ResourceLocation ANIMATION = AlienResources.entityAnimationLocation(NAME);

    public OvomorphAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerTracks(AzAnimationTrackContainer<Ovomorph> animationTrackContainer) {
        animationTrackContainer.add(
            AzAnimationTrack.builder(this, OvomorphAnimationRefs.BASE)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(Ovomorph animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(Ovomorph ovomorph, float partialTicks) {
        var bakedModel = context().boneCache().getBakedModel();
        var gVeinBottom = bakedModel.getBoneOrNull("gVeinBottom");

        if (gVeinBottom != null) {
            gVeinBottom.setHidden(!ovomorph.isRooted.get());
        }

        runPassiveAnimations(ovomorph);
    }

    private void runPassiveAnimations(Ovomorph ovomorph) {
        if (ovomorph.getHatchManager().isHatching()) {
            ovomorph.getAnimationDispatcher().open();
        } else if (ovomorph.getHatchManager().isHatched()) {
            ovomorph.getAnimationDispatcher().openHold();
        } else {
            ovomorph.getAnimationDispatcher().closeHold();
        }
    }
}

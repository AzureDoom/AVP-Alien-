package com.alien.client.animation.entity.cocoon;

import com.alien.common.gameplay.entity.living.alien.xenomorph.CocoonState;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

import java.util.function.Function;

public class CocoonAnimationStateTracker<T extends Xenomorph> {

    private static final String EMERGE_ANIMATION_NAME = "molt.emerge";

    private static final String COCOON_LOOP_ANIMATION_NAME = "molting";

    private static final int TRANSITION_ANIMATION_TICKS = 20;

    /**
     * Resolves the looping in-cocoon animation per entity. Defaults to the shared {@code molting}; the queen passes a
     * selector that picks a source-specific loop ({@code molting.prae} / {@code molting.crusher}).
     */
    private final Function<T, String> loopAnimationSelector;

    /**
     * Resolves the one-shot emerge animation per entity. Defaults to the shared {@code molt.emerge}; the queen passes a
     * selector that picks a source-specific emerge ({@code emerge.prae} / {@code emerge.crusher}).
     */
    private final Function<T, String> emergeAnimationSelector;

    private int previousAnimationId = Integer.MIN_VALUE;

    private CocoonState previousState = CocoonState.NONE;

    private int transitionAnimationStartTick = Integer.MIN_VALUE;

    public CocoonAnimationStateTracker() {
        this(xenomorph -> COCOON_LOOP_ANIMATION_NAME, xenomorph -> EMERGE_ANIMATION_NAME);
    }

    public CocoonAnimationStateTracker(
        Function<T, String> loopAnimationSelector,
        Function<T, String> emergeAnimationSelector
    ) {
        this.loopAnimationSelector = loopAnimationSelector;
        this.emergeAnimationSelector = emergeAnimationSelector;
    }

    public boolean run(T xenomorph) {
        var state = xenomorph.getCocoonManager().getState();
        var animationId = xenomorph.getCocoonManager().getAnimationId();

        return switch (state) {
            case NONE, PENDING -> {
                previousState = state;
                yield false;
            }
            case SOURCE_COCOONING -> {
                if (previousState != state || previousAnimationId != animationId) {
                    playEnter(xenomorph);
                    transitionAnimationStartTick = xenomorph.tickCount;
                    previousAnimationId = animationId;
                } else if (hasTransitionAnimationFinished(xenomorph)) {
                    playLoop(xenomorph);
                }

                previousState = state;
                yield true;
            }
            case DESTINATION_COCOONING -> {
                playLoop(xenomorph);
                previousState = state;
                previousAnimationId = animationId;
                yield true;
            }
            case EMERGING -> {
                if (previousState != state || previousAnimationId != animationId) {
                    playEmerge(xenomorph);
                    transitionAnimationStartTick = xenomorph.tickCount;
                    previousAnimationId = animationId;
                }

                previousState = state;
                yield true;
            }
        };
    }

    private boolean hasTransitionAnimationFinished(Xenomorph xenomorph) {
        return xenomorph.tickCount - transitionAnimationStartTick >= TRANSITION_ANIMATION_TICKS;
    }

    private static void playEnter(Xenomorph xenomorph) {
        AzCommand.<Xenomorph>replay()
            .play(AzAlienAnimationUtil.BODY, EMERGE_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setReverseAnimation(AzAlienAnimationUtil.BODY, true)
            .build()
            .dispatchForEntity(xenomorph);
    }

    private void playLoop(T xenomorph) {
        AzCommand.<Xenomorph>idempotent()
            .play(AzAlienAnimationUtil.BODY, loopAnimationSelector.apply(xenomorph), AzPlayBehaviors.LOOP)
            .build()
            .dispatchForEntity(xenomorph);
    }

    private void playEmerge(T xenomorph) {
        AzCommand.<Xenomorph>replay()
            .play(AzAlienAnimationUtil.BODY, emergeAnimationSelector.apply(xenomorph), AzPlayBehaviors.PLAY_ONCE)
            .build()
            .dispatchForEntity(xenomorph);
    }
}

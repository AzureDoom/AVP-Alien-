package com.alien.client.animation.entity.cocoon;

import com.alien.common.gameplay.entity.living.alien.xenomorph.CocoonState;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class CocoonAnimationStateTracker<T extends Xenomorph> {

    private static final String EMERGE_ANIMATION_NAME = "molt.emerge";

    private static final String COCOON_LOOP_ANIMATION_NAME = "molting";

    private static final int TRANSITION_ANIMATION_TICKS = 20;

    private int previousAnimationId = Integer.MIN_VALUE;

    private CocoonState previousState = CocoonState.NONE;

    private int transitionAnimationStartTick = Integer.MIN_VALUE;

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
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            EMERGE_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            1F,
            0F,
            0F,
            true
        ).dispatchForEntity(xenomorph);
    }

    private static void playLoop(Xenomorph xenomorph) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            COCOON_LOOP_ANIMATION_NAME,
            AzPlayBehaviors.LOOP
        ).dispatchForEntity(xenomorph);
    }

    private static void playEmerge(Xenomorph xenomorph) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            EMERGE_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE
        ).dispatchForEntity(xenomorph);
    }
}

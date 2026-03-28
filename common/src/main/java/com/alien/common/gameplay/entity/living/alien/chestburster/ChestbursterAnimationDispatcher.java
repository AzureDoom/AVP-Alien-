package com.alien.common.gameplay.entity.living.alien.chestburster;

import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class ChestbursterAnimationDispatcher {

    private static final AzCommand IDLE_HEAD = AzCommand.create(
        ChestbursterAnimationRefs.HEAD_TRACK_NAME,
        ChestbursterAnimationRefs.IDLE_HEAD_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand IDLE_TAIL = AzCommand.trackBuilder()
        .cancel(ChestbursterAnimationRefs.TAIL_TRACK_NAME)
        .build();

    private static final AzCommand SLITHER_TAIL = AzCommand.create(
        ChestbursterAnimationRefs.TAIL_TRACK_NAME,
        ChestbursterAnimationRefs.SLITHER_TAIL_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand IDLE = AzCommand.compose(IDLE_HEAD, IDLE_TAIL);

    private static final AzCommand SLOW_SLITHER = AzCommand.compose(IDLE_HEAD, SLITHER_TAIL);

    private final Chestburster chestburster;

    public ChestbursterAnimationDispatcher(Chestburster chestburster) {
        this.chestburster = chestburster;
    }

    public void idle() {
        IDLE.dispatch(chestburster);
    }

    public void slowSlither() {
        SLOW_SLITHER.dispatch(chestburster);
    }
}

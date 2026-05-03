package com.alien.common.gameplay.entity.living.alien.predalien_chestburster;

import com.alien.common.gameplay.entity.living.alien.chestburster.ChestbursterAnimationRefs;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class PredalienChestbursterAnimationDispatcher {

    private static final AzCommand<PredalienChestburster> IDLE_HEAD = AzCommand.<PredalienChestburster>idempotent()
        .play(ChestbursterAnimationRefs.HEAD, ChestbursterAnimationRefs.IDLE_HEAD_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<PredalienChestburster> IDLE_TAIL = AzCommand.<PredalienChestburster>builder()
        .cancel(ChestbursterAnimationRefs.TAIL)
        .build();

    private static final AzCommand<PredalienChestburster> SLITHER_TAIL = AzCommand.<PredalienChestburster>idempotent()
        .play(ChestbursterAnimationRefs.TAIL, ChestbursterAnimationRefs.SLITHER_TAIL_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<PredalienChestburster> IDLE = AzCommand.compose(IDLE_HEAD, IDLE_TAIL);

    private static final AzCommand<PredalienChestburster> SLOW_SLITHER = AzCommand.compose(IDLE_HEAD, SLITHER_TAIL);

    private final PredalienChestburster predalienChestburster;

    public PredalienChestbursterAnimationDispatcher(PredalienChestburster predalienChestburster) {
        this.predalienChestburster = predalienChestburster;
    }

    public void idle() {
        IDLE.dispatchForEntity(predalienChestburster);
    }

    public void slowSlither() {
        SLOW_SLITHER.dispatchForEntity(predalienChestburster);
    }
}

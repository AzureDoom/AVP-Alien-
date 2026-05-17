package com.alien.common.gameplay.entity.living.alien.ovomorph;

import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class OvomorphAnimationDispatcher {

    private static final AzCommand<Ovomorph> CLOSE_HOLD = AzCommand.<Ovomorph>idempotent()
        .play(OvomorphAnimationRefs.BASE, OvomorphAnimationRefs.CLOSE_HOLD_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Ovomorph> OPEN = AzCommand.<Ovomorph>idempotent()
        .play(OvomorphAnimationRefs.BASE, OvomorphAnimationRefs.OPEN_ANIMATION_NAME, AzPlayBehaviors.HOLD_ON_LAST_FRAME)
        .build();

    private static final AzCommand<Ovomorph> OPEN_HOLD = AzCommand.<Ovomorph>idempotent()
        .play(OvomorphAnimationRefs.BASE, OvomorphAnimationRefs.OPEN_HOLD_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private final Ovomorph ovomorph;

    public OvomorphAnimationDispatcher(Ovomorph ovomorph) {
        this.ovomorph = ovomorph;
    }

    public void closeHold() {
        CLOSE_HOLD.dispatchForEntity(ovomorph);
    }

    public void open() {
        OPEN.dispatchForEntity(ovomorph);
    }

    public void openHold() {
        OPEN_HOLD.dispatchForEntity(ovomorph);
    }
}

package com.alien.common.gameplay.entity.living.alien.parasite.facehugger;

import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class FacehuggerAnimationDispatcher {

    private static final AzCommand<Facehugger> FLAIL = AzCommand.<Facehugger>idempotent()
        .play(FacehuggerAnimationRefs.TAIL, FacehuggerAnimationRefs.TAIL_FLAIL_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Facehugger> IDLE = AzCommand.compose(
        AzCommand.<Facehugger>idempotent()
            .play(FacehuggerAnimationRefs.LEGS, FacehuggerAnimationRefs.IDLE_ANIMATION_NAME, AzPlayBehaviors.LOOP)
            .build(),
        AzCommand.<Facehugger>idempotent()
            .play(FacehuggerAnimationRefs.TAIL, FacehuggerAnimationRefs.IDLE_ANIMATION_NAME, AzPlayBehaviors.LOOP)
            .build()
    );

    private static final AzCommand<Facehugger> LUNGE = AzCommand.compose(
        AzCommand.<Facehugger>replay()
            .play(FacehuggerAnimationRefs.LEGS, FacehuggerAnimationRefs.LEAP_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .build(),
        AzCommand.<Facehugger>replay()
            .play(FacehuggerAnimationRefs.TAIL, FacehuggerAnimationRefs.LEAP_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .build()
    );

    private static final AzCommand<Facehugger> RUN = AzCommand.<Facehugger>idempotent()
        .play(FacehuggerAnimationRefs.LEGS, FacehuggerAnimationRefs.RUN_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Facehugger> BREATHE = AzCommand.<Facehugger>idempotent()
        .play(FacehuggerAnimationRefs.LUNGS, FacehuggerAnimationRefs.SACK_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Facehugger> CANCEL_BREATHING = AzCommand.<Facehugger>builder()
        .cancel(FacehuggerAnimationRefs.LUNGS)
        .build();

    private static final AzCommand<Facehugger> SWAY = AzCommand.<Facehugger>idempotent()
        .play(FacehuggerAnimationRefs.TAIL, FacehuggerAnimationRefs.TAIL_SWAY_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Facehugger> HUG = AzCommand.compose(
        AzCommand.<Facehugger>idempotent()
            .play(FacehuggerAnimationRefs.LEGS, FacehuggerAnimationRefs.FACEHUG_ANIMATION_NAME, AzPlayBehaviors.HOLD_ON_LAST_FRAME)
            .build(),
        AzCommand.<Facehugger>idempotent()
            .play(FacehuggerAnimationRefs.TAIL, FacehuggerAnimationRefs.FACEHUG_ANIMATION_NAME, AzPlayBehaviors.HOLD_ON_LAST_FRAME)
            .build(),
        BREATHE
    );

    private static final AzCommand<Facehugger> INFERTILE = AzCommand.compose(
        AzCommand.<Facehugger>idempotent()
            .play(FacehuggerAnimationRefs.LEGS, FacehuggerAnimationRefs.INFERTILE_ANIMATION_NAME, AzPlayBehaviors.HOLD_ON_LAST_FRAME)
            .build(),
        AzCommand.<Facehugger>idempotent()
            .play(FacehuggerAnimationRefs.TAIL, FacehuggerAnimationRefs.INFERTILE_ANIMATION_NAME, AzPlayBehaviors.HOLD_ON_LAST_FRAME)
            .build(),
        CANCEL_BREATHING
    );

    private static final AzCommand<Facehugger> LUNGE_AND_CANCEL_BREATHING = AzCommand.compose(LUNGE, CANCEL_BREATHING);

    private static final AzCommand<Facehugger> RUN_AND_FLAIL = AzCommand.compose(RUN, FLAIL, CANCEL_BREATHING);

    private static final AzCommand<Facehugger> IDLE_AND_SWAY = AzCommand.compose(IDLE, SWAY, CANCEL_BREATHING);

    private final Facehugger facehugger;

    public FacehuggerAnimationDispatcher(Facehugger facehugger) {
        this.facehugger = facehugger;
    }

    public void hug() {
        HUG.dispatchForEntity(facehugger);
    }

    public void idle() {
        IDLE_AND_SWAY.dispatchForEntity(facehugger);
    }

    public void lunge() {
        LUNGE_AND_CANCEL_BREATHING.dispatchForEntity(facehugger);
    }

    public void infertile() {
        INFERTILE.dispatchForEntity(facehugger);
    }

    public void run() {
        RUN_AND_FLAIL.dispatchForEntity(facehugger);
    }
}

package com.alien.common.gameplay.entity.living.alien.parasite.facehugger;

import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class FacehuggerAnimationDispatcher {

    private static final AzCommand FLAIL = AzCommand.create(
        FacehuggerAnimationRefs.TAIL_TRACK_NAME,
        FacehuggerAnimationRefs.TAIL_FLAIL_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand IDLE = AzCommand.compose(
        AzCommand.create(
            FacehuggerAnimationRefs.LEGS_TRACK_NAME,
            FacehuggerAnimationRefs.IDLE_ANIMATION_NAME,
            AzPlayBehaviors.LOOP
        ),
        AzCommand.create(
            FacehuggerAnimationRefs.TAIL_TRACK_NAME,
            FacehuggerAnimationRefs.IDLE_ANIMATION_NAME,
            AzPlayBehaviors.LOOP
        )
    );

    private static final AzCommand LUNGE = AzCommand.compose(
        AzCommand.create(
            FacehuggerAnimationRefs.LEGS_TRACK_NAME,
            FacehuggerAnimationRefs.LEAP_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE
        ),
        AzCommand.create(
            FacehuggerAnimationRefs.TAIL_TRACK_NAME,
            FacehuggerAnimationRefs.LEAP_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE
        )
    );

    private static final AzCommand RUN = AzCommand.create(
        FacehuggerAnimationRefs.LEGS_TRACK_NAME,
        FacehuggerAnimationRefs.RUN_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand BREATHE = AzCommand.create(
        FacehuggerAnimationRefs.LUNGS_TRACK_NAME,
        FacehuggerAnimationRefs.SACK_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand CANCEL_BREATHING = AzCommand.trackBuilder()
        .cancel(FacehuggerAnimationRefs.LUNGS_TRACK_NAME)
        .build();

    private static final AzCommand SWAY = AzCommand.create(
        FacehuggerAnimationRefs.TAIL_TRACK_NAME,
        FacehuggerAnimationRefs.TAIL_SWAY_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand HUG = AzCommand.compose(
        AzCommand.create(
            FacehuggerAnimationRefs.LEGS_TRACK_NAME,
            FacehuggerAnimationRefs.FACEHUG_ANIMATION_NAME,
            AzPlayBehaviors.HOLD_ON_LAST_FRAME
        ),
        AzCommand.create(
            FacehuggerAnimationRefs.TAIL_TRACK_NAME,
            FacehuggerAnimationRefs.FACEHUG_ANIMATION_NAME,
            AzPlayBehaviors.HOLD_ON_LAST_FRAME
        ),
        BREATHE
    );

    private static final AzCommand INFERTILE = AzCommand.compose(
        AzCommand.create(
            FacehuggerAnimationRefs.LEGS_TRACK_NAME,
            FacehuggerAnimationRefs.INFERTILE_ANIMATION_NAME,
            AzPlayBehaviors.HOLD_ON_LAST_FRAME
        ),
        AzCommand.create(
            FacehuggerAnimationRefs.TAIL_TRACK_NAME,
            FacehuggerAnimationRefs.INFERTILE_ANIMATION_NAME,
            AzPlayBehaviors.HOLD_ON_LAST_FRAME
        ),
        CANCEL_BREATHING
    );

    private static final AzCommand LUNGE_AND_CANCEL_BREATHING = AzCommand.compose(LUNGE, CANCEL_BREATHING);

    private static final AzCommand RUN_AND_FLAIL = AzCommand.compose(RUN, FLAIL, CANCEL_BREATHING);

    private static final AzCommand IDLE_AND_SWAY = AzCommand.compose(IDLE, SWAY, CANCEL_BREATHING);

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

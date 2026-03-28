package com.alien.common.gameplay.entity.living.alien.xenomorph.queen;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class QueenAnimationDispatcher {

    private static final AzCommand BACKHAND_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMB_NAMES,
        "backhand",
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand IDLE_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_QUEEN_LIMB_NAMES, "idle");

    private static final AzCommand RUN_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_QUEEN_LIMB_NAMES, "run");

    private static final AzCommand SIT_ON_OVIPOSITOR_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMB_NAMES,
        "rideeggsack"
    );

    private static final AzCommand SWIM_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_QUEEN_LIMB_NAMES, "swim");

    private static final AzCommand SWIPEDOWN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMB_NAMES,
        "swipedown",
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILSTRIKE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMB_NAMES,
        "tailstrike",
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand WALK_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_QUEEN_LIMB_NAMES, "walk");

    private final Queen queen;

    public QueenAnimationDispatcher(Queen queen) {
        this.queen = queen;
    }

    public void idle() {
        IDLE_ALL.dispatch(queen);
    }

    public void run() {
        RUN_ALL.dispatch(queen);
    }

    public void sitOnOvipositor() {
        SIT_ON_OVIPOSITOR_ALL.dispatch(queen);
    }

    public void swim() {
        SWIM_ALL.dispatch(queen);
    }

    public void walk() {
        WALK_ALL.dispatch(queen);
    }

    public void backhandAttack() {
        BACKHAND_ALL.dispatch(queen);
    }

    public void swipeDownAttack() {
        SWIPEDOWN_ALL.dispatch(queen);
    }

    public void tailStrikeAttack() {
        TAILSTRIKE_ALL.dispatch(queen);
    }
}

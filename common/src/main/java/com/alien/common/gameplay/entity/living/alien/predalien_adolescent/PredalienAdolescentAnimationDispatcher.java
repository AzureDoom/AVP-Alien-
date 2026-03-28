package com.alien.common.gameplay.entity.living.alien.predalien_adolescent;

import com.alien.common.gameplay.entity.living.alien.adolescent.AdolescentAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class PredalienAdolescentAnimationDispatcher {

    private static final AzCommand ARMATTACK_RIGHTARM = AzCommand.create(
        AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
        AdolescentAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK_HEAD = AzCommand.create(
        AzAlienAnimationUtil.HEAD_TRACK_NAME,
        AdolescentAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand CRAWL_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "crawl");

    private static final AzCommand CRAWL_ALL_HOLD = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMB_NAMES,
        "crawl",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private static final AzCommand IDLE_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "idle");

    private static final AzCommand LUNGE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMB_NAMES,
        "lunge",
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand RUN_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "sprint");

    private static final AzCommand SWIM_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "swim");

    private static final AzCommand WALK_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "walk");

    private final PredalienAdolescent predalienAdolescent;

    public PredalienAdolescentAnimationDispatcher(PredalienAdolescent predalienAdolescent) {
        this.predalienAdolescent = predalienAdolescent;
    }

    public void crawl() {
        CRAWL_ALL.dispatch(predalienAdolescent);
    }

    public void crawlHold() {
        CRAWL_ALL_HOLD.dispatch(predalienAdolescent);
    }

    public void idle() {
        IDLE_ALL.dispatch(predalienAdolescent);
    }

    public void lunge() {
        LUNGE_ALL.dispatch(predalienAdolescent);
    }

    public void run() {
        RUN_ALL.dispatch(predalienAdolescent);
    }

    public void swim() {
        SWIM_ALL.dispatch(predalienAdolescent);
    }

    public void walk() {
        WALK_ALL.dispatch(predalienAdolescent);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatch(predalienAdolescent);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatch(predalienAdolescent);
    }
}

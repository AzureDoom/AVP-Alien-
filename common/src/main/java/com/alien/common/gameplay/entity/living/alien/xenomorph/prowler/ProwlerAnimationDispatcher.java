package com.alien.common.gameplay.entity.living.alien.xenomorph.prowler;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class ProwlerAnimationDispatcher {

    private static final AzCommand CLAWATTACKQUAD_RIGHTARM = AzCommand.create(
        AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
        ProwlerAnimationRefs.CLAWATTACKQUAD_RIGHTARM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK_HEAD = AzCommand.create(
        AzAlienAnimationUtil.HEAD_TRACK_NAME,
        ProwlerAnimationRefs.BITEATTACK_HEAD_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILATTACKQUAD_TAIL = AzCommand.create(
        AzAlienAnimationUtil.TAIL_TRACK_NAME,
        ProwlerAnimationRefs.TAILATTACKQUAD_TAIL_ANIMATION_NAME,
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

    private final Prowler prowler;

    public ProwlerAnimationDispatcher(Prowler prowler) {
        this.prowler = prowler;
    }

    public void crawl() {
        CRAWL_ALL.dispatchForEntity(prowler);
    }

    public void crawlHold() {
        CRAWL_ALL_HOLD.dispatchForEntity(prowler);
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(prowler);
    }

    public void lunge() {
        LUNGE_ALL.dispatchForEntity(prowler);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(prowler);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(prowler);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(prowler);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(prowler);
    }

    public void rightClawAttack() {
        CLAWATTACKQUAD_RIGHTARM.dispatchForEntity(prowler);
    }

    public void tailAttackQuad() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(prowler);
    }
}

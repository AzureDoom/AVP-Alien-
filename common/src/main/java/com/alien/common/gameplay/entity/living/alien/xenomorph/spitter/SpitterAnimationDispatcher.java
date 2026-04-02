package com.alien.common.gameplay.entity.living.alien.xenomorph.spitter;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class SpitterAnimationDispatcher {

    private static final AzCommand ATTACKCLAW_RIGHTARM = AzCommand.create(
        AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
        SpitterAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand ATTACKCLAWQUAD_RIGHTARM = AzCommand.create(
        AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
        SpitterAnimationRefs.ATTACKCLAWQUAD_RIGHTARM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK_HEAD = AzCommand.create(
        AzAlienAnimationUtil.HEAD_TRACK_NAME,
        SpitterAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILATTACKQUAD_TAIL = AzCommand.create(
        AzAlienAnimationUtil.TAIL_TRACK_NAME,
        SpitterAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME,
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

    private final Spitter spitter;

    public SpitterAnimationDispatcher(Spitter spitter) {
        this.spitter = spitter;
    }

    public void crawl() {
        CRAWL_ALL.dispatchForEntity(spitter);
    }

    public void crawlHold() {
        CRAWL_ALL_HOLD.dispatchForEntity(spitter);
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(spitter);
    }

    public void lunge() {
        LUNGE_ALL.dispatchForEntity(spitter);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(spitter);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(spitter);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(spitter);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(spitter);
    }

    public void rightClawAttack() {
        ATTACKCLAW_RIGHTARM.dispatchForEntity(spitter);
    }

    public void rightClawAttackQuad() {
        ATTACKCLAWQUAD_RIGHTARM.dispatchForEntity(spitter);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(spitter);
    }
}

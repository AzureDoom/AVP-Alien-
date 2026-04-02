package com.alien.common.gameplay.entity.living.alien.xenomorph.runner;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class RunnerAnimationDispatcher {

    private static final AzCommand ARMATTACK_RIGHTARM = AzCommand.create(
        AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
        RunnerAnimationRefs.ATTACKCLAWQUAD_RIGHTARM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK_HEAD = AzCommand.create(
        AzAlienAnimationUtil.HEAD_TRACK_NAME,
        RunnerAnimationRefs.BITEATTACK_HEAD_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILATTACKQUAD_TAIL = AzCommand.create(
        AzAlienAnimationUtil.TAIL_TRACK_NAME,
        RunnerAnimationRefs.TAILATTACKQUAD_TAIL_ANIMATION_NAME,
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

    private final Runner runner;

    public RunnerAnimationDispatcher(Runner runner) {
        this.runner = runner;
    }

    public void crawl() {
        CRAWL_ALL.dispatchForEntity(runner);
    }

    public void crawlHold() {
        CRAWL_ALL_HOLD.dispatchForEntity(runner);
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(runner);
    }

    public void lunge() {
        LUNGE_ALL.dispatchForEntity(runner);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(runner);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(runner);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(runner);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(runner);
    }

    public void biteAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.HEAD_TRACK_NAME,
            RunnerAnimationRefs.BITEATTACK_HEAD_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F, speed, 0F, 0F, false
        ).dispatchForEntity(runner);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(runner);
    }

    public void rightClawAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
            RunnerAnimationRefs.ATTACKCLAWQUAD_RIGHTARM_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F, speed, 0F, 0F, false
        ).dispatchForEntity(runner);
    }

    public void tailAttackQuad() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(runner);
    }

    public void tailAttackQuad(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.TAIL_TRACK_NAME,
            RunnerAnimationRefs.TAILATTACKQUAD_TAIL_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F, speed, 0F, 0F, false
        ).dispatchForEntity(runner);
    }
}

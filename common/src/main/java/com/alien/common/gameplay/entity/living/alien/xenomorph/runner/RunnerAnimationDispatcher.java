package com.alien.common.gameplay.entity.living.alien.xenomorph.runner;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class RunnerAnimationDispatcher {

    private static final AzCommand<Runner> ARMATTACK_RIGHTARM = AzCommand.<Runner>replay()
        .play(AzAlienAnimationUtil.RIGHT_ARM, RunnerAnimationRefs.ATTACKCLAWQUAD_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Runner> BITEATTACK_HEAD = AzCommand.<Runner>replay()
        .play(AzAlienAnimationUtil.HEAD, RunnerAnimationRefs.BITEATTACK_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Runner> TAILATTACKQUAD_TAIL = AzCommand.<Runner>replay()
        .play(AzAlienAnimationUtil.TAIL, RunnerAnimationRefs.TAILATTACKQUAD_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Runner> CRAWL_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Runner> CRAWL_ALL_HOLD = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Runner> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Runner> LUNGE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "lunge",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Runner> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "sprint",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Runner> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Runner> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

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
        AzCommand.<Runner>replay()
            .play(AzAlienAnimationUtil.HEAD, RunnerAnimationRefs.BITEATTACK_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.HEAD, speed)
            .build()
            .dispatchForEntity(runner);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(runner);
    }

    public void rightClawAttack(float speed) {
        AzCommand.<Runner>replay()
            .play(AzAlienAnimationUtil.RIGHT_ARM, RunnerAnimationRefs.ATTACKCLAWQUAD_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.RIGHT_ARM, speed)
            .build()
            .dispatchForEntity(runner);
    }

    public void tailAttackQuad() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(runner);
    }

    public void tailAttackQuad(float speed) {
        AzCommand.<Runner>replay()
            .play(AzAlienAnimationUtil.TAIL, RunnerAnimationRefs.TAILATTACKQUAD_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.TAIL, speed)
            .build()
            .dispatchForEntity(runner);
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.spitter;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class SpitterAnimationDispatcher {

    private static final AzCommand<Spitter> ATTACKCLAW_RIGHTARM = AzCommand.<Spitter>replay()
        .play(AzAlienAnimationUtil.RIGHT_ARM, SpitterAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Spitter> ATTACKCLAWQUAD_RIGHTARM = AzCommand.<Spitter>replay()
        .play(AzAlienAnimationUtil.RIGHT_ARM, SpitterAnimationRefs.ATTACKCLAWQUAD_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Spitter> BITEATTACK_HEAD = AzCommand.<Spitter>replay()
        .play(AzAlienAnimationUtil.HEAD, SpitterAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Spitter> TAILATTACKQUAD_TAIL = AzCommand.<Spitter>replay()
        .play(AzAlienAnimationUtil.TAIL, SpitterAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Spitter> CRAWL_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Spitter> CRAWL_ALL_HOLD = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Spitter> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Spitter> LUNGE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "lunge",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Spitter> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "sprint",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Spitter> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Spitter> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

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

    public void biteAttack(float speed) {
        AzCommand.<Spitter>replay()
            .play(AzAlienAnimationUtil.HEAD, SpitterAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.HEAD, speed)
            .build()
            .dispatchForEntity(spitter);
    }

    public void rightClawAttack() {
        ATTACKCLAW_RIGHTARM.dispatchForEntity(spitter);
    }

    public void rightClawAttack(float speed) {
        AzCommand.<Spitter>replay()
            .play(AzAlienAnimationUtil.RIGHT_ARM, SpitterAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.RIGHT_ARM, speed)
            .build()
            .dispatchForEntity(spitter);
    }

    public void rightClawAttackQuad() {
        ATTACKCLAWQUAD_RIGHTARM.dispatchForEntity(spitter);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(spitter);
    }

    public void tailAttack(float speed) {
        AzCommand.<Spitter>replay()
            .play(AzAlienAnimationUtil.TAIL, SpitterAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.TAIL, speed)
            .build()
            .dispatchForEntity(spitter);
    }
}

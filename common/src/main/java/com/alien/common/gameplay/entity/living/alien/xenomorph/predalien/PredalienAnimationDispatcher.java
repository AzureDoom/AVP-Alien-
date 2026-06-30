package com.alien.common.gameplay.entity.living.alien.xenomorph.predalien;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class PredalienAnimationDispatcher {

    private static final AzCommand<Predalien> ARMATTACK_RIGHTARM = AzCommand.<Predalien>replay()
        .play(AzAlienAnimationUtil.RIGHT_ARM, PredalienAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Predalien> BITEATTACK_HEAD = AzCommand.<Predalien>replay()
        .play(AzAlienAnimationUtil.HEAD, PredalienAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Predalien> TAILATTACKQUAD_TAIL = AzCommand.<Predalien>replay()
        .play(AzAlienAnimationUtil.TAIL, PredalienAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Predalien> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Predalien> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "run",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Predalien> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Predalien> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Predalien> CRAWL_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Predalien> CRAWL_ALL_HOLD = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private final Predalien predalien;

    public PredalienAnimationDispatcher(Predalien predalien) {
        this.predalien = predalien;
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(predalien);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(predalien);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(predalien);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(predalien);
    }

    public void crawl() {
        CRAWL_ALL.dispatchForEntity(predalien);
    }

    public void crawl(float speed) {
        AzAlienAnimationUtil.composeWithSpeed(
            AzAlienAnimationUtil.XENO_LIMBS,
            "swim",
            AzPlayBehaviors.LOOP,
            AzDispatchMode.PLAY_IF_NOT_PLAYING,
            speed
        ).dispatchForEntity(predalien);
    }

    public void crawlHold() {
        CRAWL_ALL_HOLD.dispatchForEntity(predalien);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(predalien);
    }

    public void biteAttack(float speed) {
        AzCommand.<Predalien>replay()
            .play(AzAlienAnimationUtil.HEAD, PredalienAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.HEAD, speed)
            .build()
            .dispatchForEntity(predalien);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(predalien);
    }

    public void rightClawAttack(float speed) {
        AzCommand.<Predalien>replay()
            .play(AzAlienAnimationUtil.RIGHT_ARM, PredalienAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.RIGHT_ARM, speed)
            .build()
            .dispatchForEntity(predalien);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(predalien);
    }

    public void tailAttack(float speed) {
        AzCommand.<Predalien>replay()
            .play(AzAlienAnimationUtil.TAIL, PredalienAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.TAIL, speed)
            .build()
            .dispatchForEntity(predalien);
    }
}

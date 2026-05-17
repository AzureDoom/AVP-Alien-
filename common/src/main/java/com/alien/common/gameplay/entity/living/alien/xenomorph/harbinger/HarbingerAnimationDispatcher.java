package com.alien.common.gameplay.entity.living.alien.xenomorph.harbinger;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class HarbingerAnimationDispatcher {

    private static final AzCommand<Harbinger> ARMATTACK_RIGHTARM = AzCommand.<Harbinger>replay()
        .play(AzAlienAnimationUtil.RIGHT_ARM, HarbingerAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Harbinger> BITEATTACK_HEAD = AzCommand.<Harbinger>replay()
        .play(AzAlienAnimationUtil.HEAD, HarbingerAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Harbinger> TAILATTACKQUAD_TAIL = AzCommand.<Harbinger>replay()
        .play(AzAlienAnimationUtil.TAIL, HarbingerAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Harbinger> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Harbinger> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "run",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Harbinger> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Harbinger> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private final Harbinger harbinger;

    public HarbingerAnimationDispatcher(Harbinger harbinger) {
        this.harbinger = harbinger;
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(harbinger);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(harbinger);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(harbinger);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(harbinger);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(harbinger);
    }

    public void biteAttack(float speed) {
        AzCommand.<Harbinger>replay()
            .play(AzAlienAnimationUtil.HEAD, HarbingerAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.HEAD, speed)
            .build()
            .dispatchForEntity(harbinger);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(harbinger);
    }

    public void rightClawAttack(float speed) {
        AzCommand.<Harbinger>replay()
            .play(AzAlienAnimationUtil.RIGHT_ARM, HarbingerAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.RIGHT_ARM, speed)
            .build()
            .dispatchForEntity(harbinger);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(harbinger);
    }

    public void tailAttack(float speed) {
        AzCommand.<Harbinger>replay()
            .play(AzAlienAnimationUtil.TAIL, HarbingerAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.TAIL, speed)
            .build()
            .dispatchForEntity(harbinger);
    }
}

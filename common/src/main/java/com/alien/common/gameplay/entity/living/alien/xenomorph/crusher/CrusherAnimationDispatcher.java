package com.alien.common.gameplay.entity.living.alien.xenomorph.crusher;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class CrusherAnimationDispatcher {

    private static final AzCommand<Crusher> BITEATTACK_HEAD = AzCommand.<Crusher>replay()
        .play(AzAlienAnimationUtil.HEAD, CrusherAnimationRefs.BITEATTACK_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Crusher> IDLE_TAIL = AzCommand.<Crusher>idempotent()
        .play(AzAlienAnimationUtil.TAIL, CrusherAnimationRefs.IDLE_TAIL_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Crusher> RUN_TAIL_PLAY_ONCE = AzCommand.<Crusher>replay()
        .play(AzAlienAnimationUtil.TAIL, CrusherAnimationRefs.RUN_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Crusher> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Crusher> LEAP_ALL = AzCommand.compose(
        AzAnimationUtil.compose(
            AzAlienAnimationUtil.XENO_LIMBS.stream().filter(handle -> handle != AzAlienAnimationUtil.TAIL).toList(),
            "leap",
            AzPlayBehaviors.PLAY_ONCE,
            AzDispatchMode.REPLAY
        ),
        RUN_TAIL_PLAY_ONCE
    );

    private static final AzCommand<Crusher> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "run",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Crusher> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Crusher> TAILATTACK_TAIL = AzCommand.<Crusher>replay()
        .play(AzAlienAnimationUtil.TAIL, CrusherAnimationRefs.TAILATTACK_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Crusher> WALK_ALL = AzCommand.compose(
        AzAnimationUtil.compose(
            AzAlienAnimationUtil.XENO_LIMBS.stream().filter(handle -> handle != AzAlienAnimationUtil.TAIL).toList(),
            "walk",
            AzPlayBehaviors.LOOP,
            AzDispatchMode.PLAY_IF_NOT_PLAYING
        ),
        IDLE_TAIL
    );

    private final Crusher crusher;

    public CrusherAnimationDispatcher(Crusher crusher) {
        this.crusher = crusher;
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(crusher);
    }

    public void biteAttack(float speed) {
        AzCommand.<Crusher>replay()
            .play(AzAlienAnimationUtil.HEAD, CrusherAnimationRefs.BITEATTACK_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.HEAD, speed)
            .build()
            .dispatchForEntity(crusher);
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(crusher);
    }

    public void lunge() {
        LEAP_ALL.dispatchForEntity(crusher);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(crusher);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(crusher);
    }

    public void tailAttack() {
        TAILATTACK_TAIL.dispatchForEntity(crusher);
    }

    public void tailAttack(float speed) {
        AzCommand.<Crusher>replay()
            .play(AzAlienAnimationUtil.TAIL, CrusherAnimationRefs.TAILATTACK_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.TAIL, speed)
            .build()
            .dispatchForEntity(crusher);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(crusher);
    }
}

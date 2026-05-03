package com.alien.common.gameplay.entity.living.alien.xenomorph.praetorian;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class PraetorianAnimationDispatcher {

    private static final AzCommand<Praetorian> ARMATTACK_RIGHTARM = AzCommand.<Praetorian>replay()
        .play(AzAlienAnimationUtil.RIGHT_ARM, PraetorianAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Praetorian> BITEATTACK_HEAD = AzCommand.<Praetorian>replay()
        .play(AzAlienAnimationUtil.HEAD, PraetorianAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Praetorian> TAILATTACKQUAD_TAIL = AzCommand.<Praetorian>replay()
        .play(AzAlienAnimationUtil.TAIL, PraetorianAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Praetorian> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Praetorian> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "run",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Praetorian> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Praetorian> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private final Praetorian praetorian;

    public PraetorianAnimationDispatcher(Praetorian praetorian) {
        this.praetorian = praetorian;
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(praetorian);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(praetorian);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(praetorian);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(praetorian);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(praetorian);
    }

    public void biteAttack(float speed) {
        AzCommand.<Praetorian>replay()
            .play(AzAlienAnimationUtil.HEAD, PraetorianAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.HEAD, speed)
            .build()
            .dispatchForEntity(praetorian);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(praetorian);
    }

    public void rightClawAttack(float speed) {
        AzCommand.<Praetorian>replay()
            .play(AzAlienAnimationUtil.RIGHT_ARM, PraetorianAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.RIGHT_ARM, speed)
            .build()
            .dispatchForEntity(praetorian);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(praetorian);
    }

    public void tailAttack(float speed) {
        AzCommand.<Praetorian>replay()
            .play(AzAlienAnimationUtil.TAIL, PraetorianAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.TAIL, speed)
            .build()
            .dispatchForEntity(praetorian);
    }
}

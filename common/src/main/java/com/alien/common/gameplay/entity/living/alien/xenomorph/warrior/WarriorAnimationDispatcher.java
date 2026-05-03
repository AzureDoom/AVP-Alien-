package com.alien.common.gameplay.entity.living.alien.xenomorph.warrior;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class WarriorAnimationDispatcher {

    private static final AzCommand<Warrior> ARMATTACK_RIGHTARM = AzCommand.<Warrior>replay()
        .play(AzAlienAnimationUtil.RIGHT_ARM, WarriorAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Warrior> BITEATTACK_HEAD = AzCommand.<Warrior>replay()
        .play(AzAlienAnimationUtil.HEAD, WarriorAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Warrior> TAILATTACKQUAD_TAIL = AzCommand.<Warrior>replay()
        .play(AzAlienAnimationUtil.TAIL, WarriorAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Warrior> CRAWL_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Warrior> CRAWL_ALL_HOLD = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Warrior> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Warrior> LUNGE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "lunge",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Warrior> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "run",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Warrior> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Warrior> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private final Warrior warrior;

    public WarriorAnimationDispatcher(Warrior warrior) {
        this.warrior = warrior;
    }

    public void crawl() {
        CRAWL_ALL.dispatchForEntity(warrior);
    }

    public void crawlHold() {
        CRAWL_ALL_HOLD.dispatchForEntity(warrior);
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(warrior);
    }

    public void lunge() {
        LUNGE_ALL.dispatchForEntity(warrior);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(warrior);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(warrior);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(warrior);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(warrior);
    }

    public void biteAttack(float speed) {
        AzCommand.<Warrior>replay()
            .play(AzAlienAnimationUtil.HEAD, WarriorAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.HEAD, speed)
            .build()
            .dispatchForEntity(warrior);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(warrior);
    }

    public void rightClawAttack(float speed) {
        AzCommand.<Warrior>replay()
            .play(AzAlienAnimationUtil.RIGHT_ARM, WarriorAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.RIGHT_ARM, speed)
            .build()
            .dispatchForEntity(warrior);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(warrior);
    }

    public void tailAttack(float speed) {
        AzCommand.<Warrior>replay()
            .play(AzAlienAnimationUtil.TAIL, WarriorAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.TAIL, speed)
            .build()
            .dispatchForEntity(warrior);
    }
}

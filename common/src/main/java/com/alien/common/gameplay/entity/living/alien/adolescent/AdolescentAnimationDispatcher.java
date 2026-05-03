package com.alien.common.gameplay.entity.living.alien.adolescent;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class AdolescentAnimationDispatcher {

    private static final AzCommand<Adolescent> ARMATTACK_RIGHTARM = AzCommand.<Adolescent>replay()
        .play(AzAlienAnimationUtil.RIGHT_ARM, AdolescentAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Adolescent> BITEATTACK_HEAD = AzCommand.<Adolescent>replay()
        .play(AzAlienAnimationUtil.HEAD, AdolescentAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Adolescent> CRAWL_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Adolescent> CRAWL_ALL_HOLD = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Adolescent> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Adolescent> LUNGE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "lunge",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Adolescent> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "sprint",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Adolescent> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Adolescent> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private final Adolescent adolescent;

    public AdolescentAnimationDispatcher(Adolescent adolescent) {
        this.adolescent = adolescent;
    }

    public void crawl() {
        CRAWL_ALL.dispatchForEntity(adolescent);
    }

    public void crawlHold() {
        CRAWL_ALL_HOLD.dispatchForEntity(adolescent);
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(adolescent);
    }

    public void lunge() {
        LUNGE_ALL.dispatchForEntity(adolescent);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(adolescent);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(adolescent);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(adolescent);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(adolescent);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(adolescent);
    }
}

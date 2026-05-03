package com.alien.common.gameplay.entity.living.alien.predalien_adolescent;

import com.alien.common.gameplay.entity.living.alien.adolescent.AdolescentAnimationRefs;
import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class PredalienAdolescentAnimationDispatcher {

    private static final AzCommand<PredalienAdolescent> ARMATTACK_RIGHTARM = AzCommand.<PredalienAdolescent>replay()
        .play(AzAlienAnimationUtil.RIGHT_ARM, AdolescentAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<PredalienAdolescent> BITEATTACK_HEAD = AzCommand.<PredalienAdolescent>replay()
        .play(AzAlienAnimationUtil.HEAD, AdolescentAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<PredalienAdolescent> CRAWL_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<PredalienAdolescent> CRAWL_ALL_HOLD = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<PredalienAdolescent> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<PredalienAdolescent> LUNGE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "lunge",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<PredalienAdolescent> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "sprint",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<PredalienAdolescent> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<PredalienAdolescent> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private final PredalienAdolescent predalienAdolescent;

    public PredalienAdolescentAnimationDispatcher(PredalienAdolescent predalienAdolescent) {
        this.predalienAdolescent = predalienAdolescent;
    }

    public void crawl() {
        CRAWL_ALL.dispatchForEntity(predalienAdolescent);
    }

    public void crawlHold() {
        CRAWL_ALL_HOLD.dispatchForEntity(predalienAdolescent);
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(predalienAdolescent);
    }

    public void lunge() {
        LUNGE_ALL.dispatchForEntity(predalienAdolescent);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(predalienAdolescent);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(predalienAdolescent);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(predalienAdolescent);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(predalienAdolescent);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(predalienAdolescent);
    }
}

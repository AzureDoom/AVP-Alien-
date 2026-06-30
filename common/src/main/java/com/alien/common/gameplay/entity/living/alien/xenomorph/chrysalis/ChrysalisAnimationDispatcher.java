package com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class ChrysalisAnimationDispatcher {

    private static final AzCommand<Chrysalis> ARMATTACK = AzCommand.<Chrysalis>replay()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ATTACKCLAW_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Chrysalis> BITEATTACK = AzCommand.<Chrysalis>replay()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ATTACKBITE_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Chrysalis> TAILATTACK = AzCommand.<Chrysalis>replay()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ATTACKTAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Chrysalis> IDLE = AzCommand.<Chrysalis>idempotent()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.IDLE_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Chrysalis> RUN = AzCommand.<Chrysalis>idempotent()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.RUN_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Chrysalis> SWIM = AzCommand.<Chrysalis>idempotent()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.SWIM_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Chrysalis> WALK = AzCommand.<Chrysalis>idempotent()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.WALK_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Chrysalis> CRAWL = AzCommand.<Chrysalis>idempotent()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.CRAWL_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Chrysalis> CRAWL_HOLD = AzCommand.<Chrysalis>idempotent()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.CRAWL_ANIMATION_NAME, AzPlayBehaviors.HOLD_ON_LAST_FRAME)
        .build();

    private static final AzCommand<Chrysalis> ROLL_START = AzCommand.<Chrysalis>replay()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ROLL_START_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Chrysalis> ROLL_LOOP = AzCommand.<Chrysalis>idempotent()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ROLL_LOOP_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Chrysalis> ROLL_STOP = AzCommand.<Chrysalis>replay()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ROLL_STOP_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Chrysalis> ROLL_SMASHED = AzCommand.<Chrysalis>idempotent()
        .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ROLL_SMASHED_ANIMATION_NAME, AzPlayBehaviors.HOLD_ON_LAST_FRAME)
        .build();

    private final Chrysalis chrysalis;

    public ChrysalisAnimationDispatcher(Chrysalis chrysalis) {
        this.chrysalis = chrysalis;
    }

    public void idle() {
        IDLE.dispatchForEntity(chrysalis);
    }

    public void run() {
        RUN.dispatchForEntity(chrysalis);
    }

    public void swim() {
        SWIM.dispatchForEntity(chrysalis);
    }

    public void walk() {
        WALK.dispatchForEntity(chrysalis);
    }

    public void crawl() {
        CRAWL.dispatchForEntity(chrysalis);
    }

    public void crawl(float speed) {
        AzAlienAnimationUtil.singleWithSpeed(
            AzAlienAnimationUtil.BODY,
            ChrysalisAnimationRefs.CRAWL_ANIMATION_NAME,
            AzPlayBehaviors.LOOP,
            AzDispatchMode.PLAY_IF_NOT_PLAYING,
            speed
        ).dispatchForEntity(chrysalis);
    }

    public void crawlHold() {
        CRAWL_HOLD.dispatchForEntity(chrysalis);
    }

    public void rollStart() {
        ROLL_START.dispatchForEntity(chrysalis);
    }

    public void rollLoop() {
        ROLL_LOOP.dispatchForEntity(chrysalis);
    }

    public void rollStop() {
        ROLL_STOP.dispatchForEntity(chrysalis);
    }

    public void rollSmashed() {
        ROLL_SMASHED.dispatchForEntity(chrysalis);
    }

    public void rollSmashed(float speed) {
        AzCommand.<Chrysalis>idempotent()
            .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ROLL_SMASHED_ANIMATION_NAME, AzPlayBehaviors.HOLD_ON_LAST_FRAME)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(chrysalis);
    }

    public void biteAttack() {
        BITEATTACK.dispatchForEntity(chrysalis);
    }

    public void biteAttack(float speed) {
        AzCommand.<Chrysalis>replay()
            .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ATTACKBITE_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(chrysalis);
    }

    public void rightClawAttack() {
        ARMATTACK.dispatchForEntity(chrysalis);
    }

    public void rightClawAttack(float speed) {
        AzCommand.<Chrysalis>replay()
            .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ATTACKCLAW_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(chrysalis);
    }

    public void tailAttack() {
        TAILATTACK.dispatchForEntity(chrysalis);
    }

    public void tailAttack(float speed) {
        AzCommand.<Chrysalis>replay()
            .play(AzAlienAnimationUtil.BODY, ChrysalisAnimationRefs.ATTACKTAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(chrysalis);
    }
}

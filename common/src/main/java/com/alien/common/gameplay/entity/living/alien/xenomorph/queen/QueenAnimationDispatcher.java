package com.alien.common.gameplay.entity.living.alien.xenomorph.queen;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class QueenAnimationDispatcher {

    private static final AzCommand<Queen> IDLE = AzCommand.<Queen>idempotent()
        .play(AzAlienAnimationUtil.BODY, QueenAnimationRefs.IDLE_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Queen> HIBERNATE = AzCommand.<Queen>idempotent()
        .play(AzAlienAnimationUtil.BODY, QueenAnimationRefs.HIBERNATE_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Queen> INCAPACITATED = AzCommand.<Queen>idempotent()
        .play(AzAlienAnimationUtil.BODY, QueenAnimationRefs.INCAPACITATED_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Queen> CRAWL = AzCommand.<Queen>idempotent()
        .play(AzAlienAnimationUtil.BODY, QueenAnimationRefs.CRAWL_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Queen> CRAWL_IDLE = AzCommand.<Queen>idempotent()
        .play(AzAlienAnimationUtil.BODY, QueenAnimationRefs.CRAWL_IDLE_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Queen> RUN = AzCommand.<Queen>idempotent()
        .play(AzAlienAnimationUtil.BODY, QueenAnimationRefs.RUN_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Queen> SIT_ON_OVIPOSITOR = AzCommand.<Queen>idempotent()
        .play(AzAlienAnimationUtil.BODY, QueenAnimationRefs.RIDE_EGG_SACK_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Queen> SWIM = AzCommand.<Queen>idempotent()
        .play(AzAlienAnimationUtil.BODY, QueenAnimationRefs.SWIM_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Queen> WALK = AzCommand.<Queen>idempotent()
        .play(AzAlienAnimationUtil.BODY, QueenAnimationRefs.WALK_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private final Queen queen;

    public QueenAnimationDispatcher(Queen queen) {
        this.queen = queen;
    }

    public void idle() {
        IDLE.dispatchForEntity(queen);
    }

    /** Voluntary curled sleep during the hibernation phase (Stage 3). Looping; driven while she sleeps. */
    public void hibernate() {
        HIBERNATE.dispatchForEntity(queen);
    }

    /** Involuntary defeat collapse when downed/captured. Looping; driven while she is incapacitated. */
    public void incapacitated() {
        INCAPACITATED.dispatchForEntity(queen);
    }

    public void crawl() {
        CRAWL.dispatchForEntity(queen);
    }

    public void crawl(float speed) {
        AzAlienAnimationUtil.singleWithSpeed(
            AzAlienAnimationUtil.BODY,
            QueenAnimationRefs.CRAWL_ANIMATION_NAME,
            AzPlayBehaviors.LOOP,
            AzDispatchMode.PLAY_IF_NOT_PLAYING,
            speed
        ).dispatchForEntity(queen);
    }

    public void crawlIdle() {
        CRAWL_IDLE.dispatchForEntity(queen);
    }

    public void run() {
        RUN.dispatchForEntity(queen);
    }

    public void sitOnOvipositor() {
        SIT_ON_OVIPOSITOR.dispatchForEntity(queen);
    }

    public void swim() {
        SWIM.dispatchForEntity(queen);
    }

    public void walk() {
        WALK.dispatchForEntity(queen);
    }

    public void backhandAttack() {
        playAttack(QueenAnimationRefs.RIGHT_BACKHAND_ANIMATION_NAME);
    }

    public void backhandAttack(float speed) {
        backhandAttack(QueenAnimationRefs.RIGHT_BACKHAND_ANIMATION_NAME, speed);
    }

    public void backhandAttack(String animationName, float speed) {
        playAttack(animationName, speed);
    }

    public void swipeDownAttack() {
        playAttack(QueenAnimationRefs.RIGHT_SWIPE_DOWN_ANIMATION_NAME);
    }

    public void swipeDownAttack(float speed) {
        swipeDownAttack(QueenAnimationRefs.RIGHT_SWIPE_DOWN_ANIMATION_NAME, speed);
    }

    public void swipeDownAttack(String animationName, float speed) {
        playAttack(animationName, speed);
    }

    public void tailStrikeAttack() {
        playAttack(QueenAnimationRefs.RIGHT_TAIL_STRIKE_ANIMATION_NAME);
    }

    public void tailStrikeAttack(float speed) {
        tailStrikeAttack(QueenAnimationRefs.RIGHT_TAIL_STRIKE_ANIMATION_NAME, speed);
    }

    public void tailStrikeAttack(String animationName, float speed) {
        playAttack(animationName, speed);
    }

    private void playAttack(String animationName) {
        AzCommand.<Queen>replay()
            .play(AzAlienAnimationUtil.BODY, animationName, AzPlayBehaviors.PLAY_ONCE)
            .build()
            .dispatchForEntity(queen);
    }

    private void playAttack(String animationName, float speed) {
        AzCommand.<Queen>replay()
            .play(AzAlienAnimationUtil.BODY, animationName, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(queen);
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class RazorClawAnimationDispatcher {

    private static final AzCommand<RazorClaw> ARMATTACK = AzCommand.<RazorClaw>replay()
        .play(AzAlienAnimationUtil.BODY, RazorClawAnimationRefs.ATTACK_CLAW_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<RazorClaw> BITEATTACK = AzCommand.<RazorClaw>replay()
        .play(AzAlienAnimationUtil.BODY, RazorClawAnimationRefs.ATTACK_BITE_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<RazorClaw> TAILATTACK = AzCommand.<RazorClaw>replay()
        .play(AzAlienAnimationUtil.BODY, RazorClawAnimationRefs.ATTACK_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<RazorClaw> SWIM_ATTACK = AzCommand.<RazorClaw>replay()
        .play(AzAlienAnimationUtil.BODY, RazorClawAnimationRefs.SWIM_ATTACK_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<RazorClaw> SPECIAL_ATTACK_SPIN = AzCommand.<RazorClaw>replay()
        .play(AzAlienAnimationUtil.BODY, RazorClawAnimationRefs.SPECIAL_ATTACK_SPIN_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<RazorClaw> IDLE = AzCommand.<RazorClaw>idempotent()
        .play(AzAlienAnimationUtil.BODY, "idle", AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<RazorClaw> RUN = AzCommand.<RazorClaw>idempotent()
        .play(AzAlienAnimationUtil.BODY, "run", AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<RazorClaw> SWIM = AzCommand.<RazorClaw>idempotent()
        .play(AzAlienAnimationUtil.BODY, "swim", AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<RazorClaw> CRAWL = AzCommand.<RazorClaw>idempotent()
        .play(AzAlienAnimationUtil.BODY, "crawl", AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<RazorClaw> CRAWL_HOLD = AzCommand.<RazorClaw>idempotent()
        .play(AzAlienAnimationUtil.BODY, "crawl", AzPlayBehaviors.HOLD_ON_LAST_FRAME)
        .build();

    private static final AzCommand<RazorClaw> WALK = AzCommand.<RazorClaw>idempotent()
        .play(AzAlienAnimationUtil.BODY, "walk", AzPlayBehaviors.LOOP)
        .build();

    private final RazorClaw razorClaw;

    public RazorClawAnimationDispatcher(RazorClaw razorClaw) {
        this.razorClaw = razorClaw;
    }

    public void idle() {
        IDLE.dispatchForEntity(razorClaw);
    }

    public void run() {
        RUN.dispatchForEntity(razorClaw);
    }

    public void swim() {
        SWIM.dispatchForEntity(razorClaw);
    }

    public void crawl() {
        CRAWL.dispatchForEntity(razorClaw);
    }

    public void crawl(float speed) {
        AzAlienAnimationUtil.singleWithSpeed(
            AzAlienAnimationUtil.BODY,
            "crawl",
            AzPlayBehaviors.LOOP,
            AzDispatchMode.PLAY_IF_NOT_PLAYING,
            speed
        ).dispatchForEntity(razorClaw);
    }

    public void crawlHold() {
        CRAWL_HOLD.dispatchForEntity(razorClaw);
    }

    public void walk() {
        WALK.dispatchForEntity(razorClaw);
    }

    public void biteAttack() {
        BITEATTACK.dispatchForEntity(razorClaw);
    }

    public void biteAttack(float speed) {
        AzCommand.<RazorClaw>replay()
            .play(AzAlienAnimationUtil.BODY, RazorClawAnimationRefs.ATTACK_BITE_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(razorClaw);
    }

    public void rightClawAttack() {
        ARMATTACK.dispatchForEntity(razorClaw);
    }

    public void rightClawAttack(float speed) {
        AzCommand.<RazorClaw>replay()
            .play(AzAlienAnimationUtil.BODY, RazorClawAnimationRefs.ATTACK_CLAW_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(razorClaw);
    }

    public void tailAttack() {
        TAILATTACK.dispatchForEntity(razorClaw);
    }

    public void tailAttack(float speed) {
        AzCommand.<RazorClaw>replay()
            .play(AzAlienAnimationUtil.BODY, RazorClawAnimationRefs.ATTACK_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(razorClaw);
    }

    public void swimAttack() {
        SWIM_ATTACK.dispatchForEntity(razorClaw);
    }

    public void swimAttack(float speed) {
        AzCommand.<RazorClaw>replay()
            .play(AzAlienAnimationUtil.BODY, RazorClawAnimationRefs.SWIM_ATTACK_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(razorClaw);
    }

    public void specialAttackSpin() {
        SPECIAL_ATTACK_SPIN.dispatchForEntity(razorClaw);
    }

    public void specialAttackSpin(float speed) {
        AzCommand.<RazorClaw>replay()
            .play(AzAlienAnimationUtil.BODY, RazorClawAnimationRefs.SPECIAL_ATTACK_SPIN_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(razorClaw);
    }
}

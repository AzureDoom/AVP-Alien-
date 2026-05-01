package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class RazorClawAnimationDispatcher {

    private static final AzCommand ARMATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RazorClawAnimationRefs.ATTACK_CLAW_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RazorClawAnimationRefs.ATTACK_BITE_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RazorClawAnimationRefs.ATTACK_TAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand SWIM_ATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RazorClawAnimationRefs.SWIM_ATTACK_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand SPECIAL_ATTACK_SPIN = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RazorClawAnimationRefs.SPECIAL_ATTACK_SPIN_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand IDLE = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        "idle",
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand RUN = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        "run",
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand SWIM = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        "swim",
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand WALK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        "walk",
        AzPlayBehaviors.LOOP
    );

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

    public void walk() {
        WALK.dispatchForEntity(razorClaw);
    }

    public void biteAttack() {
        BITEATTACK.dispatchForEntity(razorClaw);
    }

    public void biteAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RazorClawAnimationRefs.ATTACK_BITE_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(razorClaw);
    }

    public void rightClawAttack() {
        ARMATTACK.dispatchForEntity(razorClaw);
    }

    public void rightClawAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RazorClawAnimationRefs.ATTACK_CLAW_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(razorClaw);
    }

    public void tailAttack() {
        TAILATTACK.dispatchForEntity(razorClaw);
    }

    public void tailAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RazorClawAnimationRefs.ATTACK_TAIL_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(razorClaw);
    }

    public void swimAttack() {
        SWIM_ATTACK.dispatchForEntity(razorClaw);
    }

    public void swimAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RazorClawAnimationRefs.SWIM_ATTACK_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(razorClaw);
    }

    public void specialAttackSpin() {
        SPECIAL_ATTACK_SPIN.dispatchForEntity(razorClaw);
    }

    public void specialAttackSpin(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RazorClawAnimationRefs.SPECIAL_ATTACK_SPIN_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(razorClaw);
    }
}

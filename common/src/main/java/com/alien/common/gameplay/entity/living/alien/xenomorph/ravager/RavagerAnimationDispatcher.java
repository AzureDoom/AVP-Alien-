package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class RavagerAnimationDispatcher {

    private static final AzCommand ARMATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.ATTACK_ARM_SINGLE_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand DOUBLE_ARMATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.ATTACK_ARM_DOUBLE_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.ATTACK_BITE_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.ATTACK_TAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand SWIM_ATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.SWIM_ATTACK_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand SPECIAL_ATTACK_WARMUP = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.SPECIAL_ATTACK_WARMUP_ANIMATION_NAME,
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private static final AzCommand SPECIAL_ATTACK_ACTIVATE = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.SPECIAL_ATTACK_ACTIVATE_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand IDLE = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.IDLE_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand RUN = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.RUN_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand SWIM = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.SWIM_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand WALK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        RavagerAnimationRefs.WALK_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private final Ravager ravager;

    public RavagerAnimationDispatcher(Ravager ravager) {
        this.ravager = ravager;
    }

    public void idle() {
        IDLE.dispatchForEntity(ravager);
    }

    public void run() {
        RUN.dispatchForEntity(ravager);
    }

    public void swim() {
        SWIM.dispatchForEntity(ravager);
    }

    public void walk() {
        WALK.dispatchForEntity(ravager);
    }

    public void biteAttack() {
        BITEATTACK.dispatchForEntity(ravager);
    }

    public void biteAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RavagerAnimationRefs.ATTACK_BITE_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(ravager);
    }

    public void rightClawAttack() {
        ARMATTACK.dispatchForEntity(ravager);
    }

    public void rightClawAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RavagerAnimationRefs.ATTACK_ARM_SINGLE_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(ravager);
    }

    public void doubleClawAttack() {
        DOUBLE_ARMATTACK.dispatchForEntity(ravager);
    }

    public void doubleClawAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RavagerAnimationRefs.ATTACK_ARM_DOUBLE_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(ravager);
    }

    public void tailAttack() {
        TAILATTACK.dispatchForEntity(ravager);
    }

    public void tailAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RavagerAnimationRefs.ATTACK_TAIL_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(ravager);
    }

    public void swimAttack() {
        SWIM_ATTACK.dispatchForEntity(ravager);
    }

    public void swimAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RavagerAnimationRefs.SWIM_ATTACK_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(ravager);
    }

    public void specialAttackWarmup() {
        SPECIAL_ATTACK_WARMUP.dispatchForEntity(ravager);
    }

    public void specialAttackWarmup(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RavagerAnimationRefs.SPECIAL_ATTACK_WARMUP_ANIMATION_NAME,
            AzPlayBehaviors.HOLD_ON_LAST_FRAME,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(ravager);
    }

    public void specialAttackActivate() {
        SPECIAL_ATTACK_ACTIVATE.dispatchForEntity(ravager);
    }

    public void specialAttackActivate(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            RavagerAnimationRefs.SPECIAL_ATTACK_ACTIVATE_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(ravager);
    }
}

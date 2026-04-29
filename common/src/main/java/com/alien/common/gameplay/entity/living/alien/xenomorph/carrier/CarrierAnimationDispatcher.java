package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class CarrierAnimationDispatcher {

    private static final AzCommand IDLE = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.IDLE_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand WALK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.WALK_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand RUN = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.RUN_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand SWIM = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.SWIM_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand ATTACKCLAW = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.ATTACKCLAW_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand ATTACKBITE = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.ATTACKBITE_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand ATTACKTAIL = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.ATTACKTAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand THROW_ATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.SPECIAL_ATTACK_THROW_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand SCREAM_ATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.SPECIAL_ATTACK_SCREAM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand HOLD_START = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.SPECIAL_ATTACK_HOLD_START_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand HOLD_WAITING = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        CarrierAnimationRefs.HOLD_WAITING_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private final Carrier carrier;

    public CarrierAnimationDispatcher(Carrier carrier) {
        this.carrier = carrier;
    }

    public void idle() {
        IDLE.dispatchForEntity(carrier);
    }

    public void walk() {
        WALK.dispatchForEntity(carrier);
    }

    public void run() {
        RUN.dispatchForEntity(carrier);
    }

    public void swim() {
        SWIM.dispatchForEntity(carrier);
    }

    public void clawAttack() {
        ATTACKCLAW.dispatchForEntity(carrier);
    }

    public void clawAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            CarrierAnimationRefs.ATTACKCLAW_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(carrier);
    }

    public void biteAttack() {
        ATTACKBITE.dispatchForEntity(carrier);
    }

    public void biteAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            CarrierAnimationRefs.ATTACKBITE_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(carrier);
    }

    public void tailAttack() {
        ATTACKTAIL.dispatchForEntity(carrier);
    }

    public void tailAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            CarrierAnimationRefs.ATTACKTAIL_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(carrier);
    }

    public void throwAttack() {
        THROW_ATTACK.dispatchForEntity(carrier);
    }

    public void screamAttack() {
        SCREAM_ATTACK.dispatchForEntity(carrier);
    }

    public void holdStart() {
        HOLD_START.dispatchForEntity(carrier);
    }

    public void holdWaiting() {
        HOLD_WAITING.dispatchForEntity(carrier);
    }
}

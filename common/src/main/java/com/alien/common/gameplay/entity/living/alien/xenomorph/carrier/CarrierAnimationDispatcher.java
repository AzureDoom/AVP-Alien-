package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class CarrierAnimationDispatcher {

    private static final AzCommand<Carrier> IDLE = AzCommand.<Carrier>idempotent()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.IDLE_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Carrier> WALK = AzCommand.<Carrier>idempotent()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.WALK_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Carrier> RUN = AzCommand.<Carrier>idempotent()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.RUN_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Carrier> SWIM = AzCommand.<Carrier>idempotent()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.SWIM_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Carrier> ATTACKCLAW = AzCommand.<Carrier>replay()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.ATTACKCLAW_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Carrier> ATTACKBITE = AzCommand.<Carrier>replay()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.ATTACKBITE_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Carrier> ATTACKTAIL = AzCommand.<Carrier>replay()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.ATTACKTAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Carrier> THROW_ATTACK = AzCommand.<Carrier>replay()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.SPECIAL_ATTACK_THROW_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Carrier> SCREAM_ATTACK = AzCommand.<Carrier>replay()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.SPECIAL_ATTACK_SCREAM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Carrier> HOLD_START = AzCommand.<Carrier>replay()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.SPECIAL_ATTACK_HOLD_START_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Carrier> HOLD_WAITING = AzCommand.<Carrier>idempotent()
        .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.HOLD_WAITING_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

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
        AzCommand.<Carrier>replay()
            .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.ATTACKCLAW_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(carrier);
    }

    public void biteAttack() {
        ATTACKBITE.dispatchForEntity(carrier);
    }

    public void biteAttack(float speed) {
        AzCommand.<Carrier>replay()
            .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.ATTACKBITE_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(carrier);
    }

    public void tailAttack() {
        ATTACKTAIL.dispatchForEntity(carrier);
    }

    public void tailAttack(float speed) {
        AzCommand.<Carrier>replay()
            .play(AzAlienAnimationUtil.BODY, CarrierAnimationRefs.ATTACKTAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.BODY, speed)
            .build()
            .dispatchForEntity(carrier);
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

package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class CarrierAnimationDispatcher {

    private static final AzCommand ARMATTACK_RIGHTARM = AzCommand.create(
        AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
        CarrierAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK_HEAD = AzCommand.create(
        AzAlienAnimationUtil.HEAD_TRACK_NAME,
        CarrierAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILATTACKQUAD_TAIL = AzCommand.create(
        AzAlienAnimationUtil.TAIL_TRACK_NAME,
        CarrierAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand IDLE_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "idle");

    private static final AzCommand RUN_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "run");

    private static final AzCommand SWIM_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "swim");

    private static final AzCommand WALK_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "walk");

    private final Carrier carrier;

    public CarrierAnimationDispatcher(Carrier carrier) {
        this.carrier = carrier;
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(carrier);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(carrier);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(carrier);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(carrier);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(carrier);
    }

    public void biteAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.HEAD_TRACK_NAME,
            CarrierAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(carrier);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(carrier);
    }

    public void rightClawAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
            CarrierAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(carrier);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(carrier);
    }

    public void tailAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.TAIL_TRACK_NAME,
            CarrierAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(carrier);
    }
}

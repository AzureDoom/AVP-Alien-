package com.alien.common.gameplay.entity.living.alien.xenomorph.drone;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class DroneAnimationDispatcher {

    private static final AzCommand ARMATTACK_RIGHTARM = AzCommand.create(
        AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
        DroneAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK_HEAD = AzCommand.create(
        AzAlienAnimationUtil.HEAD_TRACK_NAME,
        DroneAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILATTACKQUAD_TAIL = AzCommand.create(
        AzAlienAnimationUtil.TAIL_TRACK_NAME,
        DroneAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand CRAWL_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "crawl");

    private static final AzCommand CRAWL_ALL_HOLD = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMB_NAMES,
        "crawl",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private static final AzCommand IDLE_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "idle");

    private static final AzCommand LUNGE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMB_NAMES,
        "lunge",
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand RUN_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "run");

    private static final AzCommand SWIM_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "swim");

    private static final AzCommand WALK_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "walk");

    private final Drone drone;

    public DroneAnimationDispatcher(Drone drone) {
        this.drone = drone;
    }

    public void crawl() {
        CRAWL_ALL.dispatchForEntity(drone);
    }

    public void crawlHold() {
        CRAWL_ALL_HOLD.dispatchForEntity(drone);
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(drone);
    }

    public void lunge() {
        LUNGE_ALL.dispatchForEntity(drone);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(drone);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(drone);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(drone);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(drone);
    }

    public void biteAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.HEAD_TRACK_NAME,
            DroneAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F, speed, 0F, 0F, false
        ).dispatchForEntity(drone);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(drone);
    }

    public void rightClawAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
            DroneAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F, speed, 0F, 0F, false
        ).dispatchForEntity(drone);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(drone);
    }

    public void tailAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.TAIL_TRACK_NAME,
            DroneAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F, speed, 0F, 0F, false
        ).dispatchForEntity(drone);
    }
}

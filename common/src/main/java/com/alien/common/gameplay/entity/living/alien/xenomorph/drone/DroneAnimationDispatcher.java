package com.alien.common.gameplay.entity.living.alien.xenomorph.drone;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class DroneAnimationDispatcher {

    private static final AzCommand<Drone> ARMATTACK_RIGHTARM = AzCommand.<Drone>replay()
        .play(AzAlienAnimationUtil.RIGHT_ARM, DroneAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Drone> BITEATTACK_HEAD = AzCommand.<Drone>replay()
        .play(AzAlienAnimationUtil.HEAD, DroneAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Drone> TAILATTACKQUAD_TAIL = AzCommand.<Drone>replay()
        .play(AzAlienAnimationUtil.TAIL, DroneAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Drone> CRAWL_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Drone> CRAWL_ALL_HOLD = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "crawl",
        AzPlayBehaviors.HOLD_ON_LAST_FRAME,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Drone> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Drone> LUNGE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "lunge",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Drone> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "run",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Drone> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Drone> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private final Drone drone;

    public DroneAnimationDispatcher(Drone drone) {
        this.drone = drone;
    }

    public void crawl() {
        CRAWL_ALL.dispatchForEntity(drone);
    }

    public void crawl(float speed) {
        AzAlienAnimationUtil.composeWithSpeed(
            AzAlienAnimationUtil.XENO_LIMBS,
            "crawl",
            AzPlayBehaviors.LOOP,
            AzDispatchMode.PLAY_IF_NOT_PLAYING,
            speed
        ).dispatchForEntity(drone);
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
        AzCommand.<Drone>replay()
            .play(AzAlienAnimationUtil.HEAD, DroneAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.HEAD, speed)
            .build()
            .dispatchForEntity(drone);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(drone);
    }

    public void rightClawAttack(float speed) {
        AzCommand.<Drone>replay()
            .play(AzAlienAnimationUtil.RIGHT_ARM, DroneAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.RIGHT_ARM, speed)
            .build()
            .dispatchForEntity(drone);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(drone);
    }

    public void tailAttack(float speed) {
        AzCommand.<Drone>replay()
            .play(AzAlienAnimationUtil.TAIL, DroneAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
            .setSpeed(AzAlienAnimationUtil.TAIL, speed)
            .build()
            .dispatchForEntity(drone);
    }
}

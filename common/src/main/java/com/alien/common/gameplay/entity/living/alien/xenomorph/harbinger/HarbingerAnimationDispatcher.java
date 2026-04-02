package com.alien.common.gameplay.entity.living.alien.xenomorph.harbinger;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class HarbingerAnimationDispatcher {

    private static final AzCommand ARMATTACK_RIGHTARM = AzCommand.create(
        AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
        HarbingerAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK_HEAD = AzCommand.create(
        AzAlienAnimationUtil.HEAD_TRACK_NAME,
        HarbingerAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILATTACKQUAD_TAIL = AzCommand.create(
        AzAlienAnimationUtil.TAIL_TRACK_NAME,
        HarbingerAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand IDLE_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "idle");

    private static final AzCommand RUN_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "run");

    private static final AzCommand SWIM_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "swim");

    private static final AzCommand WALK_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "walk");

    private final Harbinger harbinger;

    public HarbingerAnimationDispatcher(Harbinger harbinger) {
        this.harbinger = harbinger;
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(harbinger);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(harbinger);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(harbinger);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(harbinger);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatchForEntity(harbinger);
    }

    public void biteAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.HEAD_TRACK_NAME,
            HarbingerAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F, speed, 0F, 0F, false
        ).dispatchForEntity(harbinger);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.dispatchForEntity(harbinger);
    }

    public void rightClawAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.RIGHT_ARM_TRACK_NAME,
            HarbingerAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F, speed, 0F, 0F, false
        ).dispatchForEntity(harbinger);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.dispatchForEntity(harbinger);
    }

    public void tailAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.TAIL_TRACK_NAME,
            HarbingerAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F, speed, 0F, 0F, false
        ).dispatchForEntity(harbinger);
    }
}

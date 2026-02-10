package com.alien.common.gameplay.entity.living.alien.xenomorph.predalien;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class PredalienAnimationDispatcher {

    private static final AzCommand ARMATTACK_RIGHTARM = AzCommand.create(
        AzAlienAnimationUtil.RIGHT_ARM_CONTROLLER_NAME,
        PredalienAnimationRefs.ATTACKCLAW_RIGHTARM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK_HEAD = AzCommand.create(
        AzAlienAnimationUtil.HEAD_CONTROLLER_NAME,
        PredalienAnimationRefs.ATTACKBITE_HEAD_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILATTACKQUAD_TAIL = AzCommand.create(
        AzAlienAnimationUtil.TAIL_CONTROLLER_NAME,
        PredalienAnimationRefs.ATTACKTAIL_TAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand IDLE_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "idle");

    private static final AzCommand RUN_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "run");

    private static final AzCommand SWIM_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "swim");

    private static final AzCommand WALK_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "walk");

    private final Predalien predalien;

    public PredalienAnimationDispatcher(Predalien predalien) {
        this.predalien = predalien;
    }

    public void idle() {
        IDLE_ALL.sendForEntity(predalien);
    }

    public void run() {
        RUN_ALL.sendForEntity(predalien);
    }

    public void swim() {
        SWIM_ALL.sendForEntity(predalien);
    }

    public void walk() {
        WALK_ALL.sendForEntity(predalien);
    }

    public void biteAttack() {
        BITEATTACK_HEAD.sendForEntity(predalien);
    }

    public void rightClawAttack() {
        ARMATTACK_RIGHTARM.sendForEntity(predalien);
    }

    public void tailAttack() {
        TAILATTACKQUAD_TAIL.sendForEntity(predalien);
    }
}

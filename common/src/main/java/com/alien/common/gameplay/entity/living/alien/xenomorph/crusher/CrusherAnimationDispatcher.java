package com.alien.common.gameplay.entity.living.alien.xenomorph.crusher;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

import java.util.Objects;

public class CrusherAnimationDispatcher {

    private static final AzCommand BITEATTACK_HEAD = AzCommand.create(
        AzAlienAnimationUtil.HEAD_TRACK_NAME,
        CrusherAnimationRefs.BITEATTACK_HEAD_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand IDLE_TAIL = AzCommand.create(
        AzAlienAnimationUtil.TAIL_TRACK_NAME,
        CrusherAnimationRefs.IDLE_TAIL_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand RUN_TAIL_PLAY_ONCE = AzCommand.create(
        AzAlienAnimationUtil.TAIL_TRACK_NAME,
        CrusherAnimationRefs.RUN_TAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand IDLE_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "idle");

    private static final AzCommand LEAP_ALL = AzCommand.compose(
        AzAnimationUtil.compose(
            AzAlienAnimationUtil.XENO_LIMB_NAMES.stream().filter(name -> !Objects.equals(name, "tail")).toList(),
            "leap",
            AzPlayBehaviors.PLAY_ONCE
        ),
        RUN_TAIL_PLAY_ONCE
    );

    private static final AzCommand RUN_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "run");

    private static final AzCommand SWIM_ALL = AzAnimationUtil.compose(AzAlienAnimationUtil.XENO_LIMB_NAMES, "swim");

    private static final AzCommand TAILATTACK_TAIL = AzCommand.create(
        AzAlienAnimationUtil.TAIL_TRACK_NAME,
        CrusherAnimationRefs.TAILATTACK_TAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand WALK_ALL = AzCommand.compose(
        AzAnimationUtil.compose(
            AzAlienAnimationUtil.XENO_LIMB_NAMES.stream().filter(name -> !Objects.equals(name, "tail")).toList(),
            "walk",
            AzPlayBehaviors.LOOP
        ),
        IDLE_TAIL
    );

    private final Crusher crusher;

    public CrusherAnimationDispatcher(Crusher crusher) {
        this.crusher = crusher;
    }

    public void biteAttack() {
        BITEATTACK_HEAD.dispatch(crusher);
    }

    public void idle() {
        IDLE_ALL.dispatch(crusher);
    }

    public void lunge() {
        LEAP_ALL.dispatch(crusher);
    }

    public void run() {
        RUN_ALL.dispatch(crusher);
    }

    public void swim() {
        SWIM_ALL.dispatch(crusher);
    }

    public void tailAttack() {
        TAILATTACK_TAIL.dispatch(crusher);
    }

    public void walk() {
        WALK_ALL.dispatch(crusher);
    }
}

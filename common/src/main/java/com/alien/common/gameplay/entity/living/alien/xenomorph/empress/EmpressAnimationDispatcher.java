package com.alien.common.gameplay.entity.living.alien.xenomorph.empress;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class EmpressAnimationDispatcher {

    private static final AzCommand<Empress> BACKHAND_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_EMPRESS_LIMBS,
        "backhand",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Empress> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_EMPRESS_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Empress> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_EMPRESS_LIMBS,
        "run",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Empress> SIT_ON_OVIPOSITOR_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_EMPRESS_LIMBS,
        "rideeggsack",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Empress> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_EMPRESS_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Empress> SWIPEDOWN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_EMPRESS_LIMBS,
        "swipedown",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Empress> TAILSTRIKE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_EMPRESS_LIMBS,
        "tailstrike",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Empress> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_EMPRESS_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private final Empress empress;

    public EmpressAnimationDispatcher(Empress empress) {
        this.empress = empress;
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(empress);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(empress);
    }

    public void sitOnOvipositor() {
        SIT_ON_OVIPOSITOR_ALL.dispatchForEntity(empress);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(empress);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(empress);
    }

    public void backhandAttack() {
        BACKHAND_ALL.dispatchForEntity(empress);
    }

    public void backhandAttack(float speed) {
        attackWithSpeed("backhand", speed);
    }

    public void swipeDownAttack() {
        SWIPEDOWN_ALL.dispatchForEntity(empress);
    }

    public void swipeDownAttack(float speed) {
        attackWithSpeed("swipedown", speed);
    }

    public void tailStrikeAttack() {
        TAILSTRIKE_ALL.dispatchForEntity(empress);
    }

    public void tailStrikeAttack(float speed) {
        attackWithSpeed("tailstrike", speed);
    }

    private void attackWithSpeed(String baseName, float speed) {
        AzCommand.compose(
            AzAlienAnimationUtil.XENO_EMPRESS_LIMBS.stream()
                .map(
                    handle -> AzCommand.<Empress>replay()
                        .play(handle, baseName + "." + handle.name(), AzPlayBehaviors.PLAY_ONCE)
                        .setSpeed(handle, speed)
                        .build()
                )
                .toList()
        ).dispatchForEntity(empress);
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.queen;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.AzAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class QueenAnimationDispatcher {

    private static final AzCommand<Queen> BACKHAND_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMBS,
        "backhand",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Queen> IDLE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMBS,
        "idle",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Queen> RUN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMBS,
        "run",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Queen> SIT_ON_OVIPOSITOR_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMBS,
        "rideeggsack",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Queen> SWIM_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMBS,
        "swim",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private static final AzCommand<Queen> SWIPEDOWN_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMBS,
        "swipedown",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Queen> TAILSTRIKE_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMBS,
        "tailstrike",
        AzPlayBehaviors.PLAY_ONCE,
        AzDispatchMode.REPLAY
    );

    private static final AzCommand<Queen> WALK_ALL = AzAnimationUtil.compose(
        AzAlienAnimationUtil.XENO_QUEEN_LIMBS,
        "walk",
        AzPlayBehaviors.LOOP,
        AzDispatchMode.PLAY_IF_NOT_PLAYING
    );

    private final Queen queen;

    public QueenAnimationDispatcher(Queen queen) {
        this.queen = queen;
    }

    public void idle() {
        IDLE_ALL.dispatchForEntity(queen);
    }

    public void run() {
        RUN_ALL.dispatchForEntity(queen);
    }

    public void sitOnOvipositor() {
        SIT_ON_OVIPOSITOR_ALL.dispatchForEntity(queen);
    }

    public void swim() {
        SWIM_ALL.dispatchForEntity(queen);
    }

    public void walk() {
        WALK_ALL.dispatchForEntity(queen);
    }

    public void backhandAttack() {
        BACKHAND_ALL.dispatchForEntity(queen);
    }

    public void backhandAttack(float speed) {
        attackWithSpeed("backhand", speed);
    }

    public void swipeDownAttack() {
        SWIPEDOWN_ALL.dispatchForEntity(queen);
    }

    public void swipeDownAttack(float speed) {
        attackWithSpeed("swipedown", speed);
    }

    public void tailStrikeAttack() {
        TAILSTRIKE_ALL.dispatchForEntity(queen);
    }

    public void tailStrikeAttack(float speed) {
        attackWithSpeed("tailstrike", speed);
    }

    private void attackWithSpeed(String baseName, float speed) {
        AzCommand.compose(
            AzAlienAnimationUtil.XENO_QUEEN_LIMBS.stream()
                .map(
                    handle -> AzCommand.<Queen>replay()
                        .play(handle, baseName + "." + handle.name(), AzPlayBehaviors.PLAY_ONCE)
                        .setSpeed(handle, speed)
                        .build()
                )
                .toList()
        ).dispatchForEntity(queen);
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.empress;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;
import com.blib.api.client.animation.v1.command.policy.AzDispatchMode;

public class EmpressAnimationDispatcher {

    private static final AzCommand<Empress> IDLE = AzCommand.<Empress>idempotent()
        .play(AzAlienAnimationUtil.BODY, EmpressAnimationRefs.IDLE_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Empress> RUN = AzCommand.<Empress>idempotent()
        .play(AzAlienAnimationUtil.BODY, EmpressAnimationRefs.RUN_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Empress> WALK = AzCommand.<Empress>idempotent()
        .play(AzAlienAnimationUtil.BODY, EmpressAnimationRefs.WALK_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Empress> SWIM = AzCommand.<Empress>idempotent()
        .play(AzAlienAnimationUtil.BODY, EmpressAnimationRefs.SWIM_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Empress> SIT_ON_OVIPOSITOR = AzCommand.<Empress>idempotent()
        .play(AzAlienAnimationUtil.BODY, EmpressAnimationRefs.RIDE_EGGSACK_ANIMATION_NAME, AzPlayBehaviors.LOOP)
        .build();

    private static final AzCommand<Empress> BACKHAND = AzCommand.<Empress>replay()
        .play(AzAlienAnimationUtil.BODY, EmpressAnimationRefs.BACKHAND_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Empress> SWIPEDOWN = AzCommand.<Empress>replay()
        .play(AzAlienAnimationUtil.BODY, EmpressAnimationRefs.SWIPEDOWN_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private static final AzCommand<Empress> TAILSTRIKE = AzCommand.<Empress>replay()
        .play(AzAlienAnimationUtil.BODY, EmpressAnimationRefs.TAILSTRIKE_ANIMATION_NAME, AzPlayBehaviors.PLAY_ONCE)
        .build();

    private final Empress empress;

    public EmpressAnimationDispatcher(Empress empress) {
        this.empress = empress;
    }

    public void idle() {
        IDLE.dispatchForEntity(empress);
    }

    public void run() {
        RUN.dispatchForEntity(empress);
    }

    public void sitOnOvipositor() {
        SIT_ON_OVIPOSITOR.dispatchForEntity(empress);
    }

    public void swim() {
        SWIM.dispatchForEntity(empress);
    }

    public void walk() {
        WALK.dispatchForEntity(empress);
    }

    public void backhandAttack() {
        BACKHAND.dispatchForEntity(empress);
    }

    public void backhandAttack(float speed) {
        attackWithSpeed(EmpressAnimationRefs.BACKHAND_ANIMATION_NAME, speed);
    }

    public void swipeDownAttack() {
        SWIPEDOWN.dispatchForEntity(empress);
    }

    public void swipeDownAttack(float speed) {
        attackWithSpeed(EmpressAnimationRefs.SWIPEDOWN_ANIMATION_NAME, speed);
    }

    public void tailStrikeAttack() {
        TAILSTRIKE.dispatchForEntity(empress);
    }

    public void tailStrikeAttack(float speed) {
        attackWithSpeed(EmpressAnimationRefs.TAILSTRIKE_ANIMATION_NAME, speed);
    }

    private void attackWithSpeed(String animationName, float speed) {
        AzAlienAnimationUtil.singleWithSpeed(
            AzAlienAnimationUtil.BODY,
            animationName,
            AzPlayBehaviors.PLAY_ONCE,
            AzDispatchMode.REPLAY,
            speed
        ).dispatchForEntity(empress);
    }
}

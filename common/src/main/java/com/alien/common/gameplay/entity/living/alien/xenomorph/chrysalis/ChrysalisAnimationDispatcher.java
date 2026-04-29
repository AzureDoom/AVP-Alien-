package com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class ChrysalisAnimationDispatcher {

    private static final AzCommand ARMATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        ChrysalisAnimationRefs.ATTACKCLAW_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand BITEATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        ChrysalisAnimationRefs.ATTACKBITE_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand TAILATTACK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        ChrysalisAnimationRefs.ATTACKTAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand IDLE = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        ChrysalisAnimationRefs.IDLE_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand RUN = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        ChrysalisAnimationRefs.RUN_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand SWIM = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        ChrysalisAnimationRefs.SWIM_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand WALK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        ChrysalisAnimationRefs.WALK_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private final Chrysalis chrysalis;

    public ChrysalisAnimationDispatcher(Chrysalis chrysalis) {
        this.chrysalis = chrysalis;
    }

    public void idle() {
        IDLE.dispatchForEntity(chrysalis);
    }

    public void run() {
        RUN.dispatchForEntity(chrysalis);
    }

    public void swim() {
        SWIM.dispatchForEntity(chrysalis);
    }

    public void walk() {
        WALK.dispatchForEntity(chrysalis);
    }

    public void biteAttack() {
        BITEATTACK.dispatchForEntity(chrysalis);
    }

    public void biteAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            ChrysalisAnimationRefs.ATTACKBITE_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(chrysalis);
    }

    public void rightClawAttack() {
        ARMATTACK.dispatchForEntity(chrysalis);
    }

    public void rightClawAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            ChrysalisAnimationRefs.ATTACKCLAW_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(chrysalis);
    }

    public void tailAttack() {
        TAILATTACK.dispatchForEntity(chrysalis);
    }

    public void tailAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            ChrysalisAnimationRefs.ATTACKTAIL_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(chrysalis);
    }
}

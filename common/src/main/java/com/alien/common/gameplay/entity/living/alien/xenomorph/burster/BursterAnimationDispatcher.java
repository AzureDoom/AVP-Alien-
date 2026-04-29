package com.alien.common.gameplay.entity.living.alien.xenomorph.burster;

import com.alien.common.util.AzAlienAnimationUtil;
import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class BursterAnimationDispatcher {

    private static final AzCommand IDLE = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        BursterAnimationRefs.IDLE_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand WALK = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        BursterAnimationRefs.WALK_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand RUN = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        BursterAnimationRefs.RUN_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand CRAWL = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        BursterAnimationRefs.CRAWL_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand CRAWL_HOLD = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        BursterAnimationRefs.CRAWL_ANIMATION_NAME,
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private static final AzCommand LUNGE = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        BursterAnimationRefs.LUNGE_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand SWIM = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        BursterAnimationRefs.SWIM_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand FULLATTACKARM = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        BursterAnimationRefs.FULLATTACKARM_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand FULLATTACKBITE = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        BursterAnimationRefs.FULLATTACKBITE_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private static final AzCommand FULLATTACKTAIL = AzCommand.create(
        AzAlienAnimationUtil.BODY_TRACK_NAME,
        BursterAnimationRefs.FULLATTACKTAIL_ANIMATION_NAME,
        AzPlayBehaviors.PLAY_ONCE
    );

    private final Burster burster;

    public BursterAnimationDispatcher(Burster burster) {
        this.burster = burster;
    }

    public void idle() {
        IDLE.dispatchForEntity(burster);
    }

    public void walk() {
        WALK.dispatchForEntity(burster);
    }

    public void run() {
        RUN.dispatchForEntity(burster);
    }

    public void crawl() {
        CRAWL.dispatchForEntity(burster);
    }

    public void crawlHold() {
        CRAWL_HOLD.dispatchForEntity(burster);
    }

    public void lunge() {
        LUNGE.dispatchForEntity(burster);
    }

    public void swim() {
        SWIM.dispatchForEntity(burster);
    }

    public void clawAttack() {
        FULLATTACKARM.dispatchForEntity(burster);
    }

    public void clawAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            BursterAnimationRefs.FULLATTACKARM_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(burster);
    }

    public void biteAttack() {
        FULLATTACKBITE.dispatchForEntity(burster);
    }

    public void biteAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            BursterAnimationRefs.FULLATTACKBITE_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(burster);
    }

    public void tailAttack() {
        FULLATTACKTAIL.dispatchForEntity(burster);
    }

    public void tailAttack(float speed) {
        AzCommand.create(
            AzAlienAnimationUtil.BODY_TRACK_NAME,
            BursterAnimationRefs.FULLATTACKTAIL_ANIMATION_NAME,
            AzPlayBehaviors.PLAY_ONCE,
            0F,
            speed,
            0F,
            0F,
            false
        ).dispatchForEntity(burster);
    }
}

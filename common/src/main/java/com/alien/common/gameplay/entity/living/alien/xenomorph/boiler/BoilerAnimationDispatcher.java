package com.alien.common.gameplay.entity.living.alien.xenomorph.boiler;

import com.blib.api.client.animation.v1.command.AzCommand;
import com.blib.api.client.animation.v1.command.play_behavior.AzPlayBehaviors;

public class BoilerAnimationDispatcher {

    private static final AzCommand CRAWL = AzCommand.create(
        BoilerAnimationRefs.FULL_BODY_TRACK_NAME,
        BoilerAnimationRefs.CRAWL_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand CRAWL_HOLD = AzCommand.create(
        BoilerAnimationRefs.FULL_BODY_TRACK_NAME,
        BoilerAnimationRefs.CRAWL_ANIMATION_NAME,
        AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    private static final AzCommand IDLE = AzCommand.create(
        BoilerAnimationRefs.FULL_BODY_TRACK_NAME,
        BoilerAnimationRefs.IDLE_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand RUN = AzCommand.create(
        BoilerAnimationRefs.FULL_BODY_TRACK_NAME,
        BoilerAnimationRefs.RUN_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand SWIM = AzCommand.create(
        BoilerAnimationRefs.FULL_BODY_TRACK_NAME,
        BoilerAnimationRefs.SWIM_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private static final AzCommand WALK = AzCommand.create(
        BoilerAnimationRefs.FULL_BODY_TRACK_NAME,
        BoilerAnimationRefs.WALK_ANIMATION_NAME,
        AzPlayBehaviors.LOOP
    );

    private final Boiler boiler;

    public BoilerAnimationDispatcher(Boiler boiler) {
        this.boiler = boiler;
    }

    public void crawl() {
        CRAWL.dispatchForEntity(boiler);
    }

    public void crawlHold() {
        CRAWL_HOLD.dispatchForEntity(boiler);
    }

    public void idle() {
        IDLE.dispatchForEntity(boiler);
    }

    public void run() {
        RUN.dispatchForEntity(boiler);
    }

    public void swim() {
        SWIM.dispatchForEntity(boiler);
    }

    public void walk() {
        WALK.dispatchForEntity(boiler);
    }

}

package com.alien.common.gameplay.entity.living.alien.parasite.facehugger;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.blib.api.client.animation.v1.track.AzTrackHandle;

public class FacehuggerAnimationRefs {

    public static final String FACEHUG_ANIMATION_NAME = "animation.hug";

    public static final AzTrackHandle<Alien> LEGS = AzTrackHandle.declare("legs");

    public static final AzTrackHandle<Alien> LUNGS = AzTrackHandle.declare("lungs");

    public static final AzTrackHandle<Alien> TAIL = AzTrackHandle.declare("tail");

    public static final String IDLE_ANIMATION_NAME = "animation.idle";

    public static final String LEAP_ANIMATION_NAME = "animation.leap";

    public static final String RUN_ANIMATION_NAME = "animation.run";

    public static final String SACK_ANIMATION_NAME = "animation.sack";

    public static final String TAIL_FLAIL_ANIMATION_NAME = "animation.tailflail";

    public static final String TAIL_SWAY_ANIMATION_NAME = "animation.tailsway";

    public static final String INFERTILE_ANIMATION_NAME = "animation.dead";

}

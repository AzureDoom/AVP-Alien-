package com.alien.common.util;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.blib.api.client.animation.v1.track.AzTrackHandle;

import java.util.List;

public class AzAlienAnimationUtil {

    public static final AzTrackHandle<Alien> BODY = AzTrackHandle.declare("body");

    public static final AzTrackHandle<Alien> HEAD = AzTrackHandle.declare("head");

    public static final AzTrackHandle<Alien> LEFT_ARM = AzTrackHandle.declare("leftarm");

    public static final AzTrackHandle<Alien> LEFT_LEG = AzTrackHandle.declare("leftleg");

    public static final AzTrackHandle<Alien> LEFT_TITTY_ARM = AzTrackHandle.declare("lefttittyarm");

    public static final AzTrackHandle<Alien> RIGHT_ARM = AzTrackHandle.declare("rightarm");

    public static final AzTrackHandle<Alien> RIGHT_LEG = AzTrackHandle.declare("rightleg");

    public static final AzTrackHandle<Alien> RIGHT_TITTY_ARM = AzTrackHandle.declare("righttittyarm");

    public static final AzTrackHandle<Alien> TAIL = AzTrackHandle.declare("tail");

    public static final List<AzTrackHandle<Alien>> XENO_LIMBS = List.of(
        BODY,
        HEAD,
        LEFT_ARM,
        LEFT_LEG,
        RIGHT_ARM,
        RIGHT_LEG,
        TAIL
    );

    public static final List<AzTrackHandle<Alien>> XENO_QUEEN_LIMBS = List.of(
        BODY,
        HEAD,
        LEFT_ARM,
        LEFT_LEG,
        LEFT_TITTY_ARM,
        RIGHT_ARM,
        RIGHT_LEG,
        RIGHT_TITTY_ARM,
        TAIL
    );

    public static final List<AzTrackHandle<Alien>> XENO_EMPRESS_LIMBS = List.of(
        BODY,
        HEAD,
        LEFT_ARM,
        LEFT_LEG,
        LEFT_TITTY_ARM,
        RIGHT_ARM,
        RIGHT_LEG,
        RIGHT_TITTY_ARM,
        TAIL
    );
}

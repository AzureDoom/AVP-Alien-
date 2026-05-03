package com.alien.common.gameplay.entity.living.alien.predalien_chestburster;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.blib.api.client.animation.v1.track.AzTrackHandle;

public class PredalienChestbursterAnimationRefs {

    public static final AzTrackHandle<Alien> HEAD = AzTrackHandle.declare("head");

    public static final AzTrackHandle<Alien> TAIL = AzTrackHandle.declare("tail");

    public static final String BITE_HEAD_ANIMATION_NAME = "bite";

    public static final String IDLE_HEAD_ANIMATION_NAME = "idle";

    public static final String LOOK_HEAD_ANIMATION_NAME = "look";

    public static final String SLITHER_TAIL_ANIMATION_NAME = "slither3(justtail)";
}

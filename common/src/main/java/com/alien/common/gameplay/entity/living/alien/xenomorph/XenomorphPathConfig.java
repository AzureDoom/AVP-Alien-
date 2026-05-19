package com.alien.common.gameplay.entity.living.alien.xenomorph;

public record XenomorphPathConfig(
    int entityWidth,
    int entityHeight,
    boolean canOpenDoors,
    int crawlHeight
) {

    public XenomorphPathConfig(int entityWidth, int entityHeight, boolean canOpenDoors) {
        this(entityWidth, entityHeight, canOpenDoors, 1);
    }

    public static final XenomorphPathConfig SMALL_DOOR = new XenomorphPathConfig(1, 1, true);

    public static final XenomorphPathConfig MEDIUM_DOOR = new XenomorphPathConfig(1, 2, true);

    public static final XenomorphPathConfig MEDIUM_TALL = new XenomorphPathConfig(1, 3, true);

    public static final XenomorphPathConfig LARGE = new XenomorphPathConfig(1, 4, false);

    public static final XenomorphPathConfig LARGE_DOOR = new XenomorphPathConfig(1, 4, true);

    public static final XenomorphPathConfig WIDE = new XenomorphPathConfig(2, 2, false);

    public static final XenomorphPathConfig WIDE_TALL = new XenomorphPathConfig(2, 4, false);
}

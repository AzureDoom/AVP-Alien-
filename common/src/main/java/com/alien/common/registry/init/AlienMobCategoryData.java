package com.alien.common.registry.init;

public class AlienMobCategoryData {

    public static final Data ALIEN = new Data("avp:alien", 75, false, false, 128);

    public record Data(
        String name,
        int max,
        boolean isFriendly,
        boolean isPersistent,
        int despawnDistance
    ) {}
}

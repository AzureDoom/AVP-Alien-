package com.alien.common.gameplay.hive.location;

import com.alien.common.gameplay.hive.config.HiveConfig;

/**
 * Shared bootstrap shield for newborn hive locations. While active, systems that can shrink or evacuate a location
 * should leave it alone so abstract-spread founders have time to materialize and establish population.
 */
public final class HiveLocationBootstrapProtection {

    private HiveLocationBootstrapProtection() {}

    public static boolean isProtected(HiveLocation location, HiveConfig config) {
        return location.ageInTicks() < config.locationBootstrapGraceTicks();
    }
}

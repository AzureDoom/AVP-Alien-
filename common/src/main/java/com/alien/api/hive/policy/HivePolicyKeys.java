package com.alien.api.hive.policy;

import com.alien.AlienResources;

public final class HivePolicyKeys {

    public static final HivePolicyKey<Boolean> RESERVE_SPAWNS_CAN_IGNORE_RESIN = new HivePolicyKey<>(
        AlienResources.location("hive_policy/reserve_spawns_can_ignore_resin"),
        Boolean.class
    );

    private HivePolicyKeys() {}
}

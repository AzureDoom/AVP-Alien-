package com.alien.common.gameplay.hive2.policy;

import com.alien.Alien;
import com.alien.api.hive2.policy.HivePolicyContext;
import com.alien.api.hive2.policy.HivePolicyKeys;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

public final class HivePolicies {

    private HivePolicies() {}

    public static boolean reserveSpawnsCanIgnoreResin(@Nullable MinecraftServer server, HiveLocation location) {
        var policy = HivePolicyResolver.resolve(context(server, location));
        return policy.get(HivePolicyKeys.RESERVE_SPAWNS_CAN_IGNORE_RESIN);
    }

    public static HivePolicyContext context(@Nullable MinecraftServer server, @Nullable HiveLocation location) {
        var config = HiveLocationRegistry.INSTANCE.config();
        LineageFactionData lineage = null;

        if (location != null) {
            var lineageFaction = Alien.MOD.factions().get(location.lineageFactionId());
            if (lineageFaction != null && lineageFaction.data() instanceof LineageFactionData data) {
                lineage = data;
            }
        }

        return new HivePolicyContext(server, location, lineage, config);
    }

    public static HivePolicyContext context(HiveConfig config) {
        return new HivePolicyContext(null, null, null, config);
    }
}

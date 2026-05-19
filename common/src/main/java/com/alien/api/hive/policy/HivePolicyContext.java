package com.alien.api.hive.policy;

import com.alien.common.gameplay.hive.config.HiveConfig;
import com.alien.common.gameplay.hive.faction.LineageFactionData;
import com.alien.common.gameplay.hive.location.HiveLocation;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Context supplied to policy modifiers. Some policies can be resolved globally, so location and lineage may be null.
 */
public record HivePolicyContext(
    @Nullable MinecraftServer server,
    @Nullable HiveLocation location,
    @Nullable LineageFactionData lineage,
    HiveConfig config
) {

    public HivePolicyContext {
        Objects.requireNonNull(config, "config");
    }
}

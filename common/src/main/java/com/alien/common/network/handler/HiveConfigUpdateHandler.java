package com.alien.common.network.handler;

import com.alien.common.gameplay.hive.config.HiveConfigSchema;
import com.alien.common.gameplay.hive.location.HiveLocationRegistry;
import com.alien.common.network.payload.C2SUpdateHiveConfigPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class HiveConfigUpdateHandler {

    private HiveConfigUpdateHandler() {}

    public static void handle(C2SUpdateHiveConfigPayload payload, Player player) {
        if (!(player instanceof ServerPlayer sp)) {
            return;
        }
        if (!sp.hasPermissions(2)) {
            return;
        }

        try {
            var current = HiveLocationRegistry.INSTANCE.config();
            var updated = HiveConfigSchema.withParsedValue(current, payload.fieldName(), payload.value());
            HiveLocationRegistry.INSTANCE.setConfig(updated);
        } catch (IllegalArgumentException exception) {
            sp.sendSystemMessage(Component.literal(exception.getMessage()));
        }
    }
}

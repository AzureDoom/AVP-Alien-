package com.alien.common.gameplay.hive2.tick;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.convoy.Convoy;
import com.alien.common.gameplay.hive2.convoy.ConvoyArrival;
import com.alien.common.gameplay.hive2.convoy.ConvoyTravel;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.gameplay.hive2.location.HivePoolCascade;
import net.minecraft.server.MinecraftServer;

/**
 * Per-server-tick driver for in-flight convoys. Walks every loaded lineage's convoy list, advances each convoy's
 * position via {@link ConvoyTravel#tick}, and removes any that have arrived (firing their type-specific arrival effect
 * via {@link ConvoyArrival#checkArrival}).
 * <p>
 * Convoys are sparse — a busy lineage might have a handful active at any moment — so this is cheap. Travel is a single
 * Vec3 lerp per convoy.
 * <p>
 * See {@code HIVE_REDESIGN_12_PERFORMANCE.md} § 1.
 */
public final class LineageConvoyTickTask {

    private LineageConvoyTickTask() {}

    public static void run(MinecraftServer server) {
        var config = HiveLocationRegistry.INSTANCE.config();

        for (var factionId : Alien.MOD.factions().getAllIds()) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }
            if (lineage.convoys().isEmpty()) {
                continue;
            }

            var iterator = lineage.convoys().iterator();
            var anyChanged = false;

            while (iterator.hasNext()) {
                var convoy = iterator.next();

                if (convoy instanceof Convoy.Raid raid) {
                    if (handleRaidExpiry(raid, lineage, server)) {
                        iterator.remove();
                        anyChanged = true;
                        continue;
                    }
                    updateRaidTargetPos(raid, server);
                }

                ConvoyTravel.tick(convoy, config);

                if (ConvoyArrival.checkArrival(server, convoy, lineage, config)) {
                    iterator.remove();
                    anyChanged = true;
                }
            }

            if (anyChanged) {
                lineage.markDirty();
            }
        }
    }

    /** Refreshes a raid's last-known target position when its target player is online + same dim + alive. */
    private static void updateRaidTargetPos(Convoy.Raid raid, MinecraftServer server) {
        var player = server.getPlayerList().getPlayer(raid.targetPlayerId());
        if (player == null || !player.isAlive()) {
            return;
        }
        if (!player.level().dimension().equals(raid.dimension())) {
            return;
        }
        raid.setLastKnownTargetPos(player.blockPosition());
    }

    /**
     * Returns true if the raid has expired (passed {@code expiresAtTick}). On expiry: refunds the abstract composition
     * into the lineage pool with cascade and signals the caller to drop the convoy.
     */
    private static boolean handleRaidExpiry(Convoy.Raid raid, LineageFactionData lineage, MinecraftServer server) {
        var currentTick = server.overworld().getGameTime();
        if (currentTick < raid.expiresAtTick()) {
            return false;
        }

        Alien.LOGGER.info(
            "Raid {} expired at tick {} (dispatched at {}) — refunding composition to lineage pool",
            raid.id(),
            currentTick,
            raid.dispatchedTick()
        );
        for (var entityType : raid.composition().getAvailableEntityTypes()) {
            var count = raid.composition().getCount(entityType);
            HivePoolCascade.addToLineageCascading(lineage, entityType, count);
        }
        return true;
    }

}

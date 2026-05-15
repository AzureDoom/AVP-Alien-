package com.alien.common.gameplay.hive2.tick;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.convoy.Convoy;
import com.alien.common.gameplay.hive2.convoy.ConvoyArrival;
import com.alien.common.gameplay.hive2.convoy.ConvoyBossBars;
import com.alien.common.gameplay.hive2.convoy.ConvoyId;
import com.alien.common.gameplay.hive2.convoy.ConvoyInterception;
import com.alien.common.gameplay.hive2.convoy.ConvoyTravel;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
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
        var activeConvoyIds = new java.util.HashSet<ConvoyId>();

        for (var factionId : new java.util.ArrayList<>(Alien.MOD.factions().getAllIds())) {
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
                activeConvoyIds.add(convoy.id());

                if (convoy instanceof Convoy.Raid raid) {
                    if (handleRaidExpiry(raid, server)) {
                        ConvoyBossBars.remove(convoy);
                        activeConvoyIds.remove(convoy.id());
                        iterator.remove();
                        anyChanged = true;
                        continue;
                    }
                    updateRaidTargetPos(raid, server);
                }

                ConvoyBossBars.tick(server, convoy, lineage, config);

                if (ConvoyInterception.tryIntercept(server, convoy, config)) {
                    ConvoyBossBars.remove(convoy);
                    activeConvoyIds.remove(convoy.id());
                    iterator.remove();
                    anyChanged = true;
                    continue;
                }

                ConvoyTravel.tick(convoy, config);

                if (ConvoyArrival.checkArrival(server, convoy, lineage, config)) {
                    ConvoyBossBars.remove(convoy);
                    activeConvoyIds.remove(convoy.id());
                    iterator.remove();
                    anyChanged = true;
                }
            }

            if (anyChanged) {
                lineage.markDirty();
            }
        }

        ConvoyBossBars.retain(activeConvoyIds);
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
     * to the source hive location when it still exists and signals the caller to drop the convoy.
     */
    private static boolean handleRaidExpiry(Convoy.Raid raid, MinecraftServer server) {
        var currentTick = server.overworld().getGameTime();
        if (currentTick < raid.expiresAtTick()) {
            return false;
        }

        Alien.LOGGER.info(
            "Raid {} expired at tick {} (dispatched at {}) — refunding composition to source location",
            raid.id(),
            currentTick,
            raid.dispatchedTick()
        );
        refundToSourceLocation(raid);
        return true;
    }

    private static void refundToSourceLocation(Convoy.Raid raid) {
        var source = HiveLocationRegistry.INSTANCE.get(raid.sourceLocationId());
        if (source == null || !source.isAlive()) {
            Alien.LOGGER.warn(
                "Hive2: raid {} could not refund {} member(s) because source location {} is gone",
                raid.id(),
                raid.composition().getCount(),
                raid.sourceLocationId()
            );
            return;
        }
        addCompositionToLocation(source, raid);
    }

    private static void addCompositionToLocation(HiveLocation location, Convoy convoy) {
        for (var entityType : convoy.composition().getAvailableEntityTypes()) {
            var count = convoy.composition().getCount(entityType);
            if (count > 0) {
                location.localReserves().tryAdd(entityType, count);
            }
        }
    }
}

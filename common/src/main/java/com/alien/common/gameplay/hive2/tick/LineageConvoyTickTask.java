package com.alien.common.gameplay.hive2.tick;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.convoy.Convoy;
import com.alien.common.gameplay.hive2.convoy.ConvoyArrival;
import com.alien.common.gameplay.hive2.convoy.ConvoyBossBars;
import com.alien.common.gameplay.hive2.convoy.ConvoyId;
import com.alien.common.gameplay.hive2.convoy.ConvoyInterception;
import com.alien.common.gameplay.hive2.convoy.ConvoyMemberTracker;
import com.alien.common.gameplay.hive2.convoy.ConvoyTravel;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.registry.init.AlienMobEffects;
import com.alien.common.registry.init.AlienSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;

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

    private static final long RAID_WARNING_LEAD_TICKS = 20L * 60L;

    private static final int MARKED_FOR_DEATH_ACTIVE_RAID_TICKS = 20 * 10;

    private static final int MARKED_FOR_DEATH_DURATION_DRIFT_TICKS = 20;

    private LineageConvoyTickTask() {}

    public static void run(MinecraftServer server) {
        var config = HiveLocationRegistry.INSTANCE.config();
        var currentTick = server.overworld().getGameTime();
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
                    if (!raid.returningHome() && shouldReturnHome(raid, server)) {
                        if (beginRaidReturnHome(raid, server, lineage)) {
                            ConvoyBossBars.remove(convoy);
                            activeConvoyIds.remove(convoy.id());
                            iterator.remove();
                            anyChanged = true;
                            continue;
                        }
                        anyChanged = true;
                    }
                }

                if (ConvoyMemberTracker.returnMissingMaterializedMembers(server, convoy) > 0) {
                    anyChanged = true;
                }

                if (convoy instanceof Convoy.Raid raid) {
                    if (raid.composition().getCount() <= 0 && raid.materializedMembers().isEmpty()) {
                        ConvoyBossBars.remove(convoy);
                        activeConvoyIds.remove(convoy.id());
                        iterator.remove();
                        anyChanged = true;
                        continue;
                    }

                    if (!raid.returningHome()) {
                        updateRaidTargetPos(raid, server);
                        refreshMarkedForDeath(raid, server, config);
                        maybeWarnRaidTarget(raid, server, lineage, config);
                        if (raid.shouldStartWaveBreak()) {
                            raid.startWaveBreak(currentTick);
                            anyChanged = true;
                        }
                    }
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

                if (
                    ConvoyMemberTracker.returnDistantMaterializedMembers(
                        server,
                        convoy,
                        materializedMemberLeashDistanceSqr(config)
                    ) > 0
                ) {
                    anyChanged = true;
                }

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

    private static void refreshMarkedForDeath(Convoy.Raid raid, MinecraftServer server, HiveConfig config) {
        var player = server.getPlayerList().getPlayer(raid.targetPlayerId());
        if (player == null || !player.isAlive()) {
            return;
        }

        var duration = markedForDeathDurationTicks(raid, config);
        var currentEffect = player.getEffect(AlienMobEffects.getMarkedForDeathHolder());
        if (
            currentEffect != null
                && Math.abs(currentEffect.getDuration() - duration) <= MARKED_FOR_DEATH_DURATION_DRIFT_TICKS
        ) {
            return;
        }

        player.forceAddEffect(
            new MobEffectInstance(
                AlienMobEffects.getMarkedForDeathHolder(),
                duration,
                0,
                false,
                false,
                true
            ),
            null
        );
    }

    private static int markedForDeathDurationTicks(Convoy.Raid raid, HiveConfig config) {
        if (!raid.materializedMembers().isEmpty()) {
            return MARKED_FOR_DEATH_ACTIVE_RAID_TICKS;
        }

        var ticksToArrival = ConvoyTravel.ticksToArrival(raid, config);
        if (ticksToArrival <= 0L || ticksToArrival == Long.MAX_VALUE) {
            return MARKED_FOR_DEATH_ACTIVE_RAID_TICKS;
        }
        if (ticksToArrival >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) ticksToArrival;
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

    private static boolean shouldReturnHome(Convoy.Raid raid, MinecraftServer server) {
        var player = server.getPlayerList().getPlayer(raid.targetPlayerId());
        return player != null && (!player.isAlive() || player.isCreative() || player.isSpectator());
    }

    private static double materializedMemberLeashDistanceSqr(HiveConfig config) {
        var distance = config.manifestDistanceBlocks();
        return (double) distance * distance;
    }

    private static boolean beginRaidReturnHome(Convoy.Raid raid, MinecraftServer server, LineageFactionData lineage) {
        var recalled = ConvoyMemberTracker.recallMaterializedMembers(server, raid);
        var destination = pickReturnLocation(raid, lineage);
        if (destination == null) {
            Alien.LOGGER.info(
                "Hive2: raid {} completed against player {} but no live return location exists; disbanding {} member(s)",
                raid.id(),
                raid.targetPlayerId(),
                raid.composition().getCount()
            );
            return true;
        }

        if (raid.composition().getCount() <= 0) {
            Alien.LOGGER.info(
                "Hive2: raid {} completed against player {} with no surviving members to return",
                raid.id(),
                raid.targetPlayerId()
            );
            return true;
        }

        raid.beginReturnHome(destination.id(), destination.centerPos());
        lineage.markDirty();
        Alien.LOGGER.info(
            "Hive2: raid {} completed against player {}; returning {} member(s) to location {} (recalled {})",
            raid.id(),
            raid.targetPlayerId(),
            raid.composition().getCount(),
            destination.id(),
            recalled
        );
        return false;
    }

    private static HiveLocation pickReturnLocation(Convoy.Raid raid, LineageFactionData lineage) {
        var source = HiveLocationRegistry.INSTANCE.get(raid.sourceLocationId());
        if (source != null && source.isAlive()) {
            return source;
        }

        HiveLocation best = null;
        var bestDistance = Double.MAX_VALUE;
        for (var location : lineage.locationsById().values()) {
            if (!location.isAlive() || !location.dimension().equals(raid.dimension())) {
                continue;
            }
            var dx = location.centerPos().getX() - raid.currentPos().x;
            var dz = location.centerPos().getZ() - raid.currentPos().z;
            var distance = dx * dx + dz * dz;
            if (distance < bestDistance) {
                bestDistance = distance;
                best = location;
            }
        }
        return best;
    }

    private static void maybeWarnRaidTarget(
        Convoy.Raid raid,
        MinecraftServer server,
        LineageFactionData lineage,
        HiveConfig config
    ) {
        if (raid.warningIssued()) {
            return;
        }

        var player = server.getPlayerList().getPlayer(raid.targetPlayerId());
        if (player == null || !player.isAlive()) {
            return;
        }
        if (!player.level().dimension().equals(raid.dimension())) {
            return;
        }

        var ticksToArrival = ConvoyTravel.ticksToArrival(raid, config);
        if (ticksToArrival > RAID_WARNING_LEAD_TICKS) {
            return;
        }

        player.playNotifySound(AlienSoundEvents.ENTITY_QUEEN_SCREAM.get(), SoundSource.MASTER, 1.0F, 1.0F);
        player.sendSystemMessage(
            Component.literal("A distant screech answers your violence...")
                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC)
        );
        raid.setWarningIssued(true);
        lineage.markDirty();
    }

}

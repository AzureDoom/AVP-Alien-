package com.alien.common.gameplay.hive2.convoy;

import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.util.AlienPredicates;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Server-side boss bars for abstract convoys. Visibility follows the convoy's abstract position.
 */
public final class ConvoyBossBars {

    private static final Map<ConvoyId, BarState> BARS = new HashMap<>();

    private ConvoyBossBars() {}

    public static void tick(MinecraftServer server, Convoy convoy, LineageFactionData lineage, HiveConfig config) {
        var state = BARS.computeIfAbsent(convoy.id(), id -> createState(convoy));
        var currentTick = server.overworld().getGameTime();
        state.bossEvent().setName(title(convoy));
        state.bossEvent().setColor(colorFor(convoy));
        state.bossEvent().setProgress(progress(convoy, state.initialCount(), currentTick));
        updateTrackingPlayers(server, convoy, lineage, config, state.bossEvent());
    }

    public static void remove(Convoy convoy) {
        var state = BARS.remove(convoy.id());
        if (state != null) {
            state.bossEvent().removeAllPlayers();
        }
    }

    public static void retain(Set<ConvoyId> activeIds) {
        var iterator = BARS.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (activeIds.contains(entry.getKey())) {
                continue;
            }
            entry.getValue().bossEvent().removeAllPlayers();
            iterator.remove();
        }
    }

    public static void clear() {
        for (var state : BARS.values()) {
            state.bossEvent().removeAllPlayers();
        }
        BARS.clear();
    }

    private static BarState createState(Convoy convoy) {
        var event = new ServerBossEvent(title(convoy), colorFor(convoy), BossEvent.BossBarOverlay.PROGRESS);
        event.setProgress(1.0F);
        return new BarState(Math.max(1, memberCount(convoy)), event);
    }

    private static Component title(Convoy convoy) {
        if (convoy instanceof Convoy.Raid raid) {
            return Component.literal("Raid - wave " + (raid.displayWaveIndex() + 1) + "/" + Convoy.Raid.WAVE_COUNT);
        }
        return Component.literal("Convoy: " + typeName(convoy) + " (" + memberCount(convoy) + ")");
    }

    private static String typeName(Convoy convoy) {
        if (convoy instanceof Convoy.Raid) {
            return "Raid";
        }
        if (convoy instanceof Convoy.Migration) {
            return "Migration";
        }
        if (convoy instanceof Convoy.Reinforcement) {
            return "Reinforcement";
        }
        return "Unknown";
    }

    private static BossEvent.BossBarColor colorFor(Convoy convoy) {
        if (convoy instanceof Convoy.Raid) {
            return BossEvent.BossBarColor.RED;
        }
        if (convoy instanceof Convoy.Migration) {
            return BossEvent.BossBarColor.YELLOW;
        }
        return BossEvent.BossBarColor.WHITE;
    }

    private static float progress(Convoy convoy, int initialCount, long currentTick) {
        if (convoy instanceof Convoy.Raid raid) {
            return raidProgress(raid, currentTick);
        }

        var initial = Math.max(1, initialCount);
        var current = Math.max(0, memberCount(convoy));
        return Math.clamp(current / (float) initial, 0.0F, 1.0F);
    }

    private static float raidProgress(Convoy.Raid raid, long currentTick) {
        if (
            raid.waveBreakStartedTick() >= 0L
                && raid.materializedMembers().isEmpty()
                && raid.composition().getCount() > 0
        ) {
            return raid.waveBreakProgress(currentTick);
        }
        if (raid.activeWaveInitialCount() <= 0) {
            return 1.0F;
        }
        return Math.clamp(raid.materializedMembers().size() / (float) raid.activeWaveInitialCount(), 0.0F, 1.0F);
    }

    private static int memberCount(Convoy convoy) {
        return convoy.composition().getCount() + convoy.materializedMembers().size();
    }

    private static void updateTrackingPlayers(
        MinecraftServer server,
        Convoy convoy,
        LineageFactionData lineage,
        HiveConfig config,
        ServerBossEvent bossEvent
    ) {
        var level = server.getLevel(convoy.dimension());
        if (level == null) {
            bossEvent.removeAllPlayers();
            return;
        }

        var radius = config.manifestDistanceBlocks();
        var radiusSqr = (double) radius * radius;

        for (var player : level.players()) {
            var inRangeNow = player.position().distanceToSqr(convoy.currentPos()) <= radiusSqr
                && AlienPredicates.isValidTarget(lineage.variant(), player);

            if (inRangeNow && !bossEvent.getPlayers().contains(player)) {
                bossEvent.addPlayer(player);
            }
        }

        var toRemove = bossEvent.getPlayers()
            .stream()
            .filter(player -> shouldRemove(player, convoy, lineage, radiusSqr))
            .toList();

        toRemove.forEach(bossEvent::removePlayer);
    }

    private static boolean shouldRemove(ServerPlayer player, Convoy convoy, LineageFactionData lineage, double radiusSqr) {
        if (!player.level().dimension().equals(convoy.dimension())) {
            return true;
        }
        if (!AlienPredicates.isValidTarget(lineage.variant(), player)) {
            return true;
        }
        return player.position().distanceToSqr(convoy.currentPos()) > radiusSqr;
    }

    private record BarState(
        int initialCount,
        ServerBossEvent bossEvent
    ) {}
}

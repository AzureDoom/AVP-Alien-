package com.alien.common.gameplay.hive2.location;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.property.AlienProperties;
import com.alien.common.property.AlienPropertyAccess;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.alien.common.util.AlienPredicates;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Per-location boss bar. Visible to players within {@link HiveConfig#bossBarDisplayRadiusBlocks()} of the location's
 * center. Title and color follow the lineage variant.
 * <p>
 * Progress = (loaded xenomorphs inside this location's chunks + xenomorphs in local reserves) /
 * {@code peakXenomorphCount}. The peak decays at 1 per minute (fixes {@code HIVE_SYSTEM_ANALYSIS.md} § 9.5.22) and is
 * floored at 1 (fixes § 9.1.4 NaN).
 * <p>
 * Two display states ride on top:
 * <ul>
 * <li><b>Angry</b> — any player is on the bar (no special visual; meant as the implicit default state behavior driver
 * for later phases that pause claims, suppress shedding, etc.).</li>
 * <li><b>Evacuating</b> — set by Phase 8b migration dispatch. Color shifts to YELLOW, title gets " (Evacuating)"
 * appended. Held for {@link HiveConfig#migrationRallyTicks()}.</li>
 * </ul>
 * <p>
 * See {@code HIVE_REDESIGN_04_BOSS_BAR.md}.
 */
public final class HiveLocationBossBar {

    private static final long PEAK_DECAY_INTERVAL_TICKS = 20L * 60L; // 1 per minute

    private static final long VISIBILITY_REEVAL_INTERVAL_TICKS = 20L;

    private static final Predicate<EntityType<?>> XENOMORPH_PREDICATE = type -> type.is(AlienEntityTypeTags.XENOMORPHS);

    private static final Map<AlienVariant, String> VARIANT_TITLE_KEYS = Arrays.stream(AlienVariant.values())
        .collect(
            Collectors.toMap(
                Function.identity(),
                variant -> "bossbar.avp.hive." + variant.name().toLowerCase(Locale.US) + ".title"
            )
        );

    private final HiveLocation location;

    private final Supplier<HiveConfig> configSupplier;

    private final ServerBossEvent bossEvent;

    public HiveLocationBossBar(HiveLocation location, AlienVariant variant, Supplier<HiveConfig> configSupplier) {
        this.location = location;
        this.configSupplier = configSupplier;
        this.bossEvent = (ServerBossEvent) new ServerBossEvent(
            titleComponent(variant, 0, false),
            AlienVariantTypes.getFor(variant).bossBarColor(),
            BossEvent.BossBarOverlay.PROGRESS
        ).setDarkenScreen(AlienPropertyAccess.INSTANCE.getOrThrow(AlienProperties.Hive.DARKEN_SCREEN));
    }

    public void tick(MinecraftServer server, AlienVariant variant, LineageFactionData lineage) {
        decayPeak();
        decayEvacuating();
        var xenomorphCount = updateProgress(lineage);
        updateColorAndTitle(variant, xenomorphCount);
        updateTrackingPlayers(server, variant);
    }

    private void decayPeak() {
        var elapsed = location.peakDecayElapsedTicks() + 1L;
        var peak = location.peakXenomorphCount();

        while (elapsed >= PEAK_DECAY_INTERVAL_TICKS) {
            elapsed -= PEAK_DECAY_INTERVAL_TICKS;
            peak = Math.max(1, peak - 1);
        }

        location.setPeakDecayElapsedTicks(elapsed);
        location.setPeakXenomorphCount(peak);
    }

    private void decayEvacuating() {
        var remaining = location.evacuatingRemainingTicks();
        if (remaining > 0) {
            location.setEvacuatingRemainingTicks(remaining - 1);
        }
    }

    private int updateProgress(LineageFactionData lineage) {
        var loadedHere = countMatchingLoadedMembers(XENOMORPH_PREDICATE);
        var inReserves = location.localReserves().getCountMatching(XENOMORPH_PREDICATE);

        // Reference `lineage` to satisfy the param contract — Phase 4+ may need
        // pool-aware adjustments here.
        if (lineage == null) {
            return 0;
        }

        var total = loadedHere + inReserves;
        var peak = Math.max(location.peakXenomorphCount(), Math.max(1, total));
        location.setPeakXenomorphCount(peak);
        bossEvent.setProgress(total / (float) peak);
        return total;
    }

    private int countMatchingLoadedMembers(Predicate<EntityType<?>> predicate) {
        var count = 0;
        for (var entry : location.loadedMembersByType().entrySet()) {
            if (predicate.test(entry.getKey())) {
                count += entry.getValue().size();
            }
        }
        return count;
    }

    private void updateColorAndTitle(AlienVariant variant, int xenomorphCount) {
        var evacuating = location.evacuatingRemainingTicks() > 0;
        if (evacuating) {
            bossEvent.setColor(BossEvent.BossBarColor.YELLOW);
        } else {
            bossEvent.setColor(AlienVariantTypes.getFor(variant).bossBarColor());
        }
        bossEvent.setName(titleComponent(variant, xenomorphCount, evacuating));
    }

    private static Component titleComponent(AlienVariant variant, int xenomorphCount, boolean evacuating) {
        var title = Component.translatable(VARIANT_TITLE_KEYS.get(variant))
            .append(Component.literal(" - " + xenomorphCount));

        if (evacuating) {
            title.append(Component.literal(" (Evacuating)"));
        }

        return title;
    }

    private void updateTrackingPlayers(MinecraftServer server, AlienVariant variant) {
        if (location.ageInTicks() % VISIBILITY_REEVAL_INTERVAL_TICKS != 0) {
            return;
        }

        var level = server.getLevel(location.dimension());
        if (level == null) {
            bossEvent.removeAllPlayers();
            return;
        }

        var radius = configSupplier.get().bossBarDisplayRadiusBlocks();
        var radiusSqr = (double) radius * radius;

        // Add players newly in range.
        for (var player : level.players()) {
            var inRangeNow = player.blockPosition().distSqr(location.centerPos()) <= radiusSqr
                && AlienPredicates.isValidTarget(variant, player);

            if (inRangeNow && !bossEvent.getPlayers().contains(player)) {
                bossEvent.addPlayer(player);
            }
        }

        // Remove players who left, changed dimension, or died.
        var toRemove = bossEvent.getPlayers()
            .stream()
            .filter(player -> shouldRemove(player, variant, radiusSqr))
            .toList();

        toRemove.forEach(bossEvent::removePlayer);
    }

    private boolean shouldRemove(ServerPlayer player, AlienVariant variant, double radiusSqr) {
        if (!player.level().dimension().equals(location.dimension())) {
            return true;
        }
        if (!AlienPredicates.isValidTarget(variant, player)) {
            return true;
        }
        return player.blockPosition().distSqr(location.centerPos()) > radiusSqr;
    }

    /** Drops every player from the bar. Called on location removal. */
    public void onRemoved() {
        bossEvent.removeAllPlayers();
    }

    public boolean isAngry() {
        return !bossEvent.getPlayers().isEmpty();
    }

    public boolean isEvacuating() {
        return location.evacuatingRemainingTicks() > 0;
    }

    /** Used by Phase 8b migration dispatch to push the boss bar into the Evacuating window. */
    public void enterEvacuating(long durationTicks) {
        var current = location.evacuatingRemainingTicks();
        location.setEvacuatingRemainingTicks(Math.max(current, durationTicks));
    }
}

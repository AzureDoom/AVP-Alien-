package com.alien.common.gameplay.hive2.convoy;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.gameplay.hive2.location.HiveLocationReserves;
import com.blib.api.common.entity.v1.EntityReserves;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Periodic empress task: scans pairs of sister locations within a lineage and dispatches reinforcement convoys from
 * donors with surplus to receivers that are low.
 * <p>
 * Phase 8 ships a simple "surplus/deficit by total reserve count" heuristic. Phase 8b can refine with per-type analysis
 * or react to combat damage in real time.
 * <p>
 * Per {@code HIVE_REDESIGN_06_CONVOYS.md} § 4: empress-gated, per-source dispatch cooldown, 4–10 members per convoy.
 */
public final class ReinforcementDispatcher {

    /** Per-source-location cooldown tracker (last dispatch game-tick). */
    private static final Map<HiveLocationId, Long> lastDispatchTickByLocation = new HashMap<>();

    private ReinforcementDispatcher() {}

    public static void clear() {
        lastDispatchTickByLocation.clear();
    }

    /**
     * Scans every loaded lineage and dispatches reinforcements where appropriate. Cheap to call on a periodic schedule
     * (every few seconds is plenty); per-source cooldowns gate aggressive dispatch.
     */
    public static void scanAndDispatch(MinecraftServer server) {
        var currentTick = server.overworld().getGameTime();
        var config = HiveLocationRegistry.INSTANCE.config();

        for (var factionId : new ArrayList<>(Alien.MOD.factions().getAllIds())) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }
            if (lineage.empressId() == null) {
                continue;
            }
            if (lineage.locationsById().size() < 2) {
                continue;
            }

            scanLineage(factionId, lineage, currentTick, config);
        }
    }

    private static void scanLineage(
        ResourceLocation lineageFactionId,
        LineageFactionData lineage,
        long currentTick,
        HiveConfig config
    ) {
        var donors = new ArrayList<HiveLocation>();
        var receivers = new ArrayList<HiveLocation>();

        for (var location : lineage.locationsById().values()) {
            if (!location.isAlive()) {
                continue;
            }
            var reserveTotal = location.localReserves().getCount();
            var threshold = Math.max(1, config.populationPerChunk());

            if (reserveTotal >= threshold * 2) {
                donors.add(location);
            } else if (reserveTotal < threshold / 2) {
                receivers.add(location);
            }
        }

        if (donors.isEmpty() || receivers.isEmpty()) {
            return;
        }

        for (var receiver : receivers) {
            var donor = pickClosestEligibleDonor(receiver, donors, currentTick, config);
            if (donor == null) {
                continue;
            }

            var convoy = mintConvoy(donor, receiver, lineageFactionId, currentTick, config);
            if (convoy == null) {
                continue;
            }

            lineage.convoys().add(convoy);
            lineage.markDirty();
            lastDispatchTickByLocation.put(donor.id(), currentTick);

            Alien.LOGGER.info(
                "Hive2: dispatched reinforcement convoy {} ({} members) from {} to {} in lineage {}",
                convoy.id(),
                convoy.composition().getCount(),
                donor.id(),
                receiver.id(),
                lineageFactionId
            );
        }
    }

    private static @Nullable HiveLocation pickClosestEligibleDonor(
        HiveLocation receiver,
        List<HiveLocation> donors,
        long currentTick,
        HiveConfig config
    ) {
        HiveLocation best = null;
        var bestDistSqr = Double.MAX_VALUE;

        for (var donor : donors) {
            if (donor.id().equals(receiver.id())) {
                continue;
            }

            var lastDispatch = lastDispatchTickByLocation.get(donor.id());
            if (lastDispatch != null && currentTick - lastDispatch < config.reinforcementSourceCooldownTicks()) {
                continue;
            }

            var distSqr = donor.centerPos().distSqr(receiver.centerPos());
            if (distSqr < bestDistSqr) {
                bestDistSqr = distSqr;
                best = donor;
            }
        }

        return best;
    }

    private static @Nullable Convoy.Reinforcement mintConvoy(
        HiveLocation donor,
        HiveLocation receiver,
        ResourceLocation lineageFactionId,
        long currentTick,
        HiveConfig config
    ) {
        var available = donor.localReserves().getCount();
        if (available <= 0) {
            return null;
        }

        var size = Math.min(available, config.reinforcementMaxSize());
        size = Math.max(size, Math.min(available, config.reinforcementMinSize()));

        var composition = drainComposition(donor.localReserves(), size);
        if (composition.getCount() == 0) {
            return null;
        }

        var sourceCenter = new Vec3(
            donor.centerPos().getX() + 0.5,
            donor.centerPos().getY() + 0.5,
            donor.centerPos().getZ() + 0.5
        );

        return new Convoy.Reinforcement(
            ConvoyId.fresh(),
            lineageFactionId,
            donor.dimension(),
            donor.id(),
            receiver.id(),
            sourceCenter,
            receiver.centerPos(),
            composition,
            currentTick
        );
    }

    /** Draws up to {@code count} members from the donor's reserves, round-robin across available types. */
    private static EntityReserves drainComposition(HiveLocationReserves donorReserves, int count) {
        var composition = new EntityReserves();
        var remaining = count;
        var available = new ArrayList<>(donorReserves.getAvailableEntityTypes());

        while (remaining > 0 && !available.isEmpty()) {
            var iterator = available.iterator();
            while (iterator.hasNext() && remaining > 0) {
                var type = iterator.next();
                if (donorReserves.trySpawn(type)) {
                    composition.add(type, 1);
                    remaining--;
                    if (donorReserves.getCount(type) <= 0) {
                        iterator.remove();
                    }
                } else {
                    iterator.remove();
                }
            }
        }

        return composition;
    }
}

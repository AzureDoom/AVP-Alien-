package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.model.lifecycle.growth.GrowthStage;
import com.alien.common.registry.GrowthStageRegistry;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Queen-driven reserve promotion. Periodically (default 1 min, configurable via
 * {@link com.alien.common.gameplay.hive2.config.HiveConfig#lineageReservePromotionInterval}) walks every queen-led
 * lineage. For each, picks one of its locations at random, picks one entity type with available reserves and a growth
 * path, and converts one unit (decrement source, increment target).
 * <p>
 * Filters out promotions whose target is a queen — the only path to a queen entity is {@link QueenlessMaturationTask},
 * so reserve-driven queens would break the "1 alien matures into 1 queen" rule.
 * <p>
 * Wired into the {@link com.alien.common.gameplay.hive2.location.HiveLocationRegistry#tick} 5-second dispatcher block
 * with its own counter; the actual promotion runs roughly once per minute.
 */
public final class LineageReservePromotionTask {

    private static final Random RANDOM = new Random();

    private LineageReservePromotionTask() {}

    public static void scanAndPromote(MinecraftServer server) {
        for (var factionId : Alien.MOD.factions().getAllIds()) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }
            if (!hasLivingQueenOrEmpress(server, faction, lineage)) {
                continue;
            }
            promoteOne(lineage, factionId);
        }
    }

    /** Public entry for the debug command. */
    public static boolean forcePromote(MinecraftServer server, ResourceLocation lineageId) {
        var faction = Alien.MOD.factions().get(lineageId);
        if (faction == null || !(faction.data() instanceof LineageFactionData lineage)) {
            return false;
        }
        if (!hasLivingQueenOrEmpress(server, faction, lineage)) {
            return false;
        }
        return promoteOne(lineage, lineageId);
    }

    private static boolean promoteOne(LineageFactionData lineage, ResourceLocation lineageId) {
        var locations = new ArrayList<>(lineage.locationsById().values());
        if (locations.isEmpty()) {
            return false;
        }
        var location = locations.get(RANDOM.nextInt(locations.size()));

        var availableTypes = new ArrayList<>(location.localReserves().getAvailableEntityTypes());
        if (availableTypes.isEmpty()) {
            return false;
        }

        // Shuffle so we don't always promote the first type — gives some variety to which caste advances.
        java.util.Collections.shuffle(availableTypes, RANDOM);

        for (var fromType : availableTypes) {
            if (location.localReserves().getCount(fromType) <= 0) {
                continue;
            }
            var stage = pickPromotionStage(fromType);
            if (stage == null) {
                continue;
            }
            var toType = stage.to();
            location.localReserves().underlying().add(fromType, -1);
            location.localReserves().tryAdd(toType, 1);
            Alien.LOGGER.info(
                "Hive2: queen-driven reserve promotion in {} location {}: {} → {}",
                lineageId,
                location.id(),
                fromType.builtInRegistryHolder().key().location(),
                toType.builtInRegistryHolder().key().location()
            );
            return true;
        }
        return false;
    }

    /**
     * Picks a non-queen growth target for {@code from}. Returns null when no valid promotion exists. Filters out stages
     * that lead to a queen so reserve-driven queens never appear (queens come from {@link QueenlessMaturationTask}
     * only).
     */
    private static @Nullable GrowthStage pickPromotionStage(EntityType<?> from) {
        var candidates = GrowthStageRegistry.getCandidates(null, from);
        var filtered = new ArrayList<GrowthStage>();
        for (var candidate : candidates) {
            if (candidate.to().is(AlienEntityTypeTags.QUEENS)) {
                continue;
            }
            filtered.add(candidate);
        }
        if (filtered.isEmpty()) {
            return null;
        }
        return filtered.get(RANDOM.nextInt(filtered.size()));
    }

    private static boolean hasLivingQueenOrEmpress(
        MinecraftServer server,
        com.blib.api.common.faction.v1.Faction<?> faction,
        LineageFactionData lineage
    ) {
        // Fast-path: an empress is recorded on the lineage, and that's enough.
        if (lineage.empressId() != null) {
            return true;
        }
        // Slow-path: scan loaded membership for a queen entity in any of the lineage's locations.
        var serverLevel = server.getLevel(lineage.dimension());
        if (serverLevel == null) {
            return false;
        }
        for (HiveLocation location : lineage.locationsById().values()) {
            for (var entry : location.loadedMembersByType().entrySet()) {
                if (!entry.getKey().is(AlienEntityTypeTags.QUEENS) && !entry.getKey().is(AlienEntityTypeTags.EMPRESSES)) {
                    continue;
                }
                for (var uuid : entry.getValue()) {
                    var entity = serverLevel.getEntity(uuid);
                    if (entity != null && entity.isAlive()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

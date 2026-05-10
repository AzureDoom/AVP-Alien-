package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;

/**
 * Tracks per-location dormancy. A location is considered dormant when no xenomorphs are present in its territory
 * (loaded membership has zero xenomorph entries).
 * <p>
 * Updates {@link HiveLocation#dormantSinceTick()} as the dormancy state transitions. Dispatches to
 * {@link LocationDeathHandler} when the dormancy + zero-ovomorphs condition has held for {@code locationDecayTicks}
 * (default 24h game time).
 * <p>
 * Per {@code HIVE_REDESIGN_03_LOCATIONS.md} § 10.
 */
public final class LocationDormancyTask {

    private LocationDormancyTask() {}

    public static void scanAll(MinecraftServer server) {
        var currentTick = server.overworld().getGameTime();
        var config = HiveLocationRegistry.INSTANCE.config();
        var decayTicks = config.locationDecayTicks();

        for (var factionId : Alien.MOD.factions().getAllIds()) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }

            var serverLevel = server.getLevel(lineage.dimension());
            if (serverLevel == null) {
                continue;
            }

            // Snapshot the locations before iterating since LocationDeathHandler.killNaturalDecay can mutate.
            var locations = new ArrayList<>(lineage.locationsById().values());
            for (var location : locations) {
                if (!location.isAlive()) {
                    continue;
                }

                // 0 claimed chunks → die immediately, regardless of dormancy timer.
                if (location.claimedChunks().isEmpty()) {
                    LocationDeathHandler.killNaturalDecay(serverLevel, location, lineage);
                    continue;
                }

                tickDormancy(serverLevel, location, lineage, currentTick, decayTicks);
            }
        }
    }

    private static void tickDormancy(
        ServerLevel level,
        HiveLocation location,
        LineageFactionData lineage,
        long currentTick,
        long decayTicks
    ) {
        var hasXenomorph = hasAnyLoadedMatching(location, AlienEntityTypeTags.XENOMORPHS);
        if (hasXenomorph) {
            // Active. Reset the dormancy timer.
            if (location.dormantSinceTick() != Long.MIN_VALUE) {
                location.setDormantSinceTick(Long.MIN_VALUE);
            }
            return;
        }

        // No loaded xenomorphs. Start (or continue) the dormancy timer.
        if (location.dormantSinceTick() == Long.MIN_VALUE) {
            location.setDormantSinceTick(currentTick);
            return;
        }

        var elapsed = currentTick - location.dormantSinceTick();
        if (elapsed < decayTicks) {
            return;
        }

        // Dormant long enough — only kill if also no ovomorphs in territory (per § 10).
        if (hasAnyLoadedMatching(location, AlienEntityTypeTags.OVOMORPHS)) {
            return;
        }

        LocationDeathHandler.killNaturalDecay(level, location, lineage);
    }

    private static boolean hasAnyLoadedMatching(HiveLocation location, TagKey<EntityType<?>> tag) {
        for (var entry : location.loadedMembersByType().entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            if (entry.getKey().is(tag)) {
                return true;
            }
        }
        return false;
    }
}

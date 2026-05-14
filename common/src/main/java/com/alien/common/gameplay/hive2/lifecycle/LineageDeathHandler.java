package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.faction.LineageRemovalReason;
import com.alien.common.gameplay.hive2.id.LineageIds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;

/**
 * Per-tick lineage death check. No grace periods — runs every server tick from
 * {@link com.alien.common.gameplay.hive2.location.HiveLocationRegistry#tick}.
 * <ul>
 * <li><b>Members empty AND locationsById non-empty</b> → kill every owned location via
 * {@link LocationDeathHandler#killNaturalDecay}. The natural-decay path removes the per-location faction and releases
 * chunks.</li>
 * <li><b>Members empty AND locationsById empty</b> → kill the lineage immediately. Sets
 * {@link LineageRemovalReason.NoLocationsRemain} and calls
 * {@code Alien.MOD.factions().remove(lineageId)}.</li>
 * </ul>
 * <p>
 * The shed path ({@code HiveManager.tryShedFromLineages}) removes the last alien synchronously, then discards the
 * entity — so this cleanup fires on the very next tick when {@code members().isEmpty()} is observed. Accepted: this is
 * correct for the genuine shed case. Civil war successor wipes (unloaded ex-members never join successor lineages) are
 * a known deferred concern; if observed in playtest, fix in {@code CivilWarHandler} rather than reintroducing global
 * throttling.
 */
public final class LineageDeathHandler {

    private LineageDeathHandler() {}

    /** Per-tick scan: kill locations of empty lineages, then kill empty+locationless lineages. */
    public static void scanAndKill(MinecraftServer server) {
        var deathQueue = new ArrayList<ResourceLocation>();

        for (var factionId : new ArrayList<>(Alien.MOD.factions().getAllIds())) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }

            if (!faction.membership().getMembers().isEmpty()) {
                continue;
            }

            if (!lineage.locationsById().isEmpty()) {
                // Kill all owned locations. The lineage then becomes 0-locations and will be killed on the next branch
                // (same tick if rescanned, or next tick).
                killOwnedLocations(server, factionId, lineage);
            }

            // After location cleanup (or if locations were already empty), the lineage is dead.
            if (lineage.locationsById().isEmpty()) {
                deathQueue.add(factionId);
            }
        }

        for (var lineageId : deathQueue) {
            kill(lineageId);
        }
    }

    private static void killOwnedLocations(
        MinecraftServer server,
        ResourceLocation lineageId,
        LineageFactionData lineage
    ) {
        var serverLevel = server.getLevel(lineage.dimension());
        if (serverLevel == null) {
            Alien.LOGGER.warn(
                "Hive2: lineage {} has 0 members but its dimension {} is not loaded — skipping owned-location cleanup",
                lineageId,
                lineage.dimension().location()
            );
            return;
        }

        var snapshot = new ArrayList<>(lineage.locationsById().values());
        Alien.LOGGER.info(
            "Hive2: lineage {} has 0 members; killing {} owned location(s)",
            lineageId,
            snapshot.size()
        );

        for (var location : snapshot) {
            if (!location.isAlive()) {
                continue;
            }
            LocationDeathHandler.killNaturalDecay(serverLevel, location, lineage);
        }
    }

    /**
     * Forces lineage death now. Sets removal reason and removes from BLib.
     * Idempotent — a lineage already dead returns false.
     */
    public static boolean kill(ResourceLocation lineageId) {
        var faction = Alien.MOD.factions().get(lineageId);
        if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
            return false;
        }

        lineage.setRemovalReason(new LineageRemovalReason.NoLocationsRemain());
        Alien.MOD.factions().remove(lineageId);

        Alien.LOGGER.info(
            "Hive2: lineage {} dead (no locations remain); faction removed",
            lineageId
        );
        return true;
    }

}

package com.alien.common.gameplay.hive2.faction;

import com.alien.Alien;
import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.hive2.growth.ContestResolutionTask;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.lifecycle.CivilWarHandler;
import com.alien.common.gameplay.hive2.lifecycle.LineageAbsorptionTask;
import com.alien.common.gameplay.hive2.lifecycle.LineageDeathHandler;
import com.alien.common.gameplay.hive2.lifecycle.LocationDormancyTask;
import com.alien.common.gameplay.hive2.lifecycle.QueenlessMaturationTask;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Sanity-check pass for lineage faction state. Runs every {@code lineageScanIntervalTicks} (default 5 minutes)
 * alongside the growth scan. Three responsibilities:
 * <ul>
 * <li><b>Variant matching</b>: for each lineage, scan its loaded members across all owned locations. Members whose
 * entity-type variant doesn't match the lineage's variant are evicted from BLib membership. (Phase 9.)</li>
 * <li><b>Civil war dispatch</b>: any lineage with {@code pendingCivilWar=true} is shattered into successor lineages via
 * {@link CivilWarHandler}. (Phase 11.)</li>
 * <li><b>Lifecycle</b>: drive location dormancy + death, lineage absorption, lineage death (Phase 11). Each is a
 * separate static task.</li>
 * </ul>
 * <p>
 * Per {@code HIVE_REDESIGN_01_FACTIONS.md} § 2 + § 7 and {@code HIVE_REDESIGN_02_FACTION_LIFECYCLES.md} § 3–§ 5.
 */
public final class LineageInvariantTask {

    private LineageInvariantTask() {}

    public static void scanAll() {
        scanVariantInvariants();
    }

    /**
     * Full scan including all Phase 11 lifecycle dispatches. Called from
     * {@link com.alien.common.gameplay.hive2.location.HiveLocationRegistry#tick} on the slow cadence.
     */
    public static void scanAllWithLifecycle(MinecraftServer server) {
        // 1. Variant invariants — evict variant-mismatched members.
        scanVariantInvariants();

        // 2. Civil war first — must run before absorption / dormancy so the new successor lineages enter the rest of
        // the pipeline cleanly.
        CivilWarHandler.scanAndHandle(server);

        // 3. Queenless lineage maturation — lets civil-war successors (and any other queenless lineage) advance
        // their leader through the queen-track growth stages over time. Runs after civil war so freshly-minted
        // successor lineages get tagged on the same scan.
        QueenlessMaturationTask.scanAll(server);

        // 4. Location dormancy + death cascade. Runs before lineage death so a 0-locations lineage gets one tick of
        // grace before LineageDeathHandler picks it up.
        LocationDormancyTask.scanAll(server);

        // 5. Lineage absorption — same-variant cross-lineage merging.
        LineageAbsorptionTask.scanAll(server);

        // 6. Contested chunk resolution.
        ContestResolutionTask.scanAll(server);

        // 7. Lineage death — picks up 0-locations lineages whose grace period has elapsed.
        LineageDeathHandler.scanAndKill(server);
    }

    private static void scanVariantInvariants() {
        for (var factionId : Alien.MOD.factions().getAllIds()) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }
            scanLineage(faction.membership(), lineage);
        }
    }

    private static void scanLineage(
        com.blib.api.common.faction.v1.FactionMembership membership,
        LineageFactionData lineage
    ) {
        var lineageVariant = lineage.variant();
        var mismatchedUuids = new HashSet<UUID>();

        for (var location : lineage.locationsById().values()) {
            for (var entry : location.loadedMembersByType().entrySet()) {
                if (variantMatches(entry.getKey(), lineageVariant)) {
                    continue;
                }
                mismatchedUuids.addAll(entry.getValue());
            }
        }

        if (mismatchedUuids.isEmpty()) {
            return;
        }

        evictAll(membership, mismatchedUuids, lineage);
    }

    private static boolean variantMatches(EntityType<?> type, com.alien.common.model.alien.variant.AlienVariant lineageVariant) {
        var variantTypeOption = AlienVariantTypes.getFor(type);
        if (variantTypeOption.isNone()) {
            // Non-alien entity type ended up in our membership somehow — treat as mismatch (will be evicted).
            return false;
        }
        return variantTypeOption.unwrap().variant() == lineageVariant;
    }

    private static void evictAll(
        com.blib.api.common.faction.v1.FactionMembership membership,
        Set<UUID> uuids,
        LineageFactionData lineage
    ) {
        // Snapshot to avoid concurrent-modification when removeMember fires onMemberRemoved which mutates
        // location loadedMembersByType.
        var snapshot = new ArrayList<>(uuids);
        for (var uuid : snapshot) {
            membership.removeMember(FactionMember.entity(uuid));
        }

        Alien.LOGGER.info(
            "Hive2: LineageInvariantTask evicted {} variant-mismatched member(s) from lineage variant={}",
            snapshot.size(),
            lineage.variant()
        );
    }
}

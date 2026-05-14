package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.empress.Empress;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.hive2.convoy.Convoy;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.faction.LineageRemovalReason;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.faction.v1.Faction;
import com.blib.api.common.faction.v1.FactionMember;
import com.blib.api.common.faction.v1.FactionMembership;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Performs the actual lineage absorption per {@code HIVE_REDESIGN_02_FACTION_LIFECYCLES.md} § 5.3.
 * <p>
 * Picks the stronger of the two by the comparator chain (member count → empress age → founder age → lineage age → lex
 * compare). The weaker is destroyed: every location reparents to the stronger, members move to the stronger's BLib
 * membership, in-flight convoys are refunded to surviving hive locations, and the weaker's empress is killed (if both
 * had empresses) or demoted to a queen entity (if only the weaker had one).
 */
public final class LineageAbsorptionHandler {

    private LineageAbsorptionHandler() {}

    /**
     * Compares the two lineages, picks the stronger, and absorbs the weaker into it.
     *
     * @return the survivor's faction id, or null if absorption was skipped (one of the inputs was already removed).
     */
    public static @Nullable ResourceLocation absorb(
        MinecraftServer server,
        ResourceLocation idA,
        LineageFactionData a,
        ResourceLocation idB,
        LineageFactionData b
    ) {
        if (!a.isAlive() || !b.isAlive()) {
            return null;
        }

        var serverLevel = server.getLevel(a.dimension());
        if (serverLevel == null) {
            Alien.LOGGER.warn(
                "Hive2: absorption {} ↔ {} skipped — dimension {} not loaded",
                idA,
                idB,
                a.dimension().location()
            );
            return null;
        }

        var aIsStronger = compareStrength(server, idA, a, idB, b) > 0;
        var strongerId = aIsStronger ? idA : idB;
        var weakerId = aIsStronger ? idB : idA;
        var stronger = aIsStronger ? a : b;
        var weaker = aIsStronger ? b : a;

        var strongerFaction = Alien.MOD.factions().get(strongerId);
        var weakerFaction = Alien.MOD.factions().get(weakerId);

        if (strongerFaction == null || weakerFaction == null) {
            return null;
        }

        // 1. Reparent every location of the weaker to the stronger.
        transferLocations(weaker, stronger, strongerId);

        // 2. Transfer members.
        transferMembers(weakerFaction.membership(), strongerFaction.membership(), serverLevel);

        // 3. Remap or disband convoys.
        remapConvoys(weaker, stronger);

        // 4. Empress handling.
        handleEmpresses(serverLevel, weaker, stronger, strongerFaction);

        // 5. Cleanup: clear timers, mark removed, drop from BLib.
        weaker.firstAdjacentTickByLineage().clear();
        stronger.firstAdjacentTickByLineage().remove(weakerId);
        weaker.setRemovalReason(new LineageRemovalReason.AbsorbedBy(strongerId));
        Alien.MOD.factions().remove(weakerId);

        Alien.LOGGER.info(
            "Hive2: lineage {} absorbed lineage {} (stronger now has {} locations, {} members)",
            strongerId,
            weakerId,
            stronger.locationsById().size(),
            strongerFaction.membership().getMembers().size()
        );

        return strongerId;
    }

    /** Higher = stronger. Stable + total order — same comparator order every time, so result is reproducible. */
    private static int compareStrength(
        MinecraftServer server,
        ResourceLocation idA,
        LineageFactionData a,
        ResourceLocation idB,
        LineageFactionData b
    ) {
        // 1. Total living xenomorph member count.
        var aMembers = livingXenomorphCount(server, idA, a);
        var bMembers = livingXenomorphCount(server, idB, b);
        var memberCmp = Integer.compare(aMembers, bMembers);
        if (memberCmp != 0) {
            return memberCmp;
        }

        // 2. Empress age — older empress wins. (Both must be loaded for a meaningful compare.)
        var aEmpressAge = entityAgeOrMin(server, a.dimension(), a.empressId());
        var bEmpressAge = entityAgeOrMin(server, b.dimension(), b.empressId());
        var empressCmp = Integer.compare(aEmpressAge, bEmpressAge);
        if (empressCmp != 0) {
            return empressCmp;
        }

        // 3. Founder queen age — older founder wins.
        var aFounderAge = entityAgeOrMin(server, a.dimension(), a.founderId());
        var bFounderAge = entityAgeOrMin(server, b.dimension(), b.founderId());
        var founderCmp = Integer.compare(aFounderAge, bFounderAge);
        if (founderCmp != 0) {
            return founderCmp;
        }

        // 4. Lineage faction ageInTicks — older lineage wins.
        var ageCmp = Long.compare(a.ageInTicks(), b.ageInTicks());
        if (ageCmp != 0) {
            return ageCmp;
        }

        // 5. Lex compare — final tiebreak. Lex-greater wins (so smaller id loses).
        return idA.compareTo(idB);
    }

    private static int livingXenomorphCount(MinecraftServer server, ResourceLocation lineageId, LineageFactionData lineage) {
        var faction = Alien.MOD.factions().get(lineageId);
        if (faction == null) {
            return 0;
        }
        var serverLevel = server.getLevel(lineage.dimension());
        if (serverLevel == null) {
            // Can't tell who's alive — fall back to counting all members (overestimates a bit, but symmetric).
            return faction.membership().getMembers().size();
        }
        var count = 0;
        for (var member : faction.membership().getMembers()) {
            if (!(member instanceof FactionMember.Entity entityMember)) {
                continue;
            }
            var entity = serverLevel.getEntity(entityMember.uuid());
            if (entity == null) {
                // Unloaded — count as 1 (assume alive). Symmetric overestimation across both sides.
                count++;
                continue;
            }
            if (entity.isAlive() && entity.getType().is(AlienEntityTypeTags.XENOMORPHS)) {
                count++;
            }
        }
        return count;
    }

    private static int entityAgeOrMin(
        MinecraftServer server,
        net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dim,
        @Nullable UUID uuid
    ) {
        if (uuid == null) {
            return Integer.MIN_VALUE;
        }
        var serverLevel = server.getLevel(dim);
        if (serverLevel == null) {
            return Integer.MIN_VALUE;
        }
        var entity = serverLevel.getEntity(uuid);
        if (entity == null) {
            return Integer.MIN_VALUE;
        }
        return entity.tickCount;
    }

    private static void transferLocations(
        LineageFactionData weaker,
        LineageFactionData stronger,
        ResourceLocation strongerId
    ) {
        var locations = new ArrayList<>(weaker.locationsById().values());
        for (var location : locations) {
            // Reparent the record itself.
            location.setLineageFactionId(strongerId);
            // BLib territory is keyed by location faction id; reparenting the location does not move the claim owner.
            stronger.addLocation(location);
            weaker.removeLocation(location.id());
            // Rebuild registry indexes for this location under the stronger lineage id.
            HiveLocationRegistry.INSTANCE.unregister(location.id());
            HiveLocationRegistry.INSTANCE.register(location);
        }
    }

    private static void transferMembers(FactionMembership weakerMembership, FactionMembership strongerMembership, ServerLevel level) {
        // Snapshot first to avoid CME during iteration.
        var snapshot = new ArrayList<>(weakerMembership.getMembers());
        for (var member : snapshot) {
            weakerMembership.removeMember(member);
            if (!(member instanceof FactionMember.Entity entityMember)) {
                continue;
            }
            var entity = level.getEntity(entityMember.uuid());
            if (entity != null) {
                strongerMembership.addEntity(entity);
            }
            // If unloaded: dropped. The next time it loads, the variant-faction join will rejoin it; spawn-gate
            // routes will catch territory reentry.
        }
    }

    private static void remapConvoys(
        LineageFactionData weaker,
        LineageFactionData stronger
    ) {
        // Convoy records are immutable per the sealed interface design. Disband them and refund composition to the
        // closest surviving location now owned by the stronger lineage.
        var convoys = new ArrayList<>(weaker.convoys());
        for (var convoy : convoys) {
            var target = nearestLocationToConvoy(convoy, new ArrayList<>(stronger.locationsById().values()));
            if (target == null) {
                continue;
            }
            for (var type : new ArrayList<>(convoy.composition().getAvailableEntityTypes())) {
                var count = convoy.composition().getCount(type);
                if (count > 0) {
                    target.localReserves().tryAdd(type, count);
                    convoy.composition().add(type, -count);
                }
            }
        }
        weaker.convoys().clear();
    }

    private static @Nullable HiveLocation nearestLocationToConvoy(Convoy convoy, ArrayList<HiveLocation> locations) {
        HiveLocation best = null;
        var bestDistSqr = Double.MAX_VALUE;
        for (var location : locations) {
            if (!location.isAlive()) {
                continue;
            }
            var dx = location.centerPos().getX() - convoy.currentPos().x;
            var dz = location.centerPos().getZ() - convoy.currentPos().z;
            var distSqr = dx * dx + dz * dz;
            if (distSqr < bestDistSqr) {
                bestDistSqr = distSqr;
                best = location;
            }
        }
        return best;
    }

    /**
     * Handles the empress per § 5.3:
     * <ul>
     * <li>Both have empresses: kill the weaker's outright (only one empress per lineage).</li>
     * <li>Only weaker has one: demote her to a queen entity at her current position (instant — no public
     * ceremony).</li>
     * <li>Only stronger has one: nothing to do.</li>
     * <li>Neither: nothing to do.</li>
     * </ul>
     */
    private static void handleEmpresses(
        ServerLevel level,
        LineageFactionData weaker,
        LineageFactionData stronger,
        Faction<?> strongerFaction
    ) {
        var weakerEmpressId = weaker.empressId();
        if (weakerEmpressId == null) {
            return;
        }
        var weakerEmpress = level.getEntity(weakerEmpressId);

        if (stronger.empressId() != null) {
            // Both have empresses — kill the weaker's outright.
            if (weakerEmpress != null && weakerEmpress.isAlive()) {
                weakerEmpress.kill();
            }
            weaker.setEmpressId(null);
            return;
        }

        // Only weaker has an empress — demote her to a queen.
        if (weakerEmpress instanceof Empress empress) {
            var pos = empress.blockPosition();
            var variant = empress.getVariant();
            var queenType = Queen.getType(variant);
            var queen = queenType.spawn(level, pos, MobSpawnType.MOB_SUMMONED);
            empress.discard();
            weaker.setEmpressId(null);

            if (queen != null) {
                strongerFaction.membership().addEntity(queen);
            }
        } else {
            // Empress entity isn't loaded or is the wrong type — just clear the slot.
            weaker.setEmpressId(null);
        }
    }
}

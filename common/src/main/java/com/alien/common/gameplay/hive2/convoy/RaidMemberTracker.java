package com.alien.common.gameplay.hive2.convoy;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;

public final class RaidMemberTracker {

    private RaidMemberTracker() {}

    public static void markSpawned(Convoy.Raid raid, Entity entity) {
        raid.trackMaterializedMember(entity.getUUID(), entity.getType());

        var faction = Alien.MOD.factions().get(raid.lineageFactionId());
        if (faction != null && !faction.membership().hasMember(FactionMember.entity(entity))) {
            faction.membership().addEntity(entity);
        }

        if (entity instanceof com.alien.common.gameplay.entity.living.alien.Alien alien) {
            alien.setRaidMembership(new RaidMembership(raid.lineageFactionId(), raid.id()));
        }
        markDirty(new RaidMembership(raid.lineageFactionId(), raid.id()));
    }

    public static boolean returnDespawned(com.alien.common.gameplay.entity.living.alien.Alien alien) {
        var membership = alien.raidMembership();
        if (membership == null) {
            return false;
        }

        var raid = findRaid(membership);
        if (raid == null) {
            alien.clearRaidMembership();
            return false;
        }

        raid.composition().add(alien.getType(), 1);
        raid.untrackMaterializedMember(alien.getUUID());
        markDirty(membership);
        alien.clearRaidMembership();

        Alien.LOGGER.info(
            "Hive2: raid member {} despawned — returned {} to raid {} reserves",
            alien.getUUID(),
            alien.getType(),
            raid.id()
        );
        return true;
    }

    public static void unregisterKilled(com.alien.common.gameplay.entity.living.alien.Alien alien) {
        var membership = alien.raidMembership();
        if (membership == null) {
            return;
        }

        var raid = findRaid(membership);
        if (raid != null) {
            raid.untrackMaterializedMember(alien.getUUID());
            markDirty(membership);
        }
        alien.clearRaidMembership();
    }

    public static int recallMaterializedMembers(MinecraftServer server, Convoy.Raid raid) {
        var level = server.getLevel(raid.dimension());
        var hadTrackedMembers = !raid.materializedMembers().isEmpty();
        var recalled = 0;

        for (var entry : new ArrayList<>(raid.materializedMembers().entrySet())) {
            var entity = level == null ? null : level.getEntity(entry.getKey());
            if (entity != null && entity.isAlive() && !entity.isRemoved()) {
                raid.composition().add(entry.getValue(), 1);
                if (entity instanceof com.alien.common.gameplay.entity.living.alien.Alien alien) {
                    alien.clearRaidMembership();
                }
                entity.discard();
                recalled++;
            }
            raid.untrackMaterializedMember(entry.getKey());
        }

        if (hadTrackedMembers) {
            markDirty(new RaidMembership(raid.lineageFactionId(), raid.id()));
        }
        return recalled;
    }

    private static Convoy.Raid findRaid(RaidMembership membership) {
        var faction = Alien.MOD.factions().get(membership.lineageFactionId());
        if (faction == null || !(faction.data() instanceof LineageFactionData lineage)) {
            return null;
        }

        for (var convoy : lineage.convoys()) {
            if (convoy instanceof Convoy.Raid raid && raid.id().equals(membership.raidId())) {
                return raid;
            }
        }
        return null;
    }

    private static void markDirty(RaidMembership membership) {
        var faction = Alien.MOD.factions().get(membership.lineageFactionId());
        if (faction != null && faction.data() instanceof LineageFactionData lineage) {
            lineage.markDirty();
        }
    }
}

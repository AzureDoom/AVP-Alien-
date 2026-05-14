package com.alien.common.gameplay.hive2.faction;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.registry.init.AlienFactionDataTypes;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.server.level.ServerLevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

/**
 * Helpers for the location-tier faction. Centralizes the lineage-superset invariant
 * ({@code location.members() ⊆ lineage.members()}) so callers can't accidentally add to a location faction without
 * also being in the parent lineage.
 */
public final class LocationMembership {

    private LocationMembership() {}

    /**
     * Idempotent join: ensures the entity is a member of the location's parent lineage faction, then of the location
     * faction itself. Lineage first to maintain the subset invariant.
     */
    public static void join(HiveLocation location, Entity entity) {
        var factions = Alien.MOD.factions();
        var member = FactionMember.entity(entity);

        var lineageFaction = factions.get(location.lineageFactionId());
        if (lineageFaction != null && !lineageFaction.membership().hasMember(member)) {
            lineageFaction.membership().addEntity(entity);
        }

        var locationFaction = factions.getOrCreate(location.id().value(), AlienFactionDataTypes.LOCATION);
        if (!locationFaction.membership().hasMember(member)) {
            locationFaction.membership().addEntity(entity);
        }
    }

    /**
     * Mirrors the auto-join behavior in {@link com.alien.common.gameplay.entity.living.alien.Alien#finalizeSpawn}: if
     * the entity is a xenomorph standing in a live location's claimed chunk, join its lineage and location factions.
     * Idempotent and a no-op outside of any territory.
     */
    public static void autoJoinAtPosition(Entity entity, ServerLevelAccessor level) {
        if (!entity.getType().is(AlienEntityTypeTags.XENOMORPHS)) {
            return;
        }

        var location = HiveLocationRegistry.INSTANCE.getByChunk(
            level.getLevel().dimension(),
            new ChunkPos(entity.blockPosition())
        );
        if (location == null || !location.isAlive()) {
            return;
        }

        join(location, entity);
    }
}

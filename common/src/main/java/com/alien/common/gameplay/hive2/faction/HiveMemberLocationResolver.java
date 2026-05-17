package com.alien.common.gameplay.hive2.faction;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.id.HiveLocationIds;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * Resolves the hive location that should retain a live hive member when it leaves loaded play.
 */
public final class HiveMemberLocationResolver {

    private HiveMemberLocationResolver() {}

    public static @Nullable HiveLocation reserveReturnLocation(Entity entity) {
        var direct = directLocationMembership(entity);
        if (direct != null) {
            return direct;
        }

        return nearestLineageLocation(entity);
    }

    private static @Nullable HiveLocation directLocationMembership(Entity entity) {
        for (var factionId : Alien.MOD.factions().getFactionIds(entity.getUUID())) {
            if (!HiveLocationIds.isHiveLocationId(factionId)) {
                continue;
            }

            var location = HiveLocationRegistry.INSTANCE.get(HiveLocationId.of(factionId));
            if (location != null && location.isAlive()) {
                return location;
            }
        }
        return null;
    }

    private static @Nullable HiveLocation nearestLineageLocation(Entity entity) {
        HiveLocation best = null;
        var bestDistance = Double.MAX_VALUE;
        var entityPos = entity.blockPosition();
        var dimension = entity.level().dimension();

        for (var factionId : Alien.MOD.factions().getFactionIds(entity.getUUID())) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }

            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }

            for (var location : lineage.locationsById().values()) {
                if (!location.isAlive() || !location.dimension().equals(dimension)) {
                    continue;
                }

                var distance = location.centerPos().distSqr(entityPos);
                if (distance < bestDistance) {
                    best = location;
                    bestDistance = distance;
                }
            }
        }

        return best;
    }
}

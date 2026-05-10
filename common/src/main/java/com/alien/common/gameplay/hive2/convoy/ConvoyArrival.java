package com.alien.common.gameplay.hive2.convoy;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.gameplay.hive2.location.HivePoolCascade;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.MobSpawnType;

/**
 * Detects when a convoy has reached its destination and applies the arrival effect.
 * <p>
 * For {@link Convoy.Reinforcement}: pours the entire {@code composition} into the destination location's
 * {@link com.alien.common.gameplay.hive2.location.HiveLocationReserves} via {@link HivePoolCascade} (overflow cascades
 * up to the lineage pool, then variant pool — see {@code HIVE_REDESIGN_05_RESERVES.md} § 6).
 * <p>
 * {@link #checkArrival} returns {@code true} when the arrival fires; the caller should remove the convoy from the
 * lineage's convoy list at that point.
 */
public final class ConvoyArrival {

    private ConvoyArrival() {}

    public static boolean checkArrival(MinecraftServer server, Convoy convoy, LineageFactionData lineage, HiveConfig config) {
        var distance = ConvoyTravel.distanceToTarget(convoy);
        if (distance > config.arrivalRadiusBlocks()) {
            return false;
        }

        if (convoy instanceof Convoy.Reinforcement reinforcement) {
            arriveReinforcement(reinforcement, lineage);
            return true;
        }

        if (convoy instanceof Convoy.Migration migration) {
            arriveMigration(server, migration, lineage);
            return true;
        }

        if (convoy instanceof Convoy.Raid raid) {
            arriveRaid(server, raid, lineage);
            return true;
        }

        Alien.LOGGER.warn("Convoy {} arrived but has no arrival handler for type {}", convoy.id(), convoy.getClass().getName());
        return true;
    }

    private static void arriveReinforcement(Convoy.Reinforcement reinforcement, LineageFactionData lineage) {
        var destinationLocation = HiveLocationRegistry.INSTANCE.get(reinforcement.destinationLocationId());

        if (destinationLocation == null) {
            // Destination died mid-flight. Per HIVE_REDESIGN_06_CONVOYS.md § 8, refund the abstract
            // composition into the lineage pool with cascade.
            Alien.LOGGER.info(
                "Convoy {} arrived but destination location {} is gone — refunding composition to lineage pool",
                reinforcement.id(),
                reinforcement.destinationLocationId()
            );
            for (var entityType : reinforcement.composition().getAvailableEntityTypes()) {
                var count = reinforcement.composition().getCount(entityType);
                HivePoolCascade.addToLineageCascading(lineage, entityType, count);
            }
            return;
        }

        // Pour each type into the destination's local reserves with cascade.
        for (var entityType : reinforcement.composition().getAvailableEntityTypes()) {
            var count = reinforcement.composition().getCount(entityType);
            if (count <= 0) {
                continue;
            }
            HivePoolCascade.addToLocationCascading(destinationLocation, lineage, entityType, count);
        }

        Alien.LOGGER.info(
            "Convoy {} arrived at location {} (lineage {}); composition poured into reserves",
            reinforcement.id(),
            reinforcement.destinationLocationId(),
            reinforcement.lineageFactionId()
        );
    }

    private static void arriveMigration(MinecraftServer server, Convoy.Migration migration, LineageFactionData lineage) {
        var destination = HiveLocationRegistry.INSTANCE.get(migration.destinationLocationId());

        if (destination == null) {
            // Destination died mid-flight. Per HIVE_REDESIGN_06_CONVOYS.md § 8: if migration carries the empress,
            // she becomes a forager (Phase 9 will handle the actual respawn). For Phase 8b, refund composition
            // and biomass to the lineage pool / discard biomass.
            Alien.LOGGER.info(
                "Migration {} arrived but destination location {} is gone — refunding composition to lineage pool",
                migration.id(),
                migration.destinationLocationId()
            );
            for (var entityType : migration.composition().getAvailableEntityTypes()) {
                var count = migration.composition().getCount(entityType);
                HivePoolCascade.addToLineageCascading(lineage, entityType, count);
            }
            return;
        }

        // Pour composition into destination reserves.
        for (var entityType : migration.composition().getAvailableEntityTypes()) {
            var count = migration.composition().getCount(entityType);
            if (count <= 0) {
                continue;
            }
            HivePoolCascade.addToLocationCascading(destination, lineage, entityType, count);
        }

        // Add biomass payload (capped at biomass cap by the location's setter).
        if (migration.biomassPayload() > 0) {
            destination.setBiomass(destination.biomass() + migration.biomassPayload());
        }

        // If carrying empress: respawn her at the destination's center. For Phase 8b, we just log it; Phase 10's
        // empress emergence ritual + entity-respawn machinery will be wired together with this.
        if (migration.carriesEmpress()) {
            Alien.LOGGER.info(
                "Migration {} arrived carrying empress — empress respawn at destination is Phase 10 work",
                migration.id()
            );
        }

        Alien.LOGGER.info(
            "Migration {} arrived at location {} (lineage {}); composition + {} biomass payload delivered",
            migration.id(),
            migration.destinationLocationId(),
            migration.lineageFactionId(),
            migration.biomassPayload()
        );
    }

    /**
     * Raid arrival: the raid has reached the player's last-known position. Spawns the entire composition as real
     * entities at the convoy's current position so the player has something to fight. Composition is emptied — these
     * members are now in the world (combat losses are real losses; survivors despawn naturally).
     */
    private static void arriveRaid(MinecraftServer server, Convoy.Raid raid, LineageFactionData lineage) {
        var serverLevel = server.getLevel(raid.dimension());
        if (serverLevel == null) {
            Alien.LOGGER.info(
                "Raid {} arrived but destination dimension {} is unloaded — refunding composition to lineage pool",
                raid.id(),
                raid.dimension().location()
            );
            for (var entityType : raid.composition().getAvailableEntityTypes()) {
                var count = raid.composition().getCount(entityType);
                HivePoolCascade.addToLineageCascading(lineage, entityType, count);
            }
            return;
        }

        var spawnPos = new BlockPos(
            (int) Math.round(raid.currentPos().x),
            (int) Math.round(raid.currentPos().y),
            (int) Math.round(raid.currentPos().z)
        );

        var spawnedCount = 0;
        for (var entityType : raid.composition().getAvailableEntityTypes()) {
            var count = raid.composition().getCount(entityType);
            for (var i = 0; i < count; i++) {
                var spawned = entityType.spawn(serverLevel, spawnPos, MobSpawnType.MOB_SUMMONED);
                if (spawned != null) {
                    spawnedCount++;
                }
            }
            // Empty the composition entry — these members are now in the world.
            raid.composition().add(entityType, -count);
        }

        Alien.LOGGER.info(
            "Raid {} arrived at {} — spawned {} attackers targeting player {}",
            raid.id(),
            spawnPos,
            spawnedCount,
            raid.targetPlayerId()
        );
    }
}

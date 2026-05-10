package com.alien.common.gameplay.hive2.convoy;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

/**
 * Empress-gated direct-spawn from the lineage pool. Used when no sister can convoy in (single-location lineage, or all
 * sisters tapped). Skips travel time entirely — pool ↓ 1, entity spawns at the location's center.
 * <p>
 * Per {@code HIVE_REDESIGN_06_CONVOYS.md} § 7. Without an empress alive, this path is gated off.
 */
public final class InPlacePoolReinforcement {

    private InPlacePoolReinforcement() {}

    /**
     * Tries to spawn one member of {@code type} at {@code location}'s center, drawing from the lineage pool. Returns
     * true on success.
     * <p>
     * Conditions (all must hold):
     * <ul>
     * <li>Lineage has an empress alive ({@code lineage.empressId() != null}).</li>
     * <li>Lineage pool has at least one of {@code type}.</li>
     * </ul>
     */
    public static boolean tryReinforce(
        ServerLevel level,
        HiveLocation location,
        LineageFactionData lineage,
        EntityType<?> type
    ) {
        if (lineage.empressId() == null) {
            return false;
        }

        if (lineage.lineagePool().getCount(type) <= 0) {
            return false;
        }

        var spawned = type.spawn(level, location.centerPos(), MobSpawnType.MOB_SUMMONED);
        if (spawned == null) {
            return false;
        }

        // Decrement the pool. EntityReserves.add(type, -1) clamps at 0, so we're safe even if the count
        // raced to zero between the check and the decrement.
        lineage.lineagePool().add(type, -1);
        lineage.markDirty();

        Alien.LOGGER.info(
            "Hive2: in-place pool reinforcement spawned {} at location {} (lineage {})",
            type.builtInRegistryHolder().key().location(),
            location.id(),
            location.lineageFactionId()
        );

        return true;
    }
}

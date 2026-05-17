package com.alien.common.gameplay.hive2.convoy;

import com.alien.common.gameplay.hive2.config.HiveConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

/**
 * Per-tick straight-line travel. Moves the convoy's {@code currentPos} toward its destination by
 * {@link ConvoySpeedTable#blocksPerTick} blocks each call. Travel happens whether or not the chunks between source and
 * target are loaded — convoys clip abstractly through the world.
 * <p>
 * Phase 8 only handles fixed-position destinations (reinforcement → destination location's center). Phase 8b will add
 * raid behavior where the destination tracks the target player every tick.
 */
public final class ConvoyTravel {

    private ConvoyTravel() {}

    public static void tick(Convoy convoy, HiveConfig config) {
        var target = targetFor(convoy);
        if (target == null) {
            return;
        }

        var current = convoy.currentPos();
        var direction = target.subtract(current);
        var distance = direction.length();

        if (distance == 0.0) {
            return;
        }

        var step = ConvoySpeedTable.blocksPerTick(convoy, config);

        if (step >= distance) {
            // Snap to target — arrival check fires this tick.
            convoy.setCurrentPos(target);
            return;
        }

        var normalized = direction.scale(1.0 / distance);
        convoy.setCurrentPos(current.add(normalized.scale(step)));
    }

    /**
     * Distance from the convoy's current position to its target. Used by {@link ConvoyArrival} to decide when to fire.
     */
    public static double distanceToTarget(Convoy convoy) {
        var target = targetFor(convoy);
        if (target == null) {
            return Double.MAX_VALUE;
        }
        return convoy.currentPos().distanceTo(target);
    }

    /**
     * Ticks until the convoy reaches its configured arrival radius around the target.
     */
    public static long ticksToArrival(Convoy convoy, HiveConfig config) {
        var blocksPerTick = ConvoySpeedTable.blocksPerTick(convoy, config);
        if (blocksPerTick <= 0.0) {
            return Long.MAX_VALUE;
        }

        var distanceUntilArrival = Math.max(0.0, distanceToTarget(convoy) - config.arrivalRadiusBlocks());
        return (long) Math.ceil(distanceUntilArrival / blocksPerTick);
    }

    private static Vec3 targetFor(Convoy convoy) {
        if (convoy instanceof Convoy.Reinforcement reinforcement) {
            return centerOf(reinforcement.destinationPos());
        }
        if (convoy instanceof Convoy.Migration migration) {
            return centerOf(migration.destinationPos());
        }
        if (convoy instanceof Convoy.Raid raid) {
            if (raid.returningHome()) {
                return raid.returnPos() == null ? null : centerOf(raid.returnPos());
            }
            // Raid target tracks the player; lastKnownTargetPos is updated each tick by LineageConvoyTickTask.
            return centerOf(raid.lastKnownTargetPos());
        }
        return null;
    }

    private static Vec3 centerOf(BlockPos pos) {
        return new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }
}

package com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.attach_to_host;

import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Compose2;
import com.just.ai.goap.sensor.Sensors;
import com.just.core.functional.option.Option;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public class AttachToHostSensors {

    public static final int MAX_LUNGE_RANGE_IN_BLOCKS = 16;

    public static final int MIN_LUNGE_RANGE_IN_BLOCKS = 10;

    public static final Compose2<Facehugger, Option<LivingEntity>, Boolean, Boolean> IS_ATTACK_TARGET_IN_LUNGE_RANGE = Sensors.compose(
        GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(),
        GOAPSensors.IS_ON_GROUND.key(),
        StateKey.sensed("is_attack_target_in_range"),
        (facehugger, attackTargetOption, isOnGround) -> {
            if (!isOnGround || attackTargetOption.isNone()) {
                return false;
            }

            var attackTarget = attackTargetOption.unwrap();

            return isInRange(facehugger)
                && facehugger.getSensing().hasLineOfSight(attackTarget);
        }
    );

    private static boolean isInRange(PathfinderMob pathfinderMob) {
        var target = pathfinderMob.getTarget();

        if (target == null) {
            return false;
        }

        // Compute squared horizontal distance (ignore Y-axis).
        var dx = pathfinderMob.getX() - target.getX();
        var dz = pathfinderMob.getZ() - target.getZ();
        var horizontalDistanceSqr = dx * dx + dz * dz;

        // TODO: Store these instead of constantly recomputing.
        var minimumRangeSquared = AttachToHostSensors.MIN_LUNGE_RANGE_IN_BLOCKS * AttachToHostSensors.MIN_LUNGE_RANGE_IN_BLOCKS;
        var maximumRangeSquared = AttachToHostSensors.MAX_LUNGE_RANGE_IN_BLOCKS * AttachToHostSensors.MAX_LUNGE_RANGE_IN_BLOCKS;

        // Always reject targets too far horizontally.
        var isTargetTooFarAwayHorizontally = horizontalDistanceSqr > maximumRangeSquared;

        if (isTargetTooFarAwayHorizontally) {
            return false;
        }

        // Vertical distance (Y only).
        var dy = Math.abs(pathfinderMob.getY() - target.getY());
        var verticalDistanceSqr = dy * dy;

        var isTargetTooFarAwayVertically = verticalDistanceSqr > maximumRangeSquared;

        if (isTargetTooFarAwayVertically) {
            return false;
        }

        var minVerticalLungeRange = pathfinderMob.getBbHeight() * pathfinderMob.getBbHeight();

        var isTargetTooCloseHorizontally = horizontalDistanceSqr < minimumRangeSquared;
        var isTargetTooCloseVertically = dy <= minVerticalLungeRange;

        // Allow targets outside minimum horizontal range if they are far enough vertically.
        return !isTargetTooCloseHorizontally || !isTargetTooCloseVertically;
    }

    private AttachToHostSensors() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.ai.lunge;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.core.functional.option.Option;
import com.just.goap.StateKey;
import com.just.goap.sensor.Compose2;
import com.just.goap.sensor.Sensors;
import net.minecraft.world.entity.LivingEntity;

public class LungeSensors {

    public static final int MAX_LUNGE_RANGE_IN_BLOCKS = 15;

    public static final int MIN_LUNGE_RANGE_IN_BLOCKS = 6;

    private static final int LUNGE_COOLDOWN_IN_TICKS = 20 * 5;

    public static final Compose2<Xenomorph, Option<LivingEntity>, Boolean, Boolean> IS_TARGET_IN_LUNGE_RANGE = Sensors.compose(
        GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(),
        GOAPSensors.IS_ON_GROUND.key(),
        StateKey.sensed("is_target_in_lunge_range"),
        (xenomorph, attackTargetOption, isOnGround) -> {
            if (!isOnGround || attackTargetOption.isNone()) {
                return false;
            }

            if (!isLungeCooldownExpired(xenomorph)) {
                return false;
            }

            var attackTarget = attackTargetOption.unwrap();

            return isInLungeRange(xenomorph, attackTarget)
                && xenomorph.getSensing().hasLineOfSight(attackTarget);
        }
    );

    private static boolean isLungeCooldownExpired(Xenomorph xenomorph) {
        var ticksSinceLastLunge = xenomorph.tickCount - xenomorph.getXenomorphData().getLastLungeTick();
        return ticksSinceLastLunge >= LUNGE_COOLDOWN_IN_TICKS;
    }

    private static boolean isInLungeRange(Xenomorph xenomorph, LivingEntity target) {
        var dx = xenomorph.getX() - target.getX();
        var dz = xenomorph.getZ() - target.getZ();
        var horizontalDistanceSquared = dx * dx + dz * dz;

        var minimumRangeSquared = MIN_LUNGE_RANGE_IN_BLOCKS * MIN_LUNGE_RANGE_IN_BLOCKS;
        var maximumRangeSquared = MAX_LUNGE_RANGE_IN_BLOCKS * MAX_LUNGE_RANGE_IN_BLOCKS;

        if (horizontalDistanceSquared > maximumRangeSquared) {
            return false;
        }

        var dy = Math.abs(xenomorph.getY() - target.getY());
        var verticalDistanceSquared = dy * dy;

        if (verticalDistanceSquared > maximumRangeSquared) {
            return false;
        }

        var minimumVerticalLungeRange = xenomorph.getBbHeight() * xenomorph.getBbHeight();
        var isTargetTooCloseHorizontally = horizontalDistanceSquared < minimumRangeSquared;
        var isTargetTooCloseVertically = dy <= minimumVerticalLungeRange;

        return !isTargetTooCloseHorizontally || !isTargetTooCloseVertically;
    }

    private LungeSensors() {
        throw new UnsupportedOperationException();
    }
}

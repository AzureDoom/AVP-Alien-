package com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ai.roll;

import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.Chrysalis;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.core.functional.option.Option;
import com.just.goap.StateKey;
import com.just.goap.sensor.Compose2;
import com.just.goap.sensor.Sensors;
import net.minecraft.world.entity.LivingEntity;

public class RollSensors {

    public static final StateKey.Sensed<Boolean> IS_TARGET_IN_ROLL_RANGE_KEY = StateKey.sensed("is_target_in_roll_range");

    public static Compose2<Chrysalis, Option<LivingEntity>, Boolean, Boolean> createRollRangeSensor(RollConfig config) {
        return Sensors.compose(
            GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(),
            GOAPSensors.IS_ON_GROUND.key(),
            IS_TARGET_IN_ROLL_RANGE_KEY,
            (chrysalis, attackTargetOption, isOnGround) -> {
                if (!isOnGround || attackTargetOption.isNone()) {
                    return false;
                }

                if (!chrysalis.isRollCooldownReady()) {
                    return false;
                }

                var attackTarget = attackTargetOption.unwrap();

                if (!chrysalis.getSensing().hasLineOfSight(attackTarget)) {
                    return false;
                }

                return isInRollRange(chrysalis, attackTarget, config);
            }
        );
    }

    private static boolean isInRollRange(Chrysalis chrysalis, LivingEntity target, RollConfig config) {
        var dx = chrysalis.getX() - target.getX();
        var dz = chrysalis.getZ() - target.getZ();
        var horizontalDistanceSquared = dx * dx + dz * dz;

        var minSquared = config.minRangeInBlocks() * config.minRangeInBlocks();
        var maxSquared = config.maxRangeInBlocks() * config.maxRangeInBlocks();

        return horizontalDistanceSquared >= minSquared && horizontalDistanceSquared <= maxSquared;
    }

    private RollSensors() {
        throw new UnsupportedOperationException();
    }
}

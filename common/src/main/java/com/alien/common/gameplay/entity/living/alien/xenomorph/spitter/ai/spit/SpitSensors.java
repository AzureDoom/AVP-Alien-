package com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.ai.spit;

import com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.Spitter;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Compose2;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;
import com.just.core.functional.option.Option;
import net.minecraft.world.entity.LivingEntity;

public class SpitSensors {

    private static final int MIN_SPIT_RANGE_IN_BLOCKS = 5;

    private static final int MAX_SPIT_RANGE_IN_BLOCKS = 16;

    public static final Compose2<Spitter, Option<LivingEntity>, Boolean, Boolean> IS_TARGET_AT_SPIT_DISTANCE = Sensors.compose(
        GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(),
        GOAPSensors.IS_ON_GROUND.key(),
        StateKey.sensed("is_target_at_spit_distance"),
        (spitter, attackTargetOption, isOnGround) -> {
            if (!isOnGround || attackTargetOption.isNone()) {
                return false;
            }

            var target = attackTargetOption.unwrap();

            if (target.getType().is(AlienEntityTypeTags.ACID_IMMUNE)) {
                return false;
            }

            var distanceSquared = spitter.distanceToSqr(target);
            var minSquared = MIN_SPIT_RANGE_IN_BLOCKS * MIN_SPIT_RANGE_IN_BLOCKS;
            var maxSquared = MAX_SPIT_RANGE_IN_BLOCKS * MAX_SPIT_RANGE_IN_BLOCKS;

            return distanceSquared >= minSquared
                && distanceSquared <= maxSquared
                && spitter.getSensing().hasLineOfSight(target);
        }
    );

    public static final Sensor.Mono<Spitter, Boolean> IS_SPIT_COOLDOWN_READY = Sensors.map(
        StateKey.sensed("is_spit_cooldown_ready"),
        spitter -> spitter.getSpitterData().isCooldownReady(spitter.tickCount)
    );

    private SpitSensors() {
        throw new UnsupportedOperationException();
    }
}

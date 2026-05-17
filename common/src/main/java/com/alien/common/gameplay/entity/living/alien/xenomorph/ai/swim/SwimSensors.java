package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.swim;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;

public class SwimSensors {

    public static final Sensor.Mono<Xenomorph, Boolean> NEEDS_WATER_TO_LAND_TRANSITION = Sensors.map(
        StateKey.sensed("needs_water_to_land_transition"),
        xenomorph -> {
            if (!xenomorph.isInWater()) {
                return false;
            }

            var target = xenomorph.getTarget();

            return target != null && !target.isInWater();
        }
    );

    private SwimSensors() {
        throw new UnsupportedOperationException();
    }
}

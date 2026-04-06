package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg;

import com.alien.common.gameplay.entity.living.alien.EggCarrier;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.just.goap.StateKey;
import com.just.goap.sensor.Sensor;
import com.just.goap.sensor.Sensors;

public class EggSensors {

    public static final Sensor.Mono<Xenomorph, Boolean> HAS_TARGET_OVOMORPH = Sensors.map(
        StateKey.sensed("has_target_ovomorph"),
        xenomorph -> {
            if (!(xenomorph instanceof EggCarrier eggCarrier)) {
                return false;
            }

            var target = eggCarrier.getEggPickupManager().getTargetOvomorphOrNull();

            return target != null && target.wantsPickup && !target.isPassenger();
        }
    );

    public static final Sensor.Mono<Xenomorph, Boolean> IS_CARRYING_OVOMORPH = Sensors.map(
        StateKey.sensed("is_carrying_ovomorph"),
        xenomorph -> xenomorph.getPassengers()
            .stream()
            .anyMatch(passenger -> passenger.getType().is(AlienEntityTypeTags.OVOMORPHS))
    );

    private EggSensors() {
        throw new UnsupportedOperationException();
    }
}

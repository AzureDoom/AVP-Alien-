package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.resin;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;

public class ResinSensors {

    public static final Sensor.Mono<Xenomorph, Boolean> CAN_SPREAD_RESIN = Sensors.map(
        StateKey.sensed("can_spread_resin"),
        xenomorph -> !isCarryingOvomorph(xenomorph) && xenomorph.getResinManager().canSpreadResin()
    );

    private static boolean isCarryingOvomorph(Xenomorph xenomorph) {
        return xenomorph.getPassengers()
            .stream()
            .anyMatch(passenger -> passenger.getType().is(AlienEntityTypeTags.OVOMORPHS));
    }

    private ResinSensors() {
        throw new UnsupportedOperationException();
    }
}

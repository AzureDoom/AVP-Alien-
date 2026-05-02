package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.resin;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;

public class ResinSensors {

    public static final Sensor.Mono<Xenomorph, Boolean> CAN_SPREAD_RESIN = Sensors.map(
        StateKey.sensed("can_spread_resin"),
        xenomorph -> xenomorph.getResinManager().canSpreadResin()
    );

    private ResinSensors() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.idle;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;

public class IdleSensors {

    public static final Sensor.Mono<Xenomorph, Boolean> IS_BORED = Sensors.map(
        StateKey.sensed("is_bored"),
        xenomorph -> xenomorph.getXenomorphData().getTicksUntilBored() == 0
    );

    private IdleSensors() {
        throw new UnsupportedOperationException();
    }
}

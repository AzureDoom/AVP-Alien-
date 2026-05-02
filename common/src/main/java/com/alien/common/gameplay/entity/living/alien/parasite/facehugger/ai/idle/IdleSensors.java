package com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.idle;

import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;

public class IdleSensors {

    public static final Sensor.Mono<Facehugger, Boolean> IS_BORED = Sensors.map(
        StateKey.sensed("is_bored"),
        facehugger -> facehugger.getData().getTicksUntilBored() == 0
    );

    private IdleSensors() {
        throw new UnsupportedOperationException();
    }
}

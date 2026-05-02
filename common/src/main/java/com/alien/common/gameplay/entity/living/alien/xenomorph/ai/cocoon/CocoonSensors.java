package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.cocoon;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;

public class CocoonSensors {

    public static final Sensor.Mono<Xenomorph, Boolean> SHOULD_COCOON = Sensors.map(
        StateKey.sensed("should_cocoon"),
        xenomorph -> xenomorph.getCocoonManager().shouldRunCocoonAction()
    );

    private CocoonSensors() {
        throw new UnsupportedOperationException();
    }
}

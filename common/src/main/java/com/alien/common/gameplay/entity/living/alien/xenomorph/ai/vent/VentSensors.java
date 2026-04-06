package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent;

import com.alien.common.gameplay.entity.living.alien.xenomorph.VentBuilder;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.just.goap.StateKey;
import com.just.goap.sensor.Sensor;
import com.just.goap.sensor.Sensors;

public class VentSensors {

    private static final int VENT_COOLDOWN_IN_TICKS = 15 * 20;

    public static final Sensor.Mono<Xenomorph, Boolean> CAN_CREATE_VENT = Sensors.map(
        StateKey.sensed("can_create_vent"),
        xenomorph -> {
            if (!(xenomorph instanceof VentBuilder ventBuilder)) {
                return false;
            }

            if (xenomorph.getTarget() != null) {
                return false;
            }

            var ticksSinceLastVent = xenomorph.tickCount - ventBuilder.getVentData().getLastVentCreationTick();

            if (ticksSinceLastVent < VENT_COOLDOWN_IN_TICKS) {
                return false;
            }

            return xenomorph.getHiveManager()
                .hive()
                .isSomeAnd(
                    hive -> hive.isAlive()
                        && !hive.isAngry()
                        && hive.getSpaceManager().isEntityWithinHive(xenomorph)
                );
        }
    );

    private VentSensors() {
        throw new UnsupportedOperationException();
    }
}

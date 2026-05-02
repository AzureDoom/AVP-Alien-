package com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.Boiler;
import com.alien.common.util.AlienPredicates;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;
import net.minecraft.world.entity.LivingEntity;

public class VibrationSensors {

    public static final Sensor.Mono<Boiler, Boolean> HAS_VIBRATION_TO_INVESTIGATE = Sensors.map(
        StateKey.sensed("has_vibration_to_investigate"),
        boiler -> {
            if (boiler.getTarget() != null) {
                return false;
            }

            var vibration = boiler.getVibrationSystemManager()
                .getVibrationData()
                .getCurrentVibration();

            return vibration != null
                && vibration.entity() instanceof LivingEntity livingEntity
                && AlienPredicates.canTarget(boiler, livingEntity);
        }
    );

    private VibrationSensors() {
        throw new UnsupportedOperationException();
    }
}

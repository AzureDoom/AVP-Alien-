package com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.seek_carrier;

import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.CarrierSpine;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Compose2;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;

import java.util.Comparator;

public class SeekCarrierSensors {

    public static final Sensor.Mono<Facehugger, Carrier> NEAREST_AVAILABLE_CARRIER = Sensors.map(
        StateKey.sensed("nearest_available_carrier"),
        facehugger -> {
            if (facehugger.isPassenger()) {
                return null;
            }

            return facehugger.getEntitySenseCache()
                .getByTag(AlienEntityTypeTags.CARRIERS)
                .stream()
                .filter(e -> e instanceof Carrier)
                .map(e -> (Carrier) e)
                .filter(carrier -> carrier.getRidingFacehuggerCount() < CarrierSpine.COUNT)
                .min(Comparator.comparingDouble(facehugger::distanceToSqr))
                .orElse(null);
        }
    );

    public static final Compose2<Facehugger, Boolean, Carrier, Boolean> SHOULD_SEEK_CARRIER = Sensors.compose(
        GOAPSensors.HAS_ATTACK_TARGET.key(),
        NEAREST_AVAILABLE_CARRIER.key(),
        StateKey.sensed("should_seek_carrier"),
        (facehugger, hasAttackTarget, nearestCarrier) -> !hasAttackTarget && !facehugger.isPassenger() && nearestCarrier != null
    );

    private SeekCarrierSensors() {
        throw new UnsupportedOperationException();
    }
}

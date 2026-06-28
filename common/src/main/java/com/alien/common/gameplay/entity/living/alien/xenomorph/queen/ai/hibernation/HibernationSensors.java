package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai.hibernation;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.QueenLifecyclePhase;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.QueenLifecyclePhaseManager;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;

/**
 * Sensor for the hibernation hold (Stage 3a). {@link #IS_HIBERNATING} is the single on/off switch for the package: true
 * only while she is a never-founded queen in {@link QueenLifecyclePhase#HIBERNATION}. False when the front-end is
 * disabled (she never reaches HIBERNATION), so the package drops out with it. Once the sleep timer expires the phase
 * advances to FOUNDING_HANDOFF, this flips false, and the hold is released — handing her to the founding system.
 */
public final class HibernationSensors {

    public static final Sensor.Mono<Xenomorph, Boolean> IS_HIBERNATING = Sensors.map(
            StateKey.sensed("hibernation_is_hibernating"),
            HibernationSensors::isHibernating
    );

    public static boolean isHibernating(Xenomorph xenomorph) {
        if (!QueenLifecyclePhaseManager.isEnabled() || !(xenomorph instanceof Queen queen)) {
            return false;
        }
        return queen.getLifecyclePhaseManager().getPhase() == QueenLifecyclePhase.HIBERNATION;
    }

    private HibernationSensors() {
        throw new UnsupportedOperationException();
    }
}
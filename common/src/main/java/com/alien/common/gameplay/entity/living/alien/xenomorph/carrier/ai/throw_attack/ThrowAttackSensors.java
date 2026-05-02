package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai.throw_attack;

import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.alien.common.util.AlienPredicates;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.core.functional.option.Option;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Compose2;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;
import net.minecraft.world.entity.LivingEntity;

public class ThrowAttackSensors {

    private static final double MIN_THROW_RANGE_SQUARED = 5.0 * 5.0;

    private static final double MAX_THROW_RANGE_SQUARED = 20.0 * 20.0;

    public static final Sensor.Mono<Carrier, Boolean> HAS_RIDING_FACEHUGGER = Sensors.map(
        StateKey.sensed("has_riding_facehugger"),
        carrier -> carrier.getRidingFacehuggerCount() > 0
    );

    public static final Sensor.Mono<Carrier, Boolean> IS_THROW_COOLDOWN_READY = Sensors.map(
        StateKey.sensed("is_throw_cooldown_ready"),
        carrier -> carrier.getCarrierData().isThrowCooldownReady()
    );

    public static final Compose2<Carrier, Option<LivingEntity>, Boolean, Boolean> CAN_THROW_FACEHUGGER = Sensors.compose(
        GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(),
        HAS_RIDING_FACEHUGGER.key(),
        StateKey.sensed("can_throw_facehugger"),
        (carrier, attackTargetOption, hasRidingFacehugger) -> {
            if (
                !hasRidingFacehugger
                    || !carrier.getCarrierData().isThrowCooldownReady()
                    || attackTargetOption.isNone()
            ) {
                return false;
            }

            var target = attackTargetOption.unwrap();

            if (!AlienPredicates.isHost(target) || AlienPredicates.hasEmbryo(target)) {
                return false;
            }

            if (target.getType().is(AlienEntityTypeTags.ALIENS)) {
                return false;
            }

            var distanceSqr = carrier.distanceToSqr(target);
            if (distanceSqr < MIN_THROW_RANGE_SQUARED || distanceSqr > MAX_THROW_RANGE_SQUARED) {
                return false;
            }

            return carrier.getSensing().hasLineOfSight(target);
        }
    );

    private ThrowAttackSensors() {
        throw new UnsupportedOperationException();
    }
}

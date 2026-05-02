package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.core.functional.option.Option;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Compose;
import com.just.ai.goap.sensor.Sensors;
import net.minecraft.world.entity.LivingEntity;

public class CombatSensors {

    public static final Compose<Xenomorph, Option<LivingEntity>, Boolean> IS_TARGET_IN_MELEE_RANGE = Sensors.compose(
        GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(),
        StateKey.sensed("is_target_in_melee_range"),
        (xenomorph, attackTargetOption) -> {
            if (attackTargetOption.isNone()) {
                return false;
            }

            var attackTarget = attackTargetOption.unwrap();
            var attackRange = xenomorph.getBbWidth() + 1.0;
            var distanceSquared = xenomorph.distanceToSqr(attackTarget);

            return distanceSquared <= attackRange * attackRange
                && xenomorph.getSensing().hasLineOfSight(attackTarget);
        }
    );

    private CombatSensors() {
        throw new UnsupportedOperationException();
    }
}

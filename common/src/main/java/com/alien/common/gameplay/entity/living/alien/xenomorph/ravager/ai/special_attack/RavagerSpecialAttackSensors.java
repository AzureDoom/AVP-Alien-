package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Compose2;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;
import com.just.core.functional.option.Option;
import net.minecraft.world.entity.LivingEntity;

public class RavagerSpecialAttackSensors {

    public static final Sensor.Mono<Ravager, Boolean> IS_SPECIAL_ATTACK_COOLDOWN_READY = Sensors.map(
        StateKey.sensed("is_ravager_special_attack_cooldown_ready"),
        ravager -> ravager.getRavagerData().isSpecialAttackCooldownReady()
    );

    public static final Compose2<Ravager, Option<LivingEntity>, Boolean, Boolean> CAN_SPECIAL_ATTACK = Sensors.compose(
        GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(),
        IS_SPECIAL_ATTACK_COOLDOWN_READY.key(),
        StateKey.sensed("can_ravager_special_attack"),
        (ravager, attackTargetOption, isSpecialAttackCooldownReady) -> {
            if (!isSpecialAttackCooldownReady || attackTargetOption.isNone()) {
                return false;
            }

            return ravager.getSensing().hasLineOfSight(attackTargetOption.unwrap());
        }
    );

    private RavagerSpecialAttackSensors() {
        throw new UnsupportedOperationException();
    }
}

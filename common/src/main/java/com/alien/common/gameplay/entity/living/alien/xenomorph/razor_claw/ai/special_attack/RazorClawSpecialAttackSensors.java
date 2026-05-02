package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai.special_attack;

import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.RazorClaw;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Compose2;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;
import com.just.core.functional.option.Option;
import net.minecraft.world.entity.LivingEntity;

public class RazorClawSpecialAttackSensors {

    public static final Sensor.Mono<RazorClaw, Boolean> IS_SPECIAL_ATTACK_COOLDOWN_READY = Sensors.map(
        StateKey.sensed("is_razor_claw_special_attack_cooldown_ready"),
        razorClaw -> razorClaw.getRazorClawData().isSpecialAttackCooldownReady()
    );

    public static final Compose2<RazorClaw, Option<LivingEntity>, Boolean, Boolean> CAN_SPECIAL_ATTACK = Sensors.compose(
        GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(),
        IS_SPECIAL_ATTACK_COOLDOWN_READY.key(),
        StateKey.sensed("can_razor_claw_special_attack"),
        (razorClaw, attackTargetOption, isSpecialAttackCooldownReady) -> {
            if (!isSpecialAttackCooldownReady || attackTargetOption.isNone()) {
                return false;
            }

            if (!razorClaw.getSensing().hasLineOfSight(attackTargetOption.unwrap())) {
                return false;
            }

            return razorClaw.getNearbyMeleeAttackTargetCount() >= RazorClawSpecialAttackConfig.DEFAULT.requiredMeleeTargetCount();
        }
    );

    private RazorClawSpecialAttackSensors() {
        throw new UnsupportedOperationException();
    }
}

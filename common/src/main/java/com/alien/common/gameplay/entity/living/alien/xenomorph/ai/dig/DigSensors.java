package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.dig;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.just.goap.StateKey;
import com.just.goap.sensor.Sensor;
import com.just.goap.sensor.Sensors;
import net.minecraft.world.level.GameRules;

public class DigSensors {

    private static final int PATH_FAILURE_EXPIRY_IN_TICKS = 40;

    public static final Sensor.Mono<Xenomorph, Boolean> IS_PATH_TO_TARGET_BLOCKED = Sensors.map(
        StateKey.sensed("is_path_to_target_blocked"),
        xenomorph -> {
            if (xenomorph.getTarget() == null || !xenomorph.onGround()) {
                return false;
            }

            if (xenomorph.getHealth() < xenomorph.getMaxHealth() * 0.5f) {
                return false;
            }

            if (!xenomorph.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                return false;
            }

            var target = xenomorph.getTarget();

            if (xenomorph.getSensing().hasLineOfSight(target)) {
                return false;
            }

            var lastFailureTick = xenomorph.getXenomorphData().getLastPathFailureTick();

            return lastFailureTick > 0 && xenomorph.tickCount - lastFailureTick < PATH_FAILURE_EXPIRY_IN_TICKS;
        }
    );

    private DigSensors() {
        throw new UnsupportedOperationException();
    }
}

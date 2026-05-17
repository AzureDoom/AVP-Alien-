package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.util.AlienPredicates;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public final class XenomorphTargetSensors {

    public static final Sensor.Mono<Xenomorph, List<LivingEntity>> NEARBY_ATTACKABLE_TARGETS = Sensors.map(
        GOAPSensors.NEARBY_ATTACKABLE_TARGETS_KEY,
        xenomorph -> {
            var targets = new ArrayList<LivingEntity>();

            for (var livingEntity : xenomorph.getEntitySenseCache().getByClass(LivingEntity.class)) {
                if (AlienPredicates.canTarget(xenomorph, livingEntity)) {
                    targets.add(livingEntity);
                }
            }

            var hiveIntruderTarget = xenomorph.getHiveIntruderTargetOrNull();

            if (
                hiveIntruderTarget != null
                    && !targets.contains(hiveIntruderTarget)
                    && AlienPredicates.canTarget(xenomorph, hiveIntruderTarget)
            ) {
                targets.add(hiveIntruderTarget);
            }

            return targets;
        }
    );

    private XenomorphTargetSensors() {
        throw new UnsupportedOperationException();
    }
}

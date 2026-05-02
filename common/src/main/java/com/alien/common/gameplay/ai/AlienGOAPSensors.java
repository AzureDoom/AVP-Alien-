package com.alien.common.gameplay.ai;

import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.util.AlienPredicates;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AlienGOAPSensors {

    public static final Sensor.Mono<Entity, List<LivingEntity>> NEARBY_HOSTS = Sensors.compose(
        GOAPSensors.NEARBY_LIVING_ENTITIES.key(),
        AlienGOAPKeys.NEARBY_HOSTS,
        (entity, nearbyEntities) -> nearbyEntities.stream()
            .filter(e -> AlienPredicates.isFreeHost(entity, e))
            .toList()
    );

    static final Sensor.Mono<Ovomorph, Boolean> HAS_NEARBY_HOST =
        Sensors.compose(AlienGOAPKeys.NEARBY_HOSTS, AlienGOAPKeys.HAS_NEARBY_HOST, (ovomorph, nearbyHosts) -> !nearbyHosts.isEmpty());

    private AlienGOAPSensors() {
        throw new UnsupportedOperationException();
    }
}

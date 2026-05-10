package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.gameplay.hive2.spawning.HiveLocationSpawnGate;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;
import net.minecraft.world.phys.AABB;

public class EggLayingSensors {

    public static final StateKey.Sensed<Boolean> CAN_LAY_EGG = StateKey.sensed("can_lay_egg");

    public static <T extends EggLayer> Sensor.Mono<T, Boolean> canLayEgg() {
        return Sensors.map(
            CAN_LAY_EGG,
            eggLayer -> {
                if (!eggLayer.isEggLayCooldownReady()) {
                    return false;
                }

                if (
                    !eggLayer.isAlive()
                        || !eggLayer.hasOvipositor()
                        || !AlienVariantTypes.getFor(eggLayer.getVariant()).canReproduce()
                ) {
                    return false;
                }

                if (!isWithinSomeHive(eggLayer)) {
                    return false;
                }

                return noEggsNearby(eggLayer);
            }
        );
    }

    /**
     * Hive2: the egg-layer must be standing inside a claimed chunk of some hive location. (A per-location ovomorph cap
     * can be re-introduced later as part of biomass tuning.)
     */
    private static boolean isWithinSomeHive(EggLayer eggLayer) {
        return HiveLocationSpawnGate.locationContaining(eggLayer.level(), eggLayer.asEntity().blockPosition()) != null;
    }

    private static boolean noEggsNearby(EggLayer eggLayer) {
        var eggPos = eggLayer.getEggLayingPosition();
        var halfSize = 0.5;

        var searchBox = new AABB(
            eggPos.x - halfSize,
            eggPos.y - eggLayer.level().dimensionType().height(),
            eggPos.z - halfSize,
            eggPos.x + halfSize,
            eggPos.y + 5,
            eggPos.z + halfSize
        );

        return eggLayer.level()
            .getEntitiesOfClass(
                Ovomorph.class,
                searchBox,
                entity -> entity.getType().is(AlienEntityTypeTags.OVOMORPHS) && !entity.isRooted.get()
            )
            .isEmpty();
    }

    private EggLayingSensors() {
        throw new UnsupportedOperationException();
    }
}

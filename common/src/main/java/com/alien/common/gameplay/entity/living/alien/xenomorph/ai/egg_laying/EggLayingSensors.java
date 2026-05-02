package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;
import net.minecraft.world.phys.AABB;

public class EggLayingSensors {

    private static final int MAX_EGG_COUNT_IN_HIVE = 60;

    public static final StateKey.Sensed<Boolean> CAN_LAY_EGG = StateKey.sensed("can_lay_egg");

    public static <T extends EggLayer> Sensor.Mono<T, Boolean> canLayEgg() {
        return Sensors.map(
            CAN_LAY_EGG,
            eggLayer -> {
                if (!eggLayer.isEggLayCooldownReady()) {
                    return false;
                }

                return eggLayer.isAlive()
                    && eggLayer.hasOvipositor()
                    && AlienVariantTypes.getFor(eggLayer.getVariant()).canReproduce()
                    && eggLayer.getHiveManager()
                        .hive()
                        .isSomeAnd(
                            hive -> hive.isAlive()
                                && hive.getSpaceManager().isEntityWithinHive(eggLayer.asEntity())
                                && hive.getFactionData()
                                    .getLoadedMemberCount(member -> member.is(AlienEntityTypeTags.OVOMORPHS)) < MAX_EGG_COUNT_IN_HIVE
                        )
                    && noEggsNearby(eggLayer);
            }
        );
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

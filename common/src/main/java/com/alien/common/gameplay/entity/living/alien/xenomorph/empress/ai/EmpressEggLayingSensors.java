package com.alien.common.gameplay.entity.living.alien.xenomorph.empress.ai;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.empress.Empress;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.just.goap.StateKey;
import com.just.goap.sensor.Sensor;
import com.just.goap.sensor.Sensors;
import net.minecraft.world.phys.AABB;

public class EmpressEggLayingSensors {

    private static final int MAX_EGG_COUNT_IN_HIVE = 60;

    public static final Sensor.Mono<Empress, Boolean> CAN_LAY_EGG = Sensors.map(
        StateKey.sensed("can_lay_egg"),
        empress -> {
            if (!empress.getEmpressData().isEggLayCooldownReady()) {
                return false;
            }

            return empress.isAlive()
                && empress.getEmpressOvipositorManager().hasOvipositor()
                && AlienVariantTypes.getFor(empress.getVariant()).canReproduce()
                && empress.getHiveManager()
                    .hive()
                    .isSomeAnd(
                        hive -> hive.isAlive()
                            && hive.getSpaceManager().isEntityWithinHive(empress)
                            && hive.getFactionData()
                                .getLoadedMemberCount(member -> member.is(AlienEntityTypeTags.OVOMORPHS)) < MAX_EGG_COUNT_IN_HIVE
                    )
                && noEggsNearby(empress);
        }
    );

    private static boolean noEggsNearby(Empress empress) {
        var eggPos = empress.getEmpressOvipositorManager().getEggLayingPosition();
        var halfSize = 0.5;

        var searchBox = new AABB(
            eggPos.x - halfSize,
            eggPos.y - empress.level().dimensionType().height(),
            eggPos.z - halfSize,
            eggPos.x + halfSize,
            eggPos.y + 5,
            eggPos.z + halfSize
        );

        return empress.level()
            .getEntitiesOfClass(
                Ovomorph.class,
                searchBox,
                entity -> entity.getType().is(AlienEntityTypeTags.OVOMORPHS) && !entity.isRooted.get()
            )
            .isEmpty();
    }

    private EmpressEggLayingSensors() {
        throw new UnsupportedOperationException();
    }
}

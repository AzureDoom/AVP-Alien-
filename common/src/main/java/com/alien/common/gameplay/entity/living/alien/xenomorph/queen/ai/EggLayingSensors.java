package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.just.goap.StateKey;
import com.just.goap.sensor.Sensor;
import com.just.goap.sensor.Sensors;
import net.minecraft.world.phys.AABB;

public class EggLayingSensors {

    private static final int MAX_EGG_COUNT_IN_HIVE = 60;

    public static final Sensor.Mono<Queen, Boolean> CAN_LAY_EGG = Sensors.map(
        StateKey.sensed("can_lay_egg"),
        queen -> {
            if (!queen.getQueenData().isEggLayCooldownReady()) {
                return false;
            }

            return queen.isAlive()
                && queen.getOvipositorManager().hasOvipositor()
                && AlienVariantTypes.getFor(queen.getVariant()).canReproduce()
                && queen.getHiveManager()
                    .hive()
                    .isSomeAnd(
                        hive -> hive.isAlive()
                            && hive.getSpaceManager().isEntityWithinHive(queen)
                            && hive.getFactionData()
                                .getLoadedMemberCount(member -> member.is(AlienEntityTypeTags.OVOMORPHS)) < MAX_EGG_COUNT_IN_HIVE
                    )
                && noEggsNearby(queen);
        }
    );

    private static boolean noEggsNearby(Queen queen) {
        var eggPos = queen.getOvipositorManager().getEggLayingPosition();
        var halfSize = 0.5;

        var searchBox = new AABB(
            eggPos.x - halfSize,
            eggPos.y - queen.level().dimensionType().height(),
            eggPos.z - halfSize,
            eggPos.x + halfSize,
            eggPos.y + 5,
            eggPos.z + halfSize
        );

        return queen.level()
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

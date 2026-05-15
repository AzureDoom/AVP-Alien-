package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.gameplay.hive2.spawning.HiveLocationSpawnGate;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.sensor.Sensor;
import com.just.ai.goap.sensor.Sensors;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EggLayingSensors {

    public static final StateKey.Sensed<Boolean> CAN_LAY_EGG = StateKey.sensed("can_lay_egg");

    private static final double MIN_HORIZONTAL_OVOMORPH_SPACING_BLOCKS = 2.0;

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

                var location = HiveLocationSpawnGate.locationContaining(eggLayer.level(), eggLayer.asEntity().blockPosition());
                if (location == null) {
                    return false;
                }
                if (!hasOvomorphCapacity(eggLayer, location)) {
                    return false;
                }

                return noEggsNearby(eggLayer);
            }
        );
    }

    private static boolean hasOvomorphCapacity(EggLayer eggLayer, HiveLocation location) {
        var cap = HiveLocationRegistry.INSTANCE.config().maxOvomorphsPerHiveLocation();
        return cap > 0 && countSameVariantOvomorphs(eggLayer.level(), location, eggLayer.getVariant()) < cap;
    }

    private static int countSameVariantOvomorphs(Level level, HiveLocation location, AlienVariant fallbackVariant) {
        if (location.claimedChunks().isEmpty()) {
            return 0;
        }

        var variant = location.lineageVariantOrNull();
        if (variant == null) {
            variant = fallbackVariant;
        }

        var normalType = Ovomorph.getType(variant, false);
        var royalType = Ovomorph.getType(variant, true);
        if (normalType == null && royalType == null) {
            return 0;
        }

        var bounds = claimedChunkBounds(level, location);
        return level.getEntitiesOfClass(
            Ovomorph.class,
            bounds,
            entity -> isSameVariantOvomorph(entity.getType(), normalType, royalType)
                && location.claimedChunks().contains(new ChunkPos(entity.blockPosition()))
        ).size();
    }

    private static AABB claimedChunkBounds(Level level, HiveLocation location) {
        var minX = Integer.MAX_VALUE;
        var minZ = Integer.MAX_VALUE;
        var maxX = Integer.MIN_VALUE;
        var maxZ = Integer.MIN_VALUE;

        for (var chunk : location.claimedChunks()) {
            var chunkMinX = chunk.x * 16;
            var chunkMinZ = chunk.z * 16;
            minX = Math.min(minX, chunkMinX);
            minZ = Math.min(minZ, chunkMinZ);
            maxX = Math.max(maxX, chunkMinX + 15);
            maxZ = Math.max(maxZ, chunkMinZ + 15);
        }

        return new AABB(minX, level.getMinBuildHeight(), minZ, maxX + 1, level.getMaxBuildHeight(), maxZ + 1);
    }

    private static boolean isSameVariantOvomorph(
        EntityType<?> type,
        EntityType<? extends Ovomorph> normalType,
        EntityType<? extends Ovomorph> royalType
    ) {
        return type == normalType || type == royalType;
    }

    private static boolean noEggsNearby(EggLayer eggLayer) {
        var eggPos = eggLayer.getEggLayingPosition();
        var halfSize = MIN_HORIZONTAL_OVOMORPH_SPACING_BLOCKS;

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
                entity -> entity.getType().is(AlienEntityTypeTags.OVOMORPHS) && !entity.isPassenger()
            )
            .isEmpty();
    }

    private EggLayingSensors() {
        throw new UnsupportedOperationException();
    }
}

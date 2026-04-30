package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.alien.common.util.AcidBleedUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ExplosiveXenomorphUtil {

    public static void explodeWithAcid(Xenomorph xenomorph, float radius, int acidAmount) {
        xenomorph.level()
            .explode(xenomorph, xenomorph.getX(), xenomorph.getY(), xenomorph.getZ(), radius, Level.ExplosionInteraction.MOB);

        getBlockArea(xenomorph.blockPosition(), (int) radius, (int) radius, (int) radius)
            .stream()
            .filter(blockPos -> {
                var blockState = xenomorph.level().getBlockState(blockPos);
                return blockState.isAir() || blockState.canBeReplaced();
            })
            .forEach(blockPos -> AcidBleedUtil.spawnAcid(xenomorph, acidAmount, blockPos.getCenter()));
    }

    private static List<BlockPos> getBlockArea(BlockPos center, int radiusX, int radiusY, int radiusZ) {
        var positions = new ArrayList<BlockPos>();

        for (var dx = -radiusX; dx <= radiusX; dx++) {
            for (var dy = -radiusY; dy <= radiusY; dy++) {
                for (var dz = -radiusZ; dz <= radiusZ; dz++) {
                    positions.add(center.offset(dx, dy, dz));
                }
            }
        }

        return positions;
    }

    private ExplosiveXenomorphUtil() {
        throw new UnsupportedOperationException();
    }
}

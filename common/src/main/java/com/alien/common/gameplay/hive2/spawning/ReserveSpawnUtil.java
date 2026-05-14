package com.alien.common.gameplay.hive2.spawning;

import com.alien.common.gameplay.entity.living.alien.Alien;
import net.minecraft.world.entity.Entity;

public final class ReserveSpawnUtil {

    private ReserveSpawnUtil() {}

    public static void markSpawnedFromReserves(Entity entity) {
        if (entity instanceof Alien alien) {
            alien.getMoltingManager().skipToFullMaturity();
        }
    }
}

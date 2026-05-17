package com.alien.common.gameplay.hive.spawning;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import net.minecraft.world.entity.Entity;

public final class ReserveSpawnUtil {

    private ReserveSpawnUtil() {}

    public static void markSpawnedFromReserves(Entity entity) {
        if (entity instanceof Alien alien) {
            alien.getMoltingManager().skipToFullMaturity();
        }
        if (entity instanceof Carrier carrier) {
            carrier.queueReserveFacehuggerPayload();
        }
    }
}

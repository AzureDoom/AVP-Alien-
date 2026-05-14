package com.alien.common.gameplay.hive2.spawning;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public final class ReserveSpawnUtil {

    public enum ReserveSpawnSource {
        LOCAL,
        ARRIVAL
    }

    private static final ThreadLocal<ReserveSpawnSource> ACTIVE_SOURCE = new ThreadLocal<>();

    private ReserveSpawnUtil() {}

    public static <T> T withReserveSpawnSource(ReserveSpawnSource source, Supplier<T> supplier) {
        var previous = ACTIVE_SOURCE.get();
        ACTIVE_SOURCE.set(source);
        try {
            return supplier.get();
        } finally {
            if (previous == null) {
                ACTIVE_SOURCE.remove();
            } else {
                ACTIVE_SOURCE.set(previous);
            }
        }
    }

    public static ReserveSpawnSource activeSpawnSource() {
        return ACTIVE_SOURCE.get();
    }

    public static void markSpawnedFromReserves(Entity entity) {
        if (entity instanceof Alien alien) {
            alien.getMoltingManager().skipToFullMaturity();
        }
        if (entity instanceof Carrier carrier) {
            carrier.queueReserveFacehuggerPayload();
        }
    }
}

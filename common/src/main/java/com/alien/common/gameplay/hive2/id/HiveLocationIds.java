package com.alien.common.gameplay.hive2.id;

import com.alien.Alien;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public final class HiveLocationIds {

    private static final String HIVE_LOCATION_PATH_PREFIX = "hive_location/";

    public static HiveLocationId create() {
        return new HiveLocationId(
            ResourceLocation.fromNamespaceAndPath(Alien.MOD_ID, HIVE_LOCATION_PATH_PREFIX + UUID.randomUUID())
        );
    }

    public static boolean isHiveLocationId(ResourceLocation id) {
        return Alien.MOD_ID.equals(id.getNamespace()) && id.getPath().startsWith(HIVE_LOCATION_PATH_PREFIX);
    }

    private HiveLocationIds() {}
}

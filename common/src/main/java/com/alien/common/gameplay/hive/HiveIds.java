package com.alien.common.gameplay.hive;

import com.alien.Alien;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public final class HiveIds {

    private static final String HIVE_PATH_PREFIX = "hive/";

    public static ResourceLocation create() {
        return ResourceLocation.fromNamespaceAndPath(Alien.MOD_ID, HIVE_PATH_PREFIX + UUID.randomUUID());
    }

    public static boolean isHiveId(ResourceLocation id) {
        return Alien.MOD_ID.equals(id.getNamespace()) && id.getPath().startsWith(HIVE_PATH_PREFIX);
    }

    private HiveIds() {}
}

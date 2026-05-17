package com.alien.common.gameplay.hive2.id;

import com.alien.Alien;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public final class LineageIds {

    private static final String LINEAGE_PATH_PREFIX = "lineage/";

    public static ResourceLocation create() {
        return ResourceLocation.fromNamespaceAndPath(Alien.MOD_ID, LINEAGE_PATH_PREFIX + UUID.randomUUID());
    }

    public static boolean isLineageId(ResourceLocation id) {
        return Alien.MOD_ID.equals(id.getNamespace()) && id.getPath().startsWith(LINEAGE_PATH_PREFIX);
    }

    private LineageIds() {}
}

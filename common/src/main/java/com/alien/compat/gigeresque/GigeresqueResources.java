package com.alien.compat.gigeresque;

import net.minecraft.resources.ResourceLocation;

public class GigeresqueResources {

    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(Gigeresque.MOD_ID, path);
    }
}

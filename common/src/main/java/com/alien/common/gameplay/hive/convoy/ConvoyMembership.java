package com.alien.common.gameplay.hive.convoy;

import net.minecraft.resources.ResourceLocation;

public record ConvoyMembership(
    ResourceLocation lineageFactionId,
    ConvoyId convoyId
) {}

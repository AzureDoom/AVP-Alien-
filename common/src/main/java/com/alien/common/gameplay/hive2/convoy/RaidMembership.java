package com.alien.common.gameplay.hive2.convoy;

import net.minecraft.resources.ResourceLocation;

public record RaidMembership(
    ResourceLocation lineageFactionId,
    ConvoyId raidId
) {}

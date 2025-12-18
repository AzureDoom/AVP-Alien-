package com.alien.client.render.entity.head;

import net.minecraft.world.phys.Vec3;

public record EntityHeadData(
    Vec3 size,
    Vec3 position,
    Vec3 pivot
) {}

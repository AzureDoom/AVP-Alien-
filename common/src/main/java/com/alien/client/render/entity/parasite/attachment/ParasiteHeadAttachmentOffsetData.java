package com.alien.client.render.entity.parasite.attachment;

import com.alien.client.render.entity.head.EntityHeadData;
import net.minecraft.world.entity.Entity;

import java.util.function.BiFunction;

public record ParasiteHeadAttachmentOffsetData(
    BiFunction<EntityHeadData, Entity, Double> verticalOffsetSupplier,
    BiFunction<EntityHeadData, Entity, Double> faceOffsetSupplier
) {}

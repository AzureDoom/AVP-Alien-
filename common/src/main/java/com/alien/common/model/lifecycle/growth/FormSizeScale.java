package com.alien.common.model.lifecycle.growth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

public record FormSizeScale(
    EntityType<?> entityType,
    float startScale,
    float endScale,
    int maturationTimeInTicks
) {

    public static final Codec<FormSizeScale> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entityType").forGetter(FormSizeScale::entityType),
            Codec.FLOAT.fieldOf("startScale").forGetter(FormSizeScale::startScale),
            Codec.FLOAT.fieldOf("endScale").forGetter(FormSizeScale::endScale),
            Codec.INT.fieldOf("maturationTimeInTicks").forGetter(FormSizeScale::maturationTimeInTicks)
        ).apply(instance, FormSizeScale::new)
    );

    public float computeScale(int elapsedTicks) {
        if (maturationTimeInTicks <= 0) {
            return endScale;
        }

        var progress = Math.min(1.0f, (float) elapsedTicks / maturationTimeInTicks);
        return startScale + (endScale - startScale) * progress;
    }

    public boolean isFullyMatured(int elapsedTicks) {
        return elapsedTicks >= maturationTimeInTicks;
    }
}

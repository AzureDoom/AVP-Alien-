package com.alien.common.model.lifecycle.growth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public record FormSizeScale(
    EntityType<?> entityType,
    float startScale,
    float endScale,
    List<MoltPhase> phases
) {

    public static final Codec<FormSizeScale> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entityType").forGetter(FormSizeScale::entityType),
            Codec.FLOAT.fieldOf("startScale").forGetter(FormSizeScale::startScale),
            Codec.FLOAT.fieldOf("endScale").forGetter(FormSizeScale::endScale),
            MoltPhase.CODEC.listOf().fieldOf("phases").forGetter(FormSizeScale::phases)
        ).apply(instance, FormSizeScale::new)
    );

    public int totalMaturationTicks() {
        return phases.stream().mapToInt(MoltPhase::totalTicks).sum();
    }

    public float scaleForPhase(int phaseIndex) {
        int phaseCount = phases.size();

        if (phaseCount == 0) {
            return endScale;
        }

        float fraction = (float) (phaseIndex + 1) / phaseCount;
        return startScale + (endScale - startScale) * fraction;
    }

    public float scaleBeforePhase(int phaseIndex) {
        if (phaseIndex <= 0) {
            return startScale;
        }

        return scaleForPhase(phaseIndex - 1);
    }

    public boolean isFullyMatured(int phaseIndex) {
        return phaseIndex >= phases.size();
    }
}

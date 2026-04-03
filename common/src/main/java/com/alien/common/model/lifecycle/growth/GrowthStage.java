package com.alien.common.model.lifecycle.growth;

import com.blib.api.common.entity.v1.EntityTypePredicate;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Optional;

public record GrowthStage(
    Optional<EntityTypePredicate> hostTypePredicate,
    EntityType<?> from,
    EntityType<?> to,
    int growthTimeInTicks,
    List<GrowthRequirement> requirements
) {

    public static final Codec<GrowthStage> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            EntityTypePredicate.CODEC.optionalFieldOf("hostTypePredicate").forGetter(GrowthStage::hostTypePredicate),
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("from").forGetter(GrowthStage::from),
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("to").forGetter(GrowthStage::to),
            Codec.INT.optionalFieldOf("growthTimeInTicks", 0).forGetter(GrowthStage::growthTimeInTicks),
            GrowthRequirement.CODEC.listOf().optionalFieldOf("requirements", List.of()).forGetter(GrowthStage::requirements)
        ).apply(instance, GrowthStage::new)
    );

    public boolean hasRequirements() {
        return !requirements.isEmpty();
    }

    public GrowthStage(
        EntityType<?> from,
        EntityType<?> to,
        int growthTimeInTicks
    ) {
        this(Optional.empty(), from, to, growthTimeInTicks, List.of());
    }

    public GrowthStage(
        EntityType<?> from,
        EntityType<?> to,
        List<GrowthRequirement> requirements
    ) {
        this(Optional.empty(), from, to, 0, requirements);
    }

    public GrowthStage(
        TagKey<EntityType<?>> hostTag,
        EntityType<?> from,
        EntityType<?> to,
        int growthTimeInTicks
    ) {
        this(Optional.of(new EntityTypePredicate.Tag(hostTag)), from, to, growthTimeInTicks, List.of());
    }

    public GrowthStage(
        List<EntityType<?>> hostTypes,
        EntityType<?> from,
        EntityType<?> to,
        int growthTimeInTicks
    ) {
        this(Optional.of(new EntityTypePredicate.List(hostTypes)), from, to, growthTimeInTicks, List.of());
    }

    public GrowthStage(
        EntityType<?> hostType,
        EntityType<?> from,
        EntityType<?> to,
        int growthTimeInTicks
    ) {
        this(Optional.of(new EntityTypePredicate.Single(hostType)), from, to, growthTimeInTicks, List.of());
    }
}

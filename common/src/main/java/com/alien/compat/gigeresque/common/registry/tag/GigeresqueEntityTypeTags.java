package com.alien.compat.gigeresque.common.registry.tag;

import com.alien.compat.gigeresque.GigeresqueResources;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class GigeresqueEntityTypeTags {

    public static final TagKey<EntityType<?>> ACID_RESISTANT = create("acid_resistant");

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, GigeresqueResources.location(name));
    }
}

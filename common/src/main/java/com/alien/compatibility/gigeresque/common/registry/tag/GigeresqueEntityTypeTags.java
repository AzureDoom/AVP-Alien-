package com.alien.compatibility.gigeresque.common.registry.tag;

import com.alien.compatibility.gigeresque.Gigeresque;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class GigeresqueEntityTypeTags {

    public static final TagKey<EntityType<?>> ACID_RESISTANT = create("acid_resistant");

    private static TagKey<EntityType<?>> create(String path) {
        return Gigeresque.MOD.resources().createTagKey(Registries.ENTITY_TYPE, path);
    }
}

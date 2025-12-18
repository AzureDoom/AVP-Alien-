package com.alien.fabric.compatibility.stellaris.common.registry.tag;

import com.alien.fabric.compatibility.stellaris.StellarisFabric;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class StellarisEntityTypeTags {

    public static final TagKey<EntityType<?>> NO_OXYGEN_NEEDED = create("no_oxygen_needed");

    private static TagKey<EntityType<?>> create(String path) {
        return StellarisFabric.MOD.resources().createTagKey(Registries.ENTITY_TYPE, path);
    }
}

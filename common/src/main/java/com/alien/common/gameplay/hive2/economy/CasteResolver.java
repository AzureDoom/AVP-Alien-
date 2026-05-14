package com.alien.common.gameplay.hive2.economy;

import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

/**
 * Maps {@code (caste tag, variant)} pairs to a concrete {@link EntityType}. Each xenomorph variant tags exactly one
 * entity type into a given caste tag (e.g., normal drones are in both {@link AlienEntityTypeTags#DRONES} and
 * {@link AlienEntityTypeTags#NORMAL_ALIENS}), so the resolver walks the caste tag's contents and returns the one
 * whose type belongs to the variant's all-aliens tag.
 */
public final class CasteResolver {

    private CasteResolver() {}

    /** Returns the concrete entity type for {@code casteTag} matching {@code variant}, or null if none exists. */
    public static @Nullable EntityType<?> entityTypeForCaste(AlienVariant variant, TagKey<EntityType<?>> casteTag) {
        var variantTag = variantTag(variant);
        for (var holder : BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(casteTag)) {
            var type = holder.value();
            if (type.builtInRegistryHolder().is(variantTag)) {
                return type;
            }
        }
        return null;
    }

    public static TagKey<EntityType<?>> variantTag(AlienVariant variant) {
        return switch (variant) {
            case NORMAL -> AlienEntityTypeTags.NORMAL_ALIENS;
            case ABERRANT -> AlienEntityTypeTags.ABERRANT_ALIENS;
            case IRRADIATED -> AlienEntityTypeTags.IRRADIATED_ALIENS;
            case NETHER -> AlienEntityTypeTags.NETHER_ALIENS;
        };
    }
}

package com.alien.common.gameplay.hive.faction;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.model.alien.variant.AlienVariant;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

/**
 * Centralized variant lookup + variant-match policy for hive faction joins.
 * <p>
 * Every hive faction tier (variant, lineage, location) is bound to a single {@link AlienVariant}. Cross-variant joins
 * must be rejected — variant factions by definition, lineage factions because their reserves and population are
 * species-keyed, and location factions because they inherit their parent lineage's variant.
 */
public final class FactionVariantPolicy {

    private FactionVariantPolicy() {}

    /** Returns the variant of an entity, or null if the entity type isn't a known alien variant type. */
    public static @Nullable AlienVariant variantOf(Entity entity) {
        return variantOf(entity.getType());
    }

    /** Returns the variant of an entity type, or null if the type isn't a known alien variant type. */
    public static @Nullable AlienVariant variantOf(EntityType<?> type) {
        var opt = AlienVariantTypes.getFor(type);
        return opt.isSome() ? opt.unwrap().variant() : null;
    }

    /**
     * True iff the entity is an alien of the required variant. False for non-alien entities and for aliens of any other
     * variant.
     */
    public static boolean variantMatches(Entity entity, AlienVariant required) {
        return variantMatches(entity.getType(), required);
    }

    public static boolean variantMatches(EntityType<?> type, AlienVariant required) {
        var v = variantOf(type);
        return v != null && v == required;
    }
}

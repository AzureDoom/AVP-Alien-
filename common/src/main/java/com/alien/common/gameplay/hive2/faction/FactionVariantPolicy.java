package com.alien.common.gameplay.hive2.faction;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.model.alien.variant.AlienVariant;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * Centralized variant lookup + variant-match policy for hive2 faction joins.
 * <p>
 * Every hive2 faction tier (variant, lineage, location) is bound to a single {@link AlienVariant}. Cross-variant joins
 * must be rejected — variant factions by definition, lineage factions because their reserves and population are
 * species-keyed, and location factions because they inherit their parent lineage's variant.
 */
public final class FactionVariantPolicy {

    private FactionVariantPolicy() {}

    /** Returns the variant of an entity, or null if the entity type isn't a known alien variant type. */
    public static @Nullable AlienVariant variantOf(Entity entity) {
        var opt = AlienVariantTypes.getFor(entity.getType());
        return opt.isSome() ? opt.unwrap().variant() : null;
    }

    /**
     * True iff the entity is an alien of the required variant. False for non-alien entities and for aliens of any other
     * variant.
     */
    public static boolean variantMatches(Entity entity, AlienVariant required) {
        var v = variantOf(entity);
        return v != null && v == required;
    }
}

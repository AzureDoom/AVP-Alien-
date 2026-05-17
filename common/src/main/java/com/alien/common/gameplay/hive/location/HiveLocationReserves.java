package com.alien.common.gameplay.hive.location;

import com.alien.Alien;
import com.alien.common.gameplay.hive.faction.FactionVariantPolicy;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.common.codec.v1.BLibCodecs;
import com.blib.api.common.entity.v1.EntityReserves;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Variant-aware wrapper around BLib's {@link EntityReserves}. Each location gets one, and it is the authoritative
 * storage for that hive location's abstract members. Population caps gate buying and spawning elsewhere; reserve
 * storage itself is uncapped so already-owned members are not lost when they unload, travel, or return from convoys.
 */
public final class HiveLocationReserves {

    private static final String NBT_KEY = "EntityReserves";

    private final EntityReserves underlying;

    private final Supplier<AlienVariant> variantSupplier;

    public HiveLocationReserves(Supplier<AlienVariant> variantSupplier) {
        this.underlying = new EntityReserves();
        this.variantSupplier = variantSupplier;
    }

    /**
     * Adds all of {@code count} to this location's reserves when the entity type matches the location variant.
     */
    public boolean tryAdd(EntityType<?> type, int count) {
        if (count <= 0) {
            return false;
        }
        if (!accepts(type)) {
            var required = variantSupplier.get();
            Alien.LOGGER.warn(
                "Hive: rejected {} local reserve add of {} because it does not match location variant {}.",
                count,
                BuiltInRegistries.ENTITY_TYPE.getKey(type),
                required
            );
            return false;
        }

        underlying.add(type, count);
        return true;
    }

    /**
     * Returns already-owned live members to this location. Kept separate for readability at call sites that are
     * preserving existing population rather than buying new units.
     */
    public boolean addReturningMember(EntityType<?> type, int count) {
        return tryAdd(type, count);
    }

    /**
     * Unconditional spawn-side decrement. Returns true if a unit was successfully consumed; false if the type had zero.
     */
    public boolean trySpawn(EntityType<?> type) {
        if (!canSpawn(type)) {
            return false;
        }
        underlying.add(type, -1);
        return true;
    }

    public boolean canSpawn(EntityType<?> type) {
        return accepts(type) && underlying.getCount(type) > 0;
    }

    public int getCount(EntityType<?> type) {
        return accepts(type) ? underlying.getCount(type) : 0;
    }

    public int getCountMatching(Predicate<EntityType<?>> predicate) {
        return underlying.getCountMatching(type -> accepts(type) && predicate.test(type));
    }

    public int getCount() {
        return underlying.getCount();
    }

    public List<EntityType<?>> getAvailableEntityTypes() {
        return underlying.getAvailableEntityTypes()
            .stream()
            .filter(this::accepts)
            .toList();
    }

    /** Direct access for callers that need to interoperate with raw BLib APIs. */
    public EntityReserves underlying() {
        return underlying;
    }

    public void save(CompoundTag tag) {
        tag.put(NBT_KEY, EntityReserves.CODEC.encode(BLibCodecs.Schema.NBT, underlying));
    }

    public void load(CompoundTag tag) {
        if (!tag.contains(NBT_KEY)) {
            return;
        }

        EntityReserves.CODEC.decode(BLibCodecs.Schema.NBT, tag.getCompound(NBT_KEY))
            .inspectErr(failure -> Alien.LOGGER.error("Failed to load HiveLocationReserves: {}", failure))
            .ifOk(loaded -> {
                var rejected = 0;
                for (var entry : loaded.getBackingMap().entrySet()) {
                    var count = Math.max(0, entry.getValue());
                    if (count <= 0) {
                        continue;
                    }
                    if (accepts(entry.getKey())) {
                        underlying.add(entry.getKey(), count);
                    } else {
                        rejected += count;
                    }
                }
                if (rejected > 0) {
                    Alien.LOGGER.warn(
                        "Hive: discarded {} variant-mismatched local reserve entries while loading a hive location.",
                        rejected
                    );
                }
            });
    }

    public boolean accepts(EntityType<?> type) {
        var required = variantSupplier.get();
        return required == null || FactionVariantPolicy.variantMatches(type, required);
    }

    public int removeVariantMismatches(AlienVariant required) {
        var removed = 0;
        for (var entry : new ArrayList<>(underlying.getBackingMap().entrySet())) {
            if (FactionVariantPolicy.variantMatches(entry.getKey(), required)) {
                continue;
            }
            var count = Math.max(0, entry.getValue());
            if (count <= 0) {
                continue;
            }
            underlying.add(entry.getKey(), -count);
            removed += count;
        }
        return removed;
    }
}

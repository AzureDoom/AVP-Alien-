package com.alien.common.gameplay.hive2.location;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.FactionVariantPolicy;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.common.codec.v1.BLibCodecs;
import com.blib.api.common.entity.v1.EntityReserves;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Cap-aware wrapper around BLib's {@link EntityReserves}. Each location gets one. Caps grow with the location's
 * claimed-chunk count (per {@code HIVE_REDESIGN_05_RESERVES.md} § 6):
 *
 * <pre>
 * cap = baseLocalCapPerType + localCapPerClaimedChunkPerType * claimedChunks
 * </pre>
 * <p>
 * {@link #tryAdd} is the safe add path: it caps at the per-type ceiling and returns the overflow. Phase 3 callers
 * discard the overflow; Phase 4 will re-route it into the lineage pool, then into the variant pool, per the cascade in
 * § 6.
 * <p>
 * Chunk count and config are pulled from suppliers so the wrapper doesn't lock a snapshot at construction — the
 * underlying location's chunk set and the in-use config are both mutable.
 */
public final class HiveLocationReserves {

    private static final String NBT_KEY = "EntityReserves";

    private final EntityReserves underlying;

    private final IntSupplier claimedChunkCountSupplier;

    private final Supplier<HiveConfig> configSupplier;

    private final Supplier<AlienVariant> variantSupplier;

    public HiveLocationReserves(
        IntSupplier claimedChunkCountSupplier,
        Supplier<HiveConfig> configSupplier,
        Supplier<AlienVariant> variantSupplier
    ) {
        this.underlying = new EntityReserves();
        this.claimedChunkCountSupplier = claimedChunkCountSupplier;
        this.configSupplier = configSupplier;
        this.variantSupplier = variantSupplier;
    }

    /**
     * Cap for a given entity type at the current chunk count. Phase 3 uses one flat per-type cap; future phases may
     * specialize per caste.
     */
    public int capFor(EntityType<?> type) {
        var config = configSupplier.get();
        return config.baseLocalCapPerType() + config.localCapPerClaimedChunkPerType() * claimedChunkCountSupplier.getAsInt();
    }

    /**
     * Adds {@code count} of {@code type} to the reserves up to the per-type cap. Returns the overflow (zero if the
     * whole add fit). The overflow is the caller's problem — Phase 3 discards it.
     */
    public int tryAdd(EntityType<?> type, int count) {
        if (count <= 0) {
            return 0;
        }
        if (!accepts(type)) {
            var required = variantSupplier.get();
            Alien.LOGGER.warn(
                "Hive2: rejected {} local reserve add of {} because it does not match location variant {}.",
                count,
                BuiltInRegistries.ENTITY_TYPE.getKey(type),
                required
            );
            return count;
        }

        var current = underlying.getCount(type);
        var cap = capFor(type);
        var headroom = Math.max(0, cap - current);
        var added = Math.min(count, headroom);

        if (added > 0) {
            underlying.add(type, added);
        }

        return count - added;
    }

    /**
     * Returns already-owned live members to this location. This intentionally bypasses the reserve cap: caps gate new
     * purchases/spawns, while despawn/shed return should preserve a hive's existing population.
     */
    public boolean addReturningMember(EntityType<?> type, int count) {
        if (count <= 0) {
            return false;
        }
        if (!accepts(type)) {
            var required = variantSupplier.get();
            Alien.LOGGER.warn(
                "Hive2: rejected returning {} reserve member(s) of {} because it does not match location variant {}.",
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
                        "Hive2: discarded {} variant-mismatched local reserve entries while loading a hive location.",
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

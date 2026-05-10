package com.alien.common.gameplay.hive2.location;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.blib.api.common.codec.v1.BLibCodecs;
import com.blib.api.common.entity.v1.EntityReserves;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;

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

    public HiveLocationReserves(IntSupplier claimedChunkCountSupplier, Supplier<HiveConfig> configSupplier) {
        this.underlying = new EntityReserves();
        this.claimedChunkCountSupplier = claimedChunkCountSupplier;
        this.configSupplier = configSupplier;
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
     * Unconditional spawn-side decrement. Returns true if a unit was successfully consumed; false if the type had zero.
     */
    public boolean trySpawn(EntityType<?> type) {
        if (underlying.getCount(type) <= 0) {
            return false;
        }
        underlying.add(type, -1);
        return true;
    }

    public boolean canSpawn(EntityType<?> type) {
        return underlying.getCount(type) > 0;
    }

    public int getCount(EntityType<?> type) {
        return underlying.getCount(type);
    }

    public int getCountMatching(Predicate<EntityType<?>> predicate) {
        return underlying.getCountMatching(predicate);
    }

    public int getCount() {
        return underlying.getCount();
    }

    public List<EntityType<?>> getAvailableEntityTypes() {
        return underlying.getAvailableEntityTypes();
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
            .ifOk(loaded -> underlying.putAll(loaded.getBackingMap()));
    }
}

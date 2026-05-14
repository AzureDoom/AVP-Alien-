package com.alien.common.gameplay.hive2.faction;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.id.VariantIds;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.common.codec.v1.BLibCodecs;
import com.blib.api.common.entity.v1.EntityReserves;
import com.blib.api.common.faction.v1.FactionData;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The species-tier faction. Exactly one per {@link AlienVariant}, world-wide. Holds the future Queen Mother slot and
 * the per-dimension variant pool that catches overflow when a lineage dies.
 * <p>
 * Variant factions never die. Their id is fixed ({@code avp_alien:variant/<variant_name>}); creation is idempotent.
 * <p>
 * See {@code HIVE_REDESIGN_01_FACTIONS.md} § 1, § 3.
 */
public class VariantFactionData extends FactionData {

    private static final String NBT_VARIANT_ID = "VariantId";

    private static final String NBT_AGE_IN_TICKS = "AgeInTicks";

    private static final String NBT_NEXT_LINEAGE_NUMBER = "NextLineageNumber";

    private static final String NBT_QUEEN_MOTHERS = "QueenMothersByDimension";

    private static final String NBT_VARIANT_POOLS = "VariantPoolsByDimension";

    private static final String NBT_DIMENSION_KEY = "Dimension";

    private static final String NBT_UUID_KEY = "Uuid";

    private static final String NBT_RESERVES_KEY = "Reserves";

    private static final AlienVariant DEFAULT_VARIANT = AlienVariant.NORMAL;

    /**
     * Kept stable for the lifetime of this faction. Set once by {@link VariantFactionRegistry} when the faction is
     * first created from a known variant id; round-tripped through NBT thereafter.
     */
    private AlienVariant variant;

    private long ageInTicks;

    /** Monotonic per-variant lineage counter. Allocated at lineage mint via {@link #allocateLineageNumber()}. */
    private long nextLineageNumber;

    private final Map<ResourceKey<Level>, UUID> queenMotherIdsByDimension;

    private final Map<ResourceKey<Level>, EntityReserves> variantPoolsByDimension;

    public VariantFactionData() {
        this.variant = DEFAULT_VARIANT;
        this.ageInTicks = 0L;
        this.nextLineageNumber = 0L;
        this.queenMotherIdsByDimension = new HashMap<>();
        this.variantPoolsByDimension = new HashMap<>();
    }

    /** Returns the next lineage number and advances the counter. Monotonic — dead lineages don't release numbers. */
    public long allocateLineageNumber() {
        var n = nextLineageNumber++;
        markDirty();
        return n;
    }

    public long nextLineageNumber() {
        return nextLineageNumber;
    }

    public void setNextLineageNumber(long nextLineageNumber) {
        this.nextLineageNumber = nextLineageNumber;
        markDirty();
    }

    @Override
    public void onMemberAdded(FactionMember member, Entity entity) {
        if (FactionVariantPolicy.variantMatches(entity, variant)) {
            return;
        }
        Alien.LOGGER.warn(
            "Hive2: evicting variant-mismatched member {} (type={}) from variant faction {} (variant={})",
            entity.getUUID(),
            entity.getType(),
            VariantIds.of(variant),
            variant
        );
        var faction = Alien.MOD.factions().get(VariantIds.of(variant));
        if (faction != null) {
            faction.membership().removeMember(member);
        }
    }

    public AlienVariant variant() {
        return variant;
    }

    public void setVariant(AlienVariant variant) {
        this.variant = variant;
        markDirty();
    }

    public long ageInTicks() {
        return ageInTicks;
    }

    public void incrementAge() {
        this.ageInTicks++;
    }

    public Map<ResourceKey<Level>, UUID> queenMotherIdsByDimension() {
        return queenMotherIdsByDimension;
    }

    public Map<ResourceKey<Level>, EntityReserves> variantPoolsByDimension() {
        return variantPoolsByDimension;
    }

    public EntityReserves variantPool(ResourceKey<Level> dimension) {
        return variantPoolsByDimension.computeIfAbsent(dimension, $ -> new EntityReserves());
    }

    /**
     * Cap-aware add to the per-dimension variant pool. Cap is the flat {@code variantPoolCapPerType} from
     * {@link com.alien.common.gameplay.hive2.config.HiveConfig} (default {@link Integer#MAX_VALUE} — effectively
     * uncapped per the resolved decision in the implementation plan).
     * <p>
     * Returns the overflow that didn't fit. Phase 4 callers discard the overflow (per
     * {@code HIVE_REDESIGN_05_RESERVES.md} § 6 — there is no further tier to cascade into).
     */
    public int tryAddToVariantPool(ResourceKey<Level> dimension, EntityType<?> type, int count) {
        if (count <= 0) {
            return 0;
        }

        var config = com.alien.common.gameplay.hive2.location.HiveLocationRegistry.INSTANCE.config();
        var cap = config.variantPoolCapPerType();
        var pool = variantPool(dimension);
        var current = pool.getCount(type);
        var headroom = Math.max(0, cap - current);
        var added = Math.min(count, headroom);

        if (added > 0) {
            pool.add(type, added);
            markDirty();
        }

        return count - added;
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains(NBT_VARIANT_ID)) {
            this.variant = AlienVariant.getById(tag.getByte(NBT_VARIANT_ID)).unwrapOr(DEFAULT_VARIANT);
        }

        this.ageInTicks = tag.getLong(NBT_AGE_IN_TICKS);
        this.nextLineageNumber = tag.getLong(NBT_NEXT_LINEAGE_NUMBER);

        queenMotherIdsByDimension.clear();
        if (tag.contains(NBT_QUEEN_MOTHERS)) {
            var listTag = tag.getList(NBT_QUEEN_MOTHERS, net.minecraft.nbt.Tag.TAG_COMPOUND);
            for (var i = 0; i < listTag.size(); i++) {
                var entry = listTag.getCompound(i);
                var dim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(entry.getString(NBT_DIMENSION_KEY)));
                queenMotherIdsByDimension.put(dim, entry.getUUID(NBT_UUID_KEY));
            }
        }

        variantPoolsByDimension.clear();
        if (tag.contains(NBT_VARIANT_POOLS)) {
            var listTag = tag.getList(NBT_VARIANT_POOLS, net.minecraft.nbt.Tag.TAG_COMPOUND);
            for (var i = 0; i < listTag.size(); i++) {
                var entry = listTag.getCompound(i);
                var dim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(entry.getString(NBT_DIMENSION_KEY)));
                var reserves = new EntityReserves();
                EntityReserves.CODEC.decode(BLibCodecs.Schema.NBT, entry.getCompound(NBT_RESERVES_KEY))
                    .inspectErr(
                        failure -> Alien.LOGGER.error(
                            "Failed to load variant pool for {} in {}: {}",
                            variant,
                            dim.location(),
                            failure
                        )
                    )
                    .ifOk(loaded -> reserves.putAll(loaded.getBackingMap()));
                variantPoolsByDimension.put(dim, reserves);
            }
        }
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putByte(NBT_VARIANT_ID, (byte) variant.getId());
        tag.putLong(NBT_AGE_IN_TICKS, ageInTicks);
        tag.putLong(NBT_NEXT_LINEAGE_NUMBER, nextLineageNumber);

        var queenMothers = new net.minecraft.nbt.ListTag();
        for (var entry : queenMotherIdsByDimension.entrySet()) {
            var entryTag = new CompoundTag();
            entryTag.putString(NBT_DIMENSION_KEY, entry.getKey().location().toString());
            entryTag.putUUID(NBT_UUID_KEY, entry.getValue());
            queenMothers.add(entryTag);
        }
        tag.put(NBT_QUEEN_MOTHERS, queenMothers);

        var pools = new net.minecraft.nbt.ListTag();
        for (var entry : variantPoolsByDimension.entrySet()) {
            var entryTag = new CompoundTag();
            entryTag.putString(NBT_DIMENSION_KEY, entry.getKey().location().toString());
            entryTag.put(NBT_RESERVES_KEY, EntityReserves.CODEC.encode(BLibCodecs.Schema.NBT, entry.getValue()));
            pools.add(entryTag);
        }
        tag.put(NBT_VARIANT_POOLS, pools);
    }
}

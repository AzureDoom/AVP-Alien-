package com.alien.common.gameplay.hive2.faction;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.id.VariantIds;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.common.faction.v1.FactionData;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The species-tier faction. Exactly one per {@link AlienVariant}, world-wide. Holds the future Queen Mother slot.
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

    private static final String NBT_DIMENSION_KEY = "Dimension";

    private static final String NBT_UUID_KEY = "Uuid";

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

    public VariantFactionData() {
        this.variant = DEFAULT_VARIANT;
        this.ageInTicks = 0L;
        this.nextLineageNumber = 0L;
        this.queenMotherIdsByDimension = new HashMap<>();
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
    }
}

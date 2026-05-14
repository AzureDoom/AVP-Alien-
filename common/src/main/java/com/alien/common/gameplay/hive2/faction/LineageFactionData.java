package com.alien.common.gameplay.hive2.faction;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.convoy.Convoy;
import com.alien.common.gameplay.hive2.convoy.ConvoyCodec;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.common.codec.v1.BLibCodecs;
import com.blib.api.common.entity.v1.EntityReserves;
import com.blib.api.common.faction.v1.FactionData;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * One queen's bloodline. Lives in a single dimension; owns one or more {@link HiveLocation}s; contains the lineage pool
 * and (eventually) the empress.
 * <p>
 * Per {@code HIVE_REDESIGN_12_PERFORMANCE.md} § 3, this class also carries its location records as nested NBT — they're
 * persisted with the lineage shard, not separately.
 * <p>
 * Phase 1 ships the data shape only. Member-tracking, the empress, civil war, and convoys all attach in later phases
 * (referenced fields are present but inert for now).
 * <p>
 * See {@code HIVE_REDESIGN_01_FACTIONS.md} § 2.
 */
public class LineageFactionData extends FactionData {

    private static final String NBT_VARIANT_ID = "VariantId";

    private static final String NBT_FACTION_ID = "FactionId";

    private static final String NBT_PARENT_VARIANT_FACTION_ID = "ParentVariantFactionId";

    private static final String NBT_DIMENSION = "Dimension";

    private static final String NBT_FOUNDER_ID = "FounderId";

    private static final String NBT_EMPRESS_ID = "EmpressId";

    private static final String NBT_AGE_IN_TICKS = "AgeInTicks";

    private static final String NBT_LAST_SPREAD_TICK = "LastSpreadTick";

    private static final String NBT_PENDING_EMPRESS_EMERGENCE = "PendingEmpressEmergence";

    private static final String NBT_PENDING_CIVIL_WAR = "PendingCivilWar";

    private static final String NBT_LINEAGE_NUMBER = "LineageNumber";

    private static final String NBT_NEXT_LOCATION_NUMBER = "NextLocationNumber";

    private static final String NBT_LINEAGE_POOL = "LineagePool";

    private static final String NBT_LOCATIONS = "Locations";

    private static final String NBT_CONVOYS = "Convoys";

    private static final String NBT_FIRST_ADJACENT_BY_LINEAGE = "FirstAdjacentByLineage";

    private static final String NBT_REMOVAL_REASON = "RemovalReason";

    private static final AlienVariant DEFAULT_VARIANT = AlienVariant.NORMAL;

    private AlienVariant variant;

    /**
     * Own BLib faction id. Set right after the faction is minted so onMemberAdded reactive guards can evict mismatches.
     */
    private @Nullable ResourceLocation factionId;

    private @Nullable ResourceLocation parentVariantFactionId;

    private ResourceKey<Level> dimension;

    private @Nullable UUID founderId;

    private @Nullable UUID empressId;

    private long ageInTicks;

    private long lastSpreadTick;

    private boolean pendingEmpressEmergence;

    private boolean pendingCivilWar;

    /** Per-variant lineage index assigned at mint (used in {@link FactionNaming} paths). -1 = unassigned. */
    private long lineageNumber;

    /** Monotonic per-lineage location counter. Allocated at location mint via {@link #allocateLocationNumber()}. */
    private long nextLocationNumber;

    private final EntityReserves lineagePool;

    /** Insertion-ordered so the "oldest location" tiebreak is stable across restarts. */
    private final Map<HiveLocationId, HiveLocation> locationsById;

    /** In-flight convoys belonging to this lineage. Persisted via {@link ConvoyCodec}. Wired in Phase 8. */
    private final List<Convoy> convoys;

    /**
     * Player-kill attribution table for Phase 8b raid dispatch. Each entry is the list of game-tick timestamps when
     * that player killed members of this lineage. {@link #recordKillByPlayer} prunes timestamps older than the raid
     * aggro window on each insertion.
     * <p>
     * Not persisted — kill aggro is a transient runtime concern; restarting the server resets player aggro.
     */
    private final Map<UUID, List<Long>> killAttributionByPlayer;

    /**
     * Per-pair "first adjacent tick" timestamps for Phase 11 absorption cooldown. Persisted so the timer survives
     * restarts.
     */
    private final Map<ResourceLocation, Long> firstAdjacentTickByLineage;

    private @Nullable LineageRemovalReason removalReason;

    public LineageFactionData() {
        this.variant = DEFAULT_VARIANT;
        this.factionId = null;
        this.parentVariantFactionId = null;
        this.dimension = Level.OVERWORLD;
        this.founderId = null;
        this.empressId = null;
        this.ageInTicks = 0L;
        this.lastSpreadTick = 0L;
        this.pendingEmpressEmergence = false;
        this.pendingCivilWar = false;
        this.lineageNumber = -1L;
        this.nextLocationNumber = 0L;
        this.lineagePool = new EntityReserves();
        this.locationsById = new LinkedHashMap<>();
        this.convoys = new ArrayList<>();
        this.killAttributionByPlayer = new HashMap<>();
        this.firstAdjacentTickByLineage = new HashMap<>();
        this.removalReason = null;
    }

    @Override
    public void onMemberAdded(FactionMember member) {
        markDirty();
    }

    @Override
    public void onMemberAdded(FactionMember member, Entity entity) {
        // Reactive variant guard: lineage factions accept only members of their own variant. Reject mismatches
        // immediately by removing them. (Proactive guards in LocationMembership.join and
        // FactionMembershipTransfer.apply catch the common paths; this is defense-in-depth for direct addEntity calls
        // — civil war partition, queenless maturation, debug commands, etc.)
        if (!FactionVariantPolicy.variantMatches(entity, variant) && factionId != null) {
            Alien.LOGGER.warn(
                "Hive2: evicting variant-mismatched member {} (type={}) from lineage {} (variant={})",
                entity.getUUID(),
                entity.getType(),
                factionId,
                variant
            );
            var faction = Alien.MOD.factions().get(factionId);
            if (faction != null) {
                faction.membership().removeMember(member);
            }
            return;
        }

        // The entity is, by virtue of being passed here, currently loaded, so route it
        // into the per-location loadedMembersByType immediately — BLib does not fire
        // onMemberLoaded for entities that were already loaded when added.
        registerLoadedMember(entity);
        markDirty();
    }

    @Override
    public void onMemberRemoved(FactionMember member) {
        if (member instanceof FactionMember.Entity entityMember) {
            for (var location : locationsById.values()) {
                removeLoadedMemberByUuid(location, entityMember.uuid());
            }
        }
        markDirty();
    }

    @Override
    public void onMemberLoaded(Entity entity) {
        registerLoadedMember(entity);
    }

    @Override
    public void onMemberUnloaded(Entity entity) {
        // Be defensive: an entity may have crossed a chunk boundary into a sister location's
        // territory between load and unload. Sweep every owned location to remove the UUID.
        for (var location : locationsById.values()) {
            removeLoadedMember(location, entity.getType(), entity.getUUID());
        }
    }

    private void registerLoadedMember(Entity entity) {
        var location = locationContaining(entity);
        if (location == null) {
            return;
        }
        location
            .loadedMembersByType()
            .computeIfAbsent(entity.getType(), $ -> new HashSet<>())
            .add(entity.getUUID());
    }

    private @Nullable HiveLocation locationContaining(Entity entity) {
        if (!entity.level().dimension().equals(dimension)) {
            return null;
        }
        var chunk = new ChunkPos(entity.blockPosition());
        var hit = HiveLocationRegistry.INSTANCE.getByChunk(dimension, chunk);

        if (hit == null) {
            return null;
        }

        // Cross-lineage same-variant chunk overlaps are possible per HIVE_REDESIGN_03_LOCATIONS § 3.
        // Only route the entity to a location we actually own.
        return locationsById.containsKey(hit.id()) ? hit : null;
    }

    private static void removeLoadedMember(HiveLocation location, EntityType<?> type, UUID uuid) {
        var perType = location.loadedMembersByType().get(type);
        if (perType == null) {
            return;
        }
        perType.remove(uuid);
        if (perType.isEmpty()) {
            location.loadedMembersByType().remove(type);
        }
    }

    private static void removeLoadedMemberByUuid(HiveLocation location, UUID uuid) {
        var iterator = location.loadedMembersByType().entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            entry.getValue().remove(uuid);
            if (entry.getValue().isEmpty()) {
                iterator.remove();
            }
        }
    }

    public AlienVariant variant() {
        return variant;
    }

    public void setVariant(AlienVariant variant) {
        this.variant = variant;
        markDirty();
    }

    public @Nullable ResourceLocation factionId() {
        return factionId;
    }

    public void setFactionId(ResourceLocation factionId) {
        this.factionId = factionId;
        markDirty();
    }

    public long lineageNumber() {
        return lineageNumber;
    }

    public void setLineageNumber(long lineageNumber) {
        this.lineageNumber = lineageNumber;
        markDirty();
    }

    public long nextLocationNumber() {
        return nextLocationNumber;
    }

    public void setNextLocationNumber(long nextLocationNumber) {
        this.nextLocationNumber = nextLocationNumber;
        markDirty();
    }

    /** Returns the next location number and advances the counter. Monotonic — dead locations don't release numbers. */
    public long allocateLocationNumber() {
        var n = nextLocationNumber++;
        markDirty();
        return n;
    }

    public @Nullable ResourceLocation parentVariantFactionId() {
        return parentVariantFactionId;
    }

    public void setParentVariantFactionId(ResourceLocation parentVariantFactionId) {
        this.parentVariantFactionId = parentVariantFactionId;
        markDirty();
    }

    public ResourceKey<Level> dimension() {
        return dimension;
    }

    public void setDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
        markDirty();
    }

    public @Nullable UUID founderId() {
        return founderId;
    }

    public void setFounderId(@Nullable UUID founderId) {
        this.founderId = founderId;
        markDirty();
    }

    public @Nullable UUID empressId() {
        return empressId;
    }

    public void setEmpressId(@Nullable UUID empressId) {
        this.empressId = empressId;
        markDirty();
    }

    public long ageInTicks() {
        return ageInTicks;
    }

    public void incrementAge() {
        this.ageInTicks++;
    }

    public long lastSpreadTick() {
        return lastSpreadTick;
    }

    public void setLastSpreadTick(long lastSpreadTick) {
        this.lastSpreadTick = lastSpreadTick;
        markDirty();
    }

    public boolean pendingEmpressEmergence() {
        return pendingEmpressEmergence;
    }

    public void setPendingEmpressEmergence(boolean pendingEmpressEmergence) {
        this.pendingEmpressEmergence = pendingEmpressEmergence;
        markDirty();
    }

    public boolean pendingCivilWar() {
        return pendingCivilWar;
    }

    public void setPendingCivilWar(boolean pendingCivilWar) {
        this.pendingCivilWar = pendingCivilWar;
        markDirty();
    }

    public EntityReserves lineagePool() {
        return lineagePool;
    }

    /**
     * Cap-aware add to the lineage pool. Cap grows with location count per {@code HIVE_REDESIGN_05_RESERVES.md} § 6:
     *
     * <pre>
     * cap = baseLineageCapPerType + lineageCapPerLocationPerType * locationCount
     * </pre>
     * <p>
     * Returns the overflow that didn't fit. The caller is responsible for routing it onward (typically into the variant
     * pool — see {@link com.alien.common.gameplay.hive2.location.HivePoolCascade}).
     */
    public int tryAddToLineagePool(EntityType<?> type, int count) {
        if (count <= 0) {
            return 0;
        }

        var config = com.alien.common.gameplay.hive2.location.HiveLocationRegistry.INSTANCE.config();
        var cap = config.baseLineageCapPerType()
            + config.lineageCapPerLocationPerType() * locationsById.size();
        var current = lineagePool.getCount(type);
        var headroom = Math.max(0, cap - current);
        var added = Math.min(count, headroom);

        if (added > 0) {
            lineagePool.add(type, added);
            markDirty();
        }

        return count - added;
    }

    public Map<HiveLocationId, HiveLocation> locationsById() {
        return locationsById;
    }

    /** Live mutable list of in-flight convoys. Callers should call {@link #markDirty()} after modifying. */
    public List<Convoy> convoys() {
        return convoys;
    }

    public Set<HiveLocationId> locationIds() {
        return locationsById.keySet();
    }

    public void addLocation(HiveLocation location) {
        locationsById.put(location.id(), location);
        markDirty();
    }

    public @Nullable HiveLocation removeLocation(HiveLocationId id) {
        var removed = locationsById.remove(id);
        if (removed != null) {
            markDirty();
        }
        return removed;
    }

    public Map<UUID, List<Long>> killAttributionByPlayer() {
        return killAttributionByPlayer;
    }

    /**
     * Records that {@code playerId} killed a member of this lineage at {@code currentTick}. Prunes timestamps outside
     * the raid aggro window so the list stays bounded.
     */
    public void recordKillByPlayer(UUID playerId, long currentTick, long aggroWindowTicks) {
        var timestamps = killAttributionByPlayer.computeIfAbsent(playerId, $ -> new ArrayList<>());
        timestamps.removeIf(t -> currentTick - t > aggroWindowTicks);
        timestamps.add(currentTick);
    }

    /** Count of recent kills by {@code playerId}, dropping entries older than {@code aggroWindowTicks}. */
    public int countRecentKills(UUID playerId, long currentTick, long aggroWindowTicks) {
        var timestamps = killAttributionByPlayer.get(playerId);
        if (timestamps == null) {
            return 0;
        }
        timestamps.removeIf(t -> currentTick - t > aggroWindowTicks);
        return timestamps.size();
    }

    public Map<ResourceLocation, Long> firstAdjacentTickByLineage() {
        return firstAdjacentTickByLineage;
    }

    public @Nullable LineageRemovalReason removalReason() {
        return removalReason;
    }

    public void setRemovalReason(@Nullable LineageRemovalReason removalReason) {
        this.removalReason = removalReason;
        markDirty();
    }

    public boolean isAlive() {
        return removalReason == null;
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains(NBT_VARIANT_ID)) {
            this.variant = AlienVariant.getById(tag.getByte(NBT_VARIANT_ID)).unwrapOr(DEFAULT_VARIANT);
        }

        if (tag.contains(NBT_FACTION_ID)) {
            this.factionId = ResourceLocation.parse(tag.getString(NBT_FACTION_ID));
        }

        if (tag.contains(NBT_PARENT_VARIANT_FACTION_ID)) {
            this.parentVariantFactionId = ResourceLocation.parse(tag.getString(NBT_PARENT_VARIANT_FACTION_ID));
        }

        if (tag.contains(NBT_DIMENSION)) {
            this.dimension = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString(NBT_DIMENSION)));
        }

        if (tag.hasUUID(NBT_FOUNDER_ID)) {
            this.founderId = tag.getUUID(NBT_FOUNDER_ID);
        }

        if (tag.hasUUID(NBT_EMPRESS_ID)) {
            this.empressId = tag.getUUID(NBT_EMPRESS_ID);
        }

        this.ageInTicks = tag.getLong(NBT_AGE_IN_TICKS);
        this.lastSpreadTick = tag.getLong(NBT_LAST_SPREAD_TICK);
        this.pendingEmpressEmergence = tag.getBoolean(NBT_PENDING_EMPRESS_EMERGENCE);
        this.pendingCivilWar = tag.getBoolean(NBT_PENDING_CIVIL_WAR);
        this.lineageNumber = tag.contains(NBT_LINEAGE_NUMBER) ? tag.getLong(NBT_LINEAGE_NUMBER) : -1L;
        this.nextLocationNumber = tag.getLong(NBT_NEXT_LOCATION_NUMBER);

        if (tag.contains(NBT_LINEAGE_POOL)) {
            EntityReserves.CODEC.decode(BLibCodecs.Schema.NBT, tag.getCompound(NBT_LINEAGE_POOL))
                .inspectErr(
                    failure -> Alien.LOGGER.error(
                        "Failed to load lineage pool: {}",
                        failure
                    )
                )
                .ifOk(loaded -> lineagePool.putAll(loaded.getBackingMap()));
        }

        locationsById.clear();
        if (tag.contains(NBT_LOCATIONS)) {
            var listTag = tag.getList(NBT_LOCATIONS, Tag.TAG_COMPOUND);
            for (var i = 0; i < listTag.size(); i++) {
                var locationTag = listTag.getCompound(i);
                var location = HiveLocation.load(locationTag);
                locationsById.put(location.id(), location);
            }
        }

        convoys.clear();
        if (tag.contains(NBT_CONVOYS)) {
            convoys.addAll(ConvoyCodec.loadAll(tag.getList(NBT_CONVOYS, Tag.TAG_COMPOUND)));
        }

        firstAdjacentTickByLineage.clear();
        if (tag.contains(NBT_FIRST_ADJACENT_BY_LINEAGE)) {
            var listTag = tag.getList(NBT_FIRST_ADJACENT_BY_LINEAGE, Tag.TAG_COMPOUND);
            for (var i = 0; i < listTag.size(); i++) {
                var entry = listTag.getCompound(i);
                firstAdjacentTickByLineage.put(
                    ResourceLocation.parse(entry.getString("LineageId")),
                    entry.getLong("Tick")
                );
            }
        }

        if (tag.contains(NBT_REMOVAL_REASON)) {
            this.removalReason = LineageRemovalReason.load(tag.getCompound(NBT_REMOVAL_REASON));
        }
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putByte(NBT_VARIANT_ID, (byte) variant.getId());

        if (factionId != null) {
            tag.putString(NBT_FACTION_ID, factionId.toString());
        }

        if (parentVariantFactionId != null) {
            tag.putString(NBT_PARENT_VARIANT_FACTION_ID, parentVariantFactionId.toString());
        }

        tag.putString(NBT_DIMENSION, dimension.location().toString());

        if (founderId != null) {
            tag.putUUID(NBT_FOUNDER_ID, founderId);
        }

        if (empressId != null) {
            tag.putUUID(NBT_EMPRESS_ID, empressId);
        }

        tag.putLong(NBT_AGE_IN_TICKS, ageInTicks);
        tag.putLong(NBT_LAST_SPREAD_TICK, lastSpreadTick);
        tag.putBoolean(NBT_PENDING_EMPRESS_EMERGENCE, pendingEmpressEmergence);
        tag.putBoolean(NBT_PENDING_CIVIL_WAR, pendingCivilWar);
        if (lineageNumber >= 0) {
            tag.putLong(NBT_LINEAGE_NUMBER, lineageNumber);
        }
        tag.putLong(NBT_NEXT_LOCATION_NUMBER, nextLocationNumber);

        tag.put(NBT_LINEAGE_POOL, EntityReserves.CODEC.encode(BLibCodecs.Schema.NBT, lineagePool));

        var locationsTag = new ListTag();
        for (var location : locationsById.values()) {
            locationsTag.add(location.save());
        }
        tag.put(NBT_LOCATIONS, locationsTag);

        tag.put(NBT_CONVOYS, ConvoyCodec.saveAll(convoys));

        if (!firstAdjacentTickByLineage.isEmpty()) {
            var listTag = new ListTag();
            for (var entry : firstAdjacentTickByLineage.entrySet()) {
                var entryTag = new CompoundTag();
                entryTag.putString("LineageId", entry.getKey().toString());
                entryTag.putLong("Tick", entry.getValue());
                listTag.add(entryTag);
            }
            tag.put(NBT_FIRST_ADJACENT_BY_LINEAGE, listTag);
        }

        if (removalReason != null) {
            tag.put(NBT_REMOVAL_REASON, LineageRemovalReason.save(removalReason));
        }
    }
}

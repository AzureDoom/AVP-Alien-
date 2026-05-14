package com.alien.common.gameplay.hive2.location;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * A fixed-spot hive settlement owned by exactly one lineage faction. The canonical anchor for territory, reserves,
 * leadership, and a boss bar.
 * <p>
 * Per the redesign, {@link #centerPos} is set at founding and never moves. Records of this type live nested inside
 * their owning {@link LineageFactionData}'s NBT — the lineage shard is the authoritative on-disk home. The
 * {@link HiveLocationRegistry}'s indexes are rebuilt from that NBT at server start (see
 * {@code HIVE_REDESIGN_12_PERFORMANCE.md} § 3).
 * <p>
 * Phase 3 wires three behavioral managers behind the data: {@link HiveLocationLeadership} (per-territory leader pick),
 * {@link HiveLocationBossBar} (distance-visible per-location bar), and {@link HiveLocationReserves} (cap-aware spawn
 * budget). The boss bar is constructed lazily on first {@link #tick} call because it needs the lineage's variant.
 */
public final class HiveLocation {

    private static final String NBT_ID = "Id";

    private static final String NBT_LINEAGE_FACTION_ID = "LineageFactionId";

    private static final String NBT_DIMENSION = "Dimension";

    private static final String NBT_CENTER_POS = "CenterPos";

    private static final String NBT_FOUNDER_ID = "FounderId";

    private static final String NBT_AGE_IN_TICKS = "AgeInTicks";

    private static final String NBT_LAST_GROWTH_TICK = "LastGrowthTick";

    private static final String NBT_PEAK_XENOMORPH_COUNT = "PeakXenomorphCount";

    private static final String NBT_PEAK_DECAY_ELAPSED = "PeakDecayElapsedTicks";

    private static final String NBT_EVACUATING_REMAINING = "EvacuatingRemainingTicks";

    private static final String NBT_NO_CONTACT_TICKS_ACCRUED = "NoContactTicksAccrued";

    private static final String NBT_QUEENLESS_MATURATION_LAST_ADVANCE = "QueenlessMaturationLastAdvanceTick";

    private static final String NBT_QUEENLESS_LEADER_SNAPSHOT = "QueenlessLeaderSnapshot";

    private static final String NBT_BIOMASS = "Biomass";

    private static final String NBT_CLAIMED_CHUNKS = "ClaimedChunks";

    private static final String NBT_CHUNK_CLAIM_TICKS = "ChunkClaimTicks";

    private static final String NBT_DECORATED_CHUNKS = "DecoratedChunks";

    private static final String NBT_LOCAL_RESERVES = "LocalReserves";

    private static final String NBT_LEADERSHIP = "Leadership";

    private static final String NBT_REMOVAL_REASON = "RemovalReason";

    private final HiveLocationId id;

    private ResourceLocation lineageFactionId;

    private ResourceKey<Level> dimension;

    private BlockPos centerPos;

    private @Nullable UUID founderId;

    private long ageInTicks;

    private long lastGrowthTick;

    /**
     * Decays at 1/min during normal ticking; floored at 1 to avoid the NaN-divide bug from
     * {@code HIVE_SYSTEM_ANALYSIS.md} § 9.1.4.
     */
    private int peakXenomorphCount;

    private long peakDecayElapsedTicks;

    private long evacuatingRemainingTicks;

    /**
     * Accrued ticks during which this location has had no loaded location-faction member in any of its claimed chunks
     * while at least one claimed chunk was loaded. Drives the no-contact safety-net kill in
     * {@link com.alien.common.gameplay.hive2.lifecycle.LocationDormancyTask}: when this exceeds
     * {@code config.locationMaxNoContactTicks()} the location dies.
     * <p>
     * Pauses (neither advances nor resets) when no claimed chunk is loaded; resets to 0 when a member is observed
     * inside the territory. Persisted across restarts.
     */
    private long noContactTicksAccrued;

    /**
     * Tick at which {@link com.alien.common.gameplay.hive2.lifecycle.QueenlessMaturationTask} last advanced this
     * location's leader through a growth stage. {@link Long#MIN_VALUE} means "no advance yet." Combined with
     * {@link #queenlessLeaderSnapshot}, lets the maturation task detect leader changes (i.e., when an alien finishes
     * cocooning into its next form, the new entity has a fresh UUID — we reset the timer).
     */
    private long queenlessMaturationLastAdvanceTick;

    /** UUID of the alien observed as leader on the last queenless maturation advance. Persisted across restarts. */
    private @Nullable UUID queenlessLeaderSnapshot;

    private int biomass;

    private final Set<ChunkPos> claimedChunks;

    private final Map<ChunkPos, Long> chunkClaimTicks;

    private final Set<ChunkPos> decoratedChunks;

    private final HiveLocationReserves localReserves;

    private final HiveLocationLeadership leadership;

    private final com.alien.common.gameplay.hive2.vent.HiveVentManager ventManager;

    private final Map<EntityType<?>, Set<UUID>> loadedMembersByType;

    private @Nullable HiveLocationBossBar bossBar;

    private @Nullable HiveLocationRemovalReason removalReason;

    public HiveLocation(
        HiveLocationId id,
        ResourceLocation lineageFactionId,
        ResourceKey<Level> dimension,
        BlockPos centerPos,
        @Nullable UUID founderId
    ) {
        this(id);
        this.lineageFactionId = lineageFactionId;
        this.dimension = dimension;
        this.centerPos = centerPos;
        this.founderId = founderId;
    }

    private HiveLocation(HiveLocationId id) {
        this.id = id;
        this.lineageFactionId = ResourceLocation.fromNamespaceAndPath(Alien.MOD_ID, "lineage/unbound");
        this.dimension = Level.OVERWORLD;
        this.centerPos = BlockPos.ZERO;
        this.founderId = null;
        this.ageInTicks = 0L;
        this.lastGrowthTick = 0L;
        this.peakXenomorphCount = 1;
        this.peakDecayElapsedTicks = 0L;
        this.evacuatingRemainingTicks = 0L;
        this.noContactTicksAccrued = 0L;
        this.queenlessMaturationLastAdvanceTick = Long.MIN_VALUE;
        this.queenlessLeaderSnapshot = null;
        this.biomass = 0;
        this.claimedChunks = new LinkedHashSet<>();
        this.chunkClaimTicks = new HashMap<>();
        this.decoratedChunks = new HashSet<>();
        this.localReserves = new HiveLocationReserves(
            () -> claimedChunks.size(),
            () -> HiveLocationRegistry.INSTANCE.config()
        );
        this.leadership = new HiveLocationLeadership();
        this.ventManager = new com.alien.common.gameplay.hive2.vent.HiveVentManager();
        this.loadedMembersByType = new HashMap<>();
        this.bossBar = null;
        this.removalReason = null;
    }

    public HiveLocationId id() {
        return id;
    }

    public ResourceLocation lineageFactionId() {
        return lineageFactionId;
    }

    public void setLineageFactionId(ResourceLocation lineageFactionId) {
        this.lineageFactionId = lineageFactionId;
    }

    public ResourceKey<Level> dimension() {
        return dimension;
    }

    public BlockPos centerPos() {
        return centerPos;
    }

    public @Nullable UUID founderId() {
        return founderId;
    }

    public void setFounderId(@Nullable UUID founderId) {
        this.founderId = founderId;
    }

    public long ageInTicks() {
        return ageInTicks;
    }

    public void incrementAge() {
        this.ageInTicks++;
    }

    public long lastGrowthTick() {
        return lastGrowthTick;
    }

    public void setLastGrowthTick(long lastGrowthTick) {
        this.lastGrowthTick = lastGrowthTick;
    }

    public int peakXenomorphCount() {
        return peakXenomorphCount;
    }

    public void setPeakXenomorphCount(int peakXenomorphCount) {
        this.peakXenomorphCount = Math.max(1, peakXenomorphCount);
    }

    public long peakDecayElapsedTicks() {
        return peakDecayElapsedTicks;
    }

    public void setPeakDecayElapsedTicks(long peakDecayElapsedTicks) {
        this.peakDecayElapsedTicks = Math.max(0L, peakDecayElapsedTicks);
    }

    public long evacuatingRemainingTicks() {
        return evacuatingRemainingTicks;
    }

    public void setEvacuatingRemainingTicks(long evacuatingRemainingTicks) {
        this.evacuatingRemainingTicks = Math.max(0L, evacuatingRemainingTicks);
    }

    public long noContactTicksAccrued() {
        return noContactTicksAccrued;
    }

    public void setNoContactTicksAccrued(long noContactTicksAccrued) {
        this.noContactTicksAccrued = Math.max(0L, noContactTicksAccrued);
    }

    public long queenlessMaturationLastAdvanceTick() {
        return queenlessMaturationLastAdvanceTick;
    }

    public void setQueenlessMaturationLastAdvanceTick(long tick) {
        this.queenlessMaturationLastAdvanceTick = tick;
    }

    public @Nullable UUID queenlessLeaderSnapshot() {
        return queenlessLeaderSnapshot;
    }

    public void setQueenlessLeaderSnapshot(@Nullable UUID uuid) {
        this.queenlessLeaderSnapshot = uuid;
    }

    public int biomass() {
        return biomass;
    }

    public void setBiomass(int biomass) {
        this.biomass = Math.max(0, biomass);
    }

    public Set<ChunkPos> claimedChunks() {
        return claimedChunks;
    }

    public Map<ChunkPos, Long> chunkClaimTicks() {
        return chunkClaimTicks;
    }

    public Set<ChunkPos> decoratedChunks() {
        return decoratedChunks;
    }

    public HiveLocationReserves localReserves() {
        return localReserves;
    }

    public HiveLocationLeadership leadership() {
        return leadership;
    }

    public com.alien.common.gameplay.hive2.vent.HiveVentManager ventManager() {
        return ventManager;
    }

    public Map<EntityType<?>, Set<UUID>> loadedMembersByType() {
        return loadedMembersByType;
    }

    /**
     * Returns the boss bar if one has been constructed (only after the first {@link #tick}). Null is OK and meaningful:
     * a location that has never ticked yet has no live bar.
     */
    public @Nullable HiveLocationBossBar bossBar() {
        return bossBar;
    }

    public @Nullable HiveLocationRemovalReason removalReason() {
        return removalReason;
    }

    public void setRemovalReason(@Nullable HiveLocationRemovalReason removalReason) {
        this.removalReason = removalReason;
    }

    public boolean isAlive() {
        return removalReason == null;
    }

    /**
     * Per-server-tick driver. Looks up the owning lineage, lazy-inits the boss bar on first call, then drives leader
     * pick → boss bar update → reserve top-up. Phase 3 stops here; later phases append biomass income, claim attempts,
     * convoy interactions, etc.
     */
    public void tick(MinecraftServer server, LineageFactionData lineage) {
        leadership.pickBestLeader(loadedMembersByType);

        if (bossBar == null) {
            bossBar = new HiveLocationBossBar(this, lineage.variant(), () -> HiveLocationRegistry.INSTANCE.config());
        }

        bossBar.tick(server, lineage.variant(), lineage);

        // Phase 3 leaves reserve top-up empty here. Phase 4 will hook the
        // periodic outer-edge top-up from HIVE_REDESIGN_05_RESERVES.md § 3 row 3.
    }

    /** Called by the registry when this location is unregistered (lineage absorbed, civil war, location death). */
    public void onUnregistered() {
        if (bossBar != null) {
            bossBar.onRemoved();
            bossBar = null;
        }
    }

    public CompoundTag save() {
        var tag = new CompoundTag();

        tag.putString(NBT_ID, id.value().toString());
        tag.putString(NBT_LINEAGE_FACTION_ID, lineageFactionId.toString());
        tag.putString(NBT_DIMENSION, dimension.location().toString());

        var centerComponents = new int[] { centerPos.getX(), centerPos.getY(), centerPos.getZ() };
        tag.putIntArray(NBT_CENTER_POS, centerComponents);

        if (founderId != null) {
            tag.putUUID(NBT_FOUNDER_ID, founderId);
        }

        tag.putLong(NBT_AGE_IN_TICKS, ageInTicks);
        tag.putLong(NBT_LAST_GROWTH_TICK, lastGrowthTick);
        tag.putInt(NBT_PEAK_XENOMORPH_COUNT, peakXenomorphCount);
        tag.putLong(NBT_PEAK_DECAY_ELAPSED, peakDecayElapsedTicks);
        tag.putLong(NBT_EVACUATING_REMAINING, evacuatingRemainingTicks);
        if (noContactTicksAccrued > 0L) {
            tag.putLong(NBT_NO_CONTACT_TICKS_ACCRUED, noContactTicksAccrued);
        }
        if (queenlessMaturationLastAdvanceTick != Long.MIN_VALUE) {
            tag.putLong(NBT_QUEENLESS_MATURATION_LAST_ADVANCE, queenlessMaturationLastAdvanceTick);
        }
        if (queenlessLeaderSnapshot != null) {
            tag.putUUID(NBT_QUEENLESS_LEADER_SNAPSHOT, queenlessLeaderSnapshot);
        }
        tag.putInt(NBT_BIOMASS, biomass);

        var claimedTag = new ListTag();
        var claimTicksTag = new ListTag();

        for (var chunk : claimedChunks) {
            var chunkTag = new CompoundTag();
            chunkTag.putInt("X", chunk.x);
            chunkTag.putInt("Z", chunk.z);
            claimedTag.add(chunkTag);

            var claimedAt = chunkClaimTicks.getOrDefault(chunk, 0L);
            var ticksTag = new CompoundTag();
            ticksTag.putInt("X", chunk.x);
            ticksTag.putInt("Z", chunk.z);
            ticksTag.putLong("Tick", claimedAt);
            claimTicksTag.add(ticksTag);
        }

        tag.put(NBT_CLAIMED_CHUNKS, claimedTag);
        tag.put(NBT_CHUNK_CLAIM_TICKS, claimTicksTag);

        var decoratedTag = new ListTag();
        for (var chunk : decoratedChunks) {
            var chunkTag = new CompoundTag();
            chunkTag.putInt("X", chunk.x);
            chunkTag.putInt("Z", chunk.z);
            decoratedTag.add(chunkTag);
        }
        tag.put(NBT_DECORATED_CHUNKS, decoratedTag);

        var reservesTag = new CompoundTag();
        localReserves.save(reservesTag);
        tag.put(NBT_LOCAL_RESERVES, reservesTag);

        var leadershipTag = new CompoundTag();
        leadership.save(leadershipTag);
        tag.put(NBT_LEADERSHIP, leadershipTag);

        if (removalReason != null) {
            tag.put(NBT_REMOVAL_REASON, HiveLocationRemovalReason.save(removalReason));
        }

        return tag;
    }

    public static HiveLocation load(CompoundTag tag) {
        var id = HiveLocationId.of(ResourceLocation.parse(tag.getString(NBT_ID)));
        var location = new HiveLocation(id);

        location.lineageFactionId = ResourceLocation.parse(tag.getString(NBT_LINEAGE_FACTION_ID));
        location.dimension = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString(NBT_DIMENSION)));

        if (tag.contains(NBT_CENTER_POS)) {
            var components = tag.getIntArray(NBT_CENTER_POS);
            if (components.length >= 3) {
                location.centerPos = new BlockPos(components[0], components[1], components[2]);
            }
        }

        if (tag.hasUUID(NBT_FOUNDER_ID)) {
            location.founderId = tag.getUUID(NBT_FOUNDER_ID);
        }

        location.ageInTicks = tag.getLong(NBT_AGE_IN_TICKS);
        location.lastGrowthTick = tag.getLong(NBT_LAST_GROWTH_TICK);
        location.peakXenomorphCount = Math.max(1, tag.getInt(NBT_PEAK_XENOMORPH_COUNT));
        location.peakDecayElapsedTicks = Math.max(0L, tag.getLong(NBT_PEAK_DECAY_ELAPSED));
        location.evacuatingRemainingTicks = Math.max(0L, tag.getLong(NBT_EVACUATING_REMAINING));
        location.noContactTicksAccrued = tag.contains(NBT_NO_CONTACT_TICKS_ACCRUED)
            ? Math.max(0L, tag.getLong(NBT_NO_CONTACT_TICKS_ACCRUED))
            : 0L;
        location.queenlessMaturationLastAdvanceTick = tag.contains(NBT_QUEENLESS_MATURATION_LAST_ADVANCE)
            ? tag.getLong(NBT_QUEENLESS_MATURATION_LAST_ADVANCE)
            : Long.MIN_VALUE;
        location.queenlessLeaderSnapshot = tag.hasUUID(NBT_QUEENLESS_LEADER_SNAPSHOT)
            ? tag.getUUID(NBT_QUEENLESS_LEADER_SNAPSHOT)
            : null;
        location.biomass = Math.max(0, tag.getInt(NBT_BIOMASS));

        if (tag.contains(NBT_CLAIMED_CHUNKS)) {
            var claimedTag = tag.getList(NBT_CLAIMED_CHUNKS, Tag.TAG_COMPOUND);
            for (var i = 0; i < claimedTag.size(); i++) {
                var chunkTag = claimedTag.getCompound(i);
                location.claimedChunks.add(new ChunkPos(chunkTag.getInt("X"), chunkTag.getInt("Z")));
            }
        }

        if (tag.contains(NBT_CHUNK_CLAIM_TICKS)) {
            var ticksTag = tag.getList(NBT_CHUNK_CLAIM_TICKS, Tag.TAG_COMPOUND);
            for (var i = 0; i < ticksTag.size(); i++) {
                var entryTag = ticksTag.getCompound(i);
                var chunk = new ChunkPos(entryTag.getInt("X"), entryTag.getInt("Z"));
                location.chunkClaimTicks.put(chunk, entryTag.getLong("Tick"));
            }
        }

        if (tag.contains(NBT_DECORATED_CHUNKS)) {
            var decoratedTag = tag.getList(NBT_DECORATED_CHUNKS, Tag.TAG_COMPOUND);
            for (var i = 0; i < decoratedTag.size(); i++) {
                var chunkTag = decoratedTag.getCompound(i);
                location.decoratedChunks.add(new ChunkPos(chunkTag.getInt("X"), chunkTag.getInt("Z")));
            }
        }

        if (tag.contains(NBT_LOCAL_RESERVES)) {
            location.localReserves.load(tag.getCompound(NBT_LOCAL_RESERVES));
        }

        if (tag.contains(NBT_LEADERSHIP)) {
            location.leadership.load(tag.getCompound(NBT_LEADERSHIP));
        }

        if (tag.contains(NBT_REMOVAL_REASON)) {
            location.removalReason = HiveLocationRemovalReason.load(tag.getCompound(NBT_REMOVAL_REASON));
        }

        return location;
    }
}

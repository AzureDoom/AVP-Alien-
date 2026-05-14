package com.alien.common.gameplay.hive2.inspection;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.economy.CastePopulation;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.faction.VariantFactionData;
import com.alien.common.gameplay.hive2.growth.BiomassIncome;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.blib.api.common.faction.v1.Faction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

/**
 * Server-side builder for the hive2 inspection panel's data feed. Produces a {@link CompoundTag} per kind of selected
 * faction so the wire format is one shape (a tag) regardless of whether the user clicked a location, lineage, or
 * variant row in the FactionBrowser. The {@code /hive2 inspect_location} command and the
 * {@code S2CHiveInspectionPayload} both pull from these builders, so anything the text command surfaces is also
 * available in the panel and the two can't drift apart.
 * <p>
 * Schema keys are declared as constants so the client-side reader can pull fields by name; nested lists (per-caste
 * counts, claimed chunks, owned locations) use {@link ListTag} of compounds, each with the same shape.
 */
public final class HiveInspectionSnapshot {

    public static final String KIND_LOCATION = "location";

    public static final String KIND_LINEAGE = "lineage";

    public static final String KIND_VARIANT = "variant";

    // Shared keys
    public static final String K_FACTION_ID = "FactionId";

    public static final String K_DISPLAY_NAME = "DisplayName";

    public static final String K_DIMENSION = "Dimension";

    public static final String K_AGE_TICKS = "AgeTicks";

    public static final String K_REMOVAL_REASON = "RemovalReason";

    // Location keys
    public static final String K_LOCATION_ID = "LocationId";

    public static final String K_LINEAGE_FACTION_ID = "LineageFactionId";

    public static final String K_CENTER_X = "CenterX";

    public static final String K_CENTER_Y = "CenterY";

    public static final String K_CENTER_Z = "CenterZ";

    public static final String K_FOUNDER_ID = "FounderId";

    public static final String K_BIOMASS = "Biomass";

    public static final String K_BIOMASS_CAP = "BiomassCap";

    public static final String K_NEXT_CLAIM_COST = "NextClaimCost";

    public static final String K_ROYAL_JELLY = "RoyalJelly";

    public static final String K_ROYAL_JELLY_CAP = "RoyalJellyCap";

    public static final String K_ROYAL_JELLY_ACC = "RoyalJellyAcc";

    public static final String K_SCOURGE_JELLY = "ScourgeJelly";

    public static final String K_SCOURGE_JELLY_CAP = "ScourgeJellyCap";

    public static final String K_SCOURGE_QUEEN_ACC = "ScourgeQueenAcc";

    public static final String K_SCOURGE_HARBINGER_ACC = "ScourgeHarbingerAcc";

    public static final String K_CLAIMED_CHUNKS = "ClaimedChunks";

    public static final String K_CHUNKS_LOADED = "ChunksLoaded";

    public static final String K_DECORATED_CHUNKS = "DecoratedChunks";

    public static final String K_PEAK_XENO = "PeakXeno";

    public static final String K_LAST_GROWTH_TICK = "LastGrowthTick";

    public static final String K_NO_CONTACT_TICKS = "NoContactTicks";

    public static final String K_NO_CONTACT_CAP = "NoContactCap";

    public static final String K_EVACUATING_TICKS = "EvacuatingTicks";

    public static final String K_LEADER_ID = "LeaderId";

    public static final String K_TOTAL_POP = "TotalPop";

    public static final String K_POP_CAP = "PopCap";

    public static final String K_PER_CASTE = "PerCaste";

    public static final String K_LOADED_BY_TYPE = "LoadedByType";

    public static final String K_RESERVES_BY_TYPE = "ReservesByType";

    public static final String K_LINEAGE_MEMBER_COUNT = "LineageMembers";

    public static final String K_LOCATION_FACTION_MEMBER_COUNT = "LocationFactionMembers";

    // Lineage keys
    public static final String K_VARIANT_NAME = "VariantName";

    public static final String K_LINEAGE_NUMBER = "LineageNumber";

    public static final String K_NEXT_LOCATION_NUMBER = "NextLocationNumber";

    public static final String K_EMPRESS_ID = "EmpressId";

    public static final String K_PENDING_EMPRESS = "PendingEmpress";

    public static final String K_PENDING_CIVIL_WAR = "PendingCivilWar";

    public static final String K_LINEAGE_MEMBER_TOTAL = "LineageMemberTotal";

    public static final String K_LOCATIONS = "Locations";

    public static final String K_AGG_BIOMASS = "AggBiomass";

    public static final String K_AGG_ROYAL_JELLY = "AggRoyalJelly";

    public static final String K_AGG_SCOURGE_JELLY = "AggScourgeJelly";

    public static final String K_AGG_TOTAL_POP = "AggTotalPop";

    public static final String K_AGG_POP_CAP = "AggPopCap";

    // Variant keys
    public static final String K_NEXT_LINEAGE_NUMBER = "NextLineageNumber";

    public static final String K_LINEAGES = "Lineages";

    public static final String K_VARIANT_MEMBERS = "VariantMembers";

    // Per-entry keys (used inside nested lists)
    public static final String K_KEY = "Key";

    public static final String K_VALUE = "Value";

    public static final String K_LOADED = "Loaded";

    private HiveInspectionSnapshot() {}

    /** Build a snapshot for a single hive location. Mirrors what {@code /hive2 inspect_location} prints. */
    public static CompoundTag buildLocation(HiveLocation location, @Nullable MinecraftServer server) {
        var tag = new CompoundTag();
        var config = HiveLocationRegistry.INSTANCE.config();
        var locationFactionId = location.id().value();

        tag.putString(K_FACTION_ID, locationFactionId.toString());
        tag.putString(K_LOCATION_ID, location.id().value().toString());
        tag.putString(K_LINEAGE_FACTION_ID, location.lineageFactionId().toString());
        tag.putString(K_DIMENSION, location.dimension().location().toString());

        var center = location.centerPos();
        tag.putInt(K_CENTER_X, center.getX());
        tag.putInt(K_CENTER_Y, center.getY());
        tag.putInt(K_CENTER_Z, center.getZ());

        if (location.founderId() != null) {
            tag.putUUID(K_FOUNDER_ID, location.founderId());
        }

        tag.putLong(K_AGE_TICKS, location.ageInTicks());
        tag.putInt(K_BIOMASS, location.biomass());
        tag.putInt(K_BIOMASS_CAP, BiomassIncome.biomassCap(location, config));
        tag.putInt(K_NEXT_CLAIM_COST, BiomassIncome.claimCost(location, config));

        tag.putInt(K_ROYAL_JELLY, location.royalJelly());
        tag.putInt(K_ROYAL_JELLY_CAP, config.royalJellyCap());
        tag.putLong(K_ROYAL_JELLY_ACC, location.royalJellyAccumulator());

        tag.putInt(K_SCOURGE_JELLY, location.scourgeJelly());
        tag.putInt(K_SCOURGE_JELLY_CAP, config.scourgeJellyCap());
        tag.putLong(K_SCOURGE_QUEEN_ACC, location.queenScourgeAccumulator());
        tag.putLong(K_SCOURGE_HARBINGER_ACC, location.harbingerScourgeAccumulator());

        var claimedChunks = location.claimedChunks();
        tag.putInt(K_CLAIMED_CHUNKS, claimedChunks.size());
        tag.putInt(K_DECORATED_CHUNKS, location.decoratedChunks().size());

        var serverLevel = server != null ? server.getLevel(location.dimension()) : null;
        var chunksLoaded = countLoadedChunks(location, serverLevel);
        tag.putInt(K_CHUNKS_LOADED, chunksLoaded);

        tag.putInt(K_PEAK_XENO, location.peakXenomorphCount());
        tag.putLong(K_LAST_GROWTH_TICK, location.lastGrowthTick());
        tag.putLong(K_NO_CONTACT_TICKS, location.noContactTicksAccrued());
        tag.putLong(K_NO_CONTACT_CAP, config.locationMaxNoContactTicks());
        tag.putLong(K_EVACUATING_TICKS, location.evacuatingRemainingTicks());

        var leaderId = location.leadership().getLeaderIdOrNull();
        if (leaderId != null) {
            tag.putUUID(K_LEADER_ID, leaderId);
        }

        var totalPop = CastePopulation.totalTrackedPopulation(location);
        var popCap = config.populationPerChunk() * Math.max(1, claimedChunks.size());
        tag.putInt(K_TOTAL_POP, totalPop);
        tag.putInt(K_POP_CAP, popCap);

        var perCaste = CastePopulation.popByCaste(location);
        var perCasteList = new ListTag();
        for (var entry : perCaste.entrySet()) {
            if (entry.getValue() <= 0) {
                continue;
            }
            var row = new CompoundTag();
            row.putString(K_KEY, entry.getKey().location().toString());
            row.putInt(K_VALUE, entry.getValue());
            perCasteList.add(row);
        }
        tag.put(K_PER_CASTE, perCasteList);

        var loadedList = new ListTag();
        for (var entry : location.loadedMembersByType().entrySet()) {
            var typeId = BuiltInRegistries.ENTITY_TYPE.getKey(entry.getKey());
            var row = new CompoundTag();
            row.putString(K_KEY, typeId.toString());
            row.putInt(K_VALUE, entry.getValue().size());
            loadedList.add(row);
        }
        tag.put(K_LOADED_BY_TYPE, loadedList);

        var reservesList = new ListTag();
        for (var entry : location.localReserves().underlying().getBackingMap().entrySet()) {
            var typeId = BuiltInRegistries.ENTITY_TYPE.getKey(entry.getKey());
            var row = new CompoundTag();
            row.putString(K_KEY, typeId.toString());
            row.putInt(K_VALUE, entry.getValue());
            reservesList.add(row);
        }
        tag.put(K_RESERVES_BY_TYPE, reservesList);

        var locationFaction = Alien.MOD.factions().get(locationFactionId);
        var locationFactionMembers = locationFaction != null ? locationFaction.membership().getMembers().size() : 0;
        tag.putInt(K_LOCATION_FACTION_MEMBER_COUNT, locationFactionMembers);

        var lineageFaction = Alien.MOD.factions().get(location.lineageFactionId());
        var lineageMembers = lineageFaction != null ? lineageFaction.membership().getMembers().size() : 0;
        tag.putInt(K_LINEAGE_MEMBER_COUNT, lineageMembers);

        if (location.removalReason() != null) {
            tag.putString(K_REMOVAL_REASON, location.removalReason().typeKind());
        }

        return tag;
    }

    /** Build a snapshot for a lineage faction. Renders an aggregate over its owned locations. */
    public static CompoundTag buildLineage(Faction<LineageFactionData> faction, @Nullable MinecraftServer server) {
        var tag = new CompoundTag();
        var data = faction.data();
        if (data == null) {
            return tag;
        }
        var config = HiveLocationRegistry.INSTANCE.config();
        var factionId = faction.id();

        tag.putString(K_FACTION_ID, factionId.toString());
        tag.putString(K_DISPLAY_NAME, faction.name());
        tag.putString(K_VARIANT_NAME, data.variant().name());
        tag.putString(K_DIMENSION, data.dimension().location().toString());
        tag.putLong(K_AGE_TICKS, data.ageInTicks());
        tag.putLong(K_LINEAGE_NUMBER, data.lineageNumber());
        tag.putLong(K_NEXT_LOCATION_NUMBER, data.nextLocationNumber());
        if (data.founderId() != null) {
            tag.putUUID(K_FOUNDER_ID, data.founderId());
        }
        if (data.empressId() != null) {
            tag.putUUID(K_EMPRESS_ID, data.empressId());
        }
        tag.putBoolean(K_PENDING_EMPRESS, data.pendingEmpressEmergence());
        tag.putBoolean(K_PENDING_CIVIL_WAR, data.pendingCivilWar());

        var memberCount = faction.membership().getMembers().size();
        tag.putInt(K_LINEAGE_MEMBER_TOTAL, memberCount);

        long aggBiomass = 0L;
        long aggRoyalJelly = 0L;
        long aggScourgeJelly = 0L;
        long aggPop = 0L;
        long aggPopCap = 0L;

        var locationsList = new ListTag();
        for (var location : data.locationsById().values()) {
            aggBiomass += location.biomass();
            aggRoyalJelly += location.royalJelly();
            aggScourgeJelly += location.scourgeJelly();
            var locTotalPop = CastePopulation.totalTrackedPopulation(location);
            var locPopCap = config.populationPerChunk() * Math.max(1, location.claimedChunks().size());
            aggPop += locTotalPop;
            aggPopCap += locPopCap;

            var row = new CompoundTag();
            row.putString(K_LOCATION_ID, location.id().value().toString());
            row.putLong(K_AGE_TICKS, location.ageInTicks());
            row.putInt(K_BIOMASS, location.biomass());
            row.putInt(K_CLAIMED_CHUNKS, location.claimedChunks().size());
            row.putInt(K_TOTAL_POP, locTotalPop);
            row.putInt(K_POP_CAP, locPopCap);
            locationsList.add(row);
        }
        tag.put(K_LOCATIONS, locationsList);

        tag.putLong(K_AGG_BIOMASS, aggBiomass);
        tag.putLong(K_AGG_ROYAL_JELLY, aggRoyalJelly);
        tag.putLong(K_AGG_SCOURGE_JELLY, aggScourgeJelly);
        tag.putLong(K_AGG_TOTAL_POP, aggPop);
        tag.putLong(K_AGG_POP_CAP, aggPopCap);

        if (data.removalReason() != null) {
            tag.putString(K_REMOVAL_REASON, data.removalReason().typeKind());
        }

        return tag;
    }

    /** Build a snapshot for a variant faction. Lists owned lineages plus aggregates across them. */
    public static CompoundTag buildVariant(Faction<VariantFactionData> faction, @Nullable MinecraftServer server) {
        var tag = new CompoundTag();
        var data = faction.data();
        if (data == null) {
            return tag;
        }
        var config = HiveLocationRegistry.INSTANCE.config();
        var factionId = faction.id();

        tag.putString(K_FACTION_ID, factionId.toString());
        tag.putString(K_DISPLAY_NAME, faction.name());
        tag.putString(K_VARIANT_NAME, data.variant().name());
        tag.putLong(K_AGE_TICKS, data.ageInTicks());
        tag.putLong(K_NEXT_LINEAGE_NUMBER, data.nextLineageNumber());
        tag.putInt(K_VARIANT_MEMBERS, faction.membership().getMembers().size());

        long aggBiomass = 0L;
        long aggRoyalJelly = 0L;
        long aggScourgeJelly = 0L;
        long aggPop = 0L;
        long aggPopCap = 0L;
        var lineagesList = new ListTag();

        // Walk every registered faction and collect lineages whose parent variant is this one.
        for (var id : Alien.MOD.factions().getAllIds()) {
            var f = Alien.MOD.factions().get(id);
            if (f == null || !(f.data() instanceof LineageFactionData lineage)) {
                continue;
            }
            if (!factionId.equals(lineage.parentVariantFactionId())) {
                continue;
            }
            long lineageBiomass = 0L;
            long lineagePop = 0L;
            long lineagePopCap = 0L;
            for (var loc : lineage.locationsById().values()) {
                lineageBiomass += loc.biomass();
                aggBiomass += loc.biomass();
                aggRoyalJelly += loc.royalJelly();
                aggScourgeJelly += loc.scourgeJelly();
                var locPop = CastePopulation.totalTrackedPopulation(loc);
                var locCap = config.populationPerChunk() * Math.max(1, loc.claimedChunks().size());
                lineagePop += locPop;
                lineagePopCap += locCap;
                aggPop += locPop;
                aggPopCap += locCap;
            }
            var row = new CompoundTag();
            row.putString(K_FACTION_ID, f.id().toString());
            row.putString(K_DISPLAY_NAME, f.name());
            row.putString(K_DIMENSION, lineage.dimension().location().toString());
            row.putLong(K_LINEAGE_NUMBER, lineage.lineageNumber());
            row.putInt(K_CLAIMED_CHUNKS, lineage.locationsById().size());
            row.putLong(K_BIOMASS, lineageBiomass);
            row.putLong(K_TOTAL_POP, lineagePop);
            row.putLong(K_POP_CAP, lineagePopCap);
            row.putBoolean(K_PENDING_EMPRESS, lineage.pendingEmpressEmergence());
            row.putBoolean(K_PENDING_CIVIL_WAR, lineage.pendingCivilWar());
            lineagesList.add(row);
        }
        tag.put(K_LINEAGES, lineagesList);

        tag.putLong(K_AGG_BIOMASS, aggBiomass);
        tag.putLong(K_AGG_ROYAL_JELLY, aggRoyalJelly);
        tag.putLong(K_AGG_SCOURGE_JELLY, aggScourgeJelly);
        tag.putLong(K_AGG_TOTAL_POP, aggPop);
        tag.putLong(K_AGG_POP_CAP, aggPopCap);

        return tag;
    }

    private static int countLoadedChunks(HiveLocation location, @Nullable ServerLevel level) {
        if (level == null) {
            return 0;
        }
        var loaded = 0;
        for (var chunk : location.claimedChunks()) {
            if (level.getChunkSource().hasChunk(chunk.x, chunk.z)) {
                loaded++;
            }
        }
        return loaded;
    }

}

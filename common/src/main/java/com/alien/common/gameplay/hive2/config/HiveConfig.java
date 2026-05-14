package com.alien.common.gameplay.hive2.config;

/**
 * Single-source-of-truth tunables for the new hive system. A constructor config object — not abstract method overrides
 * — per the project's preference for explicit, top-down configuration.
 * <p>
 * Phase 1 ships sensible defaults from {@code HIVE_REDESIGN_13_CONFIGURATION.md}. Later phases consume each value as
 * they implement the corresponding mechanic. Where the design doc lists a value as "not yet specified — recommend X",
 * the recommended value is used here.
 * <p>
 * To tune, swap {@link #defaults()} for a custom instance at the call site (typically in
 * {@link com.alien.common.gameplay.hive2.location.HiveLocationRegistry} or in the per-tick task constructors).
 */
public record HiveConfig(
    // ---------- § 1 Faction lifecycle ----------
    long lineageAbsorptionAdjacencyTicks,
    long protoHiveStageInterval,
    long lineageReservePromotionInterval,

    // ---------- § 2 Locations ----------
    long settlementTicks,
    long locationMaxNoContactTicks,
    int bossBarDisplayRadiusBlocks,
    long angryGraceTicks,
    long contestTickWindow,
    int queenRangeChunksMin,
    int queenRangeChunksMax,
    int praetorianRangeChunksMin,
    int praetorianRangeChunksMax,
    int droneRangeChunksMin,
    int droneRangeChunksMax,
    int warriorRangeChunksMin,
    int warriorRangeChunksMax,

    // ---------- § 3 Reserves ----------
    long shedGraceTicks,
    long minLineageAgeForShedding,
    int baseLocalCapPerType,
    int localCapPerClaimedChunkPerType,
    int baseLineageCapPerType,
    int lineageCapPerLocationPerType,
    int variantPoolCapPerType,

    // ---------- § 4 Convoys (common) ----------
    double convoySpeedBlocksPerSecond,
    int arrivalRadiusBlocks,
    int manifestDistanceBlocks,
    double reinforcementSpeedMultiplier,
    double migrationSpeedMultiplier,
    double raidSpeedMultiplier,

    // ---------- § 5 Reinforcement convoys ----------
    long reinforcementSourceCooldownTicks,
    int reinforcementMinSize,
    int reinforcementMaxSize,

    // ---------- § 6 Migration convoys ----------
    long resettleGraceTicks,
    long migrationBiomassDecayTicks,
    int migrationTerritoryFloorChunks,
    long migrationRallyTicks,
    int migrationBiomassPayloadCap,

    // ---------- § 7 Raid convoys ----------
    int raidThresholdKills,
    long raidAggroWindowTicks,
    int raidMinLocationSizeChunks,
    long perSourceRaidCooldownTicks,
    long raidExpiryTicks,
    int baseRaidSize,
    double raidSizePerClaimedChunk,
    int raidEngageRadiusBlocks,

    // ---------- § 8 Leadership ----------
    long empressMoltDurationTicks,
    long localLeaderPickCadenceTicks,

    // ---------- § 9 Lineage spread ----------
    int maxLineageSpreadChunks,
    long lineageSpreadCooldownTicks,
    int maxLocationsPerLineage,
    long foragerJoinTicks,

    // ---------- § 10 Growth ----------
    long claimActivityWindowTicks,
    long perLocationClaimCooldownTicks,
    long lineageScanIntervalTicks,
    int maxChunksPerLocation,
    int maxChunksPerLineage,
    int maxLineagesPerDimensionPerVariant,
    int maxClaimsPerScan,
    long resinFullDensityTicks,

    // ---------- § 11 Biomass ----------
    int baseChunkCost,
    double growthFactor,
    double baseUnloadedBiomassPerChunkPerSec,
    double unloadedEmpressBonusPerSec,
    double loadedBiomassPerLoadedXenomorphPerSec,
    int loadedBiomassPerNonAlienKill,
    int loadedBiomassPerResinBlockPlaced,
    double loadedBiomassPerOvomorphPerSec,
    double loadedBiomassEmpressPresentBonusPerSec,
    double loadedBiomassIdleBonusPerSec,
    int biomassAccumulationCapMultiplier,

    // ---------- § 12 Persistence and performance ----------
    int biomassDirtyThreshold,
    long lastGrowthTickDirtyThreshold,
    int poolDirtyThreshold
) {

    private static final int TICKS_PER_SECOND = 20;

    private static final long TICKS_PER_MINUTE = 60L * TICKS_PER_SECOND;

    private static final long TICKS_PER_HOUR = 60L * TICKS_PER_MINUTE;

    public static HiveConfig defaults() {
        return new HiveConfig(
            // § 1 Faction lifecycle
            5L * TICKS_PER_MINUTE, // lineageAbsorptionAdjacencyTicks: 5 min
            5L * TICKS_PER_MINUTE, // protoHiveStageInterval: 5 min between drone→warrior→praetorian→queen advances
            1L * TICKS_PER_MINUTE, // lineageReservePromotionInterval: 1 min between queen-driven reserve promotions

            // § 2 Locations
            60L * TICKS_PER_SECOND, // settlementTicks: 60s
            7L * 24L * TICKS_PER_HOUR, // locationMaxNoContactTicks: 7 game-days of loaded-no-contact time
            96, // bossBarDisplayRadiusBlocks
            60L * TICKS_PER_SECOND, // angryGraceTicks: 60s
            60L * TICKS_PER_SECOND, // contestTickWindow: 60s
            0,
            0, // queen range: center chunk only
            0,
            2, // praetorian range: 0–2 chunks
            0,
            6, // drone range: 0–6 chunks
            2,
            10, // warrior range: 2–10 chunks

            // § 3 Reserves
            5L * TICKS_PER_MINUTE, // shedGraceTicks: 5 min
            30L * TICKS_PER_MINUTE, // minLineageAgeForShedding: 30 min
            8, // baseLocalCapPerType
            2, // localCapPerClaimedChunkPerType
            32, // baseLineageCapPerType
            8, // lineageCapPerLocationPerType
            Integer.MAX_VALUE, // variantPoolCapPerType: effectively uncapped

            // § 4 Convoys (common)
            10.0, // convoySpeedBlocksPerSecond
            16, // arrivalRadiusBlocks
            80, // manifestDistanceBlocks
            1.0, // reinforcementSpeedMultiplier
            0.7, // migrationSpeedMultiplier
            1.5, // raidSpeedMultiplier

            // § 5 Reinforcement convoys
            5L * TICKS_PER_MINUTE, // reinforcementSourceCooldownTicks
            4, // reinforcementMinSize
            10, // reinforcementMaxSize

            // § 6 Migration convoys
            5L * TICKS_PER_MINUTE, // resettleGraceTicks
            30L * TICKS_PER_MINUTE, // migrationBiomassDecayTicks
            2, // migrationTerritoryFloorChunks
            30L * TICKS_PER_SECOND, // migrationRallyTicks
            500, // migrationBiomassPayloadCap

            // § 7 Raid convoys
            5, // raidThresholdKills
            10L * TICKS_PER_MINUTE, // raidAggroWindowTicks
            8, // raidMinLocationSizeChunks
            20L * TICKS_PER_MINUTE, // perSourceRaidCooldownTicks
            30L * TICKS_PER_MINUTE, // raidExpiryTicks
            4, // baseRaidSize
            0.25, // raidSizePerClaimedChunk
            32, // raidEngageRadiusBlocks

            // § 8 Leadership
            30L * TICKS_PER_SECOND, // empressMoltDurationTicks
            5L * TICKS_PER_SECOND, // localLeaderPickCadenceTicks (100 ticks)

            // § 9 Lineage spread
            32, // maxLineageSpreadChunks
            30L * TICKS_PER_MINUTE, // lineageSpreadCooldownTicks
            64, // maxLocationsPerLineage
            30L * TICKS_PER_SECOND, // foragerJoinTicks

            // § 10 Growth
            30L * TICKS_PER_SECOND, // claimActivityWindowTicks
            30L * TICKS_PER_SECOND, // perLocationClaimCooldownTicks
            5L * TICKS_PER_MINUTE, // lineageScanIntervalTicks
            256, // maxChunksPerLocation
            10000, // maxChunksPerLineage
            16, // maxLineagesPerDimensionPerVariant
            20, // maxClaimsPerScan
            24L * TICKS_PER_HOUR, // resinFullDensityTicks

            // § 11 Biomass
            100, // baseChunkCost
            0.05, // growthFactor
            0.05, // baseUnloadedBiomassPerChunkPerSec
            1.0, // unloadedEmpressBonusPerSec
            1.0, // loadedBiomassPerLoadedXenomorphPerSec
            25, // loadedBiomassPerNonAlienKill
            5, // loadedBiomassPerResinBlockPlaced
            0.5, // loadedBiomassPerOvomorphPerSec
            5.0, // loadedBiomassEmpressPresentBonusPerSec
            0.1, // loadedBiomassIdleBonusPerSec
            100, // biomassAccumulationCapMultiplier

            // § 12 Persistence and performance
            10, // biomassDirtyThreshold (±10)
            30L * TICKS_PER_MINUTE, // lastGrowthTickDirtyThreshold (30 min)
            5 // poolDirtyThreshold (±5)
        );
    }
}

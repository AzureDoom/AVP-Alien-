package com.alien.common.gameplay.hive2.spawning;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.AlienSpawning;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.faction.LocationMembership;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationReserves;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.gameplay.hive2.location.HiveLocationSpacing;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class HiveLoadedSpawner {

    private static final int MIN_DISTANCE_FROM_PLAYER_BLOCKS = 24;

    private static final int MAX_DISTANCE_FROM_PLAYER_BLOCKS = 96;

    private static final int PLAYER_CHUNK_RANGE = MAX_DISTANCE_FROM_PLAYER_BLOCKS / 16;

    private static final int FOUNDER_QUEEN_CAVE_COLUMNS_PER_SCAN = 32;

    private static final int FOUNDER_QUEEN_SURFACE_ATTEMPTS = 64;

    private static final int ENTOURAGE_SPAWN_ATTEMPTS = 24;

    private static final int ENTOURAGE_SPAWN_RADIUS = 4;

    private HiveLoadedSpawner() {}

    public static void scanAndSpawn(MinecraftServer server) {
        var config = HiveLocationRegistry.INSTANCE.config();

        for (var location : HiveLocationRegistry.INSTANCE.all()) {
            if (!location.isAlive()) {
                continue;
            }

            var level = server.getLevel(location.dimension());
            if (level == null) {
                continue;
            }

            if (location.pendingFounderQueen()) {
                trySpawnPendingFounderQueen(level, location);
                continue;
            }

            var loadedCount = countLoadedXenomorphs(location);
            if (loadedCount >= config.hiveSpawnerMinimumLoadedXenomorphs()) {
                continue;
            }

            var players = nearbyPlayers(level, location);
            if (players.isEmpty()) {
                continue;
            }

            var spawned = 0;
            var attempts = 0;
            while (
                loadedCount + spawned < config.hiveSpawnerMinimumLoadedXenomorphs()
                    && spawned < config.hiveSpawnerMaxSpawnsPerLocation()
                    && attempts < config.hiveSpawnerMaxSpawnAttemptsPerLocation()
            ) {
                attempts++;

                var entity = trySpawnLocalReserve(level, location, players);
                if (entity != null) {
                    spawned++;
                }
            }
        }
    }

    private static int countLoadedXenomorphs(HiveLocation location) {
        var count = 0;
        for (var entry : location.loadedMembersByType().entrySet()) {
            if (entry.getKey().is(AlienEntityTypeTags.XENOMORPHS)) {
                count += entry.getValue().size();
            }
        }
        return count;
    }

    private static List<ServerPlayer> nearbyPlayers(ServerLevel level, HiveLocation location) {
        var radius = HiveLocationRegistry.INSTANCE.config().bossBarDisplayRadiusBlocks();
        var radiusSqr = (double) radius * radius;
        return level.players()
            .stream()
            .filter(player -> player.blockPosition().distSqr(location.centerPos()) <= radiusSqr)
            .toList();
    }

    private static @Nullable Entity trySpawnLocalReserve(
        ServerLevel level,
        HiveLocation location,
        List<ServerPlayer> players
    ) {
        var type = pickWeightedReserveType(level, location.localReserves());
        if (type == null) {
            return null;
        }

        var player = players.get(level.random.nextInt(players.size()));
        var pos = pickSpawnPosition(level, location, player, type);
        if (pos == null) {
            return null;
        }

        return type.spawn(level, pos, MobSpawnType.NATURAL);
    }

    private static @Nullable Entity trySpawnPendingFounderQueen(ServerLevel level, HiveLocation location) {
        var queenType = queenTypeFor(location);
        if (queenType == null) {
            return null;
        }

        var pos = pickFounderQueenSpawnPosition(level, location, queenType);
        if (pos == null) {
            return null;
        }

        var entity = queenType.spawn(level, pos, MobSpawnType.MOB_SUMMONED);
        if (!(entity instanceof Queen queen)) {
            return null;
        }

        queen.setPersistenceRequired();
        location.setFounderId(queen.getUUID());
        location.setPendingFounderQueen(false);
        LocationMembership.join(location, queen);
        spawnLocalReservesWithFounder(level, location, queen.blockPosition());

        return queen;
    }

    private static @Nullable EntityType<? extends Alien> queenTypeFor(HiveLocation location) {
        var faction = com.alien.Alien.MOD.factions().get(location.lineageFactionId());
        if (faction == null || !(faction.data() instanceof LineageFactionData lineage)) {
            return null;
        }
        return Queen.getType(lineage.variant());
    }

    private static @Nullable EntityType<?> pickWeightedReserveType(ServerLevel level, HiveLocationReserves reserves) {
        var weightedTypes = new ArrayList<WeightedType>();
        var totalWeight = 0;

        for (var type : reserves.getAvailableEntityTypes()) {
            if (!type.is(AlienEntityTypeTags.XENOMORPHS)) {
                continue;
            }

            var reserveCount = reserves.getCount(type);
            var weight = weightFor(type) * Math.max(1, reserveCount);
            if (weight <= 0) {
                continue;
            }

            weightedTypes.add(new WeightedType(type, weight));
            totalWeight += weight;
        }

        if (weightedTypes.isEmpty() || totalWeight <= 0) {
            return null;
        }

        var roll = level.random.nextInt(totalWeight);
        for (var weightedType : weightedTypes) {
            roll -= weightedType.weight();
            if (roll < 0) {
                return weightedType.type();
            }
        }

        return weightedTypes.get(weightedTypes.size() - 1).type();
    }

    private static int weightFor(EntityType<?> type) {
        if (
            type.is(AlienEntityTypeTags.CHESTBURSTERS)
                || type.is(AlienEntityTypeTags.ADOLESCENTS)
                || type.is(AlienEntityTypeTags.BURSTERS)
        ) {
            return 14;
        }
        if (type.is(AlienEntityTypeTags.DRONES) || type.is(AlienEntityTypeTags.RUNNERS)) {
            return 12;
        }
        if (
            type.is(AlienEntityTypeTags.WARRIORS)
                || type.is(AlienEntityTypeTags.PROWLERS)
                || type.is(AlienEntityTypeTags.SPITTERS)
        ) {
            return 8;
        }
        if (
            type.is(AlienEntityTypeTags.CARRIERS)
                || type.is(AlienEntityTypeTags.RAVAGERS)
                || type.is(AlienEntityTypeTags.RAZOR_CLAWS)
                || type.is(AlienEntityTypeTags.PREDALIENS)
                || type.is(AlienEntityTypeTags.CHRYSALISES)
        ) {
            return 3;
        }
        return 1;
    }

    private static @Nullable BlockPos pickSpawnPosition(
        ServerLevel level,
        HiveLocation location,
        ServerPlayer player,
        EntityType<?> type
    ) {
        var candidateChunks = candidateChunks(location, player, requiresCoreSpawn(type));
        if (candidateChunks.isEmpty()) {
            return null;
        }

        for (var attempt = 0; attempt < 8; attempt++) {
            var chunk = candidateChunks.get(level.random.nextInt(candidateChunks.size()));
            var x = chunk.x * 16 + level.random.nextInt(16);
            var z = chunk.z * 16 + level.random.nextInt(16);
            var baseY = player.blockPosition().getY() + level.random.nextInt(17) - 8;

            for (var dy = -8; dy <= 8; dy++) {
                var y = Math.clamp(baseY + dy, level.getMinBuildHeight() + 1, level.getMaxBuildHeight() - 1);
                var pos = new BlockPos(x, y, z);
                if (isValidSpawnPosition(level, pos, type)) {
                    return pos;
                }
            }
        }

        return null;
    }

    private static List<ChunkPos> candidateChunks(HiveLocation location, ServerPlayer player, boolean requiresCoreSpawn) {
        var playerChunk = new ChunkPos(player.blockPosition());
        var centerChunk = new ChunkPos(location.centerPos());
        var coreRadius = HiveLocationRegistry.INSTANCE.config().initialHiveLocationClaimRadiusChunks();

        return location.claimedChunks()
            .stream()
            .filter(chunk -> !requiresCoreSpawn || HiveLocationSpacing.chunkDistance(chunk, centerChunk) <= coreRadius)
            .filter(chunk -> HiveLocationSpacing.chunkDistance(chunk, playerChunk) <= PLAYER_CHUNK_RANGE)
            .toList();
    }

    private static boolean requiresCoreSpawn(EntityType<?> type) {
        return type.is(AlienEntityTypeTags.QUEENS)
            || type.is(AlienEntityTypeTags.HARBINGERS)
            || type.is(AlienEntityTypeTags.PRAETORIANS)
            || type.is(AlienEntityTypeTags.CRUSHERS);
    }

    @SuppressWarnings("unchecked")
    private static boolean isValidSpawnPosition(ServerLevel level, BlockPos pos, EntityType<?> rawType) {
        if (!isValidPlayerDistance(level, pos)) {
            return false;
        }
        if (!level.noCollision(rawType.getSpawnAABB(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5))) {
            return false;
        }
        if (!AlienSpawning.canSpawnAt((EntityType<? extends Alien>) rawType, level, MobSpawnType.NATURAL, pos, level.random)) {
            return false;
        }
        return true;
    }

    private static @Nullable BlockPos pickFounderQueenSpawnPosition(
        ServerLevel level,
        HiveLocation location,
        EntityType<?> queenType
    ) {
        var candidateChunks = loadedCoreChunks(level, location);
        if (candidateChunks.isEmpty()) {
            return null;
        }

        var cavePos = pickFounderQueenCavePosition(level, location, queenType, candidateChunks);
        if (cavePos != null) {
            return cavePos;
        }

        return pickFounderQueenSurfacePosition(level, location, queenType, candidateChunks);
    }

    private static @Nullable BlockPos pickFounderQueenCavePosition(
        ServerLevel level,
        HiveLocation location,
        EntityType<?> queenType,
        List<ChunkPos> candidateChunks
    ) {
        for (var columnAttempt = 0; columnAttempt < FOUNDER_QUEEN_CAVE_COLUMNS_PER_SCAN; columnAttempt++) {
            var chunk = candidateChunks.get(level.random.nextInt(candidateChunks.size()));
            var x = chunk.x * 16 + level.random.nextInt(16);
            var z = chunk.z * 16 + level.random.nextInt(16);
            var surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            var minY = level.getMinBuildHeight() + 1;
            var maxY = Math.min(surfaceY - 1, level.getMaxBuildHeight() - 1);

            for (var y = maxY; y >= minY; y--) {
                var pos = new BlockPos(x, y, z);
                if (!level.canSeeSky(pos) && isValidFounderQueenSpawnPosition(level, location, pos, queenType)) {
                    return pos;
                }
            }
        }

        return null;
    }

    private static @Nullable BlockPos pickFounderQueenSurfacePosition(
        ServerLevel level,
        HiveLocation location,
        EntityType<?> queenType,
        List<ChunkPos> candidateChunks
    ) {
        for (var attempt = 0; attempt < FOUNDER_QUEEN_SURFACE_ATTEMPTS; attempt++) {
            var chunk = candidateChunks.get(level.random.nextInt(candidateChunks.size()));
            var x = chunk.x * 16 + level.random.nextInt(16);
            var z = chunk.z * 16 + level.random.nextInt(16);
            var y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            var pos = new BlockPos(x, y, z);

            if (isValidFounderQueenSpawnPosition(level, location, pos, queenType)) {
                return pos;
            }
        }

        return null;
    }

    private static List<ChunkPos> loadedCoreChunks(ServerLevel level, HiveLocation location) {
        var centerChunk = new ChunkPos(location.centerPos());
        var coreRadius = HiveLocationRegistry.INSTANCE.config().initialHiveLocationClaimRadiusChunks();

        return location.claimedChunks()
            .stream()
            .filter(chunk -> HiveLocationSpacing.chunkDistance(chunk, centerChunk) <= coreRadius)
            .filter(chunk -> level.getChunkSource().hasChunk(chunk.x, chunk.z))
            .toList();
    }

    private static boolean isValidFounderQueenSpawnPosition(
        ServerLevel level,
        HiveLocation location,
        BlockPos pos,
        EntityType<?> queenType
    ) {
        return HiveLocationSpawnGate.locationContaining(level, pos) == location
            && isValidForcedSpawnPosition(level, pos, queenType);
    }

    private static void spawnLocalReservesWithFounder(ServerLevel level, HiveLocation location, BlockPos founderPos) {
        for (var type : new ArrayList<>(location.localReserves().getAvailableEntityTypes())) {
            if (!type.is(AlienEntityTypeTags.XENOMORPHS) || type.is(AlienEntityTypeTags.QUEENS)) {
                continue;
            }

            var count = location.localReserves().getCount(type);
            for (var i = 0; i < count; i++) {
                var pos = pickEntourageSpawnPosition(level, location, founderPos, type);
                if (pos == null) {
                    break;
                }
                if (type.spawn(level, pos, MobSpawnType.MOB_SUMMONED) == null) {
                    break;
                }
            }
        }
    }

    private static @Nullable BlockPos pickEntourageSpawnPosition(
        ServerLevel level,
        HiveLocation location,
        BlockPos founderPos,
        EntityType<?> type
    ) {
        for (var attempt = 0; attempt < ENTOURAGE_SPAWN_ATTEMPTS; attempt++) {
            var x = founderPos.getX() + level.random.nextInt(ENTOURAGE_SPAWN_RADIUS * 2 + 1) - ENTOURAGE_SPAWN_RADIUS;
            var y = founderPos.getY() + level.random.nextInt(5) - 2;
            var z = founderPos.getZ() + level.random.nextInt(ENTOURAGE_SPAWN_RADIUS * 2 + 1) - ENTOURAGE_SPAWN_RADIUS;
            var pos = new BlockPos(x, Math.clamp(y, level.getMinBuildHeight() + 1, level.getMaxBuildHeight() - 1), z);
            if (HiveLocationSpawnGate.locationContaining(level, pos) == location && isValidForcedSpawnPosition(level, pos, type)) {
                return pos;
            }
        }

        return null;
    }

    private static boolean isValidForcedSpawnPosition(ServerLevel level, BlockPos pos, EntityType<?> type) {
        var floorPos = pos.below();
        var floorState = level.getBlockState(floorPos);
        if (!floorState.isFaceSturdy(level, floorPos, Direction.UP)) {
            return false;
        }
        if (!level.getBlockState(pos).getFluidState().isEmpty()) {
            return false;
        }
        return level.noCollision(type.getSpawnAABB(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
    }

    private static boolean isValidPlayerDistance(ServerLevel level, BlockPos pos) {
        var minSqr = (double) MIN_DISTANCE_FROM_PLAYER_BLOCKS * MIN_DISTANCE_FROM_PLAYER_BLOCKS;
        var maxSqr = (double) MAX_DISTANCE_FROM_PLAYER_BLOCKS * MAX_DISTANCE_FROM_PLAYER_BLOCKS;
        var hasPlayerInRange = false;

        for (var player : level.players()) {
            var distanceSqr = player.blockPosition().distSqr(pos);
            if (distanceSqr < minSqr) {
                return false;
            }
            if (distanceSqr <= maxSqr) {
                hasPlayerInRange = true;
            }
        }

        return hasPlayerInRange;
    }

    private record WeightedType(EntityType<?> type, int weight) {}
}

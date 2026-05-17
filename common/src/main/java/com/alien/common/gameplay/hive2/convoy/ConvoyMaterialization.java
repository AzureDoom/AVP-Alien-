package com.alien.common.gameplay.hive2.convoy;

import com.alien.common.gameplay.hive2.spawning.ReserveSpawnUtil;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class ConvoyMaterialization {

    private static final int RAID_WAVE_COUNT = 5;

    private static final int RAID_LAST_WAVE_INDEX = RAID_WAVE_COUNT - 1;

    private static final int SPAWN_SEARCH_RADIUS_BLOCKS = 8;

    private static final int SPAWN_SEARCH_BLOCKS_ABOVE_ORIGIN = 12;

    private ConvoyMaterialization() {}

    static int spawnAll(ServerLevel level, Convoy convoy, BlockPos spawnPos, ServerPlayer targetPlayer) {
        return spawnEntities(level, convoy, expandComposition(convoy, false), spawnPos, targetPlayer);
    }

    static int spawnNextRaidWave(ServerLevel level, Convoy.Raid raid, BlockPos spawnPos, ServerPlayer targetPlayer) {
        while (raid.composition().getCount() > 0) {
            var wave = selectRaidWave(raid);
            if (wave.isEmpty()) {
                raid.advanceWave();
                continue;
            }

            var spawnedCount = spawnEntities(level, raid, wave, spawnPos, targetPlayer);
            if (spawnedCount > 0) {
                if (raid.nextWaveIndex() < RAID_WAVE_COUNT) {
                    raid.advanceWave();
                }
                return spawnedCount;
            }

            return 0;
        }

        return 0;
    }

    private static List<EntityType<?>> selectRaidWave(Convoy.Raid raid) {
        var waveIndex = Math.min(raid.nextWaveIndex(), RAID_LAST_WAVE_INDEX);
        if (waveIndex >= RAID_LAST_WAVE_INDEX) {
            return expandComposition(raid, false);
        }

        var nonHarbingers = expandComposition(raid, true);
        if (nonHarbingers.isEmpty()) {
            return List.of();
        }

        var nonHarbingerWavesRemaining = RAID_LAST_WAVE_INDEX - waveIndex;
        var waveSize = Math.max(1, (int) Math.ceil(nonHarbingers.size() / (double) nonHarbingerWavesRemaining));
        return new ArrayList<>(nonHarbingers.subList(0, Math.min(waveSize, nonHarbingers.size())));
    }

    private static List<EntityType<?>> expandComposition(Convoy convoy, boolean excludeHarbingers) {
        var expanded = new ArrayList<EntityType<?>>();
        for (var entityType : new ArrayList<>(convoy.composition().getAvailableEntityTypes())) {
            if (excludeHarbingers && entityType.is(AlienEntityTypeTags.HARBINGERS)) {
                continue;
            }
            var count = convoy.composition().getCount(entityType);
            for (var i = 0; i < count; i++) {
                expanded.add(entityType);
            }
        }
        return expanded;
    }

    private static int spawnEntities(
        ServerLevel level,
        Convoy convoy,
        List<EntityType<?>> entityTypes,
        BlockPos spawnPos,
        ServerPlayer targetPlayer
    ) {
        var spawnedCount = 0;
        for (var entityType : entityTypes) {
            var spawned = spawnRelaxed(entityType, level, spawnPos);
            if (spawned == null) {
                continue;
            }

            ReserveSpawnUtil.markSpawnedFromReserves(spawned);
            if (spawned instanceof Mob mob) {
                mob.setTarget(targetPlayer);
                if (!(convoy instanceof Convoy.Raid)) {
                    mob.setPersistenceRequired();
                }
            }
            ConvoyMemberTracker.markSpawned(convoy, spawned);

            convoy.composition().add(entityType, -1);
            spawnedCount++;
        }
        return spawnedCount;
    }

    private static @Nullable Entity spawnRelaxed(EntityType<?> entityType, ServerLevel level, BlockPos origin) {
        var spawnPos = findGroundSpawnPos(entityType, level, origin);
        if (spawnPos == null) {
            return null;
        }

        var entity = entityType.create(level);
        if (entity == null) {
            return null;
        }

        entity.moveTo(
            spawnPos.getX() + 0.5,
            spawnPos.getY(),
            spawnPos.getZ() + 0.5,
            level.random.nextFloat() * 360.0F,
            0.0F
        );
        if (entity instanceof Mob mob) {
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.MOB_SUMMONED, null);
        }
        level.addFreshEntityWithPassengers(entity);
        return entity;
    }

    private static @Nullable BlockPos findGroundSpawnPos(EntityType<?> entityType, ServerLevel level, BlockPos origin) {
        for (var radius = 0; radius <= SPAWN_SEARCH_RADIUS_BLOCKS; radius++) {
            for (var dx = -radius; dx <= radius; dx++) {
                for (var dz = -radius; dz <= radius; dz++) {
                    if (radius > 0 && Math.max(Math.abs(dx), Math.abs(dz)) != radius) {
                        continue;
                    }

                    var columnOrigin = origin.offset(dx, 0, dz);
                    var pos = findGroundSpawnPosInColumn(entityType, level, columnOrigin);
                    if (pos != null) {
                        return pos;
                    }
                }
            }
        }
        return null;
    }

    private static @Nullable BlockPos findGroundSpawnPosInColumn(
        EntityType<?> entityType,
        ServerLevel level,
        BlockPos origin
    ) {
        var minY = level.getMinBuildHeight() + 1;
        var maxY = level.getMaxBuildHeight() - 2;
        var startY = Math.clamp(origin.getY() + SPAWN_SEARCH_BLOCKS_ABOVE_ORIGIN, minY, maxY);

        for (var y = startY; y >= minY; y--) {
            var pos = new BlockPos(origin.getX(), y, origin.getZ());
            if (isValidGroundSpawnPos(entityType, level, pos)) {
                return pos;
            }
        }
        return null;
    }

    private static boolean isValidGroundSpawnPos(EntityType<?> entityType, ServerLevel level, BlockPos pos) {
        if (!level.getWorldBorder().isWithinBounds(pos)) {
            return false;
        }

        var groundPos = pos.below();
        if (!level.getBlockState(groundPos).isFaceSturdy(level, groundPos, Direction.UP)) {
            return false;
        }

        return level.noCollision(entityType.getSpawnAABB(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
    }
}

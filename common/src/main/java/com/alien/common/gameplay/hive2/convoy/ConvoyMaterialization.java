package com.alien.common.gameplay.hive2.convoy;

import com.alien.common.gameplay.hive2.spawning.ReserveSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

import java.util.ArrayList;

final class ConvoyMaterialization {

    private ConvoyMaterialization() {}

    static int spawnAll(ServerLevel level, Convoy convoy, BlockPos spawnPos, ServerPlayer targetPlayer) {
        var spawnedCount = 0;

        for (var entityType : new ArrayList<>(convoy.composition().getAvailableEntityTypes())) {
            var count = convoy.composition().getCount(entityType);
            for (var i = 0; i < count; i++) {
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
                if (convoy instanceof Convoy.Raid raid) {
                    RaidMemberTracker.markSpawned(raid, spawned);
                }

                convoy.composition().add(entityType, -1);
                spawnedCount++;
            }
        }

        return spawnedCount;
    }

    private static Entity spawnRelaxed(EntityType<?> entityType, ServerLevel level, BlockPos spawnPos) {
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
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.queen;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.hive.HiveRegistry;
import com.alien.common.gameplay.level.saveddata.QueenSpawnChunkData;
import com.alien.common.gameplay.level.saveddata.StrainLeakData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Objects;

public class QueenSpawning {

    private static final int MAX_OVERWORLD_Y_LEVEL = -24;

    public static final SpawnPlacements.SpawnPredicate<Queen> PREDICATE = (
        entityType,
        serverLevelAccessor,
        mobSpawnType,
        blockPos,
        randomSource
    ) -> {
        var serverLevel = serverLevelAccessor.getLevel();

        if (
            QueenSpawnChunkData.getOrCreate(serverLevel).isSomeAnd(queenSpawnChunkData -> queenSpawnChunkData.getSpawnCooldown().isActive())
        ) {
            return false;
        }

        var queenSpawnChunkDataOption = QueenSpawnChunkData.getOrCreate(serverLevel);
        var isChunkSpawnAvailable = queenSpawnChunkDataOption
            .isSomeAnd(queenSpawnChunkData -> !queenSpawnChunkData.isChunkBlacklisted(blockPos));

        if (!isChunkSpawnAvailable) {
            return false;
        }

        int maxYLevelForDimension;

        if (serverLevel.dimension() == Level.NETHER) {
            maxYLevelForDimension = serverLevel.dimensionType().logicalHeight();
        } else {
            maxYLevelForDimension = MAX_OVERWORLD_Y_LEVEL;
        }

        return blockPos.getY() <= maxYLevelForDimension
            && canStrainSpawnInLevel(serverLevel, entityType)
            && checkSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource);
    };

    private static boolean canStrainSpawnInLevel(ServerLevel serverLevel, EntityType<Queen> entityType) {
        var strainLeakDataOption = StrainLeakData.getOrCreate(serverLevel);
        var alienVariantTypeOption = AlienVariantTypes.getFor(entityType);

        return alienVariantTypeOption.isSomeAnd(
            alienVariantType -> strainLeakDataOption
                .isSomeAnd(strainLeakData -> strainLeakData.hasVariant(alienVariantType.variant()))
        );
    }

    public static boolean checkSpawnRules(
        EntityType<? extends Monster> entityType,
        ServerLevelAccessor serverLevelAccessor,
        MobSpawnType mobSpawnType,
        BlockPos blockPos,
        RandomSource randomSource
    ) {
        var alienVariantTypeOption = AlienVariantTypes.getFor(entityType);

        return Monster.checkMonsterSpawnRules(
            entityType,
            serverLevelAccessor,
            mobSpawnType,
            blockPos,
            randomSource
        )
            && isNotWithinExistingHiveBuffer(serverLevelAccessor, blockPos, alienVariantTypeOption);
    }

    private static boolean isNotWithinExistingHiveBuffer(
        ServerLevelAccessor serverLevelAccessor,
        BlockPos blockPos,
        com.just.core.functional.option.Option<com.alien.common.model.alien.variant.AlienVariantType> alienVariantTypeOption
    ) {
        var level = serverLevelAccessor.getLevel();
        var nearestHive = HiveRegistry.INSTANCE.findNearestHive(
            blockPos,
            level.dimension(),
            hive -> alienVariantTypeOption.isSomeAnd(
                alienVariantType -> Objects.equals(hive.getVariant(), alienVariantType.variant())
            )
        );

        if (nearestHive == null) {
            return true;
        }

        return !nearestHive.getSpaceManager().isBlockPosWithinHiveBuffer(blockPos);
    }
}

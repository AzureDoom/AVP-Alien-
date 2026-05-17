package com.alien.common.gameplay.entity.living.alien;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.hive2.spawning.HiveLocationSpawnGate;
import com.alien.common.model.alien.variant.AlienVariantType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;

public class AlienSpawning {

    @SuppressWarnings("unchecked")
    public static <T extends Alien> SpawnPlacements.SpawnPredicate<T> getTypedPredicate() {
        return (SpawnPlacements.SpawnPredicate<T>) PREDICATE;
    }

    private static final SpawnPlacements.SpawnPredicate<Alien> PREDICATE = (
        entityType,
        serverLevelAccessor,
        mobSpawnType,
        blockPos,
        randomSource
    ) -> canSpawnAt(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource);

    public static boolean canSpawnAt(
        EntityType<? extends Alien> entityType,
        ServerLevelAccessor serverLevelAccessor,
        MobSpawnType mobSpawnType,
        BlockPos blockPos,
        RandomSource randomSource
    ) {
        var belowState = serverLevelAccessor.getBlockState(blockPos.below());
        var alienVariantTypeOption = AlienVariantTypes.getFor(entityType)
            .map(AlienVariantType::resinBlockTag);

        var isValidResinPos = alienVariantTypeOption.isSomeAnd(belowState::is);

        return isValidResinPos
            && checkSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource);
    }

    public static boolean checkSpawnRules(
        EntityType<? extends Monster> entityType,
        ServerLevelAccessor serverLevelAccessor,
        MobSpawnType mobSpawnType,
        BlockPos blockPos,
        RandomSource randomSource
    ) {
        return Monster.checkAnyLightMonsterSpawnRules(
            entityType,
            serverLevelAccessor,
            mobSpawnType,
            blockPos,
            randomSource
        ) &&
            canSpawnWithinNearestHive(entityType, serverLevelAccessor, blockPos);
    }

    /**
     * Hive2 spawn gate: a {@link com.alien.common.gameplay.hive2.location.HiveLocation} must contain this chunk, the
     * caste-distance rule must permit the entity at this distance from the location's center, and reserves must have
     * one available.
     */
    private static boolean canSpawnWithinNearestHive(
        EntityType<? extends Monster> entityType,
        ServerLevelAccessor serverLevelAccessor,
        BlockPos blockPos
    ) {
        return HiveLocationSpawnGate.findSpawnableLocation(serverLevelAccessor, entityType, blockPos) != null;
    }
}

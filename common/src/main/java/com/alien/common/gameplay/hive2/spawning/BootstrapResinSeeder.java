package com.alien.common.gameplay.hive2.spawning;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.model.alien.variant.AlienVariantType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;

/**
 * One-time resin foothold placement for abstract-spread arrivals. Normal hive spawns stay resin-gated; this only lets
 * the first abstract founder materialize when it can leave a physical resin seed behind.
 */
public final class BootstrapResinSeeder {

    private BootstrapResinSeeder() {}

    public static boolean canSeedAt(ServerLevel level, BlockPos spawnPos, EntityType<?> type) {
        var variantType = AlienVariantTypes.getForOrNull(type);
        return variantType != null && canSeedAt(level, spawnPos, variantType);
    }

    public static boolean seedAt(ServerLevel level, BlockPos spawnPos, EntityType<?> type) {
        var variantType = AlienVariantTypes.getForOrNull(type);
        if (variantType == null || !canSeedAt(level, spawnPos, variantType)) {
            return false;
        }

        var seedPos = spawnPos.below();
        var currentState = level.getBlockState(seedPos);
        if (currentState.is(variantType.resinBlockTag())) {
            return true;
        }

        return level.setBlockAndUpdate(seedPos, variantType.resinNode().get().defaultBlockState());
    }

    private static boolean canSeedAt(ServerLevel level, BlockPos spawnPos, AlienVariantType variantType) {
        var seedState = level.getBlockState(spawnPos.below());
        return seedState.is(variantType.resinBlockTag()) || seedState.is(variantType.resinReplaceableTag());
    }
}

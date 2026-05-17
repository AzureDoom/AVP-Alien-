package com.alien.common.gameplay.hive2.convoy;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class RaidWaveSelection {

    private RaidWaveSelection() {}

    static @Nullable EntityType<?> chooseType(
        List<RaidWaveProfile.PoolEntry> pools,
        Inventory inventory,
        Map<Integer, Integer> selectedByPool,
        RandomSource random
    ) {
        var matchingPools = new ArrayList<PoolMatch>();
        for (var i = 0; i < pools.size(); i++) {
            var pool = pools.get(i);
            if (selectedByPool.getOrDefault(i, 0) >= pool.maxCount()) {
                continue;
            }
            if (hasMatchingType(pool, inventory)) {
                matchingPools.add(new PoolMatch(i, pool));
            }
        }

        if (!matchingPools.isEmpty()) {
            var match = choosePool(matchingPools, random);
            var type = chooseMatchingType(match.pool(), inventory, random);
            if (type != null) {
                selectedByPool.merge(match.index(), 1, Integer::sum);
                return type;
            }
        }

        return null;
    }

    private static boolean hasMatchingType(
        RaidWaveProfile.PoolEntry pool,
        Inventory inventory
    ) {
        for (var type : inventory.availableTypes()) {
            if (pool.matches(type) && inventory.count(type) > 0) {
                return true;
            }
        }
        return false;
    }

    private static @Nullable EntityType<?> chooseMatchingType(
        RaidWaveProfile.PoolEntry pool,
        Inventory inventory,
        RandomSource random
    ) {
        var candidates = new ArrayList<EntityType<?>>();
        for (var type : inventory.availableTypes()) {
            if (pool.matches(type) && inventory.count(type) > 0) {
                candidates.add(type);
            }
        }
        return chooseByCount(candidates, inventory, random);
    }

    private static @Nullable PoolMatch choosePool(ArrayList<PoolMatch> matches, RandomSource random) {
        var totalWeight = 0;
        for (var match : matches) {
            totalWeight += match.pool().weight();
        }

        var target = random.nextInt(Math.max(1, totalWeight));
        for (var match : matches) {
            target -= match.pool().weight();
            if (target < 0) {
                return match;
            }
        }
        return matches.getLast();
    }

    private static @Nullable EntityType<?> chooseByCount(
        ArrayList<EntityType<?>> candidates,
        Inventory inventory,
        RandomSource random
    ) {
        if (candidates.isEmpty()) {
            return null;
        }

        var totalCount = 0;
        for (var type : candidates) {
            totalCount += inventory.count(type);
        }

        var target = random.nextInt(Math.max(1, totalCount));
        for (var type : candidates) {
            target -= inventory.count(type);
            if (target < 0) {
                return type;
            }
        }
        return candidates.getLast();
    }

    interface Inventory {
        Iterable<EntityType<?>> availableTypes();

        int count(EntityType<?> type);
    }

    private record PoolMatch(int index, RaidWaveProfile.PoolEntry pool) {}
}

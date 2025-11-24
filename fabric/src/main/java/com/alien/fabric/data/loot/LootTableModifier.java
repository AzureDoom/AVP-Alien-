package com.alien.fabric.data.loot;

import com.alien.common.registry.init.item.AlienItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;

public class LootTableModifier {

    public static void initialize() {
        LootTableEvents.MODIFY.register((key, builder, source, provider) -> {
            if (!source.isBuiltin()) {
                return;
            }

            // Archaeology loot
            addColdOceanRuinsSherdLoot(key, builder);
            addWarmOceanRuinsSherdLoot(key, builder);
        });
    }

    private static void addColdOceanRuinsSherdLoot(ResourceKey<LootTable> key, LootTable.Builder builder) {
        if (!key.equals(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY)) {
            return;
        }

        builder.modifyPools(
            (pool) -> pool
                .add(LootItem.lootTableItem(AlienItems.OVOID_POTTERY_SHERD.get()))
                .add(LootItem.lootTableItem(AlienItems.ROYALTY_POTTERY_SHERD.get()))
        );
    }

    private static void addWarmOceanRuinsSherdLoot(ResourceKey<LootTable> key, LootTable.Builder builder) {
        if (!key.equals(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY)) {
            return;
        }

        builder.modifyPools(
            (pool) -> pool
                .add(LootItem.lootTableItem(AlienItems.PARASITE_POTTERY_SHERD.get()))
                .add(LootItem.lootTableItem(AlienItems.VECTOR_POTTERY_SHERD.get()))
        );
    }

    private LootTableModifier() {
        throw new UnsupportedOperationException();
    }
}

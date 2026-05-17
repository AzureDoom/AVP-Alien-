package com.alien.common.registry;

import com.alien.common.gameplay.hive.economy.HiveUnitPurchase;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Static registry of {@link HiveUnitPurchase} keyed by concrete output entity type. Populated by
 * {@link com.alien.common.data.HiveUnitPurchaseReloadListener} on datapack reload.
 */
public final class HiveUnitPurchaseRegistry {

    private static final Map<EntityType<?>, HiveUnitPurchase> BY_OUTPUT_ENTITY = new HashMap<>();

    private HiveUnitPurchaseRegistry() {}

    public static void clear() {
        BY_OUTPUT_ENTITY.clear();
    }

    public static void register(HiveUnitPurchase purchase) {
        BY_OUTPUT_ENTITY.put(purchase.outputEntity(), purchase);
    }

    public static @Nullable HiveUnitPurchase forOutputEntity(EntityType<?> entityType) {
        return BY_OUTPUT_ENTITY.get(entityType);
    }

    public static Collection<HiveUnitPurchase> all() {
        return Collections.unmodifiableCollection(BY_OUTPUT_ENTITY.values());
    }
}

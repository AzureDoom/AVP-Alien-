package com.alien.common.registry.init.item.block;

import com.alien.Alien;
import com.alien.common.gameplay.item.RoyalJellyBlockItem;
import com.blib.BLibHolder;
import com.blib.common.registry.impl.BLibItemRegistry;
import net.minecraft.world.item.BlockItem;

import java.util.function.Supplier;

public class AlienBlockItems {

    private static final BLibItemRegistry REGISTRY = Alien.MOD.createItemRegistry();

    public static final BLibHolder<BlockItem> ROYAL_JELLY_BLOCK = createWithSupplier(
        "royal_jelly_block",
        RoyalJellyBlockItem::new
    );

    private static BLibHolder<BlockItem> createWithSupplier(String id, Supplier<BlockItem> blockItemSupplier) {
        return REGISTRY.createHolder(id, blockItemSupplier);
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

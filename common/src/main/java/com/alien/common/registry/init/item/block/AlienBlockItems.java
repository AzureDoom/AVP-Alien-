package com.alien.common.registry.init.item.block;

import com.alien.Alien;
import com.alien.common.gameplay.item.RoyalJellyBlockItem;
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class AlienBlockItems {

    private static final BLibRegistry<BlockItem> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.ITEM);

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

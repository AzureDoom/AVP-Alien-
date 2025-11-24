package com.alien.common.registry.init.item.block;

import com.alien.common.gameplay.item.RoyalJellyBlockItem;
import com.alien.common.registry.init.item.AlienItems;
import com.avp.common.registry.AVPDeferredHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class AlienBlockItems {

    public static final AVPDeferredHolder<BlockItem> ROYAL_JELLY_BLOCK = registerWithSupplier(
        "royal_jelly_block",
        RoyalJellyBlockItem::new
    );

    public static AVPDeferredHolder<BlockItem> register(String id, Supplier<? extends Block> blockSupplier) {
        return register(id, blockSupplier, new Item.Properties());
    }

    public static AVPDeferredHolder<BlockItem> register(String id, Supplier<? extends Block> blockSupplier, Item.Properties properties) {
        return registerWithSupplier(id, () -> new BlockItem(blockSupplier.get(), properties));
    }

    public static AVPDeferredHolder<BlockItem> registerWithSupplier(String id, Supplier<BlockItem> blockItemSupplier) {
        return AlienItems.register(id, blockItemSupplier);
    }

    public static void initialize() {}
}

package com.alien.common.gameplay.item;

import com.alien.common.registry.init.block.AlienBlocks;
import net.minecraft.world.item.BlockItem;

public class RoyalJellyBlockItem extends BlockItem {

    public RoyalJellyBlockItem() {
        super(AlienBlocks.ROYAL_JELLY_BLOCK.get(), new Properties().stacksTo(64));
    }
}

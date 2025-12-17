package com.alien.fabric.data.recipe.impl.resin;

import com.alien.common.gameplay.block.resin.vein.ResinVeinBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

record ResinSet(
    Supplier<Item> resinBallItem,
    Supplier<Block> resinBlock,
    Supplier<Block> resinBlockSlab,
    Supplier<Block> resinBlockStairs,
    Supplier<Block> brick,
    Supplier<Block> brickSlab,
    Supplier<Block> brickStairs,
    Supplier<Block> brickWall,
    Supplier<Block> smooth,
    Supplier<Block> smoothSlab,
    Supplier<Block> smoothStairs,
    Supplier<Block> smoothWall,
    Supplier<ResinVeinBlock> vein,
    Supplier<Block> web
) {}

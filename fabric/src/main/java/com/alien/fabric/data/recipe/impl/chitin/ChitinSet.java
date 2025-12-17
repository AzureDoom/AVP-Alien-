package com.alien.fabric.data.recipe.impl.chitin;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

record ChitinSet(
    Supplier<Item> chitinItem,
    Supplier<Item> platedChitinItem,
    Supplier<Block> chitinBlock,
    Supplier<Block> chitinBlockSlab,
    Supplier<Block> chitinBlockStairs,
    Supplier<Block> chitinBlockWall,
    Supplier<Block> bricks,
    Supplier<Block> brickSlab,
    Supplier<Block> brickStairs,
    Supplier<Block> brickWall,
    Supplier<Block> chiseledBricks,
    Supplier<Block> chiseledBricksEmbryo,
    Supplier<Block> polished,
    Supplier<Block> polishedSlab,
    Supplier<Block> polishedStairs,
    Supplier<Block> polishedWall
) {}

package com.alien.common.registry.init.block;

import com.alien.common.registry.init.block.property.AlienBlockProperties;
import com.avp.common.registry.AVPDeferredHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

public class AlienChitinBlocks {

    public static final AVPDeferredHolder<Block> ABERRANT_CHITIN_BLOCK = AlienBlocks.register(
        "aberrant_chitin_block",
        AlienBlockProperties.ABERRANT_CHITIN
    );

    public static final AVPDeferredHolder<Block> ABERRANT_CHITIN_BLOCK_SLAB = AlienBlocks.register(
        "aberrant_chitin_block_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> ABERRANT_CHITIN_BLOCK_STAIRS = AlienBlocks.register(
        "aberrant_chitin_block_stairs",
        () -> new StairBlock(
            ABERRANT_CHITIN_BLOCK.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_CHITIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> ABERRANT_CHITIN_BLOCK_WALL = AlienBlocks.register(
        "aberrant_chitin_block_wall",
        () -> new WallBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> ABERRANT_CHITIN_BRICKS = AlienBlocks.register(
        "aberrant_chitin_bricks",
        AlienBlockProperties.ABERRANT_CHITIN
    );

    public static final AVPDeferredHolder<Block> ABERRANT_CHITIN_BRICK_SLAB = AlienBlocks.register(
        "aberrant_chitin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> ABERRANT_CHITIN_BRICK_STAIRS = AlienBlocks.register(
        "aberrant_chitin_brick_stairs",
        () -> new StairBlock(
            ABERRANT_CHITIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_CHITIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> ABERRANT_CHITIN_BRICK_WALL = AlienBlocks.register(
        "aberrant_chitin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> CHISELED_ABERRANT_CHITIN_BRICKS = AlienBlocks.register(
        "chiseled_aberrant_chitin_bricks",
        AlienBlockProperties.ABERRANT_CHITIN
    );

    public static final AVPDeferredHolder<Block> CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO = AlienBlocks.register(
        "chiseled_aberrant_chitin_bricks_embryo",
        AlienBlockProperties.ABERRANT_CHITIN
    );

    public static final AVPDeferredHolder<Block> CHISELED_CHITIN_BRICKS = AlienBlocks.register(
        "chiseled_chitin_bricks",
        AlienBlockProperties.CHITIN
    );

    public static final AVPDeferredHolder<Block> CHISELED_CHITIN_BRICKS_EMBRYO = AlienBlocks.register(
        "chiseled_chitin_bricks_embryo",
        AlienBlockProperties.CHITIN
    );

    public static final AVPDeferredHolder<Block> CHISELED_NETHER_CHITIN_BRICKS = AlienBlocks.register(
        "chiseled_nether_chitin_bricks",
        AlienBlockProperties.NETHER_CHITIN
    );

    public static final AVPDeferredHolder<Block> CHISELED_NETHER_CHITIN_BRICKS_EMBRYO = AlienBlocks.register(
        "chiseled_nether_chitin_bricks_embryo",
        AlienBlockProperties.NETHER_CHITIN
    );

    public static final AVPDeferredHolder<Block> CHITIN_BLOCK = AlienBlocks.register(
        "chitin_block",
        AlienBlockProperties.CHITIN
    );

    public static final AVPDeferredHolder<Block> CHITIN_BLOCK_SLAB = AlienBlocks.register(
        "chitin_block_slab",
        () -> new SlabBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> CHITIN_BLOCK_STAIRS = AlienBlocks.register(
        "chitin_block_stairs",
        () -> new StairBlock(
            CHITIN_BLOCK.get().defaultBlockState(),
            AlienBlockProperties.CHITIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> CHITIN_BLOCK_WALL = AlienBlocks.register(
        "chitin_block_wall",
        () -> new WallBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> CHITIN_BRICKS = AlienBlocks.register(
        "chitin_bricks",
        AlienBlockProperties.CHITIN
    );

    public static final AVPDeferredHolder<Block> CHITIN_BRICK_SLAB = AlienBlocks.register(
        "chitin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> CHITIN_BRICK_STAIRS = AlienBlocks.register(
        "chitin_brick_stairs",
        () -> new StairBlock(
            CHITIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.CHITIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> CHITIN_BRICK_WALL = AlienBlocks.register(
        "chitin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> NETHER_CHITIN_BLOCK = AlienBlocks.register(
        "nether_chitin_block",
        AlienBlockProperties.NETHER_CHITIN
    );

    public static final AVPDeferredHolder<Block> NETHER_CHITIN_BLOCK_SLAB = AlienBlocks.register(
        "nether_chitin_block_slab",
        () -> new SlabBlock(AlienBlockProperties.NETHER_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> NETHER_CHITIN_BLOCK_STAIRS = AlienBlocks.register(
        "nether_chitin_block_stairs",
        () -> new StairBlock(
            NETHER_CHITIN_BLOCK.get().defaultBlockState(),
            AlienBlockProperties.NETHER_CHITIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> NETHER_CHITIN_BLOCK_WALL = AlienBlocks.register(
        "nether_chitin_block_wall",
        () -> new WallBlock(AlienBlockProperties.NETHER_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> NETHER_CHITIN_BRICKS = AlienBlocks.register(
        "nether_chitin_bricks",
        AlienBlockProperties.NETHER_CHITIN
    );

    public static final AVPDeferredHolder<Block> NETHER_CHITIN_BRICK_SLAB = AlienBlocks.register(
        "nether_chitin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.NETHER_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> NETHER_CHITIN_BRICK_STAIRS = AlienBlocks.register(
        "nether_chitin_brick_stairs",
        () -> new StairBlock(
            NETHER_CHITIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.NETHER_CHITIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> NETHER_CHITIN_BRICK_WALL = AlienBlocks.register(
        "nether_chitin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.NETHER_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> POLISHED_ABERRANT_CHITIN = AlienBlocks.register(
        "polished_aberrant_chitin",
        AlienBlockProperties.ABERRANT_CHITIN
    );

    public static final AVPDeferredHolder<Block> POLISHED_ABERRANT_CHITIN_SLAB = AlienBlocks.register(
        "polished_aberrant_chitin_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> POLISHED_ABERRANT_CHITIN_STAIRS = AlienBlocks.register(
        "polished_aberrant_chitin_stairs",
        () -> new StairBlock(
            POLISHED_ABERRANT_CHITIN.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_CHITIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> POLISHED_ABERRANT_CHITIN_WALL = AlienBlocks.register(
        "polished_aberrant_chitin_wall",
        () -> new WallBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> POLISHED_CHITIN = AlienBlocks.register(
        "polished_chitin",
        AlienBlockProperties.CHITIN
    );

    public static final AVPDeferredHolder<Block> POLISHED_CHITIN_SLAB = AlienBlocks.register(
        "polished_chitin_slab",
        () -> new SlabBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> POLISHED_CHITIN_STAIRS = AlienBlocks.register(
        "polished_chitin_stairs",
        () -> new StairBlock(
            POLISHED_CHITIN.get().defaultBlockState(),
            AlienBlockProperties.CHITIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> POLISHED_CHITIN_WALL = AlienBlocks.register(
        "polished_chitin_wall",
        () -> new WallBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> POLISHED_NETHER_CHITIN = AlienBlocks.register(
        "polished_nether_chitin",
        AlienBlockProperties.NETHER_CHITIN
    );

    public static final AVPDeferredHolder<Block> POLISHED_NETHER_CHITIN_SLAB = AlienBlocks.register(
        "polished_nether_chitin_slab",
        () -> new SlabBlock(AlienBlockProperties.NETHER_CHITIN.build())
    );

    public static final AVPDeferredHolder<Block> POLISHED_NETHER_CHITIN_STAIRS = AlienBlocks.register(
        "polished_nether_chitin_stairs",
        () -> new StairBlock(
            POLISHED_NETHER_CHITIN.get().defaultBlockState(),
            AlienBlockProperties.NETHER_CHITIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> POLISHED_NETHER_CHITIN_WALL = AlienBlocks.register(
        "polished_nether_chitin_wall",
        () -> new WallBlock(AlienBlockProperties.NETHER_CHITIN.build())
    );

    public static void initialize() {}
}

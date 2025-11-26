package com.alien.common.registry.init.item.block;

import com.alien.Alien;
import com.alien.common.registry.init.block.AlienChitinBlocks;
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class AlienChitinBlockItems {

    private static final BLibRegistry<BlockItem> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.ITEM);

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BLOCK = create(
        "aberrant_chitin_block",
        AlienChitinBlocks.ABERRANT_CHITIN_BLOCK
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BLOCK_SLAB = create(
        "aberrant_chitin_block_slab",
        AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_SLAB
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BLOCK_STAIRS = create(
        "aberrant_chitin_block_stairs",
        AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_STAIRS
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BLOCK_WALL = create(
        "aberrant_chitin_block_wall",
        AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_WALL
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BRICKS = create(
        "aberrant_chitin_bricks",
        AlienChitinBlocks.ABERRANT_CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BRICK_SLAB = create(
        "aberrant_chitin_brick_slab",
        AlienChitinBlocks.ABERRANT_CHITIN_BRICK_SLAB
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BRICK_STAIRS = create(
        "aberrant_chitin_brick_stairs",
        AlienChitinBlocks.ABERRANT_CHITIN_BRICK_STAIRS
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BRICK_WALL = create(
        "aberrant_chitin_brick_wall",
        AlienChitinBlocks.ABERRANT_CHITIN_BRICK_WALL
    );

    public static final BLibHolder<BlockItem> CHISELED_ABERRANT_CHITIN_BRICKS = create(
        "chiseled_aberrant_chitin_bricks",
        AlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO = create(
        "chiseled_aberrant_chitin_bricks_embryo",
        AlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO
    );

    public static final BLibHolder<BlockItem> CHISELED_CHITIN_BRICKS = create(
        "chiseled_chitin_bricks",
        AlienChitinBlocks.CHISELED_CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> CHISELED_CHITIN_BRICKS_EMBRYO = create(
        "chiseled_chitin_bricks_embryo",
        AlienChitinBlocks.CHISELED_CHITIN_BRICKS_EMBRYO
    );

    public static final BLibHolder<BlockItem> CHISELED_NETHER_CHITIN_BRICKS = create(
        "chiseled_nether_chitin_bricks",
        AlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> CHISELED_NETHER_CHITIN_BRICKS_EMBRYO = create(
        "chiseled_nether_chitin_bricks_embryo",
        AlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS_EMBRYO
    );

    public static final BLibHolder<BlockItem> CHITIN_BLOCK = create(
        "chitin_block",
        AlienChitinBlocks.CHITIN_BLOCK
    );

    public static final BLibHolder<BlockItem> CHITIN_BLOCK_SLAB = create(
        "chitin_block_slab",
        AlienChitinBlocks.CHITIN_BLOCK_SLAB
    );

    public static final BLibHolder<BlockItem> CHITIN_BLOCK_STAIRS = create(
        "chitin_block_stairs",
        AlienChitinBlocks.CHITIN_BLOCK_STAIRS
    );

    public static final BLibHolder<BlockItem> CHITIN_BLOCK_WALL = create(
        "chitin_block_wall",
        AlienChitinBlocks.CHITIN_BLOCK_WALL
    );

    public static final BLibHolder<BlockItem> CHITIN_BRICKS = create(
        "chitin_bricks",
        AlienChitinBlocks.CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> CHITIN_BRICK_SLAB = create(
        "chitin_brick_slab",
        AlienChitinBlocks.CHITIN_BRICK_SLAB
    );

    public static final BLibHolder<BlockItem> CHITIN_BRICK_STAIRS = create(
        "chitin_brick_stairs",
        AlienChitinBlocks.CHITIN_BRICK_STAIRS
    );

    public static final BLibHolder<BlockItem> CHITIN_BRICK_WALL = create(
        "chitin_brick_wall",
        AlienChitinBlocks.CHITIN_BRICK_WALL
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BLOCK = create(
        "nether_chitin_block",
        AlienChitinBlocks.NETHER_CHITIN_BLOCK
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BLOCK_SLAB = create(
        "nether_chitin_block_slab",
        AlienChitinBlocks.NETHER_CHITIN_BLOCK_SLAB
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BLOCK_STAIRS = create(
        "nether_chitin_block_stairs",
        AlienChitinBlocks.NETHER_CHITIN_BLOCK_STAIRS
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BLOCK_WALL = create(
        "nether_chitin_block_wall",
        AlienChitinBlocks.NETHER_CHITIN_BLOCK_WALL
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BRICKS = create(
        "nether_chitin_bricks",
        AlienChitinBlocks.NETHER_CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BRICK_SLAB = create(
        "nether_chitin_brick_slab",
        AlienChitinBlocks.NETHER_CHITIN_BRICK_SLAB
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BRICK_STAIRS = create(
        "nether_chitin_brick_stairs",
        AlienChitinBlocks.NETHER_CHITIN_BRICK_STAIRS
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BRICK_WALL = create(
        "nether_chitin_brick_wall",
        AlienChitinBlocks.NETHER_CHITIN_BRICK_WALL
    );

    public static final BLibHolder<BlockItem> POLISHED_ABERRANT_CHITIN = create(
        "polished_aberrant_chitin",
        AlienChitinBlocks.POLISHED_ABERRANT_CHITIN
    );

    public static final BLibHolder<BlockItem> POLISHED_ABERRANT_CHITIN_SLAB = create(
        "polished_aberrant_chitin_slab",
        AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_SLAB
    );

    public static final BLibHolder<BlockItem> POLISHED_ABERRANT_CHITIN_STAIRS = create(
        "polished_aberrant_chitin_stairs",
        AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_STAIRS
    );

    public static final BLibHolder<BlockItem> POLISHED_ABERRANT_CHITIN_WALL = create(
        "polished_aberrant_chitin_wall",
        AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_WALL
    );

    public static final BLibHolder<BlockItem> POLISHED_CHITIN = create(
        "polished_chitin",
        AlienChitinBlocks.POLISHED_CHITIN
    );

    public static final BLibHolder<BlockItem> POLISHED_CHITIN_SLAB = create(
        "polished_chitin_slab",
        AlienChitinBlocks.POLISHED_CHITIN_SLAB
    );

    public static final BLibHolder<BlockItem> POLISHED_CHITIN_STAIRS = create(
        "polished_chitin_stairs",
        AlienChitinBlocks.POLISHED_CHITIN_STAIRS
    );

    public static final BLibHolder<BlockItem> POLISHED_CHITIN_WALL = create(
        "polished_chitin_wall",
        AlienChitinBlocks.POLISHED_CHITIN_WALL
    );

    public static final BLibHolder<BlockItem> POLISHED_NETHER_CHITIN = create(
        "polished_nether_chitin",
        AlienChitinBlocks.POLISHED_NETHER_CHITIN
    );

    public static final BLibHolder<BlockItem> POLISHED_NETHER_CHITIN_SLAB = create(
        "polished_nether_chitin_slab",
        AlienChitinBlocks.POLISHED_NETHER_CHITIN_SLAB
    );

    public static final BLibHolder<BlockItem> POLISHED_NETHER_CHITIN_STAIRS = create(
        "polished_nether_chitin_stairs",
        AlienChitinBlocks.POLISHED_NETHER_CHITIN_STAIRS
    );

    public static final BLibHolder<BlockItem> POLISHED_NETHER_CHITIN_WALL = create(
        "polished_nether_chitin_wall",
        AlienChitinBlocks.POLISHED_NETHER_CHITIN_WALL
    );

    private static BLibHolder<BlockItem> create(String id, Supplier<? extends Block> blockSupplier) {
        return create(id, blockSupplier, new Item.Properties());
    }

    private static BLibHolder<BlockItem> create(String id, Supplier<? extends Block> blockSupplier, Item.Properties properties) {
        return createWithSupplier(id, () -> new BlockItem(blockSupplier.get(), properties));
    }

    private static BLibHolder<BlockItem> createWithSupplier(String id, Supplier<BlockItem> blockItemSupplier) {
        return REGISTRY.createHolder(id, blockItemSupplier);
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

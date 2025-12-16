package com.alien.common.registry.init.item.block;

import com.alien.Alien;
import com.alien.common.registry.init.block.AlienChitinBlocks;
import com.blib.common.registry.BLibHolder;
import com.blib.common.registry.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class AlienChitinBlockItems {

    private static final BLibRegistry<Item> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.ITEM);

    public static final BLibHolder<BlockItem> CHISELED_CHITIN_BRICKS = create(
        "chiseled_chitin_bricks",
        AlienChitinBlocks.CHISELED_CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> CHISELED_CHITIN_BRICKS_EMBRYO = create(
        "chiseled_chitin_bricks_embryo",
        AlienChitinBlocks.CHISELED_CHITIN_BRICKS_EMBRYO
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

    private static BLibHolder<BlockItem> create(String path, Supplier<? extends Block> blockSupplier) {
        return create(path, blockSupplier, new Item.Properties());
    }

    private static BLibHolder<BlockItem> create(String path, Supplier<? extends Block> blockSupplier, Item.Properties properties) {
        return createWithSupplier(path, () -> new BlockItem(blockSupplier.get(), properties));
    }

    private static BLibHolder<BlockItem> createWithSupplier(String path, Supplier<BlockItem> blockItemSupplier) {
        return REGISTRY.createHolder(path, blockItemSupplier);
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

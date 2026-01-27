package com.alien.common.registry.init.item.block;

import com.alien.Alien;
import com.alien.common.registry.init.block.NetherAlienChitinBlocks;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class NetherAlienChitinBlockItems {

    private static final BLibRegistry<Item> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.ITEM);

    public static final BLibHolder<BlockItem> CHISELED_NETHER_CHITIN_BRICKS = create(
        "chiseled_nether_chitin_bricks",
        NetherAlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> CHISELED_NETHER_CHITIN_BRICKS_EMBRYO = create(
        "chiseled_nether_chitin_bricks_embryo",
        NetherAlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS_EMBRYO
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BLOCK = create(
        "nether_chitin_block",
        NetherAlienChitinBlocks.NETHER_CHITIN_BLOCK
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BLOCK_SLAB = create(
        "nether_chitin_block_slab",
        NetherAlienChitinBlocks.NETHER_CHITIN_BLOCK_SLAB
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BLOCK_STAIRS = create(
        "nether_chitin_block_stairs",
        NetherAlienChitinBlocks.NETHER_CHITIN_BLOCK_STAIRS
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BLOCK_WALL = create(
        "nether_chitin_block_wall",
        NetherAlienChitinBlocks.NETHER_CHITIN_BLOCK_WALL
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BRICKS = create(
        "nether_chitin_bricks",
        NetherAlienChitinBlocks.NETHER_CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BRICK_SLAB = create(
        "nether_chitin_brick_slab",
        NetherAlienChitinBlocks.NETHER_CHITIN_BRICK_SLAB
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BRICK_STAIRS = create(
        "nether_chitin_brick_stairs",
        NetherAlienChitinBlocks.NETHER_CHITIN_BRICK_STAIRS
    );

    public static final BLibHolder<BlockItem> NETHER_CHITIN_BRICK_WALL = create(
        "nether_chitin_brick_wall",
        NetherAlienChitinBlocks.NETHER_CHITIN_BRICK_WALL
    );

    public static final BLibHolder<BlockItem> POLISHED_NETHER_CHITIN = create(
        "polished_nether_chitin",
        NetherAlienChitinBlocks.POLISHED_NETHER_CHITIN
    );

    public static final BLibHolder<BlockItem> POLISHED_NETHER_CHITIN_SLAB = create(
        "polished_nether_chitin_slab",
        NetherAlienChitinBlocks.POLISHED_NETHER_CHITIN_SLAB
    );

    public static final BLibHolder<BlockItem> POLISHED_NETHER_CHITIN_STAIRS = create(
        "polished_nether_chitin_stairs",
        NetherAlienChitinBlocks.POLISHED_NETHER_CHITIN_STAIRS
    );

    public static final BLibHolder<BlockItem> POLISHED_NETHER_CHITIN_WALL = create(
        "polished_nether_chitin_wall",
        NetherAlienChitinBlocks.POLISHED_NETHER_CHITIN_WALL
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

package com.alien.common.registry.init.item.block;

import com.alien.Alien;
import com.alien.common.registry.init.block.AberrantAlienChitinBlocks;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class AberrantAlienChitinBlockItems {

    private static final BLibRegistry<Item> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.ITEM);

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BLOCK = create(
        "aberrant_chitin_block",
        AberrantAlienChitinBlocks.ABERRANT_CHITIN_BLOCK
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BLOCK_SLAB = create(
        "aberrant_chitin_block_slab",
        AberrantAlienChitinBlocks.ABERRANT_CHITIN_BLOCK_SLAB
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BLOCK_STAIRS = create(
        "aberrant_chitin_block_stairs",
        AberrantAlienChitinBlocks.ABERRANT_CHITIN_BLOCK_STAIRS
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BLOCK_WALL = create(
        "aberrant_chitin_block_wall",
        AberrantAlienChitinBlocks.ABERRANT_CHITIN_BLOCK_WALL
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BRICKS = create(
        "aberrant_chitin_bricks",
        AberrantAlienChitinBlocks.ABERRANT_CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BRICK_SLAB = create(
        "aberrant_chitin_brick_slab",
        AberrantAlienChitinBlocks.ABERRANT_CHITIN_BRICK_SLAB
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BRICK_STAIRS = create(
        "aberrant_chitin_brick_stairs",
        AberrantAlienChitinBlocks.ABERRANT_CHITIN_BRICK_STAIRS
    );

    public static final BLibHolder<BlockItem> ABERRANT_CHITIN_BRICK_WALL = create(
        "aberrant_chitin_brick_wall",
        AberrantAlienChitinBlocks.ABERRANT_CHITIN_BRICK_WALL
    );

    public static final BLibHolder<BlockItem> CHISELED_ABERRANT_CHITIN_BRICKS = create(
        "chiseled_aberrant_chitin_bricks",
        AberrantAlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS
    );

    public static final BLibHolder<BlockItem> CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO = create(
        "chiseled_aberrant_chitin_bricks_embryo",
        AberrantAlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO
    );

    public static final BLibHolder<BlockItem> POLISHED_ABERRANT_CHITIN = create(
        "polished_aberrant_chitin",
        AberrantAlienChitinBlocks.POLISHED_ABERRANT_CHITIN
    );

    public static final BLibHolder<BlockItem> POLISHED_ABERRANT_CHITIN_SLAB = create(
        "polished_aberrant_chitin_slab",
        AberrantAlienChitinBlocks.POLISHED_ABERRANT_CHITIN_SLAB
    );

    public static final BLibHolder<BlockItem> POLISHED_ABERRANT_CHITIN_STAIRS = create(
        "polished_aberrant_chitin_stairs",
        AberrantAlienChitinBlocks.POLISHED_ABERRANT_CHITIN_STAIRS
    );

    public static final BLibHolder<BlockItem> POLISHED_ABERRANT_CHITIN_WALL = create(
        "polished_aberrant_chitin_wall",
        AberrantAlienChitinBlocks.POLISHED_ABERRANT_CHITIN_WALL
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

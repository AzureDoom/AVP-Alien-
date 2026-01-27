package com.alien.common.registry.init.item.block;

import com.alien.Alien;
import com.alien.common.registry.init.block.AberrantAlienResinBlocks;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class AberrantAlienResinBlockItems {

    private static final BLibRegistry<Item> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.ITEM);

    public static final BLibHolder<BlockItem> ABERRANT_RESIN = create(
        "aberrant_resin",
        AberrantAlienResinBlocks.ABERRANT_RESIN
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_BRICKS = create(
        "aberrant_resin_bricks",
        AberrantAlienResinBlocks.ABERRANT_RESIN_BRICKS
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_BRICK_SLAB = create(
        "aberrant_resin_brick_slab",
        AberrantAlienResinBlocks.ABERRANT_RESIN_BRICK_SLAB
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_BRICK_STAIRS = create(
        "aberrant_resin_brick_stairs",
        AberrantAlienResinBlocks.ABERRANT_RESIN_BRICK_STAIRS
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_BRICK_WALL = create(
        "aberrant_resin_brick_wall",
        AberrantAlienResinBlocks.ABERRANT_RESIN_BRICK_WALL
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_NODE = create(
        "aberrant_resin_node",
        AberrantAlienResinBlocks.ABERRANT_RESIN_NODE
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_SLAB = create(
        "aberrant_resin_slab",
        AberrantAlienResinBlocks.ABERRANT_RESIN_SLAB
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_STAIRS = create(
        "aberrant_resin_stairs",
        AberrantAlienResinBlocks.ABERRANT_RESIN_STAIRS
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_VEIN = create(
        "aberrant_resin_vein",
        AberrantAlienResinBlocks.ABERRANT_RESIN_VEIN
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_WEB = create(
        "aberrant_resin_web",
        AberrantAlienResinBlocks.ABERRANT_RESIN_WEB
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_VENT = create(
        "aberrant_resin_vent",
        AberrantAlienResinBlocks.ABERRANT_RESIN_VENT
    );

    public static final BLibHolder<BlockItem> RIBBED_ABERRANT_RESIN = create(
        "ribbed_aberrant_resin",
        AberrantAlienResinBlocks.RIBBED_ABERRANT_RESIN
    );

    public static final BLibHolder<BlockItem> SMOOTH_ABERRANT_RESIN = create(
        "smooth_aberrant_resin",
        AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN
    );

    public static final BLibHolder<BlockItem> SMOOTH_ABERRANT_RESIN_SLAB = create(
        "smooth_aberrant_resin_slab",
        AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN_SLAB
    );

    public static final BLibHolder<BlockItem> SMOOTH_ABERRANT_RESIN_STAIRS = create(
        "smooth_aberrant_resin_stairs",
        AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN_STAIRS
    );

    public static final BLibHolder<BlockItem> SMOOTH_ABERRANT_RESIN_WALL = create(
        "smooth_aberrant_resin_wall",
        AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN_WALL
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

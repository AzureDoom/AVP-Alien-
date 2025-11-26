package com.alien.common.registry.init.item.block;

import com.alien.Alien;
import com.alien.common.registry.init.block.AlienResinBlocks;
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class AlienResinBlockItems {

    private static final BLibRegistry<BlockItem> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.ITEM);

    public static final BLibHolder<BlockItem> ABERRANT_RESIN = create(
        "aberrant_resin",
        AlienResinBlocks.ABERRANT_RESIN
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_BRICKS = create(
        "aberrant_resin_bricks",
        AlienResinBlocks.ABERRANT_RESIN_BRICKS
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_BRICK_SLAB = create(
        "aberrant_resin_brick_slab",
        AlienResinBlocks.ABERRANT_RESIN_BRICK_SLAB
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_BRICK_STAIRS = create(
        "aberrant_resin_brick_stairs",
        AlienResinBlocks.ABERRANT_RESIN_BRICK_STAIRS
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_BRICK_WALL = create(
        "aberrant_resin_brick_wall",
        AlienResinBlocks.ABERRANT_RESIN_BRICK_WALL
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_NODE = create(
        "aberrant_resin_node",
        AlienResinBlocks.ABERRANT_RESIN_NODE
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_SLAB = create(
        "aberrant_resin_slab",
        AlienResinBlocks.ABERRANT_RESIN_SLAB
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_STAIRS = create(
        "aberrant_resin_stairs",
        AlienResinBlocks.ABERRANT_RESIN_STAIRS
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_VEIN = create(
        "aberrant_resin_vein",
        AlienResinBlocks.ABERRANT_RESIN_VEIN
    );

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_WEB = create(
        "aberrant_resin_web",
        AlienResinBlocks.ABERRANT_RESIN_WEB
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN = create(
        "irradiated_resin",
        AlienResinBlocks.IRRADIATED_RESIN
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN_BRICKS = create(
        "irradiated_resin_bricks",
        AlienResinBlocks.IRRADIATED_RESIN_BRICKS
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN_BRICK_SLAB = create(
        "irradiated_resin_brick_slab",
        AlienResinBlocks.IRRADIATED_RESIN_BRICK_SLAB
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN_BRICK_STAIRS = create(
        "irradiated_resin_brick_stairs",
        AlienResinBlocks.IRRADIATED_RESIN_BRICK_STAIRS
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN_BRICK_WALL = create(
        "irradiated_resin_brick_wall",
        AlienResinBlocks.IRRADIATED_RESIN_BRICK_WALL
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN_NODE = create(
        "irradiated_resin_node",
        AlienResinBlocks.IRRADIATED_RESIN_NODE
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN_SLAB = create(
        "irradiated_resin_slab",
        AlienResinBlocks.IRRADIATED_RESIN_SLAB
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN_STAIRS = create(
        "irradiated_resin_stairs",
        AlienResinBlocks.IRRADIATED_RESIN_STAIRS
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN_VEIN = create(
        "irradiated_resin_vein",
        AlienResinBlocks.IRRADIATED_RESIN_VEIN
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN_WEB = create(
        "irradiated_resin_web",
        AlienResinBlocks.IRRADIATED_RESIN_WEB
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN = create(
        "nether_resin",
        AlienResinBlocks.NETHER_RESIN,
        new Item.Properties().fireResistant()
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN_BRICKS = create(
        "nether_resin_bricks",
        AlienResinBlocks.NETHER_RESIN_BRICKS
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN_BRICK_SLAB = create(
        "nether_resin_brick_slab",
        AlienResinBlocks.NETHER_RESIN_BRICK_SLAB
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN_BRICK_STAIRS = create(
        "nether_resin_brick_stairs",
        AlienResinBlocks.NETHER_RESIN_BRICK_STAIRS
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN_BRICK_WALL = create(
        "nether_resin_brick_wall",
        AlienResinBlocks.NETHER_RESIN_BRICK_WALL
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN_NODE = create(
        "nether_resin_node",
        AlienResinBlocks.NETHER_RESIN_NODE,
        new Item.Properties().fireResistant()
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN_SLAB = create(
        "nether_resin_slab",
        AlienResinBlocks.NETHER_RESIN_SLAB
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN_STAIRS = create(
        "nether_resin_stairs",
        AlienResinBlocks.NETHER_RESIN_STAIRS
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN_VEIN = create(
        "nether_resin_vein",
        AlienResinBlocks.NETHER_RESIN_VEIN,
        new Item.Properties().fireResistant()
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN_WEB = create(
        "nether_resin_web",
        AlienResinBlocks.NETHER_RESIN_WEB,
        new Item.Properties().fireResistant()
    );

    public static final BLibHolder<BlockItem> RESIN = create("resin", AlienResinBlocks.RESIN);

    public static final BLibHolder<BlockItem> RESIN_BRICKS = create("resin_bricks", AlienResinBlocks.RESIN_BRICKS);

    public static final BLibHolder<BlockItem> RESIN_BRICK_SLAB = create(
        "resin_brick_slab",
        AlienResinBlocks.RESIN_BRICK_SLAB
    );

    public static final BLibHolder<BlockItem> RESIN_BRICK_STAIRS = create(
        "resin_brick_stairs",
        AlienResinBlocks.RESIN_BRICK_STAIRS
    );

    public static final BLibHolder<BlockItem> RESIN_BRICK_WALL = create(
        "resin_brick_wall",
        AlienResinBlocks.RESIN_BRICK_WALL
    );

    public static final BLibHolder<BlockItem> RESIN_NODE = create("resin_node", AlienResinBlocks.RESIN_NODE);

    public static final BLibHolder<BlockItem> RESIN_SLAB = create(
        "resin_slab",
        AlienResinBlocks.RESIN_SLAB
    );

    public static final BLibHolder<BlockItem> RESIN_STAIRS = create(
        "resin_stairs",
        AlienResinBlocks.RESIN_STAIRS
    );

    public static final BLibHolder<BlockItem> RESIN_VEIN = create("resin_vein", AlienResinBlocks.RESIN_VEIN);

    public static final BLibHolder<BlockItem> ABERRANT_RESIN_VENT = create(
        "aberrant_resin_vent",
        AlienResinBlocks.ABERRANT_RESIN_VENT
    );

    public static final BLibHolder<BlockItem> IRRADIATED_RESIN_VENT = create(
        "irradiated_resin_vent",
        AlienResinBlocks.IRRADIATED_RESIN_VENT
    );

    public static final BLibHolder<BlockItem> NETHER_RESIN_VENT = create(
        "nether_resin_vent",
        AlienResinBlocks.NETHER_RESIN_VENT
    );

    public static final BLibHolder<BlockItem> RESIN_VENT = create("resin_vent", AlienResinBlocks.RESIN_VENT);

    public static final BLibHolder<BlockItem> RESIN_WEB = create("resin_web", AlienResinBlocks.RESIN_WEB);

    public static final BLibHolder<BlockItem> RIBBED_ABERRANT_RESIN = create(
        "ribbed_aberrant_resin",
        AlienResinBlocks.RIBBED_ABERRANT_RESIN
    );

    public static final BLibHolder<BlockItem> RIBBED_IRRADIATED_RESIN = create(
        "ribbed_irradiated_resin",
        AlienResinBlocks.RIBBED_IRRADIATED_RESIN
    );

    public static final BLibHolder<BlockItem> RIBBED_NETHER_RESIN = create(
        "ribbed_nether_resin",
        AlienResinBlocks.RIBBED_NETHER_RESIN
    );

    public static final BLibHolder<BlockItem> RIBBED_RESIN = create("ribbed_resin", AlienResinBlocks.RIBBED_RESIN);

    public static final BLibHolder<BlockItem> SMOOTH_ABERRANT_RESIN = create(
        "smooth_aberrant_resin",
        AlienResinBlocks.SMOOTH_ABERRANT_RESIN
    );

    public static final BLibHolder<BlockItem> SMOOTH_ABERRANT_RESIN_SLAB = create(
        "smooth_aberrant_resin_slab",
        AlienResinBlocks.SMOOTH_ABERRANT_RESIN_SLAB
    );

    public static final BLibHolder<BlockItem> SMOOTH_ABERRANT_RESIN_STAIRS = create(
        "smooth_aberrant_resin_stairs",
        AlienResinBlocks.SMOOTH_ABERRANT_RESIN_STAIRS
    );

    public static final BLibHolder<BlockItem> SMOOTH_ABERRANT_RESIN_WALL = create(
        "smooth_aberrant_resin_wall",
        AlienResinBlocks.SMOOTH_ABERRANT_RESIN_WALL
    );

    public static final BLibHolder<BlockItem> SMOOTH_IRRADIATED_RESIN = create(
        "smooth_irradiated_resin",
        AlienResinBlocks.SMOOTH_IRRADIATED_RESIN
    );

    public static final BLibHolder<BlockItem> SMOOTH_IRRADIATED_RESIN_SLAB = create(
        "smooth_irradiated_resin_slab",
        AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_SLAB
    );

    public static final BLibHolder<BlockItem> SMOOTH_IRRADIATED_RESIN_STAIRS = create(
        "smooth_irradiated_resin_stairs",
        AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_STAIRS
    );

    public static final BLibHolder<BlockItem> SMOOTH_IRRADIATED_RESIN_WALL = create(
        "smooth_irradiated_resin_wall",
        AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_WALL
    );

    public static final BLibHolder<BlockItem> SMOOTH_NETHER_RESIN = create(
        "smooth_nether_resin",
        AlienResinBlocks.SMOOTH_NETHER_RESIN
    );

    public static final BLibHolder<BlockItem> SMOOTH_NETHER_RESIN_SLAB = create(
        "smooth_nether_resin_slab",
        AlienResinBlocks.SMOOTH_NETHER_RESIN_SLAB
    );

    public static final BLibHolder<BlockItem> SMOOTH_NETHER_RESIN_STAIRS = create(
        "smooth_nether_resin_stairs",
        AlienResinBlocks.SMOOTH_NETHER_RESIN_STAIRS
    );

    public static final BLibHolder<BlockItem> SMOOTH_NETHER_RESIN_WALL = create(
        "smooth_nether_resin_wall",
        AlienResinBlocks.SMOOTH_NETHER_RESIN_WALL
    );

    public static final BLibHolder<BlockItem> SMOOTH_RESIN = create("smooth_resin", AlienResinBlocks.SMOOTH_RESIN);

    public static final BLibHolder<BlockItem> SMOOTH_RESIN_SLAB = create(
        "smooth_resin_slab",
        AlienResinBlocks.SMOOTH_RESIN_SLAB
    );

    public static final BLibHolder<BlockItem> SMOOTH_RESIN_STAIRS = create(
        "smooth_resin_stairs",
        AlienResinBlocks.SMOOTH_RESIN_STAIRS
    );

    public static final BLibHolder<BlockItem> SMOOTH_RESIN_WALL = create(
        "smooth_resin_wall",
        AlienResinBlocks.SMOOTH_RESIN_WALL
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

package com.alien.common.registry.init.block;

import com.alien.common.gameplay.block.resin.IrradiatedResinBlock;
import com.alien.common.gameplay.block.resin.ResinBlock;
import com.alien.common.gameplay.block.resin.node.IrradiatedResinNodeBlock;
import com.alien.common.gameplay.block.resin.node.ResinNodeBlock;
import com.alien.common.gameplay.block.resin.vein.IrradiatedResinVeinBlock;
import com.alien.common.gameplay.block.resin.vein.ResinVeinBlock;
import com.alien.common.gameplay.block.resin.vent.ResinVentBlock;
import com.alien.common.gameplay.block.resin.web.IrradiatedResinWebBlock;
import com.alien.common.gameplay.block.resin.web.ResinWebBlock;
import com.alien.common.registry.init.block.property.AlienBlockProperties;
import com.avp.common.registry.AVPDeferredHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

public class AlienResinBlocks {

    public static final AVPDeferredHolder<Block> ABERRANT_RESIN = AlienBlocks.register(
        "aberrant_resin",
        () -> new ResinBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> ABERRANT_RESIN_BRICKS = AlienBlocks.register(
        "aberrant_resin_bricks",
        AlienBlockProperties.ABERRANT_RESIN_VEIN
    );

    public static final AVPDeferredHolder<Block> ABERRANT_RESIN_BRICK_SLAB = AlienBlocks.register(
        "aberrant_resin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> ABERRANT_RESIN_BRICK_STAIRS = AlienBlocks.register(
        "aberrant_resin_brick_stairs",
        () -> new StairBlock(
            ABERRANT_RESIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> ABERRANT_RESIN_BRICK_WALL = AlienBlocks.register(
        "aberrant_resin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> ABERRANT_RESIN_STAIRS = AlienBlocks.register(
        "aberrant_resin_stairs",
        () -> new StairBlock(
            ABERRANT_RESIN.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> ABERRANT_RESIN_SLAB = AlienBlocks.register(
        "aberrant_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> ABERRANT_RESIN_NODE = AlienBlocks.register(
        "aberrant_resin_node",
        () -> new ResinNodeBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final AVPDeferredHolder<ResinVeinBlock> ABERRANT_RESIN_VEIN = AlienBlocks.register(
        "aberrant_resin_vein",
        () -> new ResinVeinBlock(AlienBlockProperties.ABERRANT_RESIN_VEIN.build())
    );

    public static final AVPDeferredHolder<ResinVentBlock> ABERRANT_RESIN_VENT = AlienBlocks.register(
        "aberrant_resin_vent",
        () -> new ResinVentBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> ABERRANT_RESIN_WEB = AlienBlocks.register(
        "aberrant_resin_web",
        () -> new ResinWebBlock(AlienBlockProperties.ABERRANT_RESIN_WEB.build())
    );

    public static final AVPDeferredHolder<Block> IRRADIATED_RESIN = AlienBlocks.register(
        "irradiated_resin",
        () -> new IrradiatedResinBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> IRRADIATED_RESIN_BRICKS = AlienBlocks.register(
        "irradiated_resin_bricks",
        AlienBlockProperties.IRRADIATED_RESIN
    );

    public static final AVPDeferredHolder<Block> IRRADIATED_RESIN_BRICK_SLAB = AlienBlocks.register(
        "irradiated_resin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> IRRADIATED_RESIN_BRICK_STAIRS = AlienBlocks.register(
        "irradiated_resin_brick_stairs",
        () -> new StairBlock(
            IRRADIATED_RESIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.IRRADIATED_RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> IRRADIATED_RESIN_BRICK_WALL = AlienBlocks.register(
        "irradiated_resin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> IRRADIATED_RESIN_STAIRS = AlienBlocks.register(
        "irradiated_resin_stairs",
        () -> new StairBlock(
            IRRADIATED_RESIN.get().defaultBlockState(),
            AlienBlockProperties.IRRADIATED_RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> IRRADIATED_RESIN_SLAB = AlienBlocks.register(
        "irradiated_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> IRRADIATED_RESIN_NODE = AlienBlocks.register(
        "irradiated_resin_node",
        () -> new IrradiatedResinNodeBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final AVPDeferredHolder<ResinVeinBlock> IRRADIATED_RESIN_VEIN = AlienBlocks.register(
        "irradiated_resin_vein",
        () -> new IrradiatedResinVeinBlock(AlienBlockProperties.IRRADIATED_RESIN_VEIN.build())
    );

    public static final AVPDeferredHolder<ResinVentBlock> IRRADIATED_RESIN_VENT = AlienBlocks.register(
        "irradiated_resin_vent",
        () -> new ResinVentBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> IRRADIATED_RESIN_WEB = AlienBlocks.register(
        "irradiated_resin_web",
        () -> new IrradiatedResinWebBlock(AlienBlockProperties.IRRADIATED_RESIN_WEB.build())
    );

    public static final AVPDeferredHolder<Block> NETHER_RESIN = AlienBlocks.register(
        "nether_resin",
        () -> new ResinBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> NETHER_RESIN_BRICKS = AlienBlocks.register(
        "nether_resin_bricks",
        AlienBlockProperties.NETHER_RESIN
    );

    public static final AVPDeferredHolder<Block> NETHER_RESIN_BRICK_SLAB = AlienBlocks.register(
        "nether_resin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> NETHER_RESIN_BRICK_STAIRS = AlienBlocks.register(
        "nether_resin_brick_stairs",
        () -> new StairBlock(
            NETHER_RESIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.NETHER_RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> NETHER_RESIN_BRICK_WALL = AlienBlocks.register(
        "nether_resin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> NETHER_RESIN_STAIRS = AlienBlocks.register(
        "nether_resin_stairs",
        () -> new StairBlock(
            NETHER_RESIN.get().defaultBlockState(),
            AlienBlockProperties.NETHER_RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> NETHER_RESIN_SLAB = AlienBlocks.register(
        "nether_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> NETHER_RESIN_NODE = AlienBlocks.register(
        "nether_resin_node",
        () -> new ResinNodeBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final AVPDeferredHolder<ResinVeinBlock> NETHER_RESIN_VEIN = AlienBlocks.register(
        "nether_resin_vein",
        () -> new ResinVeinBlock(AlienBlockProperties.NETHER_RESIN_VEIN.build())
    );

    public static final AVPDeferredHolder<ResinVentBlock> NETHER_RESIN_VENT = AlienBlocks.register(
        "nether_resin_vent",
        () -> new ResinVentBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> NETHER_RESIN_WEB = AlienBlocks.register(
        "nether_resin_web",
        () -> new ResinWebBlock(AlienBlockProperties.NETHER_RESIN_WEB.build())
    );

    public static final AVPDeferredHolder<Block> RESIN = AlienBlocks.register(
        "resin",
        () -> new ResinBlock(AlienBlockProperties.RESIN.build())
    );

    public static final AVPDeferredHolder<Block> RESIN_BRICKS = AlienBlocks.register("resin_bricks", AlienBlockProperties.RESIN);

    public static final AVPDeferredHolder<Block> RESIN_BRICK_SLAB = AlienBlocks.register(
        "resin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.RESIN.build())
    );

    public static final AVPDeferredHolder<Block> RESIN_BRICK_STAIRS = AlienBlocks.register(
        "resin_brick_stairs",
        () -> new StairBlock(
            RESIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> RESIN_BRICK_WALL = AlienBlocks.register(
        "resin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.RESIN.build())
    );

    public static final AVPDeferredHolder<Block> RESIN_NODE = AlienBlocks.register(
        "resin_node",
        () -> new ResinNodeBlock(AlienBlockProperties.RESIN.build())
    );

    public static final AVPDeferredHolder<Block> RESIN_SLAB = AlienBlocks.register(
        "resin_slab",
        () -> new SlabBlock(AlienBlockProperties.RESIN.build())
    );

    public static final AVPDeferredHolder<Block> RESIN_STAIRS = AlienBlocks.register(
        "resin_stairs",
        () -> new StairBlock(
            RESIN.get().defaultBlockState(),
            AlienBlockProperties.RESIN.build()
        )
    );

    public static final AVPDeferredHolder<ResinVeinBlock> RESIN_VEIN = AlienBlocks.register(
        "resin_vein",
        () -> new ResinVeinBlock(AlienBlockProperties.RESIN_VEIN.build())
    );

    public static final AVPDeferredHolder<ResinVentBlock> RESIN_VENT = AlienBlocks.register(
        "resin_vent",
        () -> new ResinVentBlock(AlienBlockProperties.RESIN.build())
    );

    public static final AVPDeferredHolder<Block> RESIN_WEB = AlienBlocks.register(
        "resin_web",
        () -> new ResinWebBlock(AlienBlockProperties.RESIN_WEB.build())
    );

    public static final AVPDeferredHolder<Block> RIBBED_ABERRANT_RESIN = AlienBlocks.register(
        "ribbed_aberrant_resin",
        () -> new RotatedPillarBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> RIBBED_IRRADIATED_RESIN = AlienBlocks.register(
        "ribbed_irradiated_resin",
        () -> new RotatedPillarBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> RIBBED_NETHER_RESIN = AlienBlocks.register(
        "ribbed_nether_resin",
        () -> new RotatedPillarBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> RIBBED_RESIN = AlienBlocks.register(
        "ribbed_resin",
        () -> new RotatedPillarBlock(AlienBlockProperties.RESIN.build())
    );

    public static final AVPDeferredHolder<Block> SMOOTH_ABERRANT_RESIN = AlienBlocks.register(
        "smooth_aberrant_resin",
        AlienBlockProperties.ABERRANT_RESIN
    );

    public static final AVPDeferredHolder<Block> SMOOTH_ABERRANT_RESIN_SLAB = AlienBlocks.register(
        "smooth_aberrant_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> SMOOTH_ABERRANT_RESIN_STAIRS = AlienBlocks.register(
        "smooth_aberrant_resin_stairs",
        () -> new StairBlock(
            SMOOTH_ABERRANT_RESIN.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> SMOOTH_ABERRANT_RESIN_WALL = AlienBlocks.register(
        "smooth_aberrant_resin_wall",
        () -> new WallBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> SMOOTH_IRRADIATED_RESIN = AlienBlocks.register(
        "smooth_irradiated_resin",
        AlienBlockProperties.IRRADIATED_RESIN
    );

    public static final AVPDeferredHolder<Block> SMOOTH_IRRADIATED_RESIN_SLAB = AlienBlocks.register(
        "smooth_irradiated_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> SMOOTH_IRRADIATED_RESIN_STAIRS = AlienBlocks.register(
        "smooth_irradiated_resin_stairs",
        () -> new StairBlock(
            SMOOTH_IRRADIATED_RESIN.get().defaultBlockState(),
            AlienBlockProperties.IRRADIATED_RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> SMOOTH_IRRADIATED_RESIN_WALL = AlienBlocks.register(
        "smooth_irradiated_resin_wall",
        () -> new WallBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> SMOOTH_NETHER_RESIN = AlienBlocks.register(
        "smooth_nether_resin",
        AlienBlockProperties.NETHER_RESIN
    );

    public static final AVPDeferredHolder<Block> SMOOTH_NETHER_RESIN_SLAB = AlienBlocks.register(
        "smooth_nether_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> SMOOTH_NETHER_RESIN_STAIRS = AlienBlocks.register(
        "smooth_nether_resin_stairs",
        () -> new StairBlock(
            SMOOTH_NETHER_RESIN.get().defaultBlockState(),
            AlienBlockProperties.NETHER_RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> SMOOTH_NETHER_RESIN_WALL = AlienBlocks.register(
        "smooth_nether_resin_wall",
        () -> new WallBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final AVPDeferredHolder<Block> SMOOTH_RESIN = AlienBlocks.register("smooth_resin", AlienBlockProperties.RESIN);

    public static final AVPDeferredHolder<Block> SMOOTH_RESIN_SLAB = AlienBlocks.register(
        "smooth_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.RESIN.build())
    );

    public static final AVPDeferredHolder<Block> SMOOTH_RESIN_STAIRS = AlienBlocks.register(
        "smooth_resin_stairs",
        () -> new StairBlock(
            SMOOTH_RESIN.get().defaultBlockState(),
            AlienBlockProperties.RESIN.build()
        )
    );

    public static final AVPDeferredHolder<Block> SMOOTH_RESIN_WALL = AlienBlocks.register(
        "smooth_resin_wall",
        () -> new WallBlock(AlienBlockProperties.RESIN.build())
    );

    public static void initialize() {}
}

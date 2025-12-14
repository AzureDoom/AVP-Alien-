package com.alien.common.registry.init.block;

import com.alien.Alien;
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
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
import com.blib.common.gameplay.block.property.BlockPropertyBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

import java.util.function.Supplier;

public class AlienResinBlocks {

    private static final BLibRegistry<Block> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.BLOCK);

    public static final BLibHolder<Block> ABERRANT_RESIN = create(
        "aberrant_resin",
        () -> new ResinBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final BLibHolder<Block> ABERRANT_RESIN_BRICKS = create(
        "aberrant_resin_bricks",
        AlienBlockProperties.ABERRANT_RESIN_VEIN
    );

    public static final BLibHolder<Block> ABERRANT_RESIN_BRICK_SLAB = create(
        "aberrant_resin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final BLibHolder<Block> ABERRANT_RESIN_BRICK_STAIRS = create(
        "aberrant_resin_brick_stairs",
        () -> new StairBlock(
            ABERRANT_RESIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_RESIN.build()
        )
    );

    public static final BLibHolder<Block> ABERRANT_RESIN_BRICK_WALL = create(
        "aberrant_resin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final BLibHolder<Block> ABERRANT_RESIN_STAIRS = create(
        "aberrant_resin_stairs",
        () -> new StairBlock(
            ABERRANT_RESIN.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_RESIN.build()
        )
    );

    public static final BLibHolder<Block> ABERRANT_RESIN_SLAB = create(
        "aberrant_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final BLibHolder<Block> ABERRANT_RESIN_NODE = create(
        "aberrant_resin_node",
        () -> new ResinNodeBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final BLibHolder<ResinVeinBlock> ABERRANT_RESIN_VEIN = create(
        "aberrant_resin_vein",
        () -> new ResinVeinBlock(AlienBlockProperties.ABERRANT_RESIN_VEIN.build())
    );

    public static final BLibHolder<ResinVentBlock> ABERRANT_RESIN_VENT = create(
        "aberrant_resin_vent",
        () -> new ResinVentBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final BLibHolder<Block> ABERRANT_RESIN_WEB = create(
        "aberrant_resin_web",
        () -> new ResinWebBlock(AlienBlockProperties.ABERRANT_RESIN_WEB.build())
    );

    public static final BLibHolder<Block> IRRADIATED_RESIN = create(
        "irradiated_resin",
        () -> new IrradiatedResinBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final BLibHolder<Block> IRRADIATED_RESIN_BRICKS = create(
        "irradiated_resin_bricks",
        AlienBlockProperties.IRRADIATED_RESIN
    );

    public static final BLibHolder<Block> IRRADIATED_RESIN_BRICK_SLAB = create(
        "irradiated_resin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final BLibHolder<Block> IRRADIATED_RESIN_BRICK_STAIRS = create(
        "irradiated_resin_brick_stairs",
        () -> new StairBlock(
            IRRADIATED_RESIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.IRRADIATED_RESIN.build()
        )
    );

    public static final BLibHolder<Block> IRRADIATED_RESIN_BRICK_WALL = create(
        "irradiated_resin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final BLibHolder<Block> IRRADIATED_RESIN_STAIRS = create(
        "irradiated_resin_stairs",
        () -> new StairBlock(
            IRRADIATED_RESIN.get().defaultBlockState(),
            AlienBlockProperties.IRRADIATED_RESIN.build()
        )
    );

    public static final BLibHolder<Block> IRRADIATED_RESIN_SLAB = create(
        "irradiated_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final BLibHolder<Block> IRRADIATED_RESIN_NODE = create(
        "irradiated_resin_node",
        () -> new IrradiatedResinNodeBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final BLibHolder<ResinVeinBlock> IRRADIATED_RESIN_VEIN = create(
        "irradiated_resin_vein",
        () -> new IrradiatedResinVeinBlock(AlienBlockProperties.IRRADIATED_RESIN_VEIN.build())
    );

    public static final BLibHolder<ResinVentBlock> IRRADIATED_RESIN_VENT = create(
        "irradiated_resin_vent",
        () -> new ResinVentBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final BLibHolder<Block> IRRADIATED_RESIN_WEB = create(
        "irradiated_resin_web",
        () -> new IrradiatedResinWebBlock(AlienBlockProperties.IRRADIATED_RESIN_WEB.build())
    );

    public static final BLibHolder<Block> NETHER_RESIN = create(
        "nether_resin",
        () -> new ResinBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final BLibHolder<Block> NETHER_RESIN_BRICKS = create(
        "nether_resin_bricks",
        AlienBlockProperties.NETHER_RESIN
    );

    public static final BLibHolder<Block> NETHER_RESIN_BRICK_SLAB = create(
        "nether_resin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final BLibHolder<Block> NETHER_RESIN_BRICK_STAIRS = create(
        "nether_resin_brick_stairs",
        () -> new StairBlock(
            NETHER_RESIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.NETHER_RESIN.build()
        )
    );

    public static final BLibHolder<Block> NETHER_RESIN_BRICK_WALL = create(
        "nether_resin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final BLibHolder<Block> NETHER_RESIN_STAIRS = create(
        "nether_resin_stairs",
        () -> new StairBlock(
            NETHER_RESIN.get().defaultBlockState(),
            AlienBlockProperties.NETHER_RESIN.build()
        )
    );

    public static final BLibHolder<Block> NETHER_RESIN_SLAB = create(
        "nether_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final BLibHolder<Block> NETHER_RESIN_NODE = create(
        "nether_resin_node",
        () -> new ResinNodeBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final BLibHolder<ResinVeinBlock> NETHER_RESIN_VEIN = create(
        "nether_resin_vein",
        () -> new ResinVeinBlock(AlienBlockProperties.NETHER_RESIN_VEIN.build())
    );

    public static final BLibHolder<ResinVentBlock> NETHER_RESIN_VENT = create(
        "nether_resin_vent",
        () -> new ResinVentBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final BLibHolder<Block> NETHER_RESIN_WEB = create(
        "nether_resin_web",
        () -> new ResinWebBlock(AlienBlockProperties.NETHER_RESIN_WEB.build())
    );

    public static final BLibHolder<Block> RESIN = create(
        "resin",
        () -> new ResinBlock(AlienBlockProperties.RESIN.build())
    );

    public static final BLibHolder<Block> RESIN_BRICKS = create("resin_bricks", AlienBlockProperties.RESIN);

    public static final BLibHolder<Block> RESIN_BRICK_SLAB = create(
        "resin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.RESIN.build())
    );

    public static final BLibHolder<Block> RESIN_BRICK_STAIRS = create(
        "resin_brick_stairs",
        () -> new StairBlock(
            RESIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.RESIN.build()
        )
    );

    public static final BLibHolder<Block> RESIN_BRICK_WALL = create(
        "resin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.RESIN.build())
    );

    public static final BLibHolder<Block> RESIN_NODE = create(
        "resin_node",
        () -> new ResinNodeBlock(AlienBlockProperties.RESIN.build())
    );

    public static final BLibHolder<Block> RESIN_SLAB = create(
        "resin_slab",
        () -> new SlabBlock(AlienBlockProperties.RESIN.build())
    );

    public static final BLibHolder<Block> RESIN_STAIRS = create(
        "resin_stairs",
        () -> new StairBlock(
            RESIN.get().defaultBlockState(),
            AlienBlockProperties.RESIN.build()
        )
    );

    public static final BLibHolder<ResinVeinBlock> RESIN_VEIN = create(
        "resin_vein",
        () -> new ResinVeinBlock(AlienBlockProperties.RESIN_VEIN.build())
    );

    public static final BLibHolder<ResinVentBlock> RESIN_VENT = create(
        "resin_vent",
        () -> new ResinVentBlock(AlienBlockProperties.RESIN.build())
    );

    public static final BLibHolder<Block> RESIN_WEB = create(
        "resin_web",
        () -> new ResinWebBlock(AlienBlockProperties.RESIN_WEB.build())
    );

    public static final BLibHolder<Block> RIBBED_ABERRANT_RESIN = create(
        "ribbed_aberrant_resin",
        () -> new RotatedPillarBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final BLibHolder<Block> RIBBED_IRRADIATED_RESIN = create(
        "ribbed_irradiated_resin",
        () -> new RotatedPillarBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final BLibHolder<Block> RIBBED_NETHER_RESIN = create(
        "ribbed_nether_resin",
        () -> new RotatedPillarBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final BLibHolder<Block> RIBBED_RESIN = create(
        "ribbed_resin",
        () -> new RotatedPillarBlock(AlienBlockProperties.RESIN.build())
    );

    public static final BLibHolder<Block> SMOOTH_ABERRANT_RESIN = create(
        "smooth_aberrant_resin",
        AlienBlockProperties.ABERRANT_RESIN
    );

    public static final BLibHolder<Block> SMOOTH_ABERRANT_RESIN_SLAB = create(
        "smooth_aberrant_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final BLibHolder<Block> SMOOTH_ABERRANT_RESIN_STAIRS = create(
        "smooth_aberrant_resin_stairs",
        () -> new StairBlock(
            SMOOTH_ABERRANT_RESIN.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_RESIN.build()
        )
    );

    public static final BLibHolder<Block> SMOOTH_ABERRANT_RESIN_WALL = create(
        "smooth_aberrant_resin_wall",
        () -> new WallBlock(AlienBlockProperties.ABERRANT_RESIN.build())
    );

    public static final BLibHolder<Block> SMOOTH_IRRADIATED_RESIN = create(
        "smooth_irradiated_resin",
        AlienBlockProperties.IRRADIATED_RESIN
    );

    public static final BLibHolder<Block> SMOOTH_IRRADIATED_RESIN_SLAB = create(
        "smooth_irradiated_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final BLibHolder<Block> SMOOTH_IRRADIATED_RESIN_STAIRS = create(
        "smooth_irradiated_resin_stairs",
        () -> new StairBlock(
            SMOOTH_IRRADIATED_RESIN.get().defaultBlockState(),
            AlienBlockProperties.IRRADIATED_RESIN.build()
        )
    );

    public static final BLibHolder<Block> SMOOTH_IRRADIATED_RESIN_WALL = create(
        "smooth_irradiated_resin_wall",
        () -> new WallBlock(AlienBlockProperties.IRRADIATED_RESIN.build())
    );

    public static final BLibHolder<Block> SMOOTH_NETHER_RESIN = create(
        "smooth_nether_resin",
        AlienBlockProperties.NETHER_RESIN
    );

    public static final BLibHolder<Block> SMOOTH_NETHER_RESIN_SLAB = create(
        "smooth_nether_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final BLibHolder<Block> SMOOTH_NETHER_RESIN_STAIRS = create(
        "smooth_nether_resin_stairs",
        () -> new StairBlock(
            SMOOTH_NETHER_RESIN.get().defaultBlockState(),
            AlienBlockProperties.NETHER_RESIN.build()
        )
    );

    public static final BLibHolder<Block> SMOOTH_NETHER_RESIN_WALL = create(
        "smooth_nether_resin_wall",
        () -> new WallBlock(AlienBlockProperties.NETHER_RESIN.build())
    );

    public static final BLibHolder<Block> SMOOTH_RESIN = create("smooth_resin", AlienBlockProperties.RESIN);

    public static final BLibHolder<Block> SMOOTH_RESIN_SLAB = create(
        "smooth_resin_slab",
        () -> new SlabBlock(AlienBlockProperties.RESIN.build())
    );

    public static final BLibHolder<Block> SMOOTH_RESIN_STAIRS = create(
        "smooth_resin_stairs",
        () -> new StairBlock(
            SMOOTH_RESIN.get().defaultBlockState(),
            AlienBlockProperties.RESIN.build()
        )
    );

    public static final BLibHolder<Block> SMOOTH_RESIN_WALL = create(
        "smooth_resin_wall",
        () -> new WallBlock(AlienBlockProperties.RESIN.build())
    );

    private static BLibHolder<Block> create(String path, BlockPropertyBuilder blockPropertyBuilder) {
        return create(path, () -> new Block(blockPropertyBuilder.build()));
    }

    private static <T extends Block> BLibHolder<T> create(String path, Supplier<T> blockSupplier) {
        return REGISTRY.createHolder(path, blockSupplier);
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

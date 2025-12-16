package com.alien.common.registry.init.block;

import com.alien.Alien;
import com.alien.common.gameplay.block.resin.ResinBlock;
import com.alien.common.gameplay.block.resin.node.ResinNodeBlock;
import com.alien.common.gameplay.block.resin.vein.ResinVeinBlock;
import com.alien.common.gameplay.block.resin.vent.ResinVentBlock;
import com.alien.common.gameplay.block.resin.web.ResinWebBlock;
import com.alien.common.registry.init.block.property.AlienBlockProperties;
import com.blib.common.gameplay.block.property.BlockPropertyBuilder;
import com.blib.common.registry.BLibHolder;
import com.blib.common.registry.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

import java.util.function.Supplier;

public class AberrantAlienResinBlocks {

    private static final BLibRegistry<Block> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.BLOCK);

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

    public static final BLibHolder<Block> RIBBED_ABERRANT_RESIN = create(
        "ribbed_aberrant_resin",
        () -> new RotatedPillarBlock(AlienBlockProperties.ABERRANT_RESIN.build())
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

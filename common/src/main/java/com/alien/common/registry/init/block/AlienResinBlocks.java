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

public class AlienResinBlocks {

    private static final BLibRegistry<Block> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.BLOCK);

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

    public static final BLibHolder<Block> RIBBED_RESIN = create(
        "ribbed_resin",
        () -> new RotatedPillarBlock(AlienBlockProperties.RESIN.build())
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

package com.alien.common.registry.init.block;

import com.alien.Alien;
import com.alien.common.registry.init.block.property.AlienBlockProperties;
import com.blib.api.common.block.v1.BlockPropertyBuilder;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

import java.util.function.Supplier;

public class AlienChitinBlocks {

    private static final BLibRegistry<Block> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.BLOCK);

    public static final BLibHolder<Block> CHISELED_CHITIN_BRICKS = create(
        "chiseled_chitin_bricks",
        AlienBlockProperties.CHITIN
    );

    public static final BLibHolder<Block> CHISELED_CHITIN_BRICKS_EMBRYO = create(
        "chiseled_chitin_bricks_embryo",
        AlienBlockProperties.CHITIN
    );

    public static final BLibHolder<Block> CHITIN_BLOCK = create(
        "chitin_block",
        AlienBlockProperties.CHITIN
    );

    public static final BLibHolder<Block> CHITIN_BLOCK_SLAB = create(
        "chitin_block_slab",
        () -> new SlabBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final BLibHolder<Block> CHITIN_BLOCK_STAIRS = create(
        "chitin_block_stairs",
        () -> new StairBlock(
            CHITIN_BLOCK.get().defaultBlockState(),
            AlienBlockProperties.CHITIN.build()
        )
    );

    public static final BLibHolder<Block> CHITIN_BLOCK_WALL = create(
        "chitin_block_wall",
        () -> new WallBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final BLibHolder<Block> CHITIN_BRICKS = create(
        "chitin_bricks",
        AlienBlockProperties.CHITIN
    );

    public static final BLibHolder<Block> CHITIN_BRICK_SLAB = create(
        "chitin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final BLibHolder<Block> CHITIN_BRICK_STAIRS = create(
        "chitin_brick_stairs",
        () -> new StairBlock(
            CHITIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.CHITIN.build()
        )
    );

    public static final BLibHolder<Block> CHITIN_BRICK_WALL = create(
        "chitin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final BLibHolder<Block> POLISHED_CHITIN = create(
        "polished_chitin",
        AlienBlockProperties.CHITIN
    );

    public static final BLibHolder<Block> POLISHED_CHITIN_SLAB = create(
        "polished_chitin_slab",
        () -> new SlabBlock(AlienBlockProperties.CHITIN.build())
    );

    public static final BLibHolder<Block> POLISHED_CHITIN_STAIRS = create(
        "polished_chitin_stairs",
        () -> new StairBlock(
            POLISHED_CHITIN.get().defaultBlockState(),
            AlienBlockProperties.CHITIN.build()
        )
    );

    public static final BLibHolder<Block> POLISHED_CHITIN_WALL = create(
        "polished_chitin_wall",
        () -> new WallBlock(AlienBlockProperties.CHITIN.build())
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

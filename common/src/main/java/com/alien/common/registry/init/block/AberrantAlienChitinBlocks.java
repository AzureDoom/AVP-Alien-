package com.alien.common.registry.init.block;

import com.alien.Alien;
import com.alien.common.registry.init.block.property.AlienBlockProperties;
import com.blib.common.gameplay.block.property.BlockPropertyBuilder;
import com.blib.common.registry.BLibHolder;
import com.blib.common.registry.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

import java.util.function.Supplier;

public class AberrantAlienChitinBlocks {

    private static final BLibRegistry<Block> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.BLOCK);

    public static final BLibHolder<Block> ABERRANT_CHITIN_BLOCK = create(
        "aberrant_chitin_block",
        AlienBlockProperties.ABERRANT_CHITIN
    );

    public static final BLibHolder<Block> ABERRANT_CHITIN_BLOCK_SLAB = create(
        "aberrant_chitin_block_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final BLibHolder<Block> ABERRANT_CHITIN_BLOCK_STAIRS = create(
        "aberrant_chitin_block_stairs",
        () -> new StairBlock(
            ABERRANT_CHITIN_BLOCK.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_CHITIN.build()
        )
    );

    public static final BLibHolder<Block> ABERRANT_CHITIN_BLOCK_WALL = create(
        "aberrant_chitin_block_wall",
        () -> new WallBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final BLibHolder<Block> ABERRANT_CHITIN_BRICKS = create(
        "aberrant_chitin_bricks",
        AlienBlockProperties.ABERRANT_CHITIN
    );

    public static final BLibHolder<Block> ABERRANT_CHITIN_BRICK_SLAB = create(
        "aberrant_chitin_brick_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final BLibHolder<Block> ABERRANT_CHITIN_BRICK_STAIRS = create(
        "aberrant_chitin_brick_stairs",
        () -> new StairBlock(
            ABERRANT_CHITIN_BRICKS.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_CHITIN.build()
        )
    );

    public static final BLibHolder<Block> ABERRANT_CHITIN_BRICK_WALL = create(
        "aberrant_chitin_brick_wall",
        () -> new WallBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final BLibHolder<Block> CHISELED_ABERRANT_CHITIN_BRICKS = create(
        "chiseled_aberrant_chitin_bricks",
        AlienBlockProperties.ABERRANT_CHITIN
    );

    public static final BLibHolder<Block> CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO = create(
        "chiseled_aberrant_chitin_bricks_embryo",
        AlienBlockProperties.ABERRANT_CHITIN
    );

    public static final BLibHolder<Block> POLISHED_ABERRANT_CHITIN = create(
        "polished_aberrant_chitin",
        AlienBlockProperties.ABERRANT_CHITIN
    );

    public static final BLibHolder<Block> POLISHED_ABERRANT_CHITIN_SLAB = create(
        "polished_aberrant_chitin_slab",
        () -> new SlabBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
    );

    public static final BLibHolder<Block> POLISHED_ABERRANT_CHITIN_STAIRS = create(
        "polished_aberrant_chitin_stairs",
        () -> new StairBlock(
            POLISHED_ABERRANT_CHITIN.get().defaultBlockState(),
            AlienBlockProperties.ABERRANT_CHITIN.build()
        )
    );

    public static final BLibHolder<Block> POLISHED_ABERRANT_CHITIN_WALL = create(
        "polished_aberrant_chitin_wall",
        () -> new WallBlock(AlienBlockProperties.ABERRANT_CHITIN.build())
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

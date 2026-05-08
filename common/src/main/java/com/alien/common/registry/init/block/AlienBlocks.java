package com.alien.common.registry.init.block;

import com.alien.Alien;
import com.alien.common.gameplay.block.jelly.JellyBlock;
import com.alien.common.gameplay.block.queen.QueenHeadBlock;
import com.alien.common.gameplay.block.queen.QueenHeadVariant;
import com.alien.common.gameplay.block.queen.QueenWallHeadBlock;
import com.alien.common.registry.init.block.property.AlienBlockProperties;
import com.blib.api.common.block.v1.BlockPropertyBuilder;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class AlienBlocks {

    public static final BLibRegistry<Block> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.BLOCK);

    public static final BLibHolder<Block> ROYAL_JELLY_BLOCK = create(
        "royal_jelly_block",
        () -> new JellyBlock(AlienBlockProperties.JELLY.build().speedFactor(0.4F).jumpFactor(0.5F))
    );

    public static final BLibHolder<Block> SCOURGE_JELLY_BLOCK = create(
        "scourge_jelly_block",
        () -> new JellyBlock(AlienBlockProperties.JELLY.build().speedFactor(0.4F).jumpFactor(0.5F))
    );

    public static final BLibHolder<QueenHeadBlock> QUEEN_HEAD = create(
        "queen_head",
        () -> new QueenHeadBlock(QueenHeadVariant.QUEEN, queenHeadProperties())
    );

    public static final BLibHolder<QueenWallHeadBlock> QUEEN_WALL_HEAD = create(
        "queen_wall_head",
        () -> new QueenWallHeadBlock(QueenHeadVariant.QUEEN, queenHeadProperties())
    );

    public static final BLibHolder<QueenHeadBlock> ABERRANT_QUEEN_HEAD = create(
        "aberrant_queen_head",
        () -> new QueenHeadBlock(QueenHeadVariant.ABERRANT, queenHeadProperties())
    );

    public static final BLibHolder<QueenWallHeadBlock> ABERRANT_QUEEN_WALL_HEAD = create(
        "aberrant_queen_wall_head",
        () -> new QueenWallHeadBlock(QueenHeadVariant.ABERRANT, queenHeadProperties())
    );

    public static final BLibHolder<QueenHeadBlock> IRRADIATED_QUEEN_HEAD = create(
        "irradiated_queen_head",
        () -> new QueenHeadBlock(QueenHeadVariant.IRRADIATED, queenHeadProperties())
    );

    public static final BLibHolder<QueenWallHeadBlock> IRRADIATED_QUEEN_WALL_HEAD = create(
        "irradiated_queen_wall_head",
        () -> new QueenWallHeadBlock(QueenHeadVariant.IRRADIATED, queenHeadProperties())
    );

    public static final BLibHolder<QueenHeadBlock> NETHER_QUEEN_HEAD = create(
        "nether_queen_head",
        () -> new QueenHeadBlock(QueenHeadVariant.NETHER, queenHeadProperties())
    );

    public static final BLibHolder<QueenWallHeadBlock> NETHER_QUEEN_WALL_HEAD = create(
        "nether_queen_wall_head",
        () -> new QueenWallHeadBlock(QueenHeadVariant.NETHER, queenHeadProperties())
    );

    /**
     * Block properties shared by all queen-head variants — strength values mirror vanilla skull blocks
     * (1.0F to break by hand in ~1.5s, no resistance, instrument NONE), and {@code noOcclusion} since the
     * BE renderer paints a non-cube shape that doesn't fill the full 1x1x1 voxel.
     */
    private static BlockBehaviour.Properties queenHeadProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_GRAY)
            .strength(1.0F)
            .sound(SoundType.BONE_BLOCK)
            .noOcclusion();
    }

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

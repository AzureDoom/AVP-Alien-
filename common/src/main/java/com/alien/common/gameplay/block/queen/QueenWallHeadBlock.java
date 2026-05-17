package com.alien.common.gameplay.block.queen;

import com.alien.common.gameplay.block.entity.queen.QueenHeadBlockEntity;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Wall-mounted queen head — hangs out from a vertical surface, four cardinal facings (no up/down). Uses
 * {@link HorizontalDirectionalBlock#FACING} to track which way the head sticks out, and a per-direction
 * {@link VoxelShape} so the head's collision box only occupies the wall side.
 * <p>
 * Like {@link QueenHeadBlock}, all visible geometry comes from the block-entity renderer; this block has no cube model.
 */
public class QueenWallHeadBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    /**
     * Per-facing collision shape, occupying the upper-half of the block on the side opposite the wall it mounts to.
     * Mirrors vanilla wall-skull dimensions ({@code 8x8x8} starting halfway up the block) so the head visually sticks
     * out from the wall by ~half a block.
     */
    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(
        Map.of(
            Direction.NORTH,
            Block.box(4.0, 4.0, 8.0, 12.0, 12.0, 16.0),
            Direction.SOUTH,
            Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 8.0),
            Direction.EAST,
            Block.box(0.0, 4.0, 4.0, 8.0, 12.0, 12.0),
            Direction.WEST,
            Block.box(8.0, 4.0, 4.0, 16.0, 12.0, 12.0)
        )
    );

    private final QueenHeadVariant variant;

    public QueenWallHeadBlock(QueenHeadVariant variant, BlockBehaviour.Properties properties) {
        super(properties);
        this.variant = variant;
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    public QueenHeadVariant variant() {
        return variant;
    }

    @Override
    public @NotNull String getDescriptionId() {
        // Use the item's description (which routes to the floor block's name) so we don't need a separate
        // lang entry for the wall variant — the player only ever holds the floor item, never a "wall head"
        // item. Mirrors vanilla's WallSkullBlock.
        return this.asItem().getDescriptionId();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(properties -> new QueenWallHeadBlock(variant, properties));
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABBS.get(state.getValue(FACING));
    }

    @Override
    protected @NotNull VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Honor the side of the block being clicked, but reject up/down (queen heads can't sit on
        // ceilings or be placed flat-on-top of a block — those scenarios route to the floor block via
        // StandingAndWallBlockItem's selection logic).
        var clickedFace = context.getClickedFace();

        if (clickedFace.getAxis().isVertical()) {
            return null;
        }

        return this.defaultBlockState().setValue(FACING, clickedFace);
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new QueenHeadBlockEntity(pos, state);
    }
}

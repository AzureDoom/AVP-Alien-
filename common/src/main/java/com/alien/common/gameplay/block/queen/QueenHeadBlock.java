package com.alien.common.gameplay.block.queen;

import com.alien.common.gameplay.block.entity.queen.QueenHeadBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * Floor-placed queen head. Holds a {@link BlockStateProperties#ROTATION_16} state so the head can spin to match the
 * player's facing on placement (16 yaw segments — same precision as vanilla skulls). Rendered via a block-entity
 * renderer that reuses the existing queen-head item geo, so this block defines no visible cube model itself; the
 * blockstate JSON points at the empty {@code builtin/entity} parent.
 * <p>
 * The collision shape is the half-cube vanilla skulls use ({@code Block.box(4, 0, 4, 12, 8, 12)}) — small enough to
 * walk past, big enough that a placed head doesn't pass through the floor visually.
 */
public class QueenHeadBlock extends BaseEntityBlock {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    private static final int ROTATIONS = RotationSegment.getMaxSegmentIndex() + 1;

    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);

    private final QueenHeadVariant variant;

    public QueenHeadBlock(QueenHeadVariant variant, BlockBehaviour.Properties properties) {
        super(properties);
        this.variant = variant;
        this.registerDefaultState(this.defaultBlockState().setValue(ROTATION, 0));
    }

    public QueenHeadVariant variant() {
        return variant;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        // Codec is only used for command/structure-block serialization; queen heads aren't expected to
        // round-trip through those paths in normal gameplay, so the simplest stub that returns this block
        // is sufficient. Override with a real codec later if needed for save migrations.
        return simpleCodec(properties -> new QueenHeadBlock(variant, properties));
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NotNull VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ROTATION, RotationSegment.convertToSegment(context.getRotation()));
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION), ROTATIONS));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), ROTATIONS));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ROTATION);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new QueenHeadBlockEntity(pos, state);
    }
}

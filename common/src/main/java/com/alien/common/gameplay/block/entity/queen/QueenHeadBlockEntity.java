package com.alien.common.gameplay.block.entity.queen;

import com.alien.common.gameplay.block.queen.QueenHeadBlock;
import com.alien.common.gameplay.block.queen.QueenHeadVariant;
import com.alien.common.gameplay.block.queen.QueenWallHeadBlock;
import com.alien.common.registry.init.AlienBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Block entity for placed queen heads — exists primarily so the block entity renderer has a hook to render the 3D model
 * on top of the otherwise-empty {@code builtin/entity} block model. Holds no persisted data; the variant comes from the
 * parent block class, and rotation/facing comes from the block state. If we later want per-block tuning (e.g., custom
 * rotation independent of placement, or a "looking-at-player" eye-tracking pose), this is the place to add it.
 */
public class QueenHeadBlockEntity extends BlockEntity {

    public QueenHeadBlockEntity(BlockPos pos, BlockState state) {
        super(AlienBlockEntityTypes.QUEEN_HEAD.get(), pos, state);
    }

    /**
     * Returns the variant of the placed head — derived from the block class. Both the floor and wall blocks store the
     * variant on the block instance itself, so this is just a type-dispatch on whichever block this entity is attached
     * to.
     */
    public @Nullable QueenHeadVariant variant() {
        var block = this.getBlockState().getBlock();

        if (block instanceof QueenHeadBlock floor) {
            return floor.variant();
        }

        if (block instanceof QueenWallHeadBlock wall) {
            return wall.variant();
        }

        return null;
    }
}

package com.alien.common.gameplay.block.entity.crusher;

import com.alien.common.gameplay.block.crusher.CrusherHeadBlock;
import com.alien.common.gameplay.block.crusher.CrusherHeadVariant;
import com.alien.common.gameplay.block.crusher.CrusherWallHeadBlock;
import com.alien.common.registry.init.AlienBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Block entity for placed crusher heads — exists primarily so the block entity renderer has a hook to render the 3D
 * model on top of the otherwise-empty {@code builtin/entity} block model. Holds no persisted data; the variant comes
 * from the parent block class, and rotation/facing comes from the block state.
 */
public class CrusherHeadBlockEntity extends BlockEntity {

    public CrusherHeadBlockEntity(BlockPos pos, BlockState state) {
        super(AlienBlockEntityTypes.CRUSHER_HEAD.get(), pos, state);
    }

    /**
     * Returns the variant of the placed head — derived from the block class. Both the floor and wall blocks store the
     * variant on the block instance itself, so this is just a type-dispatch on whichever block this entity is attached
     * to.
     */
    public @Nullable CrusherHeadVariant variant() {
        var block = this.getBlockState().getBlock();

        if (block instanceof CrusherHeadBlock floor) {
            return floor.variant();
        }

        if (block instanceof CrusherWallHeadBlock wall) {
            return wall.variant();
        }

        return null;
    }
}

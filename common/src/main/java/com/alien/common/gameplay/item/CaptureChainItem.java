package com.alien.common.gameplay.item;

import com.alien.common.gameplay.block.capture.anchor.AnchorBlock;
import com.alien.common.gameplay.block.entity.capture.anchor.AnchorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The capture chain — a heavy-duty, lead-like restraint used to chain a mob to a capture {@link AnchorBlock}.
 * <p>
 * Right-click a leashable mob to take hold of it (it leashes to you, exactly like a lead). Then right-click an anchor
 * to bind that mob to the anchor's chain. Sneak-right-click an anchor to release its chain. Unlike a lead the chain is
 * not consumed, reaches further, and does not snap from distance — the anchor enforces the tether.
 */
public class CaptureChainItem extends Item {

    /** How far around the player to look for the mob they are holding when binding to an anchor. */
    private static final double BIND_SEARCH_RADIUS = 12.0;

    public CaptureChainItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(
        @NotNull ItemStack stack,
        @NotNull Player player,
        @NotNull LivingEntity target,
        @NotNull InteractionHand hand
    ) {
        if (target instanceof Leashable leashable && target != player && leashable.getLeashHolder() == null) {
            if (!player.level().isClientSide) {
                leashable.setLeashedTo(player, true);
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (
            player == null
                || !(level.getBlockState(pos).getBlock() instanceof AnchorBlock)
                || !(level.getBlockEntity(pos) instanceof AnchorBlockEntity anchor)
        ) {
            return super.useOn(context);
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        // Sneak: release any existing chain.
        if (player.isShiftKeyDown()) {
            if (anchor.hasChain()) {
                anchor.release();
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }

        // Already chained: leave it.
        if (anchor.hasChain()) {
            return InteractionResult.PASS;
        }

        // Bind whatever mob the player is currently holding by a leash, then hand the leash over to the anchor.
        LivingEntity held = findMobLeashedTo(level, player);
        if (held != null) {
            anchor.bind(held);
            if (held instanceof Leashable leashable) {
                leashable.dropLeash(true, false);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    private static LivingEntity findMobLeashedTo(Level level, Player player) {
        AABB box = player.getBoundingBox().inflate(BIND_SEARCH_RADIUS);
        for (
            LivingEntity mob : level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                entity -> entity instanceof Leashable leashable && leashable.getLeashHolder() == player
            )
        ) {
            return mob;
        }
        return null;
    }
}

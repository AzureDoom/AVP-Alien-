package com.alien.common.gameplay.item;

import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.hive.lifecycle.QueenInhibitionService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The inhibitor device — right-click a queen to clamp it to her crest. An inhibited queen becomes a contained breeder:
 * her hive autonomy is suppressed and her claim is capped at one chunk (wired in a later slice), though she can still
 * fight and defend. Chaining an inhibited queen later gives her the ridable chained eggsack to lay from.
 * <p>
 * Slice A scope: attach the device (set the synced + persisted flag, reveal the {@code gInhibitor} bone) and consume
 * the item. Application is gated to a helpless queen — incapacitated, hibernating, or secured with all four chains (see
 * {@link Queen#canBeInhibited()}). The behavioural effects land in their own slices.
 */
public class InhibitorItem extends Item {

    public InhibitorItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(
        @NotNull ItemStack stack,
        @NotNull Player player,
        @NotNull LivingEntity target,
        @NotNull InteractionHand hand
    ) {
        if (target instanceof Queen queen && !queen.isInhibited() && queen.canBeInhibited()) {
            if (player.level() instanceof ServerLevel serverLevel) {
                queen.setInhibited(true);
                QueenInhibitionService.onInhibited(serverLevel, queen);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }
}

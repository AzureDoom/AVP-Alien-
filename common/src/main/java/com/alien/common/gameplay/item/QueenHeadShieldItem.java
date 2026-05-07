package com.alien.common.gameplay.item;

import com.blib.api.common.shield.v1.BLibShieldConfig;
import com.blib.api.common.shield.v1.BLibShieldItem;
import com.blib.api.common.shield.v1.BlockOutcome;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class QueenHeadShieldItem extends Item implements BLibShieldItem {

    private static final BLibShieldConfig CONFIG = new BLibShieldConfig(
        72000,
        180f,
        SoundEvents.SHIELD_BLOCK
    );

    public QueenHeadShieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public BLibShieldConfig getShieldConfig() {
        return CONFIG;
    }

    @Override
    public BlockOutcome onBlocked(LivingEntity user, ItemStack stack, DamageSource source, float damage) {
        var damageDealt = (int) Math.max(1, Math.ceil(damage));
        stack.hurtAndBreak(damageDealt, user, EquipmentSlot.MAINHAND);

        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            return BlockOutcome.blockedAndDisabled(60);
        }

        if (
            source.getDirectEntity() instanceof LivingEntity attacker
            && (
                attacker.getMainHandItem().getItem() instanceof AxeItem
                    || attacker.canDisableShield()
            )
        ) {
            return BlockOutcome.blockedAndDisabled(100);
        }

        return BlockOutcome.fullBlock();
    }
}

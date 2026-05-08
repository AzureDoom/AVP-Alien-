package com.alien.common.gameplay.item;

import com.alien.common.registry.key.AlienDamageTypeKeys;
import com.alien.common.registry.tag.AlienDamageTypesTags;
import com.blib.api.common.shield.v1.BLibShieldConfig;
import com.blib.api.common.shield.v1.BLibShieldItem;
import com.blib.api.common.shield.v1.BlockResult;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class QueenHeadItem extends Item implements BLibShieldItem, Equipable {

    private static final BLibShieldConfig CONFIG = new BLibShieldConfig(
        72000,
        180f,
        SoundEvents.SHIELD_BLOCK
    );

    public QueenHeadItem(Properties properties) {
        super(properties);
    }

    @Override
    public BLibShieldConfig getShieldConfig() {
        return CONFIG;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public BlockResult onBlocked(LivingEntity user, ItemStack stack, DamageSource source, float damage) {
        // Xenomorph shields resist acid spit for free with no damage applied.
        if (source.is(AlienDamageTypeKeys.ACID_SPIT)) {
            return BlockResult.fullBlock();
        }

        var damageDealt = (int) Math.max(1, Math.ceil(damage));
        stack.hurtAndBreak(damageDealt, user, EquipmentSlot.MAINHAND);

        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            return BlockResult.blockedAndDisabled(60);
        }

        if (
            source.getDirectEntity() instanceof LivingEntity attacker
            && (
                attacker.getMainHandItem().getItem() instanceof AxeItem
                    || attacker.canDisableShield()
            )
        ) {
            return BlockResult.blockedAndDisabled(100);
        }

        return BlockResult.fullBlock();
    }
}

package com.alien.common.gameplay.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

/**
 * Wearable xenomorph head trophy without a placeable block form.
 */
public class XenomorphHeadItem extends Item implements Equipable {

    public XenomorphHeadItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}

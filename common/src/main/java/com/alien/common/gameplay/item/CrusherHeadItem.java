package com.alien.common.gameplay.item;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Wearable + placeable trophy variant of the crusher head. Extends {@link StandingAndWallBlockItem} so vanilla's
 * standing-vs-wall placement logic picks the floor or wall block based on which face the player clicks (rejecting
 * up-facing surfaces by passing {@link Direction#DOWN} as the disallowed face — heads can't sit on ceilings).
 * <p>
 * Implements {@link Equipable} for the head armor slot — drag onto the helmet slot in the inventory to wear it.
 * Right-click in the world places the block; right-click in the air does nothing (no shield use, since this trophy
 * variant doesn't implement {@code BLibShieldItem}).
 */
public class CrusherHeadItem extends StandingAndWallBlockItem implements Equipable {

    public CrusherHeadItem(Block standingBlock, Block wallBlock, Properties properties) {
        super(standingBlock, wallBlock, properties, Direction.DOWN);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}

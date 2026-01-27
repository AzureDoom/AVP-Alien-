package com.alien.common.gameplay.item;

import com.alien.common.registry.init.AlienArmorMaterials;
import com.alien.common.registry.init.item.AlienArmorItems;
import com.blib.api.common.tooltip.v1.TooltipCategoryType;
import com.blib.api.common.tooltip.v1.TooltipHintBuilder;
import com.blib.api.common.tooltip.v1.TooltipTranslationKeys;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NetherChitinArmorItem extends ArmorItem {

    private static final List<Component> TOOLTIP_COMPONENTS = new TooltipHintBuilder()
        .addCategory(TooltipCategoryType.WHEN_FULL_ARMOR_SET_EQUIPPED)
        .addPositiveEffect(TooltipTranslationKeys.EFFECT_FIRE_RESISTANCE)
        .build();

    public NetherChitinArmorItem(Type type) {
        super(
            AlienArmorMaterials.NETHER_CHITIN,
            type,
            new Properties().durability(type.getDurability(AlienArmorItems.CHITIN_DURABILITY_MULTIPLIER)).fireResistant()
        );
    }

    @Override
    public void appendHoverText(
        @NotNull ItemStack stack,
        @NotNull TooltipContext context,
        @NotNull List<Component> tooltipComponents,
        @NotNull TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.addAll(TOOLTIP_COMPONENTS);
    }
}

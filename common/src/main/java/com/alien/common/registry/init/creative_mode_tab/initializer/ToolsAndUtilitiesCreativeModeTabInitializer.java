package com.alien.common.registry.init.creative_mode_tab.initializer;

import com.alien.common.registry.init.AlienPotions;
import com.alien.common.registry.init.item.AlienItems;
import com.blib.api.common.registry.v1.BLibHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.Objects;
import java.util.function.Consumer;

public class ToolsAndUtilitiesCreativeModeTabInitializer {

    public static final Consumer<CreativeModeTab.Output> OUTPUT_CONSUMER = output -> {
        CreativeModeTabUtil.accept(output, AlienItems.ALIEN_MUSIC_DISC_1);

        acceptPotion(output, AlienPotions.METAMORPHOSIS);
        acceptPotion(output, AlienPotions.LONG_METAMORPHOSIS);
        acceptPotion(output, AlienPotions.STRONG_METAMORPHOSIS);
        acceptPotion(output, AlienPotions.SCOURGE);
        acceptPotion(output, AlienPotions.LONG_SCOURGE);
        acceptPotion(output, AlienPotions.STRONG_SCOURGE);

        acceptSplashPotion(output, AlienPotions.METAMORPHOSIS);
        acceptSplashPotion(output, AlienPotions.LONG_METAMORPHOSIS);
        acceptSplashPotion(output, AlienPotions.STRONG_METAMORPHOSIS);
        acceptSplashPotion(output, AlienPotions.SCOURGE);
        acceptSplashPotion(output, AlienPotions.LONG_SCOURGE);
        acceptSplashPotion(output, AlienPotions.STRONG_SCOURGE);

        acceptLingeringPotion(output, AlienPotions.METAMORPHOSIS);
        acceptLingeringPotion(output, AlienPotions.LONG_METAMORPHOSIS);
        acceptLingeringPotion(output, AlienPotions.STRONG_METAMORPHOSIS);
        acceptLingeringPotion(output, AlienPotions.SCOURGE);
        acceptLingeringPotion(output, AlienPotions.LONG_SCOURGE);
        acceptLingeringPotion(output, AlienPotions.STRONG_SCOURGE);
    };

    private static void acceptPotion(CreativeModeTab.Output output, BLibHolder<Potion> potionHolder) {
        var stack = new ItemStack(Items.POTION);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Objects.requireNonNull(potionHolder.getBackingHolder())));
        output.accept(stack);
    }

    private static void acceptSplashPotion(CreativeModeTab.Output output, BLibHolder<Potion> potionHolder) {
        var stack = new ItemStack(Items.SPLASH_POTION);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Objects.requireNonNull(potionHolder.getBackingHolder())));
        output.accept(stack);
    }

    private static void acceptLingeringPotion(CreativeModeTab.Output output, BLibHolder<Potion> potionHolder) {
        var stack = new ItemStack(Items.LINGERING_POTION);
        stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Objects.requireNonNull(potionHolder.getBackingHolder())));
        output.accept(stack);
    }
}

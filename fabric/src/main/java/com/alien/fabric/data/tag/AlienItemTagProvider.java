package com.alien.fabric.data.tag;

import com.alien.common.registry.init.item.AlienItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;

import java.util.concurrent.CompletableFuture;

public class AlienItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public AlienItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        addArmors();
        addAutomatedTagItems();

        addCompatibilityTags();
    }

    private void addAutomatedTagItems() {
        // Armor
        var headArmorTagProvider = getOrCreateTagBuilder(ItemTags.HEAD_ARMOR);
        var chestArmorTagProvider = getOrCreateTagBuilder(ItemTags.CHEST_ARMOR);
        var legArmorTagProvider = getOrCreateTagBuilder(ItemTags.LEG_ARMOR);
        var footArmorTagProvider = getOrCreateTagBuilder(ItemTags.FOOT_ARMOR);

        // Blocks
        var buttonTagProvider = getOrCreateTagBuilder(ItemTags.BUTTONS);
        var doorTagProvider = getOrCreateTagBuilder(ItemTags.DOORS);
        var fenceTagProvider = getOrCreateTagBuilder(ItemTags.FENCES);
        var slabTagProvider = getOrCreateTagBuilder(ItemTags.SLABS);
        var stairsTagProvider = getOrCreateTagBuilder(ItemTags.STAIRS);
        var trapdoorTagProvider = getOrCreateTagBuilder(ItemTags.TRAPDOORS);
        var wallTagBuilder = getOrCreateTagBuilder(ItemTags.WALLS);

        // Tools
        var axeTagProvider = getOrCreateTagBuilder(ItemTags.AXES);
        var hoeTagProvider = getOrCreateTagBuilder(ItemTags.HOES);
        var pickaxeTagProvider = getOrCreateTagBuilder(ItemTags.PICKAXES);
        var shovelTagProvider = getOrCreateTagBuilder(ItemTags.SHOVELS);

        // Weapons
        var swordTagProvider = getOrCreateTagBuilder(ItemTags.SWORDS);

        AlienItems.getAll().forEach(deferredHolder -> {
            var item = deferredHolder.get();

            if (item instanceof ArmorItem armorItem) {
                switch (armorItem.getType()) {
                    case HELMET -> headArmorTagProvider.add(item);
                    case CHESTPLATE -> chestArmorTagProvider.add(item);
                    case LEGGINGS -> legArmorTagProvider.add(item);
                    case BOOTS -> footArmorTagProvider.add(item);
                    case BODY -> { /* NO-OP */ }
                }
            }

            if (item instanceof BlockItem blockItem) {
                var block = blockItem.getBlock();

                if (block instanceof ButtonBlock) {
                    buttonTagProvider.add(item);
                }

                if (block instanceof DoorBlock) {
                    doorTagProvider.add(item);
                }

                if (block instanceof FenceBlock) {
                    fenceTagProvider.add(item);
                }

                if (block instanceof SlabBlock) {
                    slabTagProvider.add(item);
                }

                if (block instanceof StairBlock) {
                    stairsTagProvider.add(item);
                }

                if (block instanceof TrapDoorBlock) {
                    trapdoorTagProvider.add(item);
                }

                if (block instanceof WallBlock) {
                    wallTagBuilder.add(item);
                }
            }

            if (item instanceof AxeItem) {
                axeTagProvider.add(item);
            }

            if (item instanceof HoeItem) {
                hoeTagProvider.add(item);
            }

            if (item instanceof PickaxeItem) {
                pickaxeTagProvider.add(item);
            }

            if (item instanceof ShovelItem) {
                shovelTagProvider.add(item);
            }

            if (item instanceof SwordItem) {
                swordTagProvider.add(item);
            }
        });
    }

    private void addArmors() {
    }

    private void addCompatibilityTags() {
    }
}

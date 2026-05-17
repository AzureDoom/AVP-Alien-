package com.alien.fabric.data.tag;

import com.alien.common.registry.init.block.AberrantAlienChitinBlocks;
import com.alien.common.registry.init.block.AberrantAlienResinBlocks;
import com.alien.common.registry.init.block.AlienChitinBlocks;
import com.alien.common.registry.init.block.AlienResinBlocks;
import com.alien.common.registry.init.block.IrradiatedAlienResinBlocks;
import com.alien.common.registry.init.block.NetherAlienChitinBlocks;
import com.alien.common.registry.init.block.NetherAlienResinBlocks;
import com.alien.common.registry.tag.AlienBlockTags;
import com.blib.api.common.tag.v1.BLibBlockTags;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class AlienBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public AlienBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        addCompatibilityTags();

        getOrCreateTagBuilder(AlienBlockTags.IRRADIATED_RESIN)
            .add(
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_NODE.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_VEIN.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_WEB.get(),

                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_BRICKS.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_BRICK_SLAB.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_BRICK_STAIRS.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_BRICK_WALL.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_VENT.get(),
                IrradiatedAlienResinBlocks.RIBBED_IRRADIATED_RESIN.get(),
                IrradiatedAlienResinBlocks.SMOOTH_IRRADIATED_RESIN.get(),
                IrradiatedAlienResinBlocks.SMOOTH_IRRADIATED_RESIN_SLAB.get(),
                IrradiatedAlienResinBlocks.SMOOTH_IRRADIATED_RESIN_STAIRS.get(),
                IrradiatedAlienResinBlocks.SMOOTH_IRRADIATED_RESIN_WALL.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.ABERRANT_RESIN)
            .add(
                AberrantAlienResinBlocks.ABERRANT_RESIN.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_SLAB.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_STAIRS.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_NODE.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_VEIN.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_WEB.get(),

                AberrantAlienResinBlocks.ABERRANT_RESIN_BRICKS.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_BRICK_SLAB.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_BRICK_STAIRS.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_BRICK_WALL.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_VENT.get(),
                AberrantAlienResinBlocks.RIBBED_ABERRANT_RESIN.get(),
                AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN.get(),
                AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN_SLAB.get(),
                AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN_STAIRS.get(),
                AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN_WALL.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.NETHER_RESIN)
            .add(
                NetherAlienResinBlocks.NETHER_RESIN.get(),
                NetherAlienResinBlocks.NETHER_RESIN_SLAB.get(),
                NetherAlienResinBlocks.NETHER_RESIN_STAIRS.get(),
                NetherAlienResinBlocks.NETHER_RESIN_NODE.get(),
                NetherAlienResinBlocks.NETHER_RESIN_VEIN.get(),
                NetherAlienResinBlocks.NETHER_RESIN_WEB.get(),

                NetherAlienResinBlocks.NETHER_RESIN_BRICKS.get(),
                NetherAlienResinBlocks.NETHER_RESIN_BRICK_SLAB.get(),
                NetherAlienResinBlocks.NETHER_RESIN_BRICK_STAIRS.get(),
                NetherAlienResinBlocks.NETHER_RESIN_BRICK_WALL.get(),
                NetherAlienResinBlocks.NETHER_RESIN_VENT.get(),
                NetherAlienResinBlocks.RIBBED_NETHER_RESIN.get(),
                NetherAlienResinBlocks.SMOOTH_NETHER_RESIN.get(),
                NetherAlienResinBlocks.SMOOTH_NETHER_RESIN_SLAB.get(),
                NetherAlienResinBlocks.SMOOTH_NETHER_RESIN_STAIRS.get(),
                NetherAlienResinBlocks.SMOOTH_NETHER_RESIN_WALL.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.NORMAL_RESIN)
            .add(
                AlienResinBlocks.RESIN.get(),
                AlienResinBlocks.RESIN_SLAB.get(),
                AlienResinBlocks.RESIN_STAIRS.get(),
                AlienResinBlocks.RESIN_NODE.get(),
                AlienResinBlocks.RESIN_VEIN.get(),
                AlienResinBlocks.RESIN_WEB.get(),

                AlienResinBlocks.RESIN_BRICKS.get(),
                AlienResinBlocks.RESIN_BRICK_SLAB.get(),
                AlienResinBlocks.RESIN_BRICK_STAIRS.get(),
                AlienResinBlocks.RESIN_BRICK_WALL.get(),
                AlienResinBlocks.RESIN_VENT.get(),
                AlienResinBlocks.RIBBED_RESIN.get(),
                AlienResinBlocks.SMOOTH_RESIN.get(),
                AlienResinBlocks.SMOOTH_RESIN_SLAB.get(),
                AlienResinBlocks.SMOOTH_RESIN_STAIRS.get(),
                AlienResinBlocks.SMOOTH_RESIN_WALL.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.ABERRANT_CHITIN)
            .add(
                AberrantAlienChitinBlocks.ABERRANT_CHITIN_BLOCK.get(),
                AberrantAlienChitinBlocks.ABERRANT_CHITIN_BLOCK_SLAB.get(),
                AberrantAlienChitinBlocks.ABERRANT_CHITIN_BLOCK_STAIRS.get(),
                AberrantAlienChitinBlocks.ABERRANT_CHITIN_BLOCK_WALL.get(),
                AberrantAlienChitinBlocks.ABERRANT_CHITIN_BRICKS.get(),
                AberrantAlienChitinBlocks.ABERRANT_CHITIN_BRICK_SLAB.get(),
                AberrantAlienChitinBlocks.ABERRANT_CHITIN_BRICK_STAIRS.get(),
                AberrantAlienChitinBlocks.ABERRANT_CHITIN_BRICK_WALL.get(),
                AberrantAlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS.get(),
                AberrantAlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO.get(),
                AberrantAlienChitinBlocks.POLISHED_ABERRANT_CHITIN.get(),
                AberrantAlienChitinBlocks.POLISHED_ABERRANT_CHITIN_SLAB.get(),
                AberrantAlienChitinBlocks.POLISHED_ABERRANT_CHITIN_STAIRS.get(),
                AberrantAlienChitinBlocks.POLISHED_ABERRANT_CHITIN_WALL.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.NETHER_CHITIN)
            .add(

                NetherAlienChitinBlocks.NETHER_CHITIN_BLOCK.get(),
                NetherAlienChitinBlocks.NETHER_CHITIN_BLOCK_SLAB.get(),
                NetherAlienChitinBlocks.NETHER_CHITIN_BLOCK_STAIRS.get(),
                NetherAlienChitinBlocks.NETHER_CHITIN_BLOCK_WALL.get(),
                NetherAlienChitinBlocks.NETHER_CHITIN_BRICKS.get(),
                NetherAlienChitinBlocks.NETHER_CHITIN_BRICK_SLAB.get(),
                NetherAlienChitinBlocks.NETHER_CHITIN_BRICK_STAIRS.get(),
                NetherAlienChitinBlocks.NETHER_CHITIN_BRICK_WALL.get(),
                NetherAlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS.get(),
                NetherAlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS_EMBRYO.get(),
                NetherAlienChitinBlocks.POLISHED_NETHER_CHITIN.get(),
                NetherAlienChitinBlocks.POLISHED_NETHER_CHITIN_SLAB.get(),
                NetherAlienChitinBlocks.POLISHED_NETHER_CHITIN_STAIRS.get(),
                NetherAlienChitinBlocks.POLISHED_NETHER_CHITIN_WALL.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.NORMAL_CHITIN)
            .add(
                AlienChitinBlocks.CHITIN_BLOCK.get(),
                AlienChitinBlocks.CHITIN_BLOCK_SLAB.get(),
                AlienChitinBlocks.CHITIN_BLOCK_STAIRS.get(),
                AlienChitinBlocks.CHITIN_BLOCK_WALL.get(),
                AlienChitinBlocks.CHITIN_BRICKS.get(),
                AlienChitinBlocks.CHITIN_BRICK_SLAB.get(),
                AlienChitinBlocks.CHITIN_BRICK_STAIRS.get(),
                AlienChitinBlocks.CHITIN_BRICK_WALL.get(),
                AlienChitinBlocks.CHISELED_CHITIN_BRICKS.get(),
                AlienChitinBlocks.CHISELED_CHITIN_BRICKS_EMBRYO.get(),
                AlienChitinBlocks.POLISHED_CHITIN.get(),
                AlienChitinBlocks.POLISHED_CHITIN_SLAB.get(),
                AlienChitinBlocks.POLISHED_CHITIN_STAIRS.get(),
                AlienChitinBlocks.POLISHED_CHITIN_WALL.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.CHITIN)
            .addTag(AlienBlockTags.ABERRANT_CHITIN)
            .addTag(AlienBlockTags.NETHER_CHITIN)
            .addTag(AlienBlockTags.NORMAL_CHITIN);

        getOrCreateTagBuilder(AlienBlockTags.RESIN)
            .addTag(AlienBlockTags.ABERRANT_RESIN)
            .addTag(AlienBlockTags.IRRADIATED_RESIN)
            .addTag(AlienBlockTags.NETHER_RESIN)
            .addTag(AlienBlockTags.NORMAL_RESIN);

        getOrCreateTagBuilder(AlienBlockTags.RESIN_BLOCKS)
            .add(
                AberrantAlienResinBlocks.ABERRANT_RESIN.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN.get(),
                NetherAlienResinBlocks.NETHER_RESIN.get(),
                AlienResinBlocks.RESIN.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.RESIN_NODES)
            .add(
                AberrantAlienResinBlocks.ABERRANT_RESIN_NODE.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_NODE.get(),
                NetherAlienResinBlocks.NETHER_RESIN_NODE.get(),
                AlienResinBlocks.RESIN_NODE.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.RESIN_REPLACEABLE)
            .addOptionalTag(BlockTags.BASE_STONE_NETHER)
            .addOptionalTag(BlockTags.BASE_STONE_OVERWORLD)
            .addOptionalTag(BlockTags.DIRT)
            .addOptionalTag(BlockTags.NYLIUM)
            .addOptionalTag(BlockTags.TERRACOTTA)
            .add(
                Blocks.CALCITE,
                Blocks.CLAY,
                Blocks.DRIPSTONE_BLOCK,
                Blocks.END_STONE,
                Blocks.GRAVEL,
                Blocks.RED_SAND,
                Blocks.RED_SANDSTONE,
                Blocks.SAND,
                Blocks.SANDSTONE,
                Blocks.SMOOTH_BASALT,
                Blocks.SOUL_SAND,
                Blocks.SOUL_SOIL
            );

        getOrCreateTagBuilder(AlienBlockTags.ABERRANT_RESIN_REPLACEABLE)
            .addTag(AlienBlockTags.RESIN_REPLACEABLE)
            .addTag(AlienBlockTags.IRRADIATED_RESIN)
            .addTag(AlienBlockTags.NETHER_RESIN)
            .addTag(AlienBlockTags.NORMAL_RESIN);

        getOrCreateTagBuilder(AlienBlockTags.IRRADIATED_RESIN_REPLACEABLE)
            .addTag(AlienBlockTags.RESIN_REPLACEABLE)
            .addTag(AlienBlockTags.ABERRANT_RESIN)
            .addTag(AlienBlockTags.NETHER_RESIN)
            .addTag(AlienBlockTags.NORMAL_RESIN);

        getOrCreateTagBuilder(AlienBlockTags.NETHER_RESIN_REPLACEABLE)
            .addTag(AlienBlockTags.RESIN_REPLACEABLE)
            .addTag(AlienBlockTags.ABERRANT_RESIN)
            .addTag(AlienBlockTags.IRRADIATED_RESIN)
            .addTag(AlienBlockTags.NORMAL_RESIN);

        getOrCreateTagBuilder(AlienBlockTags.NORMAL_RESIN_REPLACEABLE)
            .addTag(AlienBlockTags.RESIN_REPLACEABLE)
            .addTag(AlienBlockTags.ABERRANT_RESIN)
            .addTag(AlienBlockTags.IRRADIATED_RESIN)
            .addTag(AlienBlockTags.NETHER_RESIN);

        getOrCreateTagBuilder(AlienBlockTags.RESIN_VEINS)
            .add(
                AberrantAlienResinBlocks.ABERRANT_RESIN_VEIN.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_VEIN.get(),
                NetherAlienResinBlocks.NETHER_RESIN_VEIN.get(),
                AlienResinBlocks.RESIN_VEIN.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.RESIN_VENTS)
            .add(
                AberrantAlienResinBlocks.ABERRANT_RESIN_VENT.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_VENT.get(),
                NetherAlienResinBlocks.NETHER_RESIN_VENT.get(),
                AlienResinBlocks.RESIN_VENT.get()
            );

        getOrCreateTagBuilder(AlienBlockTags.RESIN_WEBS)
            .add(
                AberrantAlienResinBlocks.ABERRANT_RESIN_WEB.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_WEB.get(),
                NetherAlienResinBlocks.NETHER_RESIN_WEB.get(),
                AlienResinBlocks.RESIN_WEB.get()
            );

        // Acid-immune blocks
        getOrCreateTagBuilder(AlienBlockTags.ACID_IMMUNE)
            .addOptionalTag(BLibBlockTags.SHOULD_NOT_BE_DESTROYED)
            .addTag(AlienBlockTags.CHITIN)
            .addTag(AlienBlockTags.RESIN)
            .add(Blocks.AIR)
            .add(Blocks.FIRE)
            .add(Blocks.SOUL_FIRE);

        getOrCreateTagBuilder(AlienBlockTags.NETHER_ACID_IMMUNE)
            .addOptionalTag(BlockTags.INFINIBURN_NETHER)
            .addTag(AlienBlockTags.ACID_IMMUNE);

        getOrCreateTagBuilder(AlienBlockTags.IRRADIATED_ACID_IMMUNE)
            .addTag(AlienBlockTags.ACID_IMMUNE)
            .add(
                Blocks.BLUE_ICE
            );

        getOrCreateTagBuilder(AlienBlockTags.XENOMORPH_IMMUNE)
            .addOptionalTag(BLibBlockTags.SHOULD_NOT_BE_DESTROYED)
            .addTag(AlienBlockTags.RESIN_VENTS)
            .addTag(AlienBlockTags.RESIN_WEBS);

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
            .addTag(AlienBlockTags.RESIN_VEINS)
            .addTag(AlienBlockTags.RESIN_WEBS);

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(AlienBlockTags.CHITIN)
            .add(
                AberrantAlienResinBlocks.ABERRANT_RESIN.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_SLAB.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_STAIRS.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_BRICKS.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_BRICK_SLAB.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_BRICK_STAIRS.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_BRICK_WALL.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_NODE.get(),
                AberrantAlienResinBlocks.ABERRANT_RESIN_VENT.get(),
                AberrantAlienResinBlocks.RIBBED_ABERRANT_RESIN.get(),
                AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN.get(),
                AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN_SLAB.get(),
                AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN_STAIRS.get(),
                AberrantAlienResinBlocks.SMOOTH_ABERRANT_RESIN_WALL.get(),

                IrradiatedAlienResinBlocks.IRRADIATED_RESIN.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_SLAB.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_STAIRS.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_BRICKS.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_BRICK_SLAB.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_BRICK_STAIRS.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_BRICK_WALL.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_NODE.get(),
                IrradiatedAlienResinBlocks.IRRADIATED_RESIN_VENT.get(),
                IrradiatedAlienResinBlocks.RIBBED_IRRADIATED_RESIN.get(),
                IrradiatedAlienResinBlocks.SMOOTH_IRRADIATED_RESIN.get(),
                IrradiatedAlienResinBlocks.SMOOTH_IRRADIATED_RESIN_SLAB.get(),
                IrradiatedAlienResinBlocks.SMOOTH_IRRADIATED_RESIN_STAIRS.get(),
                IrradiatedAlienResinBlocks.SMOOTH_IRRADIATED_RESIN_WALL.get(),

                NetherAlienResinBlocks.NETHER_RESIN.get(),
                NetherAlienResinBlocks.NETHER_RESIN_SLAB.get(),
                NetherAlienResinBlocks.NETHER_RESIN_STAIRS.get(),
                NetherAlienResinBlocks.NETHER_RESIN_BRICKS.get(),
                NetherAlienResinBlocks.NETHER_RESIN_BRICK_SLAB.get(),
                NetherAlienResinBlocks.NETHER_RESIN_BRICK_STAIRS.get(),
                NetherAlienResinBlocks.NETHER_RESIN_BRICK_WALL.get(),
                NetherAlienResinBlocks.NETHER_RESIN_NODE.get(),
                NetherAlienResinBlocks.NETHER_RESIN_VENT.get(),
                NetherAlienResinBlocks.RIBBED_NETHER_RESIN.get(),
                NetherAlienResinBlocks.SMOOTH_NETHER_RESIN.get(),
                NetherAlienResinBlocks.SMOOTH_NETHER_RESIN_SLAB.get(),
                NetherAlienResinBlocks.SMOOTH_NETHER_RESIN_STAIRS.get(),
                NetherAlienResinBlocks.SMOOTH_NETHER_RESIN_WALL.get(),

                AlienResinBlocks.RESIN.get(),
                AlienResinBlocks.RESIN_SLAB.get(),
                AlienResinBlocks.RESIN_STAIRS.get(),
                AlienResinBlocks.RESIN_BRICKS.get(),
                AlienResinBlocks.RESIN_BRICK_SLAB.get(),
                AlienResinBlocks.RESIN_BRICK_STAIRS.get(),
                AlienResinBlocks.RESIN_BRICK_WALL.get(),
                AlienResinBlocks.RESIN_NODE.get(),
                AlienResinBlocks.RESIN_VENT.get(),
                AlienResinBlocks.RIBBED_RESIN.get(),
                AlienResinBlocks.SMOOTH_RESIN.get(),
                AlienResinBlocks.SMOOTH_RESIN_SLAB.get(),
                AlienResinBlocks.SMOOTH_RESIN_STAIRS.get(),
                AlienResinBlocks.SMOOTH_RESIN_WALL.get()
            );

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
            .addTag(AlienBlockTags.CHITIN)
            .addTag(AlienBlockTags.RESIN);
    }

    private void addCompatibilityTags() {
        getOrCreateTagBuilder(AlienBlockTags.ACID_IMMUNE)
            .addOptionalTag(GigTags.ACID_RESISTANT);

        getOrCreateTagBuilder(GigTags.ACID_RESISTANT)
            .addTag(AlienBlockTags.CHITIN)
            .addTag(AlienBlockTags.RESIN);
    }
}

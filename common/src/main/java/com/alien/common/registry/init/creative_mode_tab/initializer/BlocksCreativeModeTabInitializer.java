package com.alien.common.registry.init.creative_mode_tab.initializer;

import com.alien.common.registry.init.block.AlienBlocks;
import com.alien.common.registry.init.block.AlienChitinBlocks;
import com.alien.common.registry.init.block.AlienResinBlocks;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Consumer;

public class BlocksCreativeModeTabInitializer {

    public static final Consumer<CreativeModeTab.Output> OUTPUT_CONSUMER = output -> {

        // Alien blocks
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RESIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RESIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RESIN_BRICKS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RESIN_BRICK_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RESIN_BRICK_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RESIN_BRICK_WALL);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RESIN_VEIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RESIN_VENT);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RESIN_WEB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RIBBED_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_RESIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_RESIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_RESIN_WALL);

        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHITIN_BLOCK);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHITIN_BLOCK_SLAB);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHITIN_BLOCK_STAIRS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHITIN_BLOCK_WALL);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHITIN_BRICKS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHITIN_BRICK_SLAB);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHITIN_BRICK_STAIRS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHITIN_BRICK_WALL);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_CHITIN);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_CHITIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_CHITIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_CHITIN_WALL);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHISELED_CHITIN_BRICKS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHISELED_CHITIN_BRICKS_EMBRYO);

        CreativeModeTabUtil.accept(output, AlienResinBlocks.NETHER_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.NETHER_RESIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.NETHER_RESIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.NETHER_RESIN_BRICKS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.NETHER_RESIN_BRICK_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.NETHER_RESIN_BRICK_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.NETHER_RESIN_BRICK_WALL);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.NETHER_RESIN_VEIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.NETHER_RESIN_VENT);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.NETHER_RESIN_WEB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RIBBED_NETHER_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_NETHER_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_NETHER_RESIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_NETHER_RESIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_NETHER_RESIN_WALL);

        CreativeModeTabUtil.accept(output, AlienChitinBlocks.NETHER_CHITIN_BLOCK);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.NETHER_CHITIN_BLOCK_SLAB);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.NETHER_CHITIN_BLOCK_STAIRS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.NETHER_CHITIN_BLOCK_WALL);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.NETHER_CHITIN_BRICKS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.NETHER_CHITIN_BRICK_SLAB);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.NETHER_CHITIN_BRICK_STAIRS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.NETHER_CHITIN_BRICK_WALL);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_NETHER_CHITIN);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_NETHER_CHITIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_NETHER_CHITIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_NETHER_CHITIN_WALL);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS_EMBRYO);

        CreativeModeTabUtil.accept(output, AlienResinBlocks.ABERRANT_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.ABERRANT_RESIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.ABERRANT_RESIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.ABERRANT_RESIN_BRICKS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.ABERRANT_RESIN_BRICK_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.ABERRANT_RESIN_BRICK_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.ABERRANT_RESIN_BRICK_WALL);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.ABERRANT_RESIN_VEIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.ABERRANT_RESIN_VENT);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.ABERRANT_RESIN_WEB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RIBBED_ABERRANT_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_ABERRANT_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_ABERRANT_RESIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_ABERRANT_RESIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_ABERRANT_RESIN_WALL);

        CreativeModeTabUtil.accept(output, AlienChitinBlocks.ABERRANT_CHITIN_BLOCK);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_SLAB);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_STAIRS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_WALL);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.ABERRANT_CHITIN_BRICKS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.ABERRANT_CHITIN_BRICK_SLAB);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.ABERRANT_CHITIN_BRICK_STAIRS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.ABERRANT_CHITIN_BRICK_WALL);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_ABERRANT_CHITIN);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_WALL);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS);
        CreativeModeTabUtil.accept(output, AlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO);

        CreativeModeTabUtil.accept(output, AlienResinBlocks.IRRADIATED_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.IRRADIATED_RESIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.IRRADIATED_RESIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.IRRADIATED_RESIN_BRICKS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.IRRADIATED_RESIN_BRICK_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.IRRADIATED_RESIN_BRICK_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.IRRADIATED_RESIN_BRICK_WALL);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.IRRADIATED_RESIN_VEIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.IRRADIATED_RESIN_VENT);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.IRRADIATED_RESIN_WEB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.RIBBED_IRRADIATED_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_IRRADIATED_RESIN);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_SLAB);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_STAIRS);
        CreativeModeTabUtil.accept(output, AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_WALL);

        CreativeModeTabUtil.accept(output, AlienBlocks.ROYAL_JELLY_BLOCK);
    };
}

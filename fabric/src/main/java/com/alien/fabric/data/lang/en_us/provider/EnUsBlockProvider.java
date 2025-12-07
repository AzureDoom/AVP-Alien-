package com.alien.fabric.data.lang.en_us.provider;

import com.alien.common.registry.init.block.AlienBlocks;
import com.alien.common.registry.init.block.AlienChitinBlocks;
import com.alien.common.registry.init.block.AlienResinBlocks;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnUsBlockProvider {

    private static final HashSet<Block> TOUCHED_ENTRIES = new HashSet<>();

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {
        addBlock(builder, AlienBlocks.ROYAL_JELLY_BLOCK, "Royal Jelly Block");

        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN, "Aberrant Resin");
        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN_BRICKS, "Aberrant Resin Bricks");
        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN_BRICK_SLAB, "Aberrant Resin Brick Slab");
        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN_BRICK_STAIRS, "Aberrant Resin Brick Stairs");
        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN_BRICK_WALL, "Aberrant Resin Brick Wall");
        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN_SLAB, "Aberrant Resin Slab");
        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN_STAIRS, "Aberrant Resin Stairs");
        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN_NODE, "Aberrant Resin");
        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN_VEIN, "Aberrant Resin Vein");
        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN_VENT, "Aberrant Resin Vent");
        addBlock(builder, AlienResinBlocks.ABERRANT_RESIN_WEB, "Aberrant Resin Web");

        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN, "Irradiated Resin");
        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN_BRICKS, "Irradiated Resin Bricks");
        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN_BRICK_SLAB, "Irradiated Resin Brick Slab");
        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN_BRICK_STAIRS, "Irradiated Resin Brick Stairs");
        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN_BRICK_WALL, "Irradiated Resin Brick Wall");
        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN_SLAB, "Irradiated Resin Slab");
        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN_STAIRS, "Irradiated Resin Stairs");
        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN_NODE, "Irradiated Resin");
        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN_VEIN, "Irradiated Resin Vein");
        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN_VENT, "Irradiated Resin Vent");
        addBlock(builder, AlienResinBlocks.IRRADIATED_RESIN_WEB, "Irradiated Resin Web");

        addBlock(builder, AlienResinBlocks.NETHER_RESIN, "Nether Resin");
        addBlock(builder, AlienResinBlocks.NETHER_RESIN_BRICKS, "Nether Resin Bricks");
        addBlock(builder, AlienResinBlocks.NETHER_RESIN_BRICK_SLAB, "Nether Resin Brick Slab");
        addBlock(builder, AlienResinBlocks.NETHER_RESIN_BRICK_STAIRS, "Nether Resin Brick Stairs");
        addBlock(builder, AlienResinBlocks.NETHER_RESIN_BRICK_WALL, "Nether Resin Brick Wall");
        addBlock(builder, AlienResinBlocks.NETHER_RESIN_SLAB, "Nether Resin Slab");
        addBlock(builder, AlienResinBlocks.NETHER_RESIN_STAIRS, "Nether Resin Stairs");
        addBlock(builder, AlienResinBlocks.NETHER_RESIN_NODE, "Nether Resin");
        addBlock(builder, AlienResinBlocks.NETHER_RESIN_VEIN, "Nether Resin Vein");
        addBlock(builder, AlienResinBlocks.NETHER_RESIN_VENT, "Nether Resin Vent");
        addBlock(builder, AlienResinBlocks.NETHER_RESIN_WEB, "Nether Resin Web");

        addBlock(builder, AlienResinBlocks.RESIN, "Resin");
        addBlock(builder, AlienResinBlocks.RESIN_BRICKS, "Resin Bricks");
        addBlock(builder, AlienResinBlocks.RESIN_BRICK_SLAB, "Resin Brick Slab");
        addBlock(builder, AlienResinBlocks.RESIN_BRICK_STAIRS, "Resin Brick Stairs");
        addBlock(builder, AlienResinBlocks.RESIN_BRICK_WALL, "Resin Brick Wall");
        addBlock(builder, AlienResinBlocks.RESIN_NODE, "Resin");
        addBlock(builder, AlienResinBlocks.RESIN_SLAB, "Resin Slab");
        addBlock(builder, AlienResinBlocks.RESIN_STAIRS, "Resin Stairs");
        addBlock(builder, AlienResinBlocks.RESIN_VEIN, "Resin Vein");
        addBlock(builder, AlienResinBlocks.RESIN_VENT, "Resin Vent");
        addBlock(builder, AlienResinBlocks.RESIN_WEB, "Resin Web");

        addBlock(builder, AlienResinBlocks.RIBBED_ABERRANT_RESIN, "Ribbed Aberrant Resin");
        addBlock(builder, AlienResinBlocks.RIBBED_IRRADIATED_RESIN, "Ribbed Irradiated Resin");
        addBlock(builder, AlienResinBlocks.RIBBED_NETHER_RESIN, "Ribbed Nether Resin");
        addBlock(builder, AlienResinBlocks.RIBBED_RESIN, "Ribbed Resin");
        addBlock(builder, AlienResinBlocks.SMOOTH_ABERRANT_RESIN, "Smooth Aberrant Resin");
        addBlock(builder, AlienResinBlocks.SMOOTH_ABERRANT_RESIN_SLAB, "Smooth Aberrant Resin Slab");
        addBlock(builder, AlienResinBlocks.SMOOTH_ABERRANT_RESIN_STAIRS, "Smooth Aberrant Resin Stairs");
        addBlock(builder, AlienResinBlocks.SMOOTH_ABERRANT_RESIN_WALL, "Smooth Aberrant Resin Wall");
        addBlock(builder, AlienResinBlocks.SMOOTH_IRRADIATED_RESIN, "Smooth Irradiated Resin");
        addBlock(builder, AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_SLAB, "Smooth Irradiated Resin Slab");
        addBlock(builder, AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_STAIRS, "Smooth Irradiated Resin Stairs");
        addBlock(builder, AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_WALL, "Smooth Irradiated Resin Wall");
        addBlock(builder, AlienResinBlocks.SMOOTH_NETHER_RESIN, "Smooth Nether Resin");
        addBlock(builder, AlienResinBlocks.SMOOTH_NETHER_RESIN_SLAB, "Smooth Nether Resin Slab");
        addBlock(builder, AlienResinBlocks.SMOOTH_NETHER_RESIN_STAIRS, "Smooth Nether Resin Stairs");
        addBlock(builder, AlienResinBlocks.SMOOTH_NETHER_RESIN_WALL, "Smooth Nether Resin Wall");
        addBlock(builder, AlienResinBlocks.SMOOTH_RESIN, "Smooth Resin");
        addBlock(builder, AlienResinBlocks.SMOOTH_RESIN_SLAB, "Smooth Resin Slab");
        addBlock(builder, AlienResinBlocks.SMOOTH_RESIN_STAIRS, "Smooth Resin Stairs");
        addBlock(builder, AlienResinBlocks.SMOOTH_RESIN_WALL, "Smooth Resin Wall");

        addBlock(builder, AlienChitinBlocks.ABERRANT_CHITIN_BLOCK, "Block of Aberrant Chitin");
        addBlock(builder, AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_SLAB, "Aberrant Chitin Slab");
        addBlock(builder, AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_STAIRS, "Aberrant Chitin Stairs");
        addBlock(builder, AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_WALL, "Aberrant Chitin Wall");
        addBlock(builder, AlienChitinBlocks.ABERRANT_CHITIN_BRICKS, "Aberrant Chitin Bricks");
        addBlock(builder, AlienChitinBlocks.ABERRANT_CHITIN_BRICK_SLAB, "Aberrant Chitin Brick Slab");
        addBlock(builder, AlienChitinBlocks.ABERRANT_CHITIN_BRICK_STAIRS, "Aberrant Chitin Brick Stairs");
        addBlock(builder, AlienChitinBlocks.ABERRANT_CHITIN_BRICK_WALL, "Aberrant Chitin Brick Wall");
        addBlock(builder, AlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS, "Chiseled Aberrant Chitin Bricks");
        addBlock(builder, AlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO, "Chiseled Aberrant Chitin Bricks (Embryo)");
        addBlock(builder, AlienChitinBlocks.POLISHED_ABERRANT_CHITIN, "Polished Aberrant Chitin");
        addBlock(builder, AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_SLAB, "Polished Aberrant Chitin Slab");
        addBlock(builder, AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_STAIRS, "Polished Aberrant Chitin Stairs");
        addBlock(builder, AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_WALL, "Polished Aberrant Chitin Wall");

        addBlock(builder, AlienChitinBlocks.CHITIN_BLOCK, "Block of Chitin");
        addBlock(builder, AlienChitinBlocks.CHITIN_BLOCK_SLAB, "Chitin Slab");
        addBlock(builder, AlienChitinBlocks.CHITIN_BLOCK_STAIRS, "Chitin Stairs");
        addBlock(builder, AlienChitinBlocks.CHITIN_BLOCK_WALL, "Chitin Wall");
        addBlock(builder, AlienChitinBlocks.CHITIN_BRICKS, "Chitin Bricks");
        addBlock(builder, AlienChitinBlocks.CHITIN_BRICK_SLAB, "Chitin Brick Slab");
        addBlock(builder, AlienChitinBlocks.CHITIN_BRICK_STAIRS, "Chitin Brick Stairs");
        addBlock(builder, AlienChitinBlocks.CHITIN_BRICK_WALL, "Chitin Brick Wall");
        addBlock(builder, AlienChitinBlocks.CHISELED_CHITIN_BRICKS, "Chiseled Chitin Bricks");
        addBlock(builder, AlienChitinBlocks.CHISELED_CHITIN_BRICKS_EMBRYO, "Chiseled Chitin Bricks (Embryo)");
        addBlock(builder, AlienChitinBlocks.POLISHED_CHITIN, "Polished Chitin");
        addBlock(builder, AlienChitinBlocks.POLISHED_CHITIN_SLAB, "Polished Chitin Slab");
        addBlock(builder, AlienChitinBlocks.POLISHED_CHITIN_STAIRS, "Polished Chitin Stairs");
        addBlock(builder, AlienChitinBlocks.POLISHED_CHITIN_WALL, "Polished Chitin Wall");

        addBlock(builder, AlienChitinBlocks.NETHER_CHITIN_BLOCK, "Block of Nether Chitin");
        addBlock(builder, AlienChitinBlocks.NETHER_CHITIN_BLOCK_SLAB, "Nether Chitin Slab");
        addBlock(builder, AlienChitinBlocks.NETHER_CHITIN_BLOCK_STAIRS, "Nether Chitin Stairs");
        addBlock(builder, AlienChitinBlocks.NETHER_CHITIN_BLOCK_WALL, "Nether Chitin Wall");
        addBlock(builder, AlienChitinBlocks.NETHER_CHITIN_BRICKS, "Nether Chitin Bricks");
        addBlock(builder, AlienChitinBlocks.NETHER_CHITIN_BRICK_SLAB, "Nether Chitin Brick Slab");
        addBlock(builder, AlienChitinBlocks.NETHER_CHITIN_BRICK_STAIRS, "Nether Chitin Brick Stairs");
        addBlock(builder, AlienChitinBlocks.NETHER_CHITIN_BRICK_WALL, "Nether Chitin Brick Wall");
        addBlock(builder, AlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS, "Chiseled Nether Chitin Bricks");
        addBlock(builder, AlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS_EMBRYO, "Chiseled Nether Chitin Bricks (Embryo)");
        addBlock(builder, AlienChitinBlocks.POLISHED_NETHER_CHITIN, "Polished Nether Chitin");
        addBlock(builder, AlienChitinBlocks.POLISHED_NETHER_CHITIN_SLAB, "Polished Nether Chitin Slab");
        addBlock(builder, AlienChitinBlocks.POLISHED_NETHER_CHITIN_STAIRS, "Polished Nether Chitin Stairs");
        addBlock(builder, AlienChitinBlocks.POLISHED_NETHER_CHITIN_WALL, "Polished Nether Chitin Wall");

        // FIXME:
        // AVPRegistryValidation.throwIfMissingEntries(
        // Alien.MOD.getAllHolders(BuiltInRegistries.BLOCK),
        // TOUCHED_ENTRIES::contains,
        // Block::getDescriptionId,
        // "Block translation did not complete successfully - there are unhandled blocks that need to be handled."
        // );
    };

    private static void addBlock(
        FabricLanguageProvider.TranslationBuilder translationBuilder,
        Supplier<? extends Block> blockSupplier,
        String value
    ) {
        addBlock(translationBuilder, blockSupplier.get(), value);
    }

    private static void addBlock(FabricLanguageProvider.TranslationBuilder translationBuilder, Block block, String value) {
        TOUCHED_ENTRIES.add(block);
        translationBuilder.add(block, value);
    }
}

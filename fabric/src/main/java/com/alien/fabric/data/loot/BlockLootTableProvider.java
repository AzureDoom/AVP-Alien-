package com.alien.fabric.data.loot;

import com.alien.common.registry.init.block.AlienBlocks;
import com.alien.common.registry.init.block.AlienChitinBlocks;
import com.alien.common.registry.init.block.AlienResinBlocks;
import com.alien.common.registry.init.item.AlienItems;
import com.avp.common.registry.AVPRegistryValidation;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockLootTableProvider extends FabricBlockLootTableProvider {

    private static final Set<Block> TOUCHED_ENTRIES = new HashSet<>();

    public BlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        generateOtherDrops();
        generateSelfDrops();
        generateSlabDrops();

        AVPRegistryValidation.throwIfMissingEntries(
            AlienBlocks.getAll(),
            TOUCHED_ENTRIES::contains,
            Block::getDescriptionId,
            "Block loot table generation did not complete successfully - there are unhandled blocks that need to be handled."
        );
    }

    private void generateOtherDrops() {
        dropOther(AlienResinBlocks.ABERRANT_RESIN_NODE, AlienResinBlocks.ABERRANT_RESIN);
        dropOther(AlienResinBlocks.ABERRANT_RESIN_VEIN, AlienItems.ABERRANT_RESIN_BALL);
        dropOther(AlienResinBlocks.ABERRANT_RESIN_VENT, AlienResinBlocks.ABERRANT_RESIN);
        dropOther(AlienResinBlocks.ABERRANT_RESIN_WEB, AlienItems.ABERRANT_RESIN_BALL);
        dropOther(AlienResinBlocks.IRRADIATED_RESIN_NODE, AlienResinBlocks.IRRADIATED_RESIN);
        dropOther(AlienResinBlocks.IRRADIATED_RESIN_VEIN, AlienItems.IRRADIATED_RESIN_BALL);
        dropOther(AlienResinBlocks.IRRADIATED_RESIN_VENT, AlienResinBlocks.IRRADIATED_RESIN);
        dropOther(AlienResinBlocks.IRRADIATED_RESIN_WEB, AlienItems.IRRADIATED_RESIN_BALL);
        dropOther(AlienResinBlocks.NETHER_RESIN_NODE, AlienResinBlocks.NETHER_RESIN);
        dropOther(AlienResinBlocks.NETHER_RESIN_VEIN, AlienItems.NETHER_RESIN_BALL);
        dropOther(AlienResinBlocks.NETHER_RESIN_VENT, AlienResinBlocks.NETHER_RESIN);
        dropOther(AlienResinBlocks.NETHER_RESIN_WEB, AlienItems.NETHER_RESIN_BALL);
        dropOther(AlienResinBlocks.RESIN_NODE, AlienResinBlocks.RESIN);
        dropOther(AlienResinBlocks.RESIN_VEIN, AlienItems.RESIN_BALL);
        dropOther(AlienResinBlocks.RESIN_VENT, AlienResinBlocks.RESIN);
        dropOther(AlienResinBlocks.RESIN_WEB, AlienItems.RESIN_BALL);
    }

    private void generateSelfDrops() {
        dropSelf(AlienResinBlocks.ABERRANT_RESIN);
        dropSelf(AlienResinBlocks.ABERRANT_RESIN_BRICKS);
        dropSelf(AlienResinBlocks.ABERRANT_RESIN_BRICK_STAIRS);
        dropSelf(AlienResinBlocks.ABERRANT_RESIN_BRICK_WALL);
        dropSelf(AlienResinBlocks.ABERRANT_RESIN_STAIRS);
        dropSelf(AlienChitinBlocks.ABERRANT_CHITIN_BLOCK);
        dropSelf(AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_STAIRS);
        dropSelf(AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_WALL);
        dropSelf(AlienChitinBlocks.ABERRANT_CHITIN_BRICKS);
        dropSelf(AlienChitinBlocks.ABERRANT_CHITIN_BRICK_STAIRS);
        dropSelf(AlienChitinBlocks.ABERRANT_CHITIN_BRICK_WALL);
        dropSelf(AlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS);
        dropSelf(AlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO);
        dropSelf(AlienChitinBlocks.POLISHED_ABERRANT_CHITIN);
        dropSelf(AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_STAIRS);
        dropSelf(AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_WALL);

        dropSelf(AlienResinBlocks.IRRADIATED_RESIN);
        dropSelf(AlienResinBlocks.IRRADIATED_RESIN_BRICKS);
        dropSelf(AlienResinBlocks.IRRADIATED_RESIN_BRICK_STAIRS);
        dropSelf(AlienResinBlocks.IRRADIATED_RESIN_BRICK_WALL);
        dropSelf(AlienResinBlocks.IRRADIATED_RESIN_STAIRS);

        dropSelf(AlienResinBlocks.NETHER_RESIN);
        dropSelf(AlienResinBlocks.NETHER_RESIN_BRICKS);
        dropSelf(AlienResinBlocks.NETHER_RESIN_BRICK_STAIRS);
        dropSelf(AlienResinBlocks.NETHER_RESIN_BRICK_WALL);
        dropSelf(AlienResinBlocks.NETHER_RESIN_STAIRS);
        dropSelf(AlienChitinBlocks.NETHER_CHITIN_BLOCK);
        dropSelf(AlienChitinBlocks.NETHER_CHITIN_BLOCK_STAIRS);
        dropSelf(AlienChitinBlocks.NETHER_CHITIN_BLOCK_WALL);
        dropSelf(AlienChitinBlocks.NETHER_CHITIN_BRICKS);
        dropSelf(AlienChitinBlocks.NETHER_CHITIN_BRICK_STAIRS);
        dropSelf(AlienChitinBlocks.NETHER_CHITIN_BRICK_WALL);
        dropSelf(AlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS);
        dropSelf(AlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS_EMBRYO);
        dropSelf(AlienChitinBlocks.POLISHED_NETHER_CHITIN);
        dropSelf(AlienChitinBlocks.POLISHED_NETHER_CHITIN_STAIRS);
        dropSelf(AlienChitinBlocks.POLISHED_NETHER_CHITIN_WALL);

        dropSelf(AlienResinBlocks.RESIN);
        dropSelf(AlienResinBlocks.RESIN_BRICKS);
        dropSelf(AlienResinBlocks.RESIN_BRICK_STAIRS);
        dropSelf(AlienResinBlocks.RESIN_BRICK_WALL);
        dropSelf(AlienResinBlocks.RESIN_STAIRS);
        dropSelf(AlienChitinBlocks.CHITIN_BLOCK);
        dropSelf(AlienChitinBlocks.CHITIN_BLOCK_STAIRS);
        dropSelf(AlienChitinBlocks.CHITIN_BLOCK_WALL);
        dropSelf(AlienChitinBlocks.CHITIN_BRICKS);
        dropSelf(AlienChitinBlocks.CHITIN_BRICK_STAIRS);
        dropSelf(AlienChitinBlocks.CHITIN_BRICK_WALL);
        dropSelf(AlienChitinBlocks.CHISELED_CHITIN_BRICKS);
        dropSelf(AlienChitinBlocks.CHISELED_CHITIN_BRICKS_EMBRYO);
        dropSelf(AlienChitinBlocks.POLISHED_CHITIN);
        dropSelf(AlienChitinBlocks.POLISHED_CHITIN_STAIRS);
        dropSelf(AlienChitinBlocks.POLISHED_CHITIN_WALL);

        dropSelf(AlienResinBlocks.RIBBED_ABERRANT_RESIN);
        dropSelf(AlienResinBlocks.RIBBED_IRRADIATED_RESIN);
        dropSelf(AlienResinBlocks.RIBBED_NETHER_RESIN);
        dropSelf(AlienResinBlocks.RIBBED_RESIN);

        dropSelf(AlienResinBlocks.SMOOTH_ABERRANT_RESIN);
        dropSelf(AlienResinBlocks.SMOOTH_ABERRANT_RESIN_STAIRS);
        dropSelf(AlienResinBlocks.SMOOTH_ABERRANT_RESIN_WALL);
        dropSelf(AlienResinBlocks.SMOOTH_IRRADIATED_RESIN);
        dropSelf(AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_STAIRS);
        dropSelf(AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_WALL);
        dropSelf(AlienResinBlocks.SMOOTH_NETHER_RESIN);
        dropSelf(AlienResinBlocks.SMOOTH_NETHER_RESIN_STAIRS);
        dropSelf(AlienResinBlocks.SMOOTH_NETHER_RESIN_WALL);
        dropSelf(AlienResinBlocks.SMOOTH_RESIN);
        dropSelf(AlienResinBlocks.SMOOTH_RESIN_STAIRS);
        dropSelf(AlienResinBlocks.SMOOTH_RESIN_WALL);

        dropSelf(AlienBlocks.ROYAL_JELLY_BLOCK);
    }

    private void generateSlabDrops() {
        dropSlab(AlienResinBlocks.ABERRANT_RESIN_SLAB);
        dropSlab(AlienResinBlocks.ABERRANT_RESIN_BRICK_SLAB);
        dropSlab(AlienResinBlocks.SMOOTH_ABERRANT_RESIN_SLAB);

        dropSlab(AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_SLAB);
        dropSlab(AlienChitinBlocks.ABERRANT_CHITIN_BRICK_SLAB);
        dropSlab(AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_SLAB);

        dropSlab(AlienResinBlocks.IRRADIATED_RESIN_SLAB);
        dropSlab(AlienResinBlocks.IRRADIATED_RESIN_BRICK_SLAB);
        dropSlab(AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_SLAB);

        dropSlab(AlienResinBlocks.NETHER_RESIN_SLAB);
        dropSlab(AlienResinBlocks.NETHER_RESIN_BRICK_SLAB);
        dropSlab(AlienResinBlocks.SMOOTH_NETHER_RESIN_SLAB);

        dropSlab(AlienChitinBlocks.NETHER_CHITIN_BLOCK_SLAB);
        dropSlab(AlienChitinBlocks.NETHER_CHITIN_BRICK_SLAB);
        dropSlab(AlienChitinBlocks.POLISHED_NETHER_CHITIN_SLAB);

        dropSlab(AlienResinBlocks.RESIN_SLAB);
        dropSlab(AlienResinBlocks.RESIN_BRICK_SLAB);
        dropSlab(AlienResinBlocks.SMOOTH_RESIN_SLAB);

        dropSlab(AlienChitinBlocks.CHITIN_BLOCK_SLAB);
        dropSlab(AlienChitinBlocks.CHITIN_BRICK_SLAB);
        dropSlab(AlienChitinBlocks.POLISHED_CHITIN_SLAB);
    }

    public void add(Supplier<? extends Block> blockSupplier, Function<Block, LootTable.Builder> factory) {
        var block = blockSupplier.get();
        add(block, factory);
        TOUCHED_ENTRIES.add(block);
    }

    public void dropOther(Supplier<? extends Block> blockSupplier, Supplier<? extends ItemLike> itemLikeSupplier) {
        var block = blockSupplier.get();
        dropOther(block, itemLikeSupplier.get());
        TOUCHED_ENTRIES.add(block);
    }

    public void dropSelf(Supplier<? extends Block> blockSupplier) {
        var block = blockSupplier.get();
        dropSelf(block);
        TOUCHED_ENTRIES.add(block);
    }

    public void dropSlab(Supplier<? extends Block> blockSupplier) {
        var block = blockSupplier.get();
        add(block, createSlabItemTable(block));
        TOUCHED_ENTRIES.add(block);
    }
}

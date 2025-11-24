package com.alien.fabric.data.model;

import com.alien.common.registry.init.block.AlienBlocks;
import com.alien.common.registry.init.block.AlienChitinBlocks;
import com.alien.common.registry.init.block.AlienResinBlocks;
import com.avp.fabric.data.model.generator.MultiFaceGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockModelProvider extends FabricModelProvider {

    public BlockModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.createRotatedVariantBlock(AlienResinBlocks.IRRADIATED_RESIN.get());
        createSlab(generators, AlienResinBlocks.IRRADIATED_RESIN.get(), AlienResinBlocks.IRRADIATED_RESIN_SLAB.get());
        createStairs(generators, AlienResinBlocks.IRRADIATED_RESIN.get(), AlienResinBlocks.IRRADIATED_RESIN_STAIRS.get());
        generators.createRotatedVariantBlock(AlienResinBlocks.IRRADIATED_RESIN_NODE.get());
        MultiFaceGenerator.generate(generators, AlienResinBlocks.IRRADIATED_RESIN_VEIN.get());
        generators.createCrossBlock(AlienResinBlocks.IRRADIATED_RESIN_WEB.get(), BlockModelGenerators.TintState.NOT_TINTED);

        generators.createRotatedVariantBlock(AlienResinBlocks.ABERRANT_RESIN.get());
        createSlab(generators, AlienResinBlocks.ABERRANT_RESIN.get(), AlienResinBlocks.ABERRANT_RESIN_SLAB.get());
        createStairs(generators, AlienResinBlocks.ABERRANT_RESIN.get(), AlienResinBlocks.ABERRANT_RESIN_STAIRS.get());
        generators.createRotatedVariantBlock(AlienResinBlocks.ABERRANT_RESIN_NODE.get());
        MultiFaceGenerator.generate(generators, AlienResinBlocks.ABERRANT_RESIN_VEIN.get());
        generators.createCrossBlock(AlienResinBlocks.ABERRANT_RESIN_WEB.get(), BlockModelGenerators.TintState.NOT_TINTED);

        generators.family(AlienChitinBlocks.ABERRANT_CHITIN_BLOCK.get())
            .slab(AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_SLAB.get())
            .stairs(AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_STAIRS.get())
            .wall(AlienChitinBlocks.ABERRANT_CHITIN_BLOCK_WALL.get());
        generators.family(AlienChitinBlocks.ABERRANT_CHITIN_BRICKS.get())
            .slab(AlienChitinBlocks.ABERRANT_CHITIN_BRICK_SLAB.get())
            .stairs(AlienChitinBlocks.ABERRANT_CHITIN_BRICK_STAIRS.get())
            .wall(AlienChitinBlocks.ABERRANT_CHITIN_BRICK_WALL.get());
        createBottomTopBlock(
            generators,
            AlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS.get(),
            AlienChitinBlocks.POLISHED_ABERRANT_CHITIN.get()
        );
        createBottomTopBlock(
            generators,
            AlienChitinBlocks.CHISELED_ABERRANT_CHITIN_BRICKS_EMBRYO.get(),
            AlienChitinBlocks.POLISHED_ABERRANT_CHITIN.get()
        );
        generators.family(AlienChitinBlocks.POLISHED_ABERRANT_CHITIN.get())
            .slab(AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_SLAB.get())
            .stairs(AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_STAIRS.get())
            .wall(AlienChitinBlocks.POLISHED_ABERRANT_CHITIN_WALL.get());

        generators.createRotatedVariantBlock(AlienResinBlocks.NETHER_RESIN.get());
        createSlab(generators, AlienResinBlocks.NETHER_RESIN.get(), AlienResinBlocks.NETHER_RESIN_SLAB.get());
        createStairs(generators, AlienResinBlocks.NETHER_RESIN.get(), AlienResinBlocks.NETHER_RESIN_STAIRS.get());
        generators.createRotatedVariantBlock(AlienResinBlocks.NETHER_RESIN_NODE.get());
        MultiFaceGenerator.generate(generators, AlienResinBlocks.NETHER_RESIN_VEIN.get());
        generators.createCrossBlock(AlienResinBlocks.NETHER_RESIN_WEB.get(), BlockModelGenerators.TintState.NOT_TINTED);

        generators.family(AlienChitinBlocks.NETHER_CHITIN_BLOCK.get())
            .slab(AlienChitinBlocks.NETHER_CHITIN_BLOCK_SLAB.get())
            .stairs(AlienChitinBlocks.NETHER_CHITIN_BLOCK_STAIRS.get())
            .wall(AlienChitinBlocks.NETHER_CHITIN_BLOCK_WALL.get());
        generators.family(AlienChitinBlocks.NETHER_CHITIN_BRICKS.get())
            .slab(AlienChitinBlocks.NETHER_CHITIN_BRICK_SLAB.get())
            .stairs(AlienChitinBlocks.NETHER_CHITIN_BRICK_STAIRS.get())
            .wall(AlienChitinBlocks.NETHER_CHITIN_BRICK_WALL.get());
        createBottomTopBlock(
            generators,
            AlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS.get(),
            AlienChitinBlocks.POLISHED_NETHER_CHITIN.get()
        );
        createBottomTopBlock(
            generators,
            AlienChitinBlocks.CHISELED_NETHER_CHITIN_BRICKS_EMBRYO.get(),
            AlienChitinBlocks.POLISHED_NETHER_CHITIN.get()
        );
        generators.family(AlienChitinBlocks.POLISHED_NETHER_CHITIN.get())
            .slab(AlienChitinBlocks.POLISHED_NETHER_CHITIN_SLAB.get())
            .stairs(AlienChitinBlocks.POLISHED_NETHER_CHITIN_STAIRS.get())
            .wall(AlienChitinBlocks.POLISHED_NETHER_CHITIN_WALL.get());

        generators.createRotatedVariantBlock(AlienResinBlocks.RESIN.get());
        createSlab(generators, AlienResinBlocks.RESIN.get(), AlienResinBlocks.RESIN_SLAB.get());
        createStairs(generators, AlienResinBlocks.RESIN.get(), AlienResinBlocks.RESIN_STAIRS.get());
        generators.createRotatedVariantBlock(AlienResinBlocks.RESIN_NODE.get());
        MultiFaceGenerator.generate(generators, AlienResinBlocks.RESIN_VEIN.get());
        generators.createCrossBlock(AlienResinBlocks.RESIN_WEB.get(), BlockModelGenerators.TintState.NOT_TINTED);

        generators.family(AlienChitinBlocks.CHITIN_BLOCK.get())
            .slab(AlienChitinBlocks.CHITIN_BLOCK_SLAB.get())
            .stairs(AlienChitinBlocks.CHITIN_BLOCK_STAIRS.get())
            .wall(AlienChitinBlocks.CHITIN_BLOCK_WALL.get());
        generators.family(AlienChitinBlocks.CHITIN_BRICKS.get())
            .slab(AlienChitinBlocks.CHITIN_BRICK_SLAB.get())
            .stairs(AlienChitinBlocks.CHITIN_BRICK_STAIRS.get())
            .wall(AlienChitinBlocks.CHITIN_BRICK_WALL.get());
        createBottomTopBlock(generators, AlienChitinBlocks.CHISELED_CHITIN_BRICKS.get(), AlienChitinBlocks.POLISHED_CHITIN.get());
        createBottomTopBlock(generators, AlienChitinBlocks.CHISELED_CHITIN_BRICKS_EMBRYO.get(), AlienChitinBlocks.POLISHED_CHITIN.get());
        generators.family(AlienChitinBlocks.POLISHED_CHITIN.get())
            .slab(AlienChitinBlocks.POLISHED_CHITIN_SLAB.get())
            .stairs(AlienChitinBlocks.POLISHED_CHITIN_STAIRS.get())
            .wall(AlienChitinBlocks.POLISHED_CHITIN_WALL.get());

        generators.createTrivialCube(AlienBlocks.ROYAL_JELLY_BLOCK.get());

        generators.family(AlienResinBlocks.ABERRANT_RESIN_BRICKS.get())
            .slab(AlienResinBlocks.ABERRANT_RESIN_BRICK_SLAB.get())
            .stairs(AlienResinBlocks.ABERRANT_RESIN_BRICK_STAIRS.get())
            .wall(AlienResinBlocks.ABERRANT_RESIN_BRICK_WALL.get());
        generators.createTrivialCube(AlienResinBlocks.ABERRANT_RESIN_VENT.get());

        generators.family(AlienResinBlocks.IRRADIATED_RESIN_BRICKS.get())
            .slab(AlienResinBlocks.IRRADIATED_RESIN_BRICK_SLAB.get())
            .stairs(AlienResinBlocks.IRRADIATED_RESIN_BRICK_STAIRS.get())
            .wall(AlienResinBlocks.IRRADIATED_RESIN_BRICK_WALL.get());
        generators.createTrivialCube(AlienResinBlocks.IRRADIATED_RESIN_VENT.get());

        generators.family(AlienResinBlocks.NETHER_RESIN_BRICKS.get())
            .slab(AlienResinBlocks.NETHER_RESIN_BRICK_SLAB.get())
            .stairs(AlienResinBlocks.NETHER_RESIN_BRICK_STAIRS.get())
            .wall(AlienResinBlocks.NETHER_RESIN_BRICK_WALL.get());
        generators.createTrivialCube(AlienResinBlocks.NETHER_RESIN_VENT.get());

        generators.family(AlienResinBlocks.RESIN_BRICKS.get())
            .slab(AlienResinBlocks.RESIN_BRICK_SLAB.get())
            .stairs(AlienResinBlocks.RESIN_BRICK_STAIRS.get())
            .wall(AlienResinBlocks.RESIN_BRICK_WALL.get());
        generators.createTrivialCube(AlienResinBlocks.RESIN_VENT.get());

        createRotatedPillar(generators, AlienResinBlocks.RIBBED_ABERRANT_RESIN.get(), TexturedModel.CUBE);
        createRotatedPillar(generators, AlienResinBlocks.RIBBED_IRRADIATED_RESIN.get(), TexturedModel.CUBE);
        createRotatedPillar(generators, AlienResinBlocks.RIBBED_NETHER_RESIN.get(), TexturedModel.CUBE);
        createRotatedPillar(generators, AlienResinBlocks.RIBBED_RESIN.get(), TexturedModel.CUBE);

        generators.family(AlienResinBlocks.SMOOTH_ABERRANT_RESIN.get())
            .slab(AlienResinBlocks.SMOOTH_ABERRANT_RESIN_SLAB.get())
            .stairs(AlienResinBlocks.SMOOTH_ABERRANT_RESIN_STAIRS.get())
            .wall(AlienResinBlocks.SMOOTH_ABERRANT_RESIN_WALL.get());

        generators.family(AlienResinBlocks.SMOOTH_IRRADIATED_RESIN.get())
            .slab(AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_SLAB.get())
            .stairs(AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_STAIRS.get())
            .wall(AlienResinBlocks.SMOOTH_IRRADIATED_RESIN_WALL.get());

        generators.family(AlienResinBlocks.SMOOTH_NETHER_RESIN.get())
            .slab(AlienResinBlocks.SMOOTH_NETHER_RESIN_SLAB.get())
            .stairs(AlienResinBlocks.SMOOTH_NETHER_RESIN_STAIRS.get())
            .wall(AlienResinBlocks.SMOOTH_NETHER_RESIN_WALL.get());

        generators.family(AlienResinBlocks.SMOOTH_RESIN.get())
            .slab(AlienResinBlocks.SMOOTH_RESIN_SLAB.get())
            .stairs(AlienResinBlocks.SMOOTH_RESIN_STAIRS.get())
            .wall(AlienResinBlocks.SMOOTH_RESIN_WALL.get());
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {}

    @Override
    public @NotNull String getName() {
        return "Block Model Definitions";
    }

    private void createBottomTopBlock(BlockModelGenerators generators, Block block, Block yBlock) {
        var yResourceLocation = ModelLocationUtils.getModelLocation(yBlock);
        var chiseledTextureResourceLocation = BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/").withSuffix("_side");

        var textureMapping = TextureMapping.cube(block)
            .put(TextureSlot.BOTTOM, yResourceLocation)
            .put(TextureSlot.SIDE, chiseledTextureResourceLocation)
            .put(TextureSlot.TOP, yResourceLocation);

        generators.createTrivialBlock(block, textureMapping, ModelTemplates.CUBE_BOTTOM_TOP);
    }

    private void createRotatedPillar(BlockModelGenerators generators, Block rotatedPillarBlock, TexturedModel.Provider modelProvider) {
        var resourceLocation = modelProvider.create(rotatedPillarBlock, generators.modelOutput);
        generators.blockStateOutput.accept(
            BlockModelGenerators.createRotatedPillarWithHorizontalVariant(rotatedPillarBlock, resourceLocation, resourceLocation)
        );
    }

    private void createSlab(
        BlockModelGenerators generators,
        Block baseBlock,
        Block slabBlock
    ) {
        var resourceLocation = ModelLocationUtils.getModelLocation(baseBlock);
        var textureMapping = TextureMapping.cube(baseBlock)
            .put(TextureSlot.BOTTOM, resourceLocation)
            .put(TextureSlot.TOP, resourceLocation);

        var bottom = ModelTemplates.SLAB_BOTTOM.create(slabBlock, textureMapping, generators.modelOutput);
        var top = ModelTemplates.SLAB_TOP.create(slabBlock, textureMapping, generators.modelOutput);

        generators.blockStateOutput.accept(
            BlockModelGenerators.createSlab(slabBlock, bottom, top, resourceLocation)
        );
    }

    private void createStairs(BlockModelGenerators generators, Block baseBlock, Block stairsBlock) {
        var resourceLocation = ModelLocationUtils.getModelLocation(baseBlock);

        var textureMapping = TextureMapping.cube(baseBlock)
            .put(TextureSlot.BOTTOM, resourceLocation)
            .put(TextureSlot.TOP, resourceLocation);

        var innerResourceLocation = ModelTemplates.STAIRS_INNER.create(stairsBlock, textureMapping, generators.modelOutput);
        var straightResourceLocation = ModelTemplates.STAIRS_STRAIGHT.create(stairsBlock, textureMapping, generators.modelOutput);
        var outerResourceLocation = ModelTemplates.STAIRS_OUTER.create(stairsBlock, textureMapping, generators.modelOutput);

        generators.blockStateOutput.accept(
            BlockModelGenerators.createStairs(stairsBlock, innerResourceLocation, straightResourceLocation, outerResourceLocation)
        );
    }
}

package com.alien.fabric.data.recipe.impl;

import com.alien.common.registry.init.item.AlienArmorItems;
import com.alien.common.registry.init.item.AlienItems;
import com.alien.fabric.compatibility.AVPHumanFabric;
import com.blib.fabric.data.recipe.RecipeTemplates;
import com.blib.fabric.data.recipe.builder.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class ArmorRecipeProvider {

    public static void provide(RecipeBuilder builder) {
        createPlatedChitinArmorSetRecipes(builder);
        createPlatedNetherChitinArmorSetRecipes(builder);
        createPlatedAberrantChitinArmorSetRecipes(builder.withCondition(AVPHumanFabric.IS_LOADED));
        // TODO: Re-implement these at some point in the future.
        // createPlatedIrradiatedChitinArmorSetRecipes(builder);

        createStandardArmorSetRecipes(
            builder,
            AlienItems.CHITIN.get(),
            AlienArmorItems.CHITIN_HELMET.get(),
            AlienArmorItems.CHITIN_CHESTPLATE.get(),
            AlienArmorItems.CHITIN_LEGGINGS.get(),
            AlienArmorItems.CHITIN_BOOTS.get()
        );
        createStandardArmorSetRecipes(
            builder,
            AlienItems.NETHER_CHITIN.get(),
            AlienArmorItems.NETHER_CHITIN_HELMET.get(),
            AlienArmorItems.NETHER_CHITIN_CHESTPLATE.get(),
            AlienArmorItems.NETHER_CHITIN_LEGGINGS.get(),
            AlienArmorItems.NETHER_CHITIN_BOOTS.get()
        );
        createStandardArmorSetRecipes(
            builder.withCondition(AVPHumanFabric.IS_LOADED),
            AlienItems.ABERRANT_CHITIN.get(),
            AlienArmorItems.ABERRANT_CHITIN_HELMET.get(),
            AlienArmorItems.ABERRANT_CHITIN_CHESTPLATE.get(),
            AlienArmorItems.ABERRANT_CHITIN_LEGGINGS.get(),
            AlienArmorItems.ABERRANT_CHITIN_BOOTS.get()
        );
        // TODO: Re-implement these at some point in the future.
        // createStandardArmorSetRecipes(
        // builder,
        // AVPItems.IRRADIATED_CHITIN,
        // ArmorItems.IRRADIATED_CHITIN_HELMET,
        // ArmorItems.IRRADIATED_CHITIN_CHESTPLATE,
        // ArmorItems.IRRADIATED_CHITIN_LEGGINGS,
        // ArmorItems.IRRADIATED_CHITIN_BOOTS
        // );
    }

    private static void createPlatedAberrantChitinArmorSetRecipes(RecipeBuilder builder) {
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.ABERRANT_CHITIN_HELMET)
            .requires(1, AlienItems.PLATED_ABERRANT_CHITIN)
            .into(1, AlienArmorItems.PLATED_ABERRANT_CHITIN_HELMET);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.ABERRANT_CHITIN_CHESTPLATE)
            .requires(1, AlienItems.PLATED_ABERRANT_CHITIN)
            .into(1, AlienArmorItems.PLATED_ABERRANT_CHITIN_CHESTPLATE);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.ABERRANT_CHITIN_LEGGINGS)
            .requires(1, AlienItems.PLATED_ABERRANT_CHITIN)
            .into(1, AlienArmorItems.PLATED_ABERRANT_CHITIN_LEGGINGS);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.ABERRANT_CHITIN_BOOTS)
            .requires(1, AlienItems.PLATED_ABERRANT_CHITIN)
            .into(1, AlienArmorItems.PLATED_ABERRANT_CHITIN_BOOTS);
    }

    private static void createPlatedIrradiatedChitinArmorSetRecipes(RecipeBuilder builder) {
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.IRRADIATED_CHITIN_HELMET)
            .requires(1, AlienItems.PLATED_IRRADIATED_CHITIN)
            .into(1, AlienArmorItems.PLATED_IRRADIATED_CHITIN_HELMET);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.IRRADIATED_CHITIN_CHESTPLATE)
            .requires(1, AlienItems.PLATED_IRRADIATED_CHITIN)
            .into(1, AlienArmorItems.PLATED_IRRADIATED_CHITIN_CHESTPLATE);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.IRRADIATED_CHITIN_LEGGINGS)
            .requires(1, AlienItems.PLATED_IRRADIATED_CHITIN)
            .into(1, AlienArmorItems.PLATED_IRRADIATED_CHITIN_LEGGINGS);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.IRRADIATED_CHITIN_BOOTS)
            .requires(1, AlienItems.PLATED_IRRADIATED_CHITIN)
            .into(1, AlienArmorItems.PLATED_IRRADIATED_CHITIN_BOOTS);
    }

    private static void createPlatedNetherChitinArmorSetRecipes(RecipeBuilder builder) {
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.NETHER_CHITIN_HELMET)
            .requires(1, AlienItems.PLATED_NETHER_CHITIN)
            .into(1, AlienArmorItems.PLATED_NETHER_CHITIN_HELMET);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.NETHER_CHITIN_CHESTPLATE)
            .requires(1, AlienItems.PLATED_NETHER_CHITIN)
            .into(1, AlienArmorItems.PLATED_NETHER_CHITIN_CHESTPLATE);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.NETHER_CHITIN_LEGGINGS)
            .requires(1, AlienItems.PLATED_NETHER_CHITIN)
            .into(1, AlienArmorItems.PLATED_NETHER_CHITIN_LEGGINGS);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.NETHER_CHITIN_BOOTS)
            .requires(1, AlienItems.PLATED_NETHER_CHITIN)
            .into(1, AlienArmorItems.PLATED_NETHER_CHITIN_BOOTS);
    }

    private static void createPlatedChitinArmorSetRecipes(RecipeBuilder builder) {
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.CHITIN_HELMET)
            .requires(1, AlienItems.PLATED_CHITIN)
            .into(1, AlienArmorItems.PLATED_CHITIN_HELMET);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.CHITIN_CHESTPLATE)
            .requires(1, AlienItems.PLATED_CHITIN)
            .into(1, AlienArmorItems.PLATED_CHITIN_CHESTPLATE);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.CHITIN_LEGGINGS)
            .requires(1, AlienItems.PLATED_CHITIN)
            .into(1, AlienArmorItems.PLATED_CHITIN_LEGGINGS);
        builder.shapeless()
            .withCategory(RecipeCategory.COMBAT)
            .requires(1, AlienArmorItems.CHITIN_BOOTS)
            .requires(1, AlienItems.PLATED_CHITIN)
            .into(1, AlienArmorItems.PLATED_CHITIN_BOOTS);
    }

    private static void createStandardArmorSetRecipes(
        RecipeBuilder builder,
        ItemLike base,
        Item helmet,
        Item chestplate,
        Item leggings,
        Item boots
    ) {
        builder.shaped()
            .apply(RecipeTemplates.HELMET.apply(base))
            .into(1, helmet);

        builder.shaped()
            .apply(RecipeTemplates.CHESTPLATE.apply(base))
            .into(1, chestplate);

        builder.shaped()
            .apply(RecipeTemplates.LEGGINGS.apply(base))
            .into(1, leggings);

        builder.shaped()
            .apply(RecipeTemplates.BOOTS.apply(base))
            .into(1, boots);
    }
}

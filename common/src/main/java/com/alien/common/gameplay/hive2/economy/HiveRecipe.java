package com.alien.common.gameplay.hive2.economy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.List;

/**
 * Datapack-defined recipe for producing one unit of {@code outputCaste} in a hive location's reserves. Inputs: a flat
 * resource cost (biomass / royal jelly / scourge jelly) and a list of prerequisite caste counts in the location's local
 * reserves that get consumed.
 * <p>
 * Castes are referenced by entity-type tag — at buy time the recipe resolver picks the concrete entity type for the
 * location's variant via {@code AlienVariantTypes}.
 */
public record HiveRecipe(
    TagKey<EntityType<?>> outputCaste,
    int biomass,
    int royalJelly,
    int scourgeJelly,
    List<InputCaste> inputCastes,
    List<HiveRecipeCondition> conditions
) {

    public static final Codec<HiveRecipe> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            TagKey.codec(Registries.ENTITY_TYPE).fieldOf("output_caste").forGetter(HiveRecipe::outputCaste),
            Codec.INT.optionalFieldOf("biomass", 0).forGetter(HiveRecipe::biomass),
            Codec.INT.optionalFieldOf("royal_jelly", 0).forGetter(HiveRecipe::royalJelly),
            Codec.INT.optionalFieldOf("scourge_jelly", 0).forGetter(HiveRecipe::scourgeJelly),
            InputCaste.CODEC.listOf().optionalFieldOf("input_castes", List.of()).forGetter(HiveRecipe::inputCastes),
            HiveRecipeCondition.CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(HiveRecipe::conditions)
        ).apply(instance, HiveRecipe::new)
    );

    public record InputCaste(
        TagKey<EntityType<?>> caste,
        int count
    ) {

        public static final Codec<InputCaste> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                TagKey.codec(Registries.ENTITY_TYPE).fieldOf("caste").forGetter(InputCaste::caste),
                Codec.INT.fieldOf("count").forGetter(InputCaste::count)
            ).apply(instance, InputCaste::new)
        );
    }
}

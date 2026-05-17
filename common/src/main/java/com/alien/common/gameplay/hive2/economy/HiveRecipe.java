package com.alien.common.gameplay.hive2.economy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.List;

/**
 * Datapack-defined recipe for producing one {@code outputEntity} in a hive location's reserves. Inputs: a flat resource
 * cost (biomass / royal jelly / scourge jelly) and a list of prerequisite entity counts in the location's local
 * reserves that get consumed.
 */
public record HiveRecipe(
    EntityType<?> outputEntity,
    int biomass,
    int royalJelly,
    int scourgeJelly,
    List<InputEntity> inputEntities,
    List<HiveRecipeCondition> conditions
) {

    public static final Codec<HiveRecipe> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("output_entity").forGetter(HiveRecipe::outputEntity),
            Codec.INT.optionalFieldOf("biomass", 0).forGetter(HiveRecipe::biomass),
            Codec.INT.optionalFieldOf("royal_jelly", 0).forGetter(HiveRecipe::royalJelly),
            Codec.INT.optionalFieldOf("scourge_jelly", 0).forGetter(HiveRecipe::scourgeJelly),
            InputEntity.CODEC.listOf().optionalFieldOf("input_entities", List.of()).forGetter(HiveRecipe::inputEntities),
            HiveRecipeCondition.CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(HiveRecipe::conditions)
        ).apply(instance, HiveRecipe::new)
    );

    public record InputEntity(
        EntityType<?> entity,
        int count
    ) {

        public static final Codec<InputEntity> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(InputEntity::entity),
                Codec.INT.fieldOf("count").forGetter(InputEntity::count)
            ).apply(instance, InputEntity::new)
        );
    }
}

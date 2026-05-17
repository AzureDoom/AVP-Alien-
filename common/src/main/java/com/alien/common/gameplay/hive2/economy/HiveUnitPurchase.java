package com.alien.common.gameplay.hive2.economy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.List;

/**
 * Datapack-defined purchase for producing one {@code outputEntity} in a hive location's reserves. Inputs: a flat
 * resource cost (biomass / royal jelly / scourge jelly) and a list of prerequisite entity counts in the location's
 * local reserves that get consumed.
 */
public record HiveUnitPurchase(
    EntityType<?> outputEntity,
    int biomass,
    double populationBiomassCostScale,
    int royalJelly,
    int scourgeJelly,
    List<InputEntity> inputEntities,
    List<HiveUnitPurchaseCondition> conditions
) {

    public static final double DEFAULT_POPULATION_BIOMASS_COST_SCALE = 0.05D;

    public static final Codec<HiveUnitPurchase> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("output_entity").forGetter(HiveUnitPurchase::outputEntity),
            Codec.INT.optionalFieldOf("biomass", 0).forGetter(HiveUnitPurchase::biomass),
            Codec.DOUBLE.optionalFieldOf("population_biomass_cost_scale", DEFAULT_POPULATION_BIOMASS_COST_SCALE)
                .forGetter(HiveUnitPurchase::populationBiomassCostScale),
            Codec.INT.optionalFieldOf("royal_jelly", 0).forGetter(HiveUnitPurchase::royalJelly),
            Codec.INT.optionalFieldOf("scourge_jelly", 0).forGetter(HiveUnitPurchase::scourgeJelly),
            InputEntity.CODEC.listOf()
                .optionalFieldOf("input_entities", List.of())
                .forGetter(HiveUnitPurchase::inputEntities),
            HiveUnitPurchaseCondition.CODEC.listOf()
                .optionalFieldOf("conditions", List.of())
                .forGetter(HiveUnitPurchase::conditions)
        ).apply(instance, HiveUnitPurchase::new)
    );

    public HiveUnitPurchase {
        populationBiomassCostScale = Math.max(0.0D, populationBiomassCostScale);
        inputEntities = List.copyOf(inputEntities);
        conditions = List.copyOf(conditions);
    }

    public HiveUnitPurchase(
        EntityType<?> outputEntity,
        int biomass,
        int royalJelly,
        int scourgeJelly,
        List<InputEntity> inputEntities,
        List<HiveUnitPurchaseCondition> conditions
    ) {
        this(
            outputEntity,
            biomass,
            DEFAULT_POPULATION_BIOMASS_COST_SCALE,
            royalJelly,
            scourgeJelly,
            inputEntities,
            conditions
        );
    }

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

package com.alien.common.gameplay.hive2.economy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

/**
 * Datapack-encoded gating condition for a {@link HiveRecipe}. Evaluated against the candidate
 * {@link com.alien.common.gameplay.hive2.location.HiveLocation} at buy time.
 * <p>
 * Sealed; each implementation is registered with a {@code type} discriminator in the dispatch codec.
 */
public sealed interface HiveRecipeCondition {

    String typeId();

    Codec<HiveRecipeCondition> CODEC = Codec.STRING
        .dispatch("type", HiveRecipeCondition::typeId, HiveRecipeCondition::mapCodecByType);

    private static MapCodec<? extends HiveRecipeCondition> mapCodecByType(String typeId) {
        return switch (typeId) {
            case MinPopulation.TYPE -> MinPopulation.MAP_CODEC;
            case MinEntityCountInLocation.TYPE -> MinEntityCountInLocation.MAP_CODEC;
            case MaxEntityCountInLocation.TYPE -> MaxEntityCountInLocation.MAP_CODEC;
            default -> throw new IllegalArgumentException("Unknown HiveRecipeCondition type: " + typeId);
        };
    }

    /** Total location population (live members + reserves) must be at least {@code value}. */
    record MinPopulation(int value) implements HiveRecipeCondition {

        public static final String TYPE = "min_population";

        public static final MapCodec<MinPopulation> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance
                .group(Codec.INT.fieldOf("value").forGetter(MinPopulation::value))
                .apply(instance, MinPopulation::new)
        );

        @Override
        public String typeId() {
            return TYPE;
        }
    }

    /** Count of {@code entity} members in this location must be at least {@code value}. */
    record MinEntityCountInLocation(
        EntityType<?> entity,
        int value
    ) implements HiveRecipeCondition {

        public static final String TYPE = "min_entity_count_in_location";

        public static final MapCodec<MinEntityCountInLocation> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(MinEntityCountInLocation::entity),
                Codec.INT.fieldOf("value").forGetter(MinEntityCountInLocation::value)
            ).apply(instance, MinEntityCountInLocation::new)
        );

        @Override
        public String typeId() {
            return TYPE;
        }
    }

    /** Count of {@code entity} members in this location must be strictly less than {@code value}. */
    record MaxEntityCountInLocation(
        EntityType<?> entity,
        int value
    ) implements HiveRecipeCondition {

        public static final String TYPE = "max_entity_count_in_location";

        public static final MapCodec<MaxEntityCountInLocation> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(MaxEntityCountInLocation::entity),
                Codec.INT.fieldOf("value").forGetter(MaxEntityCountInLocation::value)
            ).apply(instance, MaxEntityCountInLocation::new)
        );

        @Override
        public String typeId() {
            return TYPE;
        }
    }
}

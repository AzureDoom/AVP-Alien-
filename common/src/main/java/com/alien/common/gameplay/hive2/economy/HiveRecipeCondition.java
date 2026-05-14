package com.alien.common.gameplay.hive2.economy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
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
            case MaxCasteCountInLocation.TYPE -> MaxCasteCountInLocation.MAP_CODEC;
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

    /** Count of {@code caste} members in this location must be strictly less than {@code value}. */
    record MaxCasteCountInLocation(
        TagKey<EntityType<?>> caste,
        int value
    ) implements HiveRecipeCondition {

        public static final String TYPE = "max_caste_count_in_location";

        public static final MapCodec<MaxCasteCountInLocation> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                TagKey.codec(Registries.ENTITY_TYPE).fieldOf("caste").forGetter(MaxCasteCountInLocation::caste),
                Codec.INT.fieldOf("value").forGetter(MaxCasteCountInLocation::value)
            ).apply(instance, MaxCasteCountInLocation::new)
        );

        @Override
        public String typeId() {
            return TYPE;
        }
    }
}

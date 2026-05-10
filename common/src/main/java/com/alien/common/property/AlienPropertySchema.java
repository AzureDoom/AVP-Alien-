package com.alien.common.property;

import com.blib.api.common.property.v1.BLibPropertySchema;

public class AlienPropertySchema {

    static final BLibPropertySchema SCHEMA = BLibPropertySchema.builder()
        .withPropertyValueAlignment(true)
        .addComment("The damage (in half-hearts) that acid deals to entities.")
        .addProperty(AlienProperties.Entities.Acid.ATTACK_DAMAGE, 1F)
        .addBlankLine()
        .addComment("The minimum distance between natural queen spawns in chunks.")
        .addProperty(AlienProperties.Hive.MINIMUM_DISTANCE_BETWEEN_NATURAL_QUEEN_SPAWNS_IN_CHUNKS, 16)
        .addComment("Determines if the screen should darken when the hive boss bar appears.")
        .addProperty(AlienProperties.Hive.DARKEN_SCREEN, true)
        .build();

    private AlienPropertySchema() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.property;

import com.blib.api.common.property.v1.BLibPropertyKey;
import com.blib.api.common.property.v1.serializer.BLibPropertySerializers;

public class AlienProperties {

    public static class Entities {

        private static final BLibPropertyKey.Parent ENTITIES = BLibPropertyKey.parent("entities");

        public static class Acid {

            private static final BLibPropertyKey.Parent ACID = ENTITIES.child("acid");

            public static final BLibPropertyKey.Leaf<Float> ATTACK_DAMAGE = ACID.leaf("attack_damage", BLibPropertySerializers.FLOAT);

        }
    }

    public static class Hive {

        private static final BLibPropertyKey.Parent HIVE = BLibPropertyKey.parent("hive");

        public static final BLibPropertyKey.Leaf<Boolean> DARKEN_SCREEN = HIVE.leaf("darken_screen", BLibPropertySerializers.BOOLEAN);

        public static final BLibPropertyKey.Leaf<Integer> MINIMUM_DISTANCE_BETWEEN_NATURAL_QUEEN_SPAWNS_IN_CHUNKS =
            HIVE.leaf("minimum_distance_between_natural_queen_spawns_in_chunks", BLibPropertySerializers.INT);

        // ---------- Caste-distance ranges (consumed by Phase 6 spawning gate) ----------
        // Each pair is the inclusive [min, max] chunk distance from a hive location's center
        // where this caste can spawn. See HIVE_REDESIGN_03_LOCATIONS.md § 4.

        public static final BLibPropertyKey.Leaf<Integer> QUEEN_RANGE_CHUNKS_MIN =
            HIVE.leaf("queen_range_chunks_min", BLibPropertySerializers.INT);

        public static final BLibPropertyKey.Leaf<Integer> QUEEN_RANGE_CHUNKS_MAX =
            HIVE.leaf("queen_range_chunks_max", BLibPropertySerializers.INT);

        public static final BLibPropertyKey.Leaf<Integer> PRAETORIAN_RANGE_CHUNKS_MIN =
            HIVE.leaf("praetorian_range_chunks_min", BLibPropertySerializers.INT);

        public static final BLibPropertyKey.Leaf<Integer> PRAETORIAN_RANGE_CHUNKS_MAX =
            HIVE.leaf("praetorian_range_chunks_max", BLibPropertySerializers.INT);

        public static final BLibPropertyKey.Leaf<Integer> DRONE_RANGE_CHUNKS_MIN =
            HIVE.leaf("drone_range_chunks_min", BLibPropertySerializers.INT);

        public static final BLibPropertyKey.Leaf<Integer> DRONE_RANGE_CHUNKS_MAX =
            HIVE.leaf("drone_range_chunks_max", BLibPropertySerializers.INT);

        public static final BLibPropertyKey.Leaf<Integer> WARRIOR_RANGE_CHUNKS_MIN =
            HIVE.leaf("warrior_range_chunks_min", BLibPropertySerializers.INT);

        public static final BLibPropertyKey.Leaf<Integer> WARRIOR_RANGE_CHUNKS_MAX =
            HIVE.leaf("warrior_range_chunks_max", BLibPropertySerializers.INT);
    }
}

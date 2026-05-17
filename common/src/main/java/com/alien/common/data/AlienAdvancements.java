package com.alien.common.data;

import com.alien.Alien;
import com.blib.api.common.advancement.v1.BLibAdvancement;

public class AlienAdvancements {

    public static final BLibAdvancement KILL_A_HIVE = create("kill_a_hive");

    public static final BLibAdvancement KILL_A_HARBINGER = create("kill_a_harbinger");

    public static final BLibAdvancement KILL_A_LINEAGE = create("kill_a_lineage");

    public static final BLibAdvancement KILL_A_ROYAL_ALIEN = create("kill_a_royal_alien");

    public static final BLibAdvancement KILL_ALL_ALIENS = create("kill_all_aliens");

    public static final BLibAdvancement KILL_AN_ALIEN = create("kill_an_alien");

    public static final BLibAdvancement DEFEAT_A_RAID = create("defeat_a_raid");

    public static final BLibAdvancement DUAL_VARIANT_RAIDS = create("dual_variant_raids");

    public static final BLibAdvancement REMOVE_EMBRYO_WITH_CHORUS_FRUIT = create("remove_embryo_with_chorus_fruit");

    public static final BLibAdvancement ROOT = create("root");

    public static final BLibAdvancement SHEAR_AN_OVOMORPH = create("shear_an_ovomorph");

    public static final BLibAdvancement WEAR_CHITIN_ARMOR = create("chitin_armor");

    public static final BLibAdvancement WEAR_PLATED_CHITIN_ARMOR = create("plated_chitin_armor");

    private static BLibAdvancement create(String path) {
        return new BLibAdvancement(Alien.MOD_ID, "aliens", path);
    }
}

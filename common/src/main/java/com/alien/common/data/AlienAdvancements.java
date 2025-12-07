package com.alien.common.data;

import com.alien.Alien;
import com.blib.common.data.BLibAdvancementAccess;

public class AlienAdvancements {

    public static final BLibAdvancementAccess KILL_A_HIVE = create("kill_a_hive");

    public static final BLibAdvancementAccess KILL_A_ROYAL_ALIEN = create("kill_a_royal_alien");

    public static final BLibAdvancementAccess KILL_ALL_ALIENS = create("kill_all_aliens");

    public static final BLibAdvancementAccess KILL_AN_ALIEN = create("kill_an_alien");

    public static final BLibAdvancementAccess REMOVE_EMBRYO_WITH_CHORUS_FRUIT = create("remove_embryo_with_chorus_fruit");

    public static final BLibAdvancementAccess ROOT = create("root");

    public static final BLibAdvancementAccess SHEAR_AN_OVOMORPH = create("shear_an_ovomorph");

    public static final BLibAdvancementAccess WEAR_CHITIN_ARMOR = create("chitin_armor");

    public static final BLibAdvancementAccess WEAR_PLATED_CHITIN_ARMOR = create("plated_chitin_armor");

    private static BLibAdvancementAccess create(String path) {
        return new BLibAdvancementAccess(Alien.MOD_ID, "aliens", path);
    }
}

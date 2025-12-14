package com.alien.common.data.fixer.migration;

import com.alien.Alien;
import com.alien.common.data.fixer.migration.impl.AVP_0_3_0_To_Alien_0_1_0;
import com.blib.common.data.fixer.migration.BLibDataMigration;

import java.util.List;

public class AlienDataMigrations {

    private static final List<BLibDataMigration> MIGRATIONS = List.of(
        new AVP_0_3_0_To_Alien_0_1_0()
    );

    public static void initialize() {
        var version = Alien.MOD.getVersion();

        if (version != null) {
            MIGRATIONS.forEach(BLibDataMigration::apply);
        }
    }
}

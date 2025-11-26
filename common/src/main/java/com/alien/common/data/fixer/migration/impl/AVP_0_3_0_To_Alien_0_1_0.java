package com.alien.common.data.fixer.migration.impl;

import com.alien.Alien;
import com.avp.AVPResources;
import com.blib.common.data.fixer.BLibDataFixerRegistry;
import com.blib.common.data.fixer.migration.BLibDataMigration;
import com.lib.common.util.Version;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class AVP_0_3_0_To_Alien_0_1_0 implements BLibDataMigration {

    @Override
    public Version fromVersion() {
        return new Version(0, 1, 9);
    }

    @Override
    public Version toVersion() {
        return new Version(0, 2, 0);
    }

    @Override
    public void apply() {
        BuiltInRegistries.REGISTRY.forEach(AVP_0_3_0_To_Alien_0_1_0::registerMigrationsForRegistry);
    }

    private static void registerMigrationsForRegistry(Registry<?> registry) {
        Alien.MOD.getAllHolders(registry)
            .forEach(holder -> register(new BLibDataFixerRegistry.Entry(registry, AVPResources.location(holder.getPath()), holder.getResourceLocation())));
    }

    public static void register(BLibDataFixerRegistry.Entry entry) {
        BLibDataFixerRegistry.register(entry);
    }
}

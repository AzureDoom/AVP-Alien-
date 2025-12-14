package com.alien.common.data.fixer.migration.impl;

import com.alien.Alien;
import com.alien.AlienResources;
import com.blib.common.data.fixer.BLibDataFixerRegistry;
import com.blib.common.data.fixer.migration.BLibDataMigration;
import com.blib.common.model.Version;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.jetbrains.annotations.NotNull;

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
        registerTemplatePoolDataFixes();
        BuiltInRegistries.REGISTRY.forEach(AVP_0_3_0_To_Alien_0_1_0::registerMigrationsForRegistry);
    }

    private static void registerTemplatePoolDataFixes() {
        register(createResourceKeyEntry(Registries.TEMPLATE_POOL, "badlands_royal_altar_main"));
        register(createResourceKeyEntry(Registries.TEMPLATE_POOL, "badlands_royal_altar_egg"));
        register(createResourceKeyEntry(Registries.TEMPLATE_POOL, "deepslate_royal_altar_main"));
        register(createResourceKeyEntry(Registries.TEMPLATE_POOL, "deepslate_royal_altar_egg"));
        register(createResourceKeyEntry(Registries.TEMPLATE_POOL, "desert_royal_altar_main"));
        register(createResourceKeyEntry(Registries.TEMPLATE_POOL, "desert_royal_altar_egg"));
        register(createResourceKeyEntry(Registries.TEMPLATE_POOL, "jungle_royal_altar_main"));
        register(createResourceKeyEntry(Registries.TEMPLATE_POOL, "jungle_royal_altar_egg"));
        register(createResourceKeyEntry(Registries.TEMPLATE_POOL, "nether_royal_altar_main"));
        register(createResourceKeyEntry(Registries.TEMPLATE_POOL, "nether_royal_altar_egg"));
    }

    private static BLibDataFixerRegistry.Entry.@NotNull Resource createResourceKeyEntry(
        ResourceKey<Registry<StructureTemplatePool>> registry,
        String path
    ) {
        return new BLibDataFixerRegistry.Entry.Resource(registry, createAvpResourceLocation(path), AlienResources.location(path));
    }

    private static void registerMigrationsForRegistry(Registry<?> registry) {
        Alien.MOD.getAllHolders(registry)
            .forEach(
                holder -> register(
                    new BLibDataFixerRegistry.Entry.Direct(registry, createAvpResourceLocation(holder.getPath()), holder.getResourceLocation())
                )
            );
    }

    private static ResourceLocation createAvpResourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath("avp", path);
    }

    public static void register(BLibDataFixerRegistry.Entry entry) {
        BLibDataFixerRegistry.register(entry);
    }
}

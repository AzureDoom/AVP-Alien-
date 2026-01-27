package com.alien.common.data.fixer.migration.impl;

import com.alien.Alien;
import com.alien.AlienResources;
import com.blib.api.common.data_fix.v1.BLibDataFixerRegistry;
import com.blib.api.common.data_fix.v1.BLibDataMigration;
import com.blib.api.common.mod.v1.model.Version;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
        registerStructureDataFixes();
        registerStructureSetDataFixes();
        registerTemplatePoolDataFixes();
        BuiltInRegistries.REGISTRY.forEach(AVP_0_3_0_To_Alien_0_1_0::registerMigrationsForRegistry);
    }

    private static void registerStructureDataFixes() {
        register(createResourceKeyEntry(Registries.STRUCTURE, "badlands_royal_altar"));
        register(createResourceKeyEntry(Registries.STRUCTURE, "deepslate_royal_altar"));
        register(createResourceKeyEntry(Registries.STRUCTURE, "desert_royal_altar"));
        register(createResourceKeyEntry(Registries.STRUCTURE, "jungle_royal_altar"));
        register(createResourceKeyEntry(Registries.STRUCTURE, "nether_royal_altar"));
    }

    private static void registerStructureSetDataFixes() {
        register(createResourceKeyEntry(Registries.STRUCTURE_SET, "badlands_royal_altar"));
        register(createResourceKeyEntry(Registries.STRUCTURE_SET, "deepslate_royal_altar"));
        register(createResourceKeyEntry(Registries.STRUCTURE_SET, "desert_royal_altar"));
        register(createResourceKeyEntry(Registries.STRUCTURE_SET, "jungle_royal_altar"));
        register(createResourceKeyEntry(Registries.STRUCTURE_SET, "nether_royal_altar"));
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

    private static <T> BLibDataFixerRegistry.Entry.@NotNull Resource createResourceKeyEntry(
        ResourceKey<Registry<T>> registry,
        String path
    ) {
        return new BLibDataFixerRegistry.Entry.Resource(registry, createAvpResourceLocation(path), AlienResources.location(path));
    }

    private static void registerMigrationsForRegistry(Registry<?> registry) {
        Alien.MOD.registries()
            .getAllHolders(registry)
            .forEach(
                holder -> register(
                    new BLibDataFixerRegistry.Entry.Direct(
                        registry,
                        createAvpResourceLocation(holder.getPath()),
                        holder.getResourceLocation()
                    )
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

package com.alien.common.registry.tag;

import com.alien.AlienResources;
import com.avp.AVPResources;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class AlienBiomeTags {

    public static final TagKey<Biome> HAS_ALTAR = create("has_altar");

    public static final TagKey<Biome> HAS_BADLANDS_ALTAR = create("has_badlands_altar");

    public static final TagKey<Biome> HAS_DESERT_ALTAR = create("has_desert_altar");

    public static final TagKey<Biome> HAS_DEEPSLATE_ALTAR = create("has_deepslate_altar");

    public static final TagKey<Biome> HAS_JUNGLE_ALTAR = create("has_jungle_altar");

    public static final TagKey<Biome> HAS_NETHER_ALTAR = create("has_nether_altar");

    public static final TagKey<Biome> HAS_XENOMORPHS = create("has_xenomorphs");

    private static TagKey<Biome> create(String name) {
        return TagKey.create(Registries.BIOME, AlienResources.location(name));
    }
}

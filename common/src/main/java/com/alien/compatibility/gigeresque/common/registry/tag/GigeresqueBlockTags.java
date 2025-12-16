package com.alien.compatibility.gigeresque.common.registry.tag;

import com.alien.compatibility.gigeresque.Gigeresque;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class GigeresqueBlockTags {

    public static final TagKey<Block> ACID_RESISTANT = create("acid_resistant");

    private static TagKey<Block> create(String path) {
        return Gigeresque.MOD.resources().createTagKey(Registries.BLOCK, path);
    }
}

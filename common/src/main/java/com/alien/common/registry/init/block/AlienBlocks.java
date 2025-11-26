package com.alien.common.registry.init.block;

import com.alien.Alien;
import com.alien.common.registry.init.block.property.AlienBlockProperties;
import com.avp.common.gameplay.block.property.BlockPropertyBuilder;
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class AlienBlocks {

    public static final BLibRegistry<Block> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.BLOCK);

    public static final BLibHolder<Block> ROYAL_JELLY_BLOCK = create("royal_jelly_block", AlienBlockProperties.JELLY);

    private static BLibHolder<Block> create(String path, BlockPropertyBuilder blockPropertyBuilder) {
        return create(path, () -> new Block(blockPropertyBuilder.build()));
    }

    private static <T extends Block> BLibHolder<T> create(String path, Supplier<T> blockSupplier) {
        return REGISTRY.createHolder(path, blockSupplier);
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

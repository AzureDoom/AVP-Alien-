package com.alien.common.registry.init.block;

import com.alien.AlienResources;
import com.alien.common.registry.init.block.property.AlienBlockProperties;
import com.avp.common.gameplay.block.property.BlockPropertyBuilder;
import com.avp.common.registry.AVPDeferredHolder;
import com.avp.service.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class AlienBlocks {

    private static final List<AVPDeferredHolder<? extends Block>> HOLDERS = new ArrayList<>();

    public static List<AVPDeferredHolder<? extends Block>> getAll() {
        return Collections.unmodifiableList(HOLDERS);
    }

    public static final AVPDeferredHolder<Block> ROYAL_JELLY_BLOCK = register("royal_jelly_block", AlienBlockProperties.JELLY);

    public static AVPDeferredHolder<Block> register(String id, BlockPropertyBuilder blockPropertyBuilder) {
        return register(id, () -> new Block(blockPropertyBuilder.build()));
    }

    public static <T extends Block> AVPDeferredHolder<T> register(String id, Supplier<T> blockSupplier) {
        var holder = Services.REGISTRY.register(BuiltInRegistries.BLOCK, AlienResources.location(id), blockSupplier);
        HOLDERS.add(holder);
        return holder;
    }

    public static void initialize() {}
}

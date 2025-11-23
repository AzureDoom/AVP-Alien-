package com.alien.common.registry.init;

import com.alien.AlienResources;
import com.avp.common.registry.AVPDeferredHolder;
import com.avp.service.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class AlienBlockEntityTypes {

    private static <T extends BlockEntity> AVPDeferredHolder<BlockEntityType<T>> register(
        String id,
        Supplier<BlockEntityType.Builder<T>> builder
    ) {
        return Services.REGISTRY.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, AlienResources.location(id), () -> builder.get().build(null));
    }

    public static void initialize() {}
}

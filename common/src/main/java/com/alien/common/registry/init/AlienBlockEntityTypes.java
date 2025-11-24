package com.alien.common.registry.init;

import com.alien.AlienResources;
import com.alien.common.gameplay.block.entity.resin.node.ResinNodeBlockEntity;
import com.alien.common.gameplay.block.entity.resin.vent.ResinVentBlockEntity;
import com.alien.common.registry.init.block.AlienResinBlocks;
import com.avp.common.registry.AVPDeferredHolder;
import com.avp.service.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class AlienBlockEntityTypes {

    public static final AVPDeferredHolder<BlockEntityType<ResinNodeBlockEntity>> RESIN_NODE = register(
        "resin_node",
        () -> BlockEntityType.Builder.of(
            ResinNodeBlockEntity::new,
            AlienResinBlocks.IRRADIATED_RESIN_NODE.get(),
            AlienResinBlocks.ABERRANT_RESIN_NODE.get(),
            AlienResinBlocks.NETHER_RESIN_NODE.get(),
            AlienResinBlocks.RESIN_NODE.get()
        )
    );

    public static final AVPDeferredHolder<BlockEntityType<ResinVentBlockEntity>> RESIN_VENT = register(
        "resin_vent",
        () -> BlockEntityType.Builder.of(
            ResinVentBlockEntity::new,
            AlienResinBlocks.IRRADIATED_RESIN_VENT.get(),
            AlienResinBlocks.ABERRANT_RESIN_VENT.get(),
            AlienResinBlocks.NETHER_RESIN_VENT.get(),
            AlienResinBlocks.RESIN_VENT.get()
        )
    );

    private static <T extends BlockEntity> AVPDeferredHolder<BlockEntityType<T>> register(
        String id,
        Supplier<BlockEntityType.Builder<T>> builder
    ) {
        return Services.REGISTRY.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            AlienResources.location(id),
            () -> builder.get().build(null)
        );
    }

    public static void initialize() {}
}

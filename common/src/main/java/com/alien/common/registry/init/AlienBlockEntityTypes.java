package com.alien.common.registry.init;

import com.alien.Alien;
import com.alien.common.gameplay.block.entity.resin.node.ResinNodeBlockEntity;
import com.alien.common.gameplay.block.entity.resin.vent.ResinVentBlockEntity;
import com.alien.common.registry.init.block.AlienResinBlocks;
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class AlienBlockEntityTypes {

    private static final BLibRegistry<BlockEntityType<?>> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.BLOCK_ENTITY_TYPE);

    public static final BLibHolder<BlockEntityType<ResinNodeBlockEntity>> RESIN_NODE = create(
        "resin_node",
        () -> BlockEntityType.Builder.of(
            ResinNodeBlockEntity::new,
            AlienResinBlocks.IRRADIATED_RESIN_NODE.get(),
            AlienResinBlocks.ABERRANT_RESIN_NODE.get(),
            AlienResinBlocks.NETHER_RESIN_NODE.get(),
            AlienResinBlocks.RESIN_NODE.get()
        )
    );

    public static final BLibHolder<BlockEntityType<ResinVentBlockEntity>> RESIN_VENT = create(
        "resin_vent",
        () -> BlockEntityType.Builder.of(
            ResinVentBlockEntity::new,
            AlienResinBlocks.IRRADIATED_RESIN_VENT.get(),
            AlienResinBlocks.ABERRANT_RESIN_VENT.get(),
            AlienResinBlocks.NETHER_RESIN_VENT.get(),
            AlienResinBlocks.RESIN_VENT.get()
        )
    );

    private static <T extends BlockEntity> BLibHolder<BlockEntityType<T>> create(
        String path,
        Supplier<BlockEntityType.Builder<T>> builder
    ) {
        return REGISTRY.createHolder(path, () -> builder.get().build(null));
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

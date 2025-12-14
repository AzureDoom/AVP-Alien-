package com.alien.common.registry.init;

import com.alien.Alien;
import com.alien.common.registry.init.block.AlienResinBlocks;
import com.alien.common.registry.init.item.AlienItems;
import com.blib.common.registry.impl.BLibCompostableRegistry;

public class AlienCompostingChances {

    private static final BLibCompostableRegistry REGISTRY = Alien.MOD.createCompostableRegistry();

    public static void initialize() {
        REGISTRY.register(AlienItems.IRRADIATED_RESIN_BALL, 0.3F, false, false);
        REGISTRY.register(AlienItems.ABERRANT_RESIN_BALL, 0.3F, false, false);
        REGISTRY.register(AlienItems.NETHER_RESIN_BALL, 0.3F, false, false);
        REGISTRY.register(AlienItems.RESIN_BALL, 0.3F, false, false);

        REGISTRY.register(AlienResinBlocks.IRRADIATED_RESIN, 1F, false, false);
        REGISTRY.register(AlienResinBlocks.IRRADIATED_RESIN_NODE, 1F, false, false);
        REGISTRY.register(AlienResinBlocks.IRRADIATED_RESIN_SLAB, 0.5F, false, false);
        REGISTRY.register(AlienResinBlocks.IRRADIATED_RESIN_STAIRS, 0.66F, false, false);
        REGISTRY.register(AlienResinBlocks.IRRADIATED_RESIN_VEIN, 0.3F, false, false);
        REGISTRY.register(AlienResinBlocks.IRRADIATED_RESIN_WEB, 0.65F, false, false);

        REGISTRY.register(AlienResinBlocks.ABERRANT_RESIN, 1F, false, false);
        REGISTRY.register(AlienResinBlocks.ABERRANT_RESIN_NODE, 1F, false, false);
        REGISTRY.register(AlienResinBlocks.ABERRANT_RESIN_SLAB, 0.5F, false, false);
        REGISTRY.register(AlienResinBlocks.ABERRANT_RESIN_STAIRS, 0.66F, false, false);
        REGISTRY.register(AlienResinBlocks.ABERRANT_RESIN_VEIN, 0.3F, false, false);
        REGISTRY.register(AlienResinBlocks.ABERRANT_RESIN_WEB, 0.65F, false, false);

        REGISTRY.register(AlienResinBlocks.NETHER_RESIN, 1F, false, false);
        REGISTRY.register(AlienResinBlocks.NETHER_RESIN_NODE, 1F, false, false);
        REGISTRY.register(AlienResinBlocks.NETHER_RESIN_SLAB, 0.5F, false, false);
        REGISTRY.register(AlienResinBlocks.NETHER_RESIN_STAIRS, 0.66F, false, false);
        REGISTRY.register(AlienResinBlocks.NETHER_RESIN_VEIN, 0.3F, false, false);
        REGISTRY.register(AlienResinBlocks.NETHER_RESIN_WEB, 0.65F, false, false);

        REGISTRY.register(AlienResinBlocks.RESIN, 1F, false, false);
        REGISTRY.register(AlienResinBlocks.RESIN_NODE, 1F, false, false);
        REGISTRY.register(AlienResinBlocks.RESIN_SLAB, 0.5F, false, false);
        REGISTRY.register(AlienResinBlocks.RESIN_STAIRS, 0.66F, false, false);
        REGISTRY.register(AlienResinBlocks.RESIN_VEIN, 0.3F, false, false);
        REGISTRY.register(AlienResinBlocks.RESIN_WEB, 0.65F, false, false);
    }
}

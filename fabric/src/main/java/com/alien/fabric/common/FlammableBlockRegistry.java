package com.alien.fabric.common;

import com.alien.common.registry.init.block.AberrantAlienResinBlocks;
import com.alien.common.registry.init.block.AlienResinBlocks;
import com.alien.common.registry.init.block.IrradiatedAlienResinBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public class FlammableBlockRegistry {

    public static void initialize() {
        var fireBlock = (FireBlock) Blocks.FIRE;

        // TODO: Add resin slabs and resin stairs here.
        fireBlock.setFlammable(AlienResinBlocks.RESIN.get(), 1, 20);
        fireBlock.setFlammable(AlienResinBlocks.RESIN_NODE.get(), 1, 20);

        fireBlock.setFlammable(AberrantAlienResinBlocks.ABERRANT_RESIN.get(), 1, 20);
        fireBlock.setFlammable(AberrantAlienResinBlocks.ABERRANT_RESIN_NODE.get(), 1, 20);

        fireBlock.setFlammable(IrradiatedAlienResinBlocks.IRRADIATED_RESIN.get(), 1, 20);
        fireBlock.setFlammable(IrradiatedAlienResinBlocks.IRRADIATED_RESIN_NODE.get(), 1, 20);
    }
}

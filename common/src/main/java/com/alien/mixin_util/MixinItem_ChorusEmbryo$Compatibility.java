package com.alien.mixin_util;

import net.minecraft.world.entity.LivingEntity;

public class MixinItem_ChorusEmbryo$Compatibility {

    public static void addWarpGene(LivingEntity livingEmbryo) {
        // FIXME:
        // GeneManagerProxy.getOrCreate(livingEmbryo)
        // .getGeneContainer()
        // .ifPresent(
        // wrapper -> wrapper.geneContainer()
        // .getActiveGeneMap()
        // .add(Genes.WARP, GeneOperationType.ADDITIVE, 0.1)
        // );
    }
}

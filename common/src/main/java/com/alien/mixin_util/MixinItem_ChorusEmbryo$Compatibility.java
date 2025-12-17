package com.alien.mixin_util;

import com.alien.compatibility.avp_human.GeneManagerProxy;
import com.human.common.gameplay.gene.GeneOperationType;
import com.human.common.gameplay.gene.Genes;
import net.minecraft.world.entity.LivingEntity;

public class MixinItem_ChorusEmbryo$Compatibility {

    public static void addWarpGene(LivingEntity livingEmbryo) {
        GeneManagerProxy.getOrCreate(livingEmbryo)
            .getGeneContainer()
            .ifPresent(
                wrapper -> wrapper.geneContainer()
                    .getActiveGeneMap()
                    .add(Genes.WARP, GeneOperationType.ADDITIVE, 0.1)
            );
    }
}

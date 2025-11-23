package com.alien.fabric.data.gene_bonus_data;

import com.avp.common.registry.AVPDeferredHolder;
import com.just.core.functional.tuple.Tuple2;
import com.lib.common.data.EntityTypePredicate;
import com.lib.common.gameplay.gene.Gene;
import com.lib.common.gameplay.gene.GeneBonusData;
import com.lib.common.gameplay.gene.GeneBonusDataEntry;
import com.lib.common.gameplay.gene.GeneModifier;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class GeneBonusDataSubProvider extends GeneBonusDataProvider {

    public GeneBonusDataSubProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate() {
    }

    private void add(TagKey<EntityType<?>> entityTypeTagKey, List<Tuple2<AVPDeferredHolder<Gene>, GeneModifier>> geneBonusList) {
        add(
            entityTypeTagKey.location().getPath() + "_gene_bonuses",
            new GeneBonusData(
                new EntityTypePredicate.Tag(entityTypeTagKey),
                geneBonusList.stream()
                    .map(
                        tuple -> new GeneBonusDataEntry(
                            tuple.v1().get().id(),
                            tuple.v2().operation(),
                            tuple.v2().value()
                        )
                    )
                    .toList()
            )
        );
    }

    private void add(EntityType<?> entityType, List<Tuple2<AVPDeferredHolder<Gene>, GeneModifier>> geneBonusList) {
        add(
            BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath() + "_gene_bonuses",
            new GeneBonusData(
                new EntityTypePredicate.Single(entityType),
                geneBonusList.stream()
                    .map(
                        tuple -> new GeneBonusDataEntry(
                            tuple.v1().get().id(),
                            tuple.v2().operation(),
                            tuple.v2().value()
                        )
                    )
                    .toList()
            )
        );
    }
}

package com.alien.fabric.data.gene_bonus_data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class GeneBonusDataSubProvider extends GeneBonusDataProvider {

    public GeneBonusDataSubProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generate() {
        // FIXME:
        // add(
        // AlienEntityTypeTags.ALIENS,
        // List.of(
        // // Positives
        // new Tuple2<>(Genes.ACIDIC_BLOOD, new GeneModifier(GeneOperationType.MULTIPLICATIVE, 0.34)),
        // new Tuple2<>(Genes.ACID_RESISTANCE, new GeneModifier(GeneOperationType.MULTIPLICATIVE, 0.34)),
        // new Tuple2<>(Genes.INTELLIGENCE, new GeneModifier(GeneOperationType.ADDITIVE, 15.0)),
        // // Negatives
        // new Tuple2<>(Genes.GENETIC_INTEGRITY, new GeneModifier(GeneOperationType.ADDITIVE, -0.34))
        // )
        // );
    }

    // private void add(TagKey<EntityType<?>> entityTypeTagKey, List<Tuple2<AVPDeferredHolder<Gene>, GeneModifier>>
    // geneBonusList) {
    // add(
    // entityTypeTagKey.location().getPath() + "_gene_bonuses",
    // new GeneBonusData(
    // new EntityTypePredicate.Tag(entityTypeTagKey),
    // geneBonusList.stream()
    // .map(
    // tuple -> new GeneBonusDataEntry(
    // tuple.v1().get().id(),
    // tuple.v2().operation(),
    // tuple.v2().value()
    // )
    // )
    // .toList()
    // )
    // );
    // }
    //
    // private void add(EntityType<?> entityType, List<Tuple2<AVPDeferredHolder<Gene>, GeneModifier>> geneBonusList) {
    // add(
    // BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath() + "_gene_bonuses",
    // new GeneBonusData(
    // new EntityTypePredicate.Single(entityType),
    // geneBonusList.stream()
    // .map(
    // tuple -> new GeneBonusDataEntry(
    // tuple.v1().get().id(),
    // tuple.v2().operation(),
    // tuple.v2().value()
    // )
    // )
    // .toList()
    // )
    // );
    // }
}

package com.alien.fabric.data.form_size_scale.provider;

import com.alien.common.model.lifecycle.growth.FormSizeScale;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.fabric.data.form_size_scale.FormSizeScaleConstants;

import java.util.function.BiConsumer;

public class NetherAlienFormSizeScaleProvider {

    public static void provide(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "nether_chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.NETHER_CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "royal_nether_chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ROYAL_NETHER_CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "nether_predalien_chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.NETHER_PREDALIEN_CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_MATURATION_TIME_IN_TICKS
            )
        );

        biConsumer.accept(
            "nether_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.NETHER_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "royal_nether_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ROYAL_NETHER_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "nether_predalien_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.NETHER_PREDALIEN_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_MATURATION_TIME_IN_TICKS
            )
        );

        biConsumer.accept(
            "nether_praetorian_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.NETHER_PRAETORIAN.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );

        biConsumer.accept(
            "nether_queen_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.NETHER_QUEEN.get(),
                FormSizeScaleConstants.QUEEN_START_SCALE,
                FormSizeScaleConstants.QUEEN_END_SCALE,
                FormSizeScaleConstants.QUEEN_MATURATION_TIME_IN_TICKS
            )
        );
    }
}

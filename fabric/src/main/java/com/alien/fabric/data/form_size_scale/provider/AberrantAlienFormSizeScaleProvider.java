package com.alien.fabric.data.form_size_scale.provider;

import com.alien.common.model.lifecycle.growth.FormSizeScale;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.fabric.data.form_size_scale.FormSizeScaleConstants;

import java.util.function.BiConsumer;

public class AberrantAlienFormSizeScaleProvider {

    public static void provide(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "aberrant_chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "royal_aberrant_chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ROYAL_ABERRANT_CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "aberrant_predalien_chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_PREDALIEN_CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_MATURATION_TIME_IN_TICKS
            )
        );

        biConsumer.accept(
            "aberrant_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "royal_aberrant_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ROYAL_ABERRANT_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "aberrant_predalien_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_PREDALIEN_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_MATURATION_TIME_IN_TICKS
            )
        );

        biConsumer.accept(
            "aberrant_praetorian_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_PRAETORIAN.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );

        biConsumer.accept(
            "aberrant_queen_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_QUEEN.get(),
                FormSizeScaleConstants.QUEEN_START_SCALE,
                FormSizeScaleConstants.QUEEN_END_SCALE,
                FormSizeScaleConstants.QUEEN_MATURATION_TIME_IN_TICKS
            )
        );
    }
}

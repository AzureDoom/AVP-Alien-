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
                FormSizeScaleConstants.CHESTBURSTER_PHASES
            )
        );
        biConsumer.accept(
            "royal_aberrant_chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ROYAL_ABERRANT_CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_PHASES
            )
        );
        biConsumer.accept(
            "aberrant_predalien_chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_PREDALIEN_CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_PHASES
            )
        );

        biConsumer.accept(
            "aberrant_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_PHASES
            )
        );
        biConsumer.accept(
            "royal_aberrant_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ROYAL_ABERRANT_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_PHASES
            )
        );
        biConsumer.accept(
            "aberrant_predalien_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_PREDALIEN_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_PHASES
            )
        );

        biConsumer.accept(
            "aberrant_drone_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_DRONE.get(),
                FormSizeScaleConstants.DRONE_START_SCALE,
                FormSizeScaleConstants.DRONE_END_SCALE,
                FormSizeScaleConstants.DRONE_PHASES
            )
        );

        biConsumer.accept(
            "aberrant_carrier_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_CARRIER.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_PHASES
            )
        );

        biConsumer.accept(
            "aberrant_chrysalis_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_CHRYSALIS.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_PHASES
            )
        );

        biConsumer.accept(
            "aberrant_predalien_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_PREDALIEN.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_PHASES
            )
        );

        biConsumer.accept(
            "aberrant_praetorian_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_PRAETORIAN.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_PHASES
            )
        );

        biConsumer.accept(
            "aberrant_ravager_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_RAVAGER.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_PHASES
            )
        );

        biConsumer.accept(
            "aberrant_razor_claw_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_RAZOR_CLAW.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_PHASES
            )
        );

        biConsumer.accept(
            "aberrant_queen_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ABERRANT_QUEEN.get(),
                FormSizeScaleConstants.QUEEN_START_SCALE,
                FormSizeScaleConstants.QUEEN_END_SCALE,
                FormSizeScaleConstants.QUEEN_PHASES
            )
        );
    }
}

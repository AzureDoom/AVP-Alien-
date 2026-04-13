package com.alien.fabric.data.form_size_scale.provider;

import com.alien.common.model.lifecycle.growth.FormSizeScale;
import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.fabric.data.form_size_scale.FormSizeScaleConstants;

import java.util.function.BiConsumer;

public class AlienFormSizeScaleProvider {

    public static void provide(BiConsumer<String, FormSizeScale> biConsumer) {
        provideChestbursterScales(biConsumer);
        provideAdolescentScales(biConsumer);
        provideDroneScales(biConsumer);
        provideCarrierScales(biConsumer);
        provideChrysalisScales(biConsumer);
        providePredalienScales(biConsumer);
        providePraetorianScales(biConsumer);
        provideQueenScales(biConsumer);
        provideRavagerScales(biConsumer);
        provideRazorClawScales(biConsumer);
    }

    private static void provideChestbursterScales(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "royal_chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ROYAL_CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "predalien_chestburster_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.PREDALIEN_CHESTBURSTER.get(),
                FormSizeScaleConstants.CHESTBURSTER_START_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_END_SCALE,
                FormSizeScaleConstants.CHESTBURSTER_MATURATION_TIME_IN_TICKS
            )
        );
    }

    private static void provideAdolescentScales(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "royal_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.ROYAL_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "predalien_adolescent_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.PREDALIEN_ADOLESCENT.get(),
                FormSizeScaleConstants.ADOLESCENT_START_SCALE,
                FormSizeScaleConstants.ADOLESCENT_END_SCALE,
                FormSizeScaleConstants.ADOLESCENT_MATURATION_TIME_IN_TICKS
            )
        );
    }

    private static void provideDroneScales(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "drone_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.DRONE.get(),
                FormSizeScaleConstants.DRONE_START_SCALE,
                FormSizeScaleConstants.DRONE_END_SCALE,
                FormSizeScaleConstants.DRONE_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "irradiated_drone_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.IRRADIATED_DRONE.get(),
                FormSizeScaleConstants.DRONE_START_SCALE,
                FormSizeScaleConstants.DRONE_END_SCALE,
                FormSizeScaleConstants.DRONE_MATURATION_TIME_IN_TICKS
            )
        );
    }

    private static void provideCarrierScales(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "carrier_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.CARRIER.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "irradiated_carrier_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.IRRADIATED_CARRIER.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
    }

    private static void provideChrysalisScales(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "chrysalis_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.CHRYSALIS.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "irradiated_chrysalis_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.IRRADIATED_CHRYSALIS.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
    }

    private static void providePredalienScales(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "predalien_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.PREDALIEN.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
    }

    private static void providePraetorianScales(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "praetorian_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.PRAETORIAN.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "irradiated_praetorian_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.IRRADIATED_PRAETORIAN.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
    }

    private static void provideRavagerScales(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "ravager_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.RAVAGER.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "irradiated_ravager_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.IRRADIATED_RAVAGER.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
    }

    private static void provideRazorClawScales(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "razor_claw_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.RAZOR_CLAW.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "irradiated_razor_claw_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.IRRADIATED_RAZOR_CLAW.get(),
                FormSizeScaleConstants.PRAETORIAN_START_SCALE,
                FormSizeScaleConstants.PRAETORIAN_END_SCALE,
                FormSizeScaleConstants.PRAETORIAN_MATURATION_TIME_IN_TICKS
            )
        );
    }

    private static void provideQueenScales(BiConsumer<String, FormSizeScale> biConsumer) {
        biConsumer.accept(
            "queen_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.QUEEN.get(),
                FormSizeScaleConstants.QUEEN_START_SCALE,
                FormSizeScaleConstants.QUEEN_END_SCALE,
                FormSizeScaleConstants.QUEEN_MATURATION_TIME_IN_TICKS
            )
        );
        biConsumer.accept(
            "irradiated_queen_form_size_scale",
            new FormSizeScale(
                AlienEntityTypes.IRRADIATED_QUEEN.get(),
                FormSizeScaleConstants.QUEEN_START_SCALE,
                FormSizeScaleConstants.QUEEN_END_SCALE,
                FormSizeScaleConstants.QUEEN_MATURATION_TIME_IN_TICKS
            )
        );
    }
}

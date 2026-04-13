package com.alien.fabric.data.form_size_scale;

import java.util.concurrent.TimeUnit;

public class FormSizeScaleConstants {

    // Chestbursters grow from 100% to 125% scale over 3 minutes.
    public static final float CHESTBURSTER_START_SCALE = 1.0F;

    public static final float CHESTBURSTER_END_SCALE = 1.25F;

    public static final int CHESTBURSTER_MATURATION_TIME_IN_TICKS = (int) TimeUnit.MINUTES.toSeconds(3) * 20;

    // Adolescents grow from 100% to 125% scale over 3 minutes.
    public static final float ADOLESCENT_START_SCALE = 1.0F;

    public static final float ADOLESCENT_END_SCALE = 1.25F;

    public static final int ADOLESCENT_MATURATION_TIME_IN_TICKS = (int) TimeUnit.MINUTES.toSeconds(3) * 20;

    // Praetorians start at 75% of full size and grow to 100% over 10 minutes.
    public static final float PRAETORIAN_START_SCALE = 0.66F;

    public static final float PRAETORIAN_END_SCALE = 1.0F;

    public static final int PRAETORIAN_MATURATION_TIME_IN_TICKS = (int) TimeUnit.MINUTES.toSeconds(10) * 20;

    // Queens start at 85% of full size and grow to 100% over 15 minutes.
    public static final float QUEEN_START_SCALE = 0.85F;

    public static final float QUEEN_END_SCALE = 1.0F;

    public static final int QUEEN_MATURATION_TIME_IN_TICKS = (int) TimeUnit.MINUTES.toSeconds(15) * 20;
}

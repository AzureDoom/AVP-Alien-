package com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.ai;

import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.goal.Goal;

public class VibrationGoals {

    public static final Goal INVESTIGATE_VIBRATION = Goal.builder("InvestigateVibrationGoal")
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(VibrationSensors.HAS_VIBRATION_TO_INVESTIGATE.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(VibrationSensors.HAS_VIBRATION_TO_INVESTIGATE.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private VibrationGoals() {
        throw new UnsupportedOperationException();
    }
}

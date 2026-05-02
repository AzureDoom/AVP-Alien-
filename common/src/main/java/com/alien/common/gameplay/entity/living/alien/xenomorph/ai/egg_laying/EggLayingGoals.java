package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying;

import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.goal.Goal;

public class EggLayingGoals {

    public static final Goal LAY_EGG = Goal.builder("LayEggGoal")
        .addPrecondition(EggLayingSensors.CAN_LAY_EGG, Expressions.Boolean.isTrue())
        .addDesiredCondition(EggLayingSensors.CAN_LAY_EGG.asDerived(), Expressions.Boolean.isFalse())
        .build();

    private EggLayingGoals() {
        throw new UnsupportedOperationException();
    }
}

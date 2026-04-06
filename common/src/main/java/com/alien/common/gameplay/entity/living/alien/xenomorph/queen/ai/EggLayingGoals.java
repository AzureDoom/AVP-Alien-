package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai;

import com.just.goap.condition.expression.Expressions;
import com.just.goap.goal.Goal;

public class EggLayingGoals {

    public static final Goal LAY_EGG = Goal.builder("LayEggGoal")
        .addPrecondition(EggLayingSensors.CAN_LAY_EGG.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(EggLayingSensors.CAN_LAY_EGG.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private EggLayingGoals() {
        throw new UnsupportedOperationException();
    }
}

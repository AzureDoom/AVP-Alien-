package com.alien.common.gameplay.entity.living.alien.xenomorph.empress.ai;

import com.just.goap.condition.expression.Expressions;
import com.just.goap.goal.Goal;

public class EmpressEggLayingGoals {

    public static final Goal LAY_EGG = Goal.builder("LayEggGoal")
        .addPrecondition(EmpressEggLayingSensors.CAN_LAY_EGG.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(EmpressEggLayingSensors.CAN_LAY_EGG.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private EmpressEggLayingGoals() {
        throw new UnsupportedOperationException();
    }
}

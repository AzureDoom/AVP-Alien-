package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.cocoon;

import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.goal.Goal;

public class CocoonGoals {

    public static final Goal COCOON = Goal.builder("CocoonGoal")
        .addPrecondition(CocoonSensors.SHOULD_COCOON.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(CocoonSensors.SHOULD_COCOON.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private CocoonGoals() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.idle;

import com.just.goap.condition.expression.Expressions;
import com.just.goap.goal.Goal;

public class IdleGoals {

    public static final Goal SATISFY_BOREDOM = Goal.builder("SatisfyBoredomGoal")
        .addPrecondition(IdleSensors.IS_BORED.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(IdleSensors.IS_BORED.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private IdleGoals() {
        throw new UnsupportedOperationException();
    }
}

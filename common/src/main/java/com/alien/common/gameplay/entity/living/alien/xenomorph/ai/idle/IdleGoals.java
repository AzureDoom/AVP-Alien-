package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.idle;

import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.goap.condition.expression.Expressions;
import com.just.goap.goal.Goal;

public class IdleGoals {

    public static final Goal SATISFY_BOREDOM = Goal.builder("SatisfyBoredomGoal")
        .addPrecondition(IdleSensors.IS_BORED.key(), Expressions.Boolean.isTrue())
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addDesiredCondition(IdleSensors.IS_BORED.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private IdleGoals() {
        throw new UnsupportedOperationException();
    }
}

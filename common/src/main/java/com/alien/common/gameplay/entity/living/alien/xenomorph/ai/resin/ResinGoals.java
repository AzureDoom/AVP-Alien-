package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.resin;

import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.goap.condition.expression.Expressions;
import com.just.goap.goal.Goal;

public class ResinGoals {

    public static final Goal SPREAD_RESIN = Goal.builder("SpreadResinGoal")
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(ResinSensors.CAN_SPREAD_RESIN.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(ResinSensors.CAN_SPREAD_RESIN.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private ResinGoals() {
        throw new UnsupportedOperationException();
    }
}

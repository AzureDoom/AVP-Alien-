package com.alien.common.gameplay.entity.living.alien.ovomorph.ai;

import com.alien.common.model.alien.HatchState;
import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.goal.Goal;

public class OvomorphGOAPGoals {

    static final Goal HATCH = Goal.builder("HatchGoal")
        .addDesiredCondition(OvomorphGOAPStateKeys.HATCH_STATE.asDerived(), Expressions.Compare.equalTo(HatchState.HATCHED))
        .build();

    private OvomorphGOAPGoals() {
        throw new UnsupportedOperationException();
    }
}

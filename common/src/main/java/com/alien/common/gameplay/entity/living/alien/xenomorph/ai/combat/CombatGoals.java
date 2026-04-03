package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat;

import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.goap.condition.expression.Expressions;
import com.just.goap.goal.Goal;

public class CombatGoals {

    public static final Goal KILL_TARGET = Goal.builder("KillTargetGoal")
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(GOAPSensors.HAS_ATTACK_TARGET.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private CombatGoals() {
        throw new UnsupportedOperationException();
    }
}

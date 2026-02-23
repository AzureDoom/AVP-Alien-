package com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.attach_to_host;

import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.goap.condition.expression.Expressions;
import com.just.goap.goal.Goal;

public class AttachToHostGoals {

    public static final Goal ATTACH_TO_HOST_GOAL = Goal.builder("AttachToHostGoal")
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(GOAPSensors.HAS_ATTACK_TARGET.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private AttachToHostGoals() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent;

import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.goap.condition.expression.Expressions;
import com.just.goap.goal.Goal;

public class VentGoals {

    public static final Goal CREATE_VENT = Goal.builder("CreateVentGoal")
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(VentSensors.CAN_CREATE_VENT.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(VentSensors.CAN_CREATE_VENT.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private VentGoals() {
        throw new UnsupportedOperationException();
    }
}

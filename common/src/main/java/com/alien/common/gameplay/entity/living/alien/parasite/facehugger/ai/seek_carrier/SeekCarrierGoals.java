package com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.seek_carrier;

import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.goal.Goal;

public class SeekCarrierGoals {

    public static final Goal SEEK_CARRIER = Goal.builder("SeekCarrierGoal")
        .addPrecondition(SeekCarrierSensors.SHOULD_SEEK_CARRIER.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(SeekCarrierSensors.SHOULD_SEEK_CARRIER.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private SeekCarrierGoals() {
        throw new UnsupportedOperationException();
    }
}

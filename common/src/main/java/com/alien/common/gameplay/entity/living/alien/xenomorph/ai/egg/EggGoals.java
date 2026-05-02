package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg;

import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.goal.Goal;

public class EggGoals {

    public static final Goal FETCH_EGG = Goal.builder("FetchEggGoal")
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(EggSensors.HAS_TARGET_OVOMORPH.key(), Expressions.Boolean.isTrue())
        .addPrecondition(EggSensors.IS_CARRYING_OVOMORPH.key(), Expressions.Boolean.isFalse())
        .addDesiredCondition(EggSensors.IS_CARRYING_OVOMORPH.key().asDerived(), Expressions.Boolean.isTrue())
        .build();

    public static final Goal DELIVER_EGG = Goal.builder("DeliverEggGoal")
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(EggSensors.IS_CARRYING_OVOMORPH.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(EggSensors.IS_CARRYING_OVOMORPH.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private EggGoals() {
        throw new UnsupportedOperationException();
    }
}

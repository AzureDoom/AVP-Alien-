package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai.panic_release;

import com.just.goap.condition.expression.Expressions;
import com.just.goap.goal.Goal;

public class PanicReleaseGoals {

    public static final Goal PANIC_RELEASE_FACEHUGGERS = Goal.builder("PanicReleaseFacehuggersGoal")
        .addPrecondition(PanicReleaseSensors.SHOULD_PANIC_RELEASE.key(), Expressions.Boolean.isTrue())
        .addDesiredCondition(PanicReleaseSensors.SHOULD_PANIC_RELEASE.key().asDerived(), Expressions.Boolean.isFalse())
        .build();

    private PanicReleaseGoals() {
        throw new UnsupportedOperationException();
    }
}

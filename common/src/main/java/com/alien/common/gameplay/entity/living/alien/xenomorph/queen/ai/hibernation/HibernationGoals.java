package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai.hibernation;

import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.goal.Goal;

/**
 * Goal for the hibernation hold (Stage 3a). Mirrors {@code CocoonGoals.COCOON}: a continuous "be done hibernating" want
 * that keeps the hold action selected for the whole sleep. It is never actually satisfied by the world while she sleeps
 * (the action's effect is planner-only); the hold ends when the phase manager advances her out of HIBERNATION, which
 * flips {@link HibernationSensors#IS_HIBERNATING} false and drops the precondition.
 */
public final class HibernationGoals {

    public static final Goal HIBERNATE = Goal.builder("HibernateGoal")
            .addPrecondition(HibernationSensors.IS_HIBERNATING.key(), Expressions.Boolean.isTrue())
            .addDesiredCondition(HibernationSensors.IS_HIBERNATING.key().asDerived(), Expressions.Boolean.isFalse())
            .build();

    private HibernationGoals() {
        throw new UnsupportedOperationException();
    }
}
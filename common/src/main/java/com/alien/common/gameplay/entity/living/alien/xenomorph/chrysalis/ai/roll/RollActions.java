package com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ai.roll;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.Chrysalis;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.goap.StateKey;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;

public class RollActions {

    public static Action<Chrysalis> createRollAtTarget(StateKey.Sensed<Boolean> rollRangeKey) {
        return BLibAction.<Chrysalis>builder("ChrysalisRollAction")
            .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
            .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
            .addPrecondition(rollRangeKey, Expressions.Boolean.isTrue())
            .addEffect(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key().asDerived(), true)
            .withCost(-1.0F)
            .withPerformCallback(RollAction::perform)
            .build();
    }

    private RollActions() {
        throw new UnsupportedOperationException();
    }
}

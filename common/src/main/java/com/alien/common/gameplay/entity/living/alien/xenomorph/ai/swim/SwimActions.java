package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.swim;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.swim.action.SwimToLandAction;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;

public class SwimActions {

    public static final Action<Xenomorph> SWIM_TO_LAND = BLibAction.<Xenomorph>builder("SwimToLandAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(SwimSensors.NEEDS_WATER_TO_LAND_TRANSITION.key(), Expressions.Boolean.isTrue())
        .addPrecondition(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key(), Expressions.Boolean.isFalse())
        .addEffect(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key().asDerived(), true)
        .withPerformCallback(SwimToLandAction::perform)
        .build();

    private SwimActions() {
        throw new UnsupportedOperationException();
    }
}

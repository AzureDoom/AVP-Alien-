package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai.throw_attack.ThrowAttackSensors;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;

public class CarrierCombatActions {

    public static final Action<Carrier> MOVE_TO_TARGET = BLibAction.<Carrier>builder("CarrierMoveToTargetAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key(), Expressions.Boolean.isFalse())
        .addPrecondition(ThrowAttackSensors.CAN_THROW_FACEHUGGER.key(), Expressions.Boolean.isFalse())
        .addEffect(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key().asDerived(), true)
        .withPerformCallback(CombatActions::performMoveToTarget)
        .withFinishCallback(CombatActions::finishMoveToTarget)
        .build();

    private CarrierCombatActions() {
        throw new UnsupportedOperationException();
    }
}

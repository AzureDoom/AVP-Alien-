package com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.dig.DigSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.Spitter;
import com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.ai.spit.SpitSensors;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;

public class SpitterCombatActions {

    public static final Action<Spitter> MOVE_TO_TARGET = BLibAction.<Spitter>builder("MoveToTargetAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key(), Expressions.Boolean.isFalse())
        .addPrecondition(DigSensors.IS_PATH_TO_TARGET_BLOCKED.key(), Expressions.Boolean.isFalse())
        .addPrecondition(SpitSensors.IS_TARGET_AT_SPIT_DISTANCE.key(), Expressions.Boolean.isFalse())
        .addEffect(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key().asDerived(), true)
        .withPerformCallback(CombatActions::performMoveToTarget)
        .withFinishCallback(CombatActions::finishMoveToTarget)
        .build();

    private SpitterCombatActions() {
        throw new UnsupportedOperationException();
    }
}

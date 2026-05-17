package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.dig;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.dig.action.DigToTargetAction;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;

public class DigActions {

    public static final Action<Xenomorph> DIG_TO_TARGET = BLibAction.<Xenomorph>builder("DigToTargetAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(DigSensors.IS_PATH_TO_TARGET_BLOCKED.key(), Expressions.Boolean.isTrue())
        .addEffect(DigSensors.IS_PATH_TO_TARGET_BLOCKED.key().asDerived(), false)
        .withPerformCallback(DigToTargetAction::perform)
        .withFinishCallback(DigToTargetAction::onFinish)
        .build();

    private DigActions() {
        throw new UnsupportedOperationException();
    }
}

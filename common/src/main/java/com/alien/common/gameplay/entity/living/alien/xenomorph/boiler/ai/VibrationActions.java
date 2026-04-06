package com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.Boiler;
import com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.ai.action.InvestigateVibrationAction;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;

public class VibrationActions {

    public static final Action<Boiler> INVESTIGATE_VIBRATION = BLibAction.<Boiler>builder("InvestigateVibrationAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(VibrationSensors.HAS_VIBRATION_TO_INVESTIGATE.key(), Expressions.Boolean.isTrue())
        .addEffect(VibrationSensors.HAS_VIBRATION_TO_INVESTIGATE.key().asDerived(), false)
        .withPerformCallback(InvestigateVibrationAction::perform)
        .withFinishCallback(InvestigateVibrationAction::onFinish)
        .build();

    private VibrationActions() {
        throw new UnsupportedOperationException();
    }
}

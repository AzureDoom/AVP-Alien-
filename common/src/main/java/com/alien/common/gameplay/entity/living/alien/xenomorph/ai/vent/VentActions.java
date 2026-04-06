package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent.action.CreateVentAction;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;

public class VentActions {

    public static final Action<Xenomorph> CREATE_VENT = BLibAction.<Xenomorph>builder("CreateVentAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(VentSensors.CAN_CREATE_VENT.key(), Expressions.Boolean.isTrue())
        .addEffect(VentSensors.CAN_CREATE_VENT.key().asDerived(), false)
        .withPerformCallback(CreateVentAction::perform)
        .withFinishCallback(CreateVentAction::onFinish)
        .build();

    private VentActions() {
        throw new UnsupportedOperationException();
    }
}

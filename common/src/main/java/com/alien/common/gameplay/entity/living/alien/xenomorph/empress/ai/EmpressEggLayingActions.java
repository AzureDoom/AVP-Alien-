package com.alien.common.gameplay.entity.living.alien.xenomorph.empress.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.empress.Empress;
import com.alien.common.gameplay.entity.living.alien.xenomorph.empress.ai.action.EmpressLayEggAction;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;

public class EmpressEggLayingActions {

    public static final Action<Empress> LAY_EGG = BLibAction.<Empress>builder("EmpressLayEggAction")
        .addPrecondition(EmpressEggLayingSensors.CAN_LAY_EGG.key(), Expressions.Boolean.isTrue())
        .addEffect(EmpressEggLayingSensors.CAN_LAY_EGG.key().asDerived(), false)
        .withPerformCallback(EmpressLayEggAction::perform)
        .build();

    private EmpressEggLayingActions() {
        throw new UnsupportedOperationException();
    }
}

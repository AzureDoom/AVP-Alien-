package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.resin;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;

public class ResinActions {

    public static final Action<Xenomorph> SPREAD_RESIN = BLibAction.<Xenomorph>builder("SpreadResinAction")
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(ResinSensors.CAN_SPREAD_RESIN.key(), Expressions.Boolean.isTrue())
        .addEffect(ResinSensors.CAN_SPREAD_RESIN.key().asDerived(), false)
        .withPerformCallback(ResinActions::perform)
        .build();

    private static Action.Signal perform(Action.Context<? extends Xenomorph> context) {
        var xenomorph = context.getActor();

        xenomorph.getResinManager().spreadResin();

        return Action.Signal.CONTINUE;
    }

    private ResinActions() {
        throw new UnsupportedOperationException();
    }
}

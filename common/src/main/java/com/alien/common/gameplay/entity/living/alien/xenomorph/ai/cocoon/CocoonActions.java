package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.cocoon;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;

public class CocoonActions {

    public static final Action<Xenomorph> COCOON = BLibAction.<Xenomorph>builder("CocoonAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(CocoonSensors.SHOULD_COCOON.key(), Expressions.Boolean.isTrue())
        .addEffect(CocoonSensors.SHOULD_COCOON.key().asDerived(), false)
        .withPerformCallback(context -> {
            var shouldContinue = context.getActor().getCocoonManager().performCocoonTick();
            return shouldContinue ? Action.Signal.CONTINUE : Action.Signal.ABORT;
        })
        .build();

    private CocoonActions() {
        throw new UnsupportedOperationException();
    }
}

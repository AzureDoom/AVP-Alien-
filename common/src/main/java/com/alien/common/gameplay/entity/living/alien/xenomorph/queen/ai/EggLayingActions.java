package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai.action.LayEggAction;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;

public class EggLayingActions {

    public static final Action<Queen> LAY_EGG = BLibAction.<Queen>builder("LayEggAction")
        .addPrecondition(EggLayingSensors.CAN_LAY_EGG.key(), Expressions.Boolean.isTrue())
        .addEffect(EggLayingSensors.CAN_LAY_EGG.key().asDerived(), false)
        .withPerformCallback(LayEggAction::perform)
        .build();

    private EggLayingActions() {
        throw new UnsupportedOperationException();
    }
}

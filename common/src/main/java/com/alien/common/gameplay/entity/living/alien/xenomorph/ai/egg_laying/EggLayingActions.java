package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying.action.LayEggAction;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;

public class EggLayingActions {

    public static <T extends EggLayer> Action<T> layEgg() {
        return BLibAction.<T>builder("LayEggAction")
            .addPrecondition(EggLayingSensors.CAN_LAY_EGG, Expressions.Boolean.isTrue())
            .addEffect(EggLayingSensors.CAN_LAY_EGG.asDerived(), false)
            .withPerformCallback(LayEggAction::perform)
            .build();
    }

    private EggLayingActions() {
        throw new UnsupportedOperationException();
    }
}

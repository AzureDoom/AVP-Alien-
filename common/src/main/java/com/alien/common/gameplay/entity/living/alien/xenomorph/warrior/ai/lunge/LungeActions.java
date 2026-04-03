package com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.ai.lunge;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.ai.lunge.action.XenomorphLungeAction;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;

public class LungeActions {

    public static final Action<Xenomorph> LUNGE_AT_TARGET = BLibAction.<Xenomorph>builder("LungeAtTargetAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(LungeSensors.IS_TARGET_IN_LUNGE_RANGE.key(), Expressions.Boolean.isTrue())
        .addEffect(GOAPSensors.HAS_ATTACK_TARGET.key().asDerived(), false)
        .withPerformCallback(XenomorphLungeAction::perform)
        .build();

    private LungeActions() {
        throw new UnsupportedOperationException();
    }
}

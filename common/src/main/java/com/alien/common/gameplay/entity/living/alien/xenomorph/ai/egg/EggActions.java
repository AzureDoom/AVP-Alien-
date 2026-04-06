package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg.action.DropOffEggAction;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg.action.PickUpEggAction;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;

public class EggActions {

    public static final Action<Xenomorph> PICK_UP_EGG = BLibAction.<Xenomorph>builder("PickUpEggAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(EggSensors.HAS_TARGET_OVOMORPH.key(), Expressions.Boolean.isTrue())
        .addPrecondition(EggSensors.IS_CARRYING_OVOMORPH.key(), Expressions.Boolean.isFalse())
        .addEffect(EggSensors.IS_CARRYING_OVOMORPH.key().asDerived(), true)
        .withPerformCallback(PickUpEggAction::perform)
        .withFinishCallback(PickUpEggAction::onFinish)
        .build();

    public static final Action<Xenomorph> DROP_OFF_EGG = BLibAction.<Xenomorph>builder("DropOffEggAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(EggSensors.IS_CARRYING_OVOMORPH.key(), Expressions.Boolean.isTrue())
        .addEffect(EggSensors.IS_CARRYING_OVOMORPH.key().asDerived(), false)
        .withPerformCallback(DropOffEggAction::perform)
        .withFinishCallback(DropOffEggAction::onFinish)
        .build();

    private EggActions() {
        throw new UnsupportedOperationException();
    }
}

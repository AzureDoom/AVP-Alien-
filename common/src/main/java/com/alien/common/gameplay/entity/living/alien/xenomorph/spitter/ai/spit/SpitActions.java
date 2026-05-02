package com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.ai.spit;

import com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.Spitter;
import com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.ai.spit.action.SpitAtTargetAction;
import com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.ai.spit.action.WaitForSpitCooldownAction;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;

public class SpitActions {

    public static final Action<Spitter> SPIT_AT_TARGET = BLibAction.<Spitter>builder("SpitAtTargetAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(SpitSensors.IS_TARGET_AT_SPIT_DISTANCE.key(), Expressions.Boolean.isTrue())
        .addPrecondition(SpitSensors.IS_SPIT_COOLDOWN_READY.key(), Expressions.Boolean.isTrue())
        .addEffect(GOAPSensors.HAS_ATTACK_TARGET.key().asDerived(), false)
        .withPerformCallback(SpitAtTargetAction::perform)
        .build();

    public static final Action<Spitter> WAIT_FOR_SPIT_COOLDOWN = BLibAction.<Spitter>builder("WaitForSpitCooldownAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(SpitSensors.IS_TARGET_AT_SPIT_DISTANCE.key(), Expressions.Boolean.isTrue())
        .addPrecondition(SpitSensors.IS_SPIT_COOLDOWN_READY.key(), Expressions.Boolean.isFalse())
        .addEffect(SpitSensors.IS_SPIT_COOLDOWN_READY.key().asDerived(), true)
        .withPerformCallback(WaitForSpitCooldownAction::perform)
        .build();

    private SpitActions() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.attach_to_host;

import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.attach_to_host.action.LungeAction;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.blib.api.common.goap.v1.action.impl.MoveToPosAction;
import com.just.core.functional.option.Option;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;

public class AttachToHostActions {

    public static final Action<Facehugger> MOVE_TO_HOST = BLibAction.<Facehugger>builder("MoveToHostAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(AttachToHostSensors.IS_ATTACK_TARGET_IN_LUNGE_RANGE.key(), Expressions.Boolean.isFalse())
        .addEffect(AttachToHostSensors.IS_ATTACK_TARGET_IN_LUNGE_RANGE.key().asDerived(), true)
        .withPerformCallback(AttachToHostActions::performMoveToHost)
        .withFinishCallback(MoveToPosAction::onFinish)
        .build();

    public static final Action<Facehugger> LUNGE_AT_HOST = BLibAction.<Facehugger>builder("LungeAtHostAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(AttachToHostSensors.IS_ATTACK_TARGET_IN_LUNGE_RANGE.key(), Expressions.Boolean.isTrue())
        .addEffect(GOAPSensors.HAS_ATTACK_TARGET.key().asDerived(), false)
        .withPerformCallback(LungeAction::perform)
        .build();

    private static Action.Signal performMoveToHost(Action.Context<? extends Facehugger> context) {
        var facehugger = context.getActor();
        var worldState = context.getWorldState();
        var attackTargetOption = worldState.getOrDefault(GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(), Option.none());

        if (attackTargetOption.isNone()) {
            return Action.Signal.ABORT;
        }

        var attackTarget = attackTargetOption.unwrap();
        facehugger.getLookControl().setLookAt(attackTarget);

        return switch (MoveToPosAction.perform(context, attackTarget.position(), 1.1)) {
            case FINISHED, MOVING -> Action.Signal.CONTINUE;
            case NO_PATH -> Action.Signal.ABORT;
        };
    }

    private AttachToHostActions() {
        throw new UnsupportedOperationException();
    }
}

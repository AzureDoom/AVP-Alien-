package com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.seek_carrier;

import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.blib.api.common.goap.v1.action.impl.MoveToPosAction;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;

public class SeekCarrierActions {

    private static final double MOUNT_RANGE_SQUARED = 2.5 * 2.5;

    public static final Action<Facehugger> MOVE_TO_CARRIER = BLibAction.<Facehugger>builder("MoveToCarrierAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(SeekCarrierSensors.SHOULD_SEEK_CARRIER.key(), Expressions.Boolean.isTrue())
        .addEffect(SeekCarrierSensors.SHOULD_SEEK_CARRIER.key().asDerived(), false)
        .withPerformCallback(SeekCarrierActions::performMoveToCarrier)
        .withFinishCallback(MoveToPosAction::onFinish)
        .build();

    private static Action.Signal performMoveToCarrier(Action.Context<? extends Facehugger> context) {
        var facehugger = context.getActor();
        var worldState = context.getWorldState();
        var carrier = worldState.getOrDefault(SeekCarrierSensors.NEAREST_AVAILABLE_CARRIER.key(), (Carrier) null);

        if (carrier == null || !carrier.isAlive() || facehugger.isPassenger()) {
            return Action.Signal.ABORT;
        }

        facehugger.getLookControl().setLookAt(carrier);

        if (facehugger.distanceToSqr(carrier) <= MOUNT_RANGE_SQUARED) {
            facehugger.startRiding(carrier);
            return Action.Signal.ABORT;
        }

        return switch (MoveToPosAction.perform(context, carrier.position(), 1.5)) {
            case FINISHED, MOVING -> Action.Signal.CONTINUE;
            case NO_PATH -> Action.Signal.ABORT;
        };
    }

    private SeekCarrierActions() {
        throw new UnsupportedOperationException();
    }
}

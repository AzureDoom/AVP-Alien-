package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg.action;

import com.alien.common.gameplay.entity.living.alien.EggCarrier;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.blib.api.common.goap.v1.action.impl.NeoMoveToPosAction;
import com.just.goap.action.Action;

public class PickUpEggAction {

    private static final double PICKUP_RANGE_SQUARED = 2.0 * 2.0;

    public static Action.Signal perform(Action.Context<? extends Xenomorph> context) {
        var xenomorph = context.getActor();

        if (!(xenomorph instanceof EggCarrier eggCarrier)) {
            return Action.Signal.ABORT;
        }

        var targetOvomorph = eggCarrier.getEggPickupManager().getTargetOvomorphOrNull();

        if (targetOvomorph == null || !targetOvomorph.wantsPickup || targetOvomorph.isPassenger()) {
            eggCarrier.getEggPickupManager().setTargetOvomorph(null);
            return Action.Signal.ABORT;
        }

        var result = NeoMoveToPosAction.perform(context, targetOvomorph.position(), 0.5);

        return switch (result) {
            case FINISHED, MOVING -> {
                if (xenomorph.distanceToSqr(targetOvomorph) <= PICKUP_RANGE_SQUARED) {
                    targetOvomorph.startRiding(xenomorph);
                    eggCarrier.getEggPickupManager().setTargetOvomorph(null);
                    yield Action.Signal.CONTINUE;
                }

                yield Action.Signal.CONTINUE;
            }
            case WAITING_FOR_BLOCK_BREAK -> Action.Signal.CONTINUE;
            case NO_PATH -> Action.Signal.ABORT;
        };
    }

    public static void onFinish(Action.Context<? extends Xenomorph> context) {
        if (context.getActor() instanceof EggCarrier eggCarrier) {
            eggCarrier.getEggPickupManager().setTargetOvomorph(null);
        }

        NeoMoveToPosAction.onFinish(context);
    }

    private PickUpEggAction() {
        throw new UnsupportedOperationException();
    }
}

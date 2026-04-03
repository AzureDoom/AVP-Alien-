package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.action.MeleeAttackAction;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.dig.DigSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.swim.SwimSensors;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.blib.api.common.goap.v1.action.impl.MoveToPosAction;
import com.just.core.functional.option.Option;
import com.just.goap.StateKey;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;
import com.just.goap.state.Blackboard;

public class CombatActions {

    private static final StateKey<Double> KEY_LAST_DISTANCE_TO_TARGET = StateKey.sensed("move_last_distance");

    private static final StateKey<Integer> KEY_LAST_PROGRESS_TICK = StateKey.sensed("move_last_progress_tick");

    private static final int STUCK_THRESHOLD_IN_TICKS = 20;

    private static final double PROGRESS_THRESHOLD = 0.5;

    public static final Action<Xenomorph> MOVE_TO_TARGET = BLibAction.<Xenomorph>builder("MoveToTargetAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key(), Expressions.Boolean.isFalse())
        .addPrecondition(SwimSensors.NEEDS_WATER_TO_LAND_TRANSITION.key(), Expressions.Boolean.isFalse())
        .addPrecondition(DigSensors.IS_PATH_TO_TARGET_BLOCKED.key(), Expressions.Boolean.isFalse())
        .addEffect(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key().asDerived(), true)
        .withPerformCallback(CombatActions::performMoveToTarget)
        .withFinishCallback(MoveToPosAction::onFinish)
        .build();

    public static final Action<Xenomorph> MELEE_ATTACK = BLibAction.<Xenomorph>builder("MeleeAttackAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key(), Expressions.Boolean.isTrue())
        .addEffect(GOAPSensors.HAS_ATTACK_TARGET.key().asDerived(), false)
        .withPerformCallback(MeleeAttackAction::perform)
        .build();

    private static Action.Signal performMoveToTarget(Action.Context<? extends Xenomorph> context) {
        var xenomorph = context.getActor();
        var worldState = context.getWorldState();
        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);
        var attackTargetOption = worldState.getOrDefault(GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(), Option.none());

        if (attackTargetOption.isNone()) {
            return Action.Signal.ABORT;
        }

        var attackTarget = attackTargetOption.unwrap();
        xenomorph.getLookControl().setLookAt(attackTarget);

        var currentDistance = xenomorph.distanceToSqr(attackTarget);
        var lastDistance = blackboard.getOrDefault(KEY_LAST_DISTANCE_TO_TARGET, Double.MAX_VALUE);
        var lastProgressTick = blackboard.getOrDefault(KEY_LAST_PROGRESS_TICK, xenomorph.tickCount);

        if (currentDistance < lastDistance - PROGRESS_THRESHOLD) {
            blackboard.set(KEY_LAST_DISTANCE_TO_TARGET, currentDistance);
            blackboard.set(KEY_LAST_PROGRESS_TICK, xenomorph.tickCount);
        } else if (xenomorph.tickCount - lastProgressTick >= STUCK_THRESHOLD_IN_TICKS) {
            xenomorph.getXenomorphData().setLastPathFailureTick(xenomorph.tickCount);
            return Action.Signal.ABORT;
        }

        return switch (MoveToPosAction.perform(context, attackTarget.position(), 1.1)) {
            case FINISHED, MOVING -> Action.Signal.CONTINUE;
            case NO_PATH -> {
                xenomorph.getXenomorphData().setLastPathFailureTick(xenomorph.tickCount);
                yield Action.Signal.ABORT;
            }
        };
    }

    private CombatActions() {
        throw new UnsupportedOperationException();
    }
}

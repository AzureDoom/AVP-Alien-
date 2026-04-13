package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.action.MeleeAttackAction;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.dig.DigSensors;
import com.blib.api.common.block.v1.BlockBreakProgressManager;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.blib.api.common.goap.v1.action.impl.MoveToPosAction;
import com.blib.api.common.goap.v1.action.impl.NeoMoveToPosAction;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorUser;
import com.just.core.functional.option.Option;
import com.just.goap.StateKey;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;
import com.just.goap.state.Blackboard;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;

public class CombatActions {

    private static final StateKey<Double> KEY_LAST_DISTANCE_TO_TARGET = StateKey.sensed("move_last_distance");

    private static final StateKey<Integer> KEY_LAST_PROGRESS_TICK = StateKey.sensed("move_last_progress_tick");

    private static final int STUCK_THRESHOLD_IN_TICKS = 20;

    private static final double PROGRESS_THRESHOLD = 0.5;

    public static final Action<Xenomorph> MOVE_TO_TARGET = BLibAction.<Xenomorph>builder("MoveToTargetAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key(), Expressions.Boolean.isFalse())
        .addPrecondition(DigSensors.IS_PATH_TO_TARGET_BLOCKED.key(), Expressions.Boolean.isFalse())
        .addEffect(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key().asDerived(), true)
        .withPerformCallback(CombatActions::performMoveToTarget)
        .withFinishCallback(CombatActions::finishMoveToTarget)
        .build();

    public static final Action<Xenomorph> MELEE_ATTACK = BLibAction.<Xenomorph>builder("MeleeAttackAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key(), Expressions.Boolean.isTrue())
        .addEffect(GOAPSensors.HAS_ATTACK_TARGET.key().asDerived(), false)
        .withPerformCallback(MeleeAttackAction::perform)
        .build();

    public static Action.Signal performMoveToTarget(Action.Context<? extends Xenomorph> context) {
        var xenomorph = context.getActor();
        var worldState = context.getWorldState();
        var attackTargetOption = worldState.getOrDefault(GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(), Option.none());

        if (attackTargetOption.isNone()) {
            return Action.Signal.ABORT;
        }

        var attackTarget = attackTargetOption.unwrap();

        if (xenomorph instanceof PathNavigatorUser) {
            return performWithBLibNav(context, attackTarget);
        }

        xenomorph.getLookControl().setLookAt(attackTarget);

        return performWithVanillaNav(context, attackTarget);
    }

    public static void finishMoveToTarget(Action.Context<? extends Xenomorph> context) {
        if (context.getActor() instanceof PathNavigatorUser) {
            NeoMoveToPosAction.onFinish(context);
        } else {
            MoveToPosAction.onFinish(context);
        }
    }

    private static final float BLOCK_BREAKING_SPEED = 50F;

    private static final double MAX_PREDICTION_DISTANCE_SQUARED = 32.0 * 32.0;

    private static Action.Signal performWithBLibNav(
        Action.Context<? extends Xenomorph> context,
        net.minecraft.world.entity.LivingEntity attackTarget
    ) {
        var xenomorph = context.getActor();

        if (xenomorph instanceof PathNavigatorUser navigatorUser) {
            navigatorUser.getPathNavigator().setExcludedTerrains(null);
        }

        var interceptPos = computeInterceptPoint(xenomorph, attackTarget);
        var result = NeoMoveToPosAction.perform(context, interceptPos, 1.1);

        return switch (result) {
            case FINISHED, MOVING -> Action.Signal.CONTINUE;
            case WAITING_FOR_BLOCK_BREAK -> handleBlockBreak(context);
            case NO_PATH -> {
                context.getActor().getXenomorphData().setLastPathFailureTick(context.getActor().tickCount);
                yield Action.Signal.ABORT;
            }
        };
    }

    private static net.minecraft.world.phys.Vec3 computeInterceptPoint(
        Xenomorph xenomorph,
        net.minecraft.world.entity.LivingEntity target
    ) {
        var targetPos = target.position();
        var targetVelocity = target.getDeltaMovement();

        var horizontalVelocity = new net.minecraft.world.phys.Vec3(targetVelocity.x, 0, targetVelocity.z);
        var horizontalSpeed = horizontalVelocity.length();

        if (horizontalSpeed < 0.01) {
            return targetPos;
        }

        var distance = xenomorph.distanceTo(target);
        var xenomorphSpeed = xenomorph.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);

        if (xenomorphSpeed < 0.01) {
            return targetPos;
        }

        var estimatedTicksToArrive = distance / xenomorphSpeed;
        var predictedPos = targetPos.add(horizontalVelocity.scale(estimatedTicksToArrive));

        if (predictedPos.distanceToSqr(targetPos) > MAX_PREDICTION_DISTANCE_SQUARED) {
            var direction = predictedPos.subtract(targetPos).normalize();

            predictedPos = targetPos.add(direction.scale(32.0));
        }

        return predictedPos;
    }

    private static int getEntityHeight(Xenomorph xenomorph) {
        return (int) Math.ceil(xenomorph.getBbHeight());
    }

    private static final double BLOCK_BREAK_REACH_DISTANCE_SQUARED = 2.5 * 2.5;

    private static Action.Signal handleBlockBreak(Action.Context<? extends Xenomorph> context) {
        var xenomorph = context.getActor();

        if (!(xenomorph instanceof PathNavigatorUser navigatorUser)) {
            return Action.Signal.ABORT;
        }

        var navigator = navigatorUser.getPathNavigator();
        var blockPos = navigator.getBlockToBreak();

        if (blockPos == null) {
            return Action.Signal.ABORT;
        }

        var entityPos = xenomorph.blockPosition();
        var distanceSquared = entityPos.distSqr(blockPos);

        if (distanceSquared > BLOCK_BREAK_REACH_DISTANCE_SQUARED) {
            // Can't reach the block — stop and let the path recompute from our current position.
            navigator.stop();
            return Action.Signal.CONTINUE;
        }

        if (!xenomorph.isAttacking()) {
            xenomorph.runDigAnimation();
        }

        xenomorph.getLookControl().setLookAt(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);

        if (xenomorph.tickCount % 4 == 0) {
            var allCleared = breakBlocksInVolume(xenomorph, blockPos);

            if (allCleared) {
                navigator.confirmBlockBroken();
            }
        }

        return Action.Signal.CONTINUE;
    }

    private static boolean breakBlocksInVolume(Xenomorph xenomorph, BlockPos feetPos) {
        var entityHeight = getEntityHeight(xenomorph);
        var parallelDigCount = xenomorph.getXenomorphData().getParallelDigCount();
        var allCleared = true;
        var brokenThisCycle = 0;

        for (int dy = 0; dy < entityHeight; dy++) {
            var checkPos = feetPos.above(dy);
            var state = xenomorph.level().getBlockState(checkPos);

            if (!state.isSolid()) {
                continue;
            }

            if (brokenThisCycle >= parallelDigCount) {
                allCleared = false;
                continue;
            }

            var soundType = state.getSoundType();

            xenomorph.level()
                .playSound(
                    null,
                    checkPos,
                    soundType.getHitSound(),
                    SoundSource.BLOCKS,
                    (soundType.getVolume() + 1.0F) / 8.0F,
                    soundType.getPitch() * 0.5F
                );

            var result = BlockBreakProgressManager.damage(xenomorph.level(), checkPos, BLOCK_BREAKING_SPEED);

            if (result != BlockBreakProgressManager.Result.DESTROYED) {
                allCleared = false;
            }

            brokenThisCycle++;
        }

        return allCleared;
    }

    private static Action.Signal performWithVanillaNav(
        Action.Context<? extends Xenomorph> context,
        net.minecraft.world.entity.LivingEntity attackTarget
    ) {
        var xenomorph = context.getActor();
        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);

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

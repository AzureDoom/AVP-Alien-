package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai.special_attack;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.RazorClaw;
import com.alien.common.util.AlienPredicates;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.core.functional.option.Option;
import com.just.goap.StateKey;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.Set;

public class RazorClawSpecialAttackActions {

    private static final StateKey<Boolean> KEY_ATTACK_STARTED = StateKey.sensed("razor_claw_special_attack_started");

    public static Action<RazorClaw> createSpecialAttack(RazorClawSpecialAttackConfig config) {
        return BLibAction.<RazorClaw>builder("RazorClawSpecialAttackAction")
            .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
            .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
            .addPrecondition(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key(), Expressions.Boolean.isTrue())
            .addPrecondition(RazorClawSpecialAttackSensors.CAN_SPECIAL_ATTACK.key(), Expressions.Boolean.isTrue())
            .addEffect(RazorClawSpecialAttackSensors.CAN_SPECIAL_ATTACK.key().asDerived(), false)
            .withCost(-2.0F)
            .withPerformCallback(context -> perform(context, config))
            .build();
    }

    private static Action.Signal perform(Action.Context<? extends RazorClaw> context, RazorClawSpecialAttackConfig config) {
        var razorClaw = context.getActor();
        var worldState = context.getWorldState();
        var blackboard = context.getBlackboard(com.just.goap.state.Blackboard.Scope.ACTION);
        var attackTargetOption = worldState.getOrDefault(GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(), Option.<LivingEntity>none());
        var attackStarted = blackboard.getOrDefault(KEY_ATTACK_STARTED, false);

        if (attackStarted) {
            if (razorClaw.isUsingSpecialAttack()) {
                return Action.Signal.CONTINUE;
            }

            blackboard.set(KEY_ATTACK_STARTED, false);
            return Action.Signal.ABORT;
        }

        if (attackTargetOption.isNone() || !razorClaw.getRazorClawData().isSpecialAttackCooldownReady()) {
            return Action.Signal.ABORT;
        }

        var target = attackTargetOption.unwrap();

        razorClaw.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
        razorClaw.getLookControl().setLookAt(target);
        razorClaw.getNavigation().stop();

        razorClaw.startSpecialAttack(config.attackDurationInTicks(), target);
        razorClaw.getRazorClawData().resetSpecialAttackCooldown(config.cooldownInTicks());
        blackboard.set(KEY_ATTACK_STARTED, true);
        return Action.Signal.CONTINUE;
    }

    public static void damageSweptArc(
        RazorClaw razorClaw,
        RazorClawSpecialAttackConfig config,
        int previousElapsedTicks,
        int elapsedTicks,
        float startingYaw,
        Set<Integer> hitEntityIds
    ) {
        var sweepStartTick = config.damageSweepStartTick();
        var sweepEndTick = sweepStartTick + config.damageSweepDurationInTicks();

        if (elapsedTicks <= sweepStartTick || previousElapsedTicks >= sweepEndTick) {
            return;
        }

        var previousSweepTick = Mth.clamp(previousElapsedTicks - sweepStartTick, 0, config.damageSweepDurationInTicks());
        var currentSweepTick = Mth.clamp(elapsedTicks - sweepStartTick, 0, config.damageSweepDurationInTicks());

        if (currentSweepTick <= previousSweepTick) {
            return;
        }

        var previousSweepDegrees = 360.0 * previousSweepTick / config.damageSweepDurationInTicks();
        var currentSweepDegrees = 360.0 * currentSweepTick / config.damageSweepDurationInTicks();
        var arcPadding = config.hitArcDegrees() * 0.5;
        var rangeSquared = config.rangeInBlocks() * config.rangeInBlocks();

        var targets = razorClaw.level().getEntitiesOfClass(
            LivingEntity.class,
            razorClaw.getBoundingBox().inflate(config.rangeInBlocks(), 1.0, config.rangeInBlocks()),
            target -> target != razorClaw
                && target.isAlive()
                && !hitEntityIds.contains(target.getId())
                && razorClaw.distanceToSqr(target) <= rangeSquared
                && AlienPredicates.canTarget(razorClaw, target)
                && razorClaw.getSensing().hasLineOfSight(target)
        );

        for (var target : targets) {
            var targetSweepAngle = computeSweepAngle(razorClaw, target, startingYaw);

            if (targetSweepAngle < previousSweepDegrees - arcPadding || targetSweepAngle > currentSweepDegrees + arcPadding) {
                continue;
            }

            if (razorClaw.doHurtTarget(target)) {
                hitEntityIds.add(target.getId());
            }
        }
    }

    private static double computeSweepAngle(RazorClaw razorClaw, LivingEntity target, float startingYaw) {
        var dx = target.getX() - razorClaw.getX();
        var dz = target.getZ() - razorClaw.getZ();
        var targetYaw = (float) (Mth.atan2(-dx, dz) * Mth.RAD_TO_DEG);
        var relativeYaw = Mth.wrapDegrees(targetYaw - startingYaw);
        return relativeYaw < 0 ? relativeYaw + 360.0 : relativeYaw;
    }

    private RazorClawSpecialAttackActions() {
        throw new UnsupportedOperationException();
    }
}

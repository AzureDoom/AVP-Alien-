package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.action;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.XenomorphAttackType;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.RavagerClawAttackActions;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.core.functional.option.Option;
import com.just.goap.StateKey;
import com.just.goap.action.Action;
import com.just.goap.state.Blackboard;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class MeleeAttackAction {

    private static final StateKey<Boolean> KEY_ATTACK_STARTED = StateKey.sensed("melee_attack_started");

    private static final StateKey<Boolean> KEY_DAMAGE_DEALT = StateKey.sensed("melee_attack_damage_dealt");

    private static final StateKey<Integer> KEY_ELAPSED_ATTACK_TICKS = StateKey.sensed("elapsed_attack_ticks");

    private static final StateKey<Integer> KEY_ATTACK_DURATION = StateKey.sensed("attack_duration");

    private static final float DAMAGE_POINT_PERCENT = 0.5f;

    public static Action.Signal perform(Action.Context<? extends Xenomorph> context) {
        var xenomorph = context.getActor();
        var worldState = context.getWorldState();
        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);
        var attackTargetOption = worldState.getOrDefault(GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(), Option.<LivingEntity>none());

        if (attackTargetOption.isNone()) {
            return Action.Signal.ABORT;
        }

        var attackTarget = attackTargetOption.unwrap();

        xenomorph.getLookControl().setLookAt(attackTarget);

        var attackStarted = blackboard.getOrDefault(KEY_ATTACK_STARTED, false);

        if (!attackStarted) {
            xenomorph.runAttackAnimations();
            var duration = xenomorph.attackDurationInTicks.get();
            blackboard.set(KEY_ATTACK_DURATION, duration);
            blackboard.set(KEY_ELAPSED_ATTACK_TICKS, 0);
            blackboard.set(KEY_ATTACK_STARTED, true);
            blackboard.set(KEY_DAMAGE_DEALT, false);
            return Action.Signal.CONTINUE;
        }

        var elapsedTicks = blackboard.getOrDefault(KEY_ELAPSED_ATTACK_TICKS, 0) + 1;
        var duration = blackboard.getOrDefault(KEY_ATTACK_DURATION, 0);

        blackboard.set(KEY_ELAPSED_ATTACK_TICKS, elapsedTicks);

        var damageTickThreshold = (int) (duration * DAMAGE_POINT_PERCENT);
        var damageDealt = blackboard.getOrDefault(KEY_DAMAGE_DEALT, false);

        if (!damageDealt && elapsedTicks >= damageTickThreshold) {
            if (isRavagerClawAttack(xenomorph)) {
                xenomorph.swing(InteractionHand.MAIN_HAND);
                RavagerClawAttackActions.damageEntitiesInFront((Ravager) xenomorph);
            } else {
                var attackRange = xenomorph.getBbWidth() + 1.0;

                if (xenomorph.distanceTo(attackTarget) <= attackRange && xenomorph.getSensing().hasLineOfSight(attackTarget)) {
                    xenomorph.swing(InteractionHand.MAIN_HAND);
                    xenomorph.doHurtTarget(attackTarget);
                }
            }

            blackboard.set(KEY_DAMAGE_DEALT, true);
        }

        if (elapsedTicks >= duration) {
            blackboard.set(KEY_ATTACK_STARTED, false);
        }

        return Action.Signal.CONTINUE;
    }

    private static boolean isRavagerClawAttack(Xenomorph xenomorph) {
        if (!(xenomorph instanceof Ravager ravager)) {
            return false;
        }

        var attackType = ravager.attackType.get();
        return attackType == XenomorphAttackType.CLAW || attackType == XenomorphAttackType.CLAW_DOUBLE;
    }

    private MeleeAttackAction() {
        throw new UnsupportedOperationException();
    }
}

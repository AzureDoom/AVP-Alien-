package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.action;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.state.Blackboard;
import com.just.core.functional.option.Option;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class MeleeAttackAction {

    private static final StateKey<Boolean> KEY_ATTACK_STARTED = StateKey.sensed("melee_attack_started");

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
        faceXenomorphTowardTarget(xenomorph, attackTarget);

        var attackStarted = blackboard.getOrDefault(KEY_ATTACK_STARTED, false);

        if (!attackStarted) {
            xenomorph.runAttackAnimations();

            if (!xenomorph.isAttacking()) {
                return Action.Signal.ABORT;
            }

            blackboard.set(KEY_ATTACK_STARTED, true);
            return Action.Signal.CONTINUE;
        }

        if (!xenomorph.isAttacking()) {
            blackboard.set(KEY_ATTACK_STARTED, false);
        }

        return Action.Signal.CONTINUE;
    }

    private static void faceXenomorphTowardTarget(Xenomorph xenomorph, LivingEntity target) {
        if (!(xenomorph instanceof Ravager ravager)) {
            return;
        }

        var dx = target.getX() - ravager.getX();
        var dz = target.getZ() - ravager.getZ();
        var yaw = (float) (Mth.atan2(-dx, dz) * Mth.RAD_TO_DEG);

        ravager.setYRot(yaw);
        ravager.setYHeadRot(yaw);
        ravager.setYBodyRot(yaw);
    }

    private MeleeAttackAction() {
        throw new UnsupportedOperationException();
    }
}

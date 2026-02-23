package com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.attach_to_host.action;

import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.core.functional.option.Option;
import com.just.goap.StateKey;
import com.just.goap.action.Action;
import com.just.goap.state.Blackboard;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;

public class LungeAction {

    private static final StateKey<Boolean> KEY_HAS_LUNGED = StateKey.sensed("has_lunged");

    private static final StateKey<Integer> KEY_WIND_UP_TICKS_REMAINING = StateKey.sensed("wind_up_ticks_remaining");

    private static final int LUNGE_WIND_UP_TICKS = 10;

    public static Action.Signal perform(Action.Context<? extends Facehugger> context) {
        var facehugger = context.getActor();
        var worldState = context.getWorldState();
        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);
        var attackTargetOption = worldState.getOrDefault(GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(), Option.<LivingEntity>none());

        if (attackTargetOption.isNone()) {
            return Action.Signal.ABORT;
        }

        var attackTarget = attackTargetOption.unwrap();

        if (!facehugger.onGround()) {
            return Action.Signal.ABORT;
        }

        // Look at the target.
        facehugger.lookAt(EntityAnchorArgument.Anchor.EYES, attackTarget.getEyePosition());
        facehugger.getLookControl().setLookAt(attackTarget);

        // Wind-up phase: pause before lunging.
        var windUpTicksRemaining = blackboard.getOrDefault(KEY_WIND_UP_TICKS_REMAINING, LUNGE_WIND_UP_TICKS);

        // If recently hurt or target is moving away, skip wind-up.
        if (facehugger.getLastHurtByMobTimestamp() > 0 && facehugger.tickCount - facehugger.getLastHurtByMobTimestamp() < 20) {
            windUpTicksRemaining = 0;
        }

        if (windUpTicksRemaining > 0) {
            windUpTicksRemaining--;
            blackboard.set(KEY_WIND_UP_TICKS_REMAINING, windUpTicksRemaining);
            facehugger.getNavigation().stop();
            return Action.Signal.CONTINUE;
        }

        // Perform the lunge.
        var hasLunged = blackboard.getOrDefault(KEY_HAS_LUNGED, false);

        if (!hasLunged) {
            var distanceToTarget = facehugger.distanceTo(attackTarget);
            var deltaMovement = facehugger.getDeltaMovement().scale(0.2);
            var vectorDifference = attackTarget.getEyePosition().subtract(facehugger.getEyePosition());

            vectorDifference = vectorDifference.normalize()
                .scale(0.2 * distanceToTarget)
                .add(deltaMovement.x, 0, deltaMovement.z);

            facehugger.setDeltaMovement(vectorDifference.x, Math.max(0.6, vectorDifference.y), vectorDifference.z);

            facehugger.getAnimationDispatcher().lunge();
            blackboard.set(KEY_HAS_LUNGED, true);
        }

        return Action.Signal.CONTINUE;
    }
}

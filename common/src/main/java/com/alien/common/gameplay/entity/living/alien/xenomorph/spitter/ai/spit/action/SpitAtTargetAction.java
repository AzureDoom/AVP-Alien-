package com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.ai.spit.action;

import com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.Spitter;
import com.alien.common.gameplay.entity.projectile.AcidSpit;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.state.Blackboard;
import com.just.core.functional.option.Option;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;

public class SpitAtTargetAction {

    private static final float PROJECTILE_POWER = 1.5F;

    private static final float PROJECTILE_INACCURACY = 2.0F;

    private static final double ARC_COMPENSATION_FACTOR = 0.1;

    private static final int WIND_UP_TICKS = 10;

    private static final StateKey<Integer> KEY_WIND_UP_REMAINING = StateKey.sensed("spit_wind_up_remaining");

    private static final StateKey<Boolean> KEY_HAS_FIRED = StateKey.sensed("spit_has_fired");

    public static Action.Signal perform(Action.Context<? extends Spitter> context) {
        var spitter = context.getActor();
        var worldState = context.getWorldState();
        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);
        var attackTargetOption = worldState.getOrDefault(
            GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(),
            Option.<LivingEntity>none()
        );

        if (attackTargetOption.isNone()) {
            return Action.Signal.ABORT;
        }

        var target = attackTargetOption.unwrap();

        spitter.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
        spitter.getLookControl().setLookAt(target);
        spitter.getNavigation().stop();

        if (blackboard.getOrDefault(KEY_HAS_FIRED, false)) {
            return Action.Signal.ABORT;
        }

        var isStationary = spitter.getDeltaMovement().horizontalDistanceSqr() < 0.001;

        if (!isStationary) {
            return Action.Signal.CONTINUE;
        }

        var windUpRemaining = blackboard.getOrDefault(KEY_WIND_UP_REMAINING, WIND_UP_TICKS);

        if (windUpRemaining > 0) {
            blackboard.set(KEY_WIND_UP_REMAINING, windUpRemaining - 1);
            return Action.Signal.CONTINUE;
        }

        fireProjectile(spitter, target);
        blackboard.set(KEY_HAS_FIRED, true);

        return Action.Signal.ABORT;
    }

    private static void fireProjectile(Spitter spitter, LivingEntity target) {
        var spit = new AcidSpit(spitter, spitter.level());
        var targetEyePos = target.getEyePosition();
        var directionX = targetEyePos.x - spitter.getX();
        var directionY = targetEyePos.y - spitter.getEyeY();
        var directionZ = targetEyePos.z - spitter.getZ();
        var gravityCompensation = computeGravityCompensation(directionX, directionZ);

        spit.shoot(directionX, directionY + gravityCompensation, directionZ, PROJECTILE_POWER, PROJECTILE_INACCURACY);
        spitter.level().addFreshEntity(spit);
        spitter.getSpitterData().setLastSpitTick(spitter.tickCount);
    }

    private static double computeGravityCompensation(double directionX, double directionZ) {
        var horizontalDistance = Math.sqrt(directionX * directionX + directionZ * directionZ);
        return horizontalDistance * ARC_COMPENSATION_FACTOR;
    }

    private SpitAtTargetAction() {
        throw new UnsupportedOperationException();
    }
}

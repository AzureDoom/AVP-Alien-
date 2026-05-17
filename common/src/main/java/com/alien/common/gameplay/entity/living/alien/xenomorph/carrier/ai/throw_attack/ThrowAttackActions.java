package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai.throw_attack;

import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.state.Blackboard;
import com.just.core.functional.option.Option;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ThrowAttackActions {

    private static final StateKey<Boolean> KEY_THROW_STARTED = StateKey.sensed("throw_started");

    private static final StateKey<Boolean> KEY_HAS_THROWN = StateKey.sensed("has_thrown");

    private static final StateKey<Integer> KEY_ELAPSED_THROW_TICKS = StateKey.sensed("elapsed_throw_ticks");

    private static final StateKey<Integer> KEY_THROW_DURATION = StateKey.sensed("throw_duration");

    private static final float THROW_POINT_PERCENT = 0.5F;

    public static final Action<Carrier> THROW_FACEHUGGER = BLibAction.<Carrier>builder("ThrowFacehuggerAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
        .addPrecondition(ThrowAttackSensors.CAN_THROW_FACEHUGGER.key(), Expressions.Boolean.isTrue())
        .addEffect(GOAPSensors.HAS_ATTACK_TARGET.key().asDerived(), false)
        .withCost(-1.0F)
        .withPerformCallback(ThrowAttackActions::perform)
        .build();

    private static Action.Signal perform(Action.Context<? extends Carrier> context) {
        var carrier = context.getActor();
        var worldState = context.getWorldState();
        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);

        var attackTargetOption = worldState.getOrDefault(GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(), Option.<LivingEntity>none());

        if (attackTargetOption.isNone()) {
            return Action.Signal.ABORT;
        }

        var target = attackTargetOption.unwrap();
        carrier.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
        carrier.getLookControl().setLookAt(target);
        carrier.getNavigation().stop();

        var throwStarted = blackboard.getOrDefault(KEY_THROW_STARTED, false);

        if (!throwStarted) {
            carrier.throwFacehugger();
            var duration = carrier.attackDurationInTicks.get();
            blackboard.set(KEY_THROW_DURATION, duration);
            blackboard.set(KEY_ELAPSED_THROW_TICKS, 0);
            blackboard.set(KEY_THROW_STARTED, true);
            blackboard.set(KEY_HAS_THROWN, false);
            return Action.Signal.CONTINUE;
        }

        var elapsedTicks = blackboard.getOrDefault(KEY_ELAPSED_THROW_TICKS, 0) + 1;
        var duration = blackboard.getOrDefault(KEY_THROW_DURATION, 0);

        blackboard.set(KEY_ELAPSED_THROW_TICKS, elapsedTicks);

        var throwTickThreshold = (int) (duration * THROW_POINT_PERCENT);
        var hasThrown = blackboard.getOrDefault(KEY_HAS_THROWN, false);

        if (!hasThrown && elapsedTicks >= throwTickThreshold) {
            if (!releaseFacehugger(carrier, target)) {
                return Action.Signal.ABORT;
            }
            blackboard.set(KEY_HAS_THROWN, true);
        }

        if (elapsedTicks >= duration) {
            blackboard.set(KEY_THROW_STARTED, false);
            return Action.Signal.ABORT;
        }

        return Action.Signal.CONTINUE;
    }

    private static boolean releaseFacehugger(Carrier carrier, LivingEntity target) {
        carrier.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());

        var facehugger = carrier.getPassengers()
            .stream()
            .filter(p -> p.getType().is(AlienEntityTypeTags.FACEHUGGERS))
            .filter(p -> p instanceof Facehugger)
            .map(p -> (Facehugger) p)
            .findFirst()
            .orElse(null);

        if (facehugger == null) {
            return false;
        }

        facehugger.stopRiding();

        var direction = target.getEyePosition().subtract(carrier.getEyePosition());
        var distance = direction.length();
        var normalized = direction.normalize();

        var horizontalSpeed = Math.min(distance * 0.1, 1.5);
        var verticalBoost = 0.4 + distance * 0.02;

        facehugger.setDeltaMovement(
            new Vec3(
                normalized.x * horizontalSpeed,
                normalized.y * horizontalSpeed + verticalBoost,
                normalized.z * horizontalSpeed
            )
        );

        facehugger.isLunging.set(true);
        carrier.getCarrierData().resetThrowCooldown(carrier.getRandom());

        return true;
    }

    private ThrowAttackActions() {
        throw new UnsupportedOperationException();
    }
}

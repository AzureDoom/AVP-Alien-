package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai.throw_attack;

import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.core.functional.option.Option;
import com.just.goap.StateKey;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;
import com.just.goap.state.Blackboard;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ThrowAttackActions {

    private static final StateKey<Integer> KEY_WIND_UP_TICKS = StateKey.sensed("throw_wind_up_ticks");

    private static final StateKey<Boolean> KEY_HAS_THROWN = StateKey.sensed("has_thrown");

    private static final int WIND_UP_DURATION_TICKS = 15;

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
        carrier.getLookControl().setLookAt(target);
        carrier.getNavigation().stop();

        var windUpTicks = blackboard.getOrDefault(KEY_WIND_UP_TICKS, WIND_UP_DURATION_TICKS);

        if (windUpTicks > 0) {
            blackboard.set(KEY_WIND_UP_TICKS, windUpTicks - 1);
            return Action.Signal.CONTINUE;
        }

        var hasThrown = blackboard.getOrDefault(KEY_HAS_THROWN, false);

        if (!hasThrown) {
            var facehugger = carrier.getPassengers().stream()
                .filter(p -> p.getType().is(AlienEntityTypeTags.FACEHUGGERS))
                .filter(p -> p instanceof Facehugger)
                .map(p -> (Facehugger) p)
                .findFirst()
                .orElse(null);

            if (facehugger == null) {
                return Action.Signal.ABORT;
            }

            facehugger.stopRiding();

            var direction = target.getEyePosition().subtract(carrier.getEyePosition());
            var distance = direction.length();
            var normalized = direction.normalize();

            var horizontalSpeed = Math.min(distance * 0.1, 1.5);
            var verticalBoost = 0.4 + distance * 0.02;

            facehugger.setDeltaMovement(new Vec3(
                normalized.x * horizontalSpeed,
                normalized.y * horizontalSpeed + verticalBoost,
                normalized.z * horizontalSpeed
            ));

            facehugger.isLunging.set(true);

            carrier.throwFacehugger();

            blackboard.set(KEY_HAS_THROWN, true);
        }

        return Action.Signal.ABORT;
    }

    private ThrowAttackActions() {
        throw new UnsupportedOperationException();
    }
}

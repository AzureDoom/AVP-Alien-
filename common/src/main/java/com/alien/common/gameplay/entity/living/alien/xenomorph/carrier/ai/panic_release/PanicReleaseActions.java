package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai.panic_release;

import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.core.functional.option.Option;
import com.just.goap.StateKey;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;
import com.just.goap.state.Blackboard;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;

public class PanicReleaseActions {

    private static final StateKey<Boolean> KEY_ATTACK_STARTED = StateKey.sensed("panic_release_started");

    private static final StateKey<Boolean> KEY_HAS_RELEASED = StateKey.sensed("panic_release_has_released");

    private static final StateKey<Integer> KEY_ELAPSED_TICKS = StateKey.sensed("panic_release_elapsed_ticks");

    private static final StateKey<Integer> KEY_DURATION = StateKey.sensed("panic_release_duration");

    private static final float RELEASE_POINT_PERCENT = 0.5F;

    public static final Action<Carrier> PANIC_RELEASE_FACEHUGGERS = BLibAction.<Carrier>builder("PanicReleaseFacehuggersAction")
        .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
        .addPrecondition(PanicReleaseSensors.SHOULD_PANIC_RELEASE.key(), Expressions.Boolean.isTrue())
        .addEffect(PanicReleaseSensors.SHOULD_PANIC_RELEASE.key().asDerived(), false)
        .withCost(-2.0F)
        .withPerformCallback(PanicReleaseActions::perform)
        .build();

    private static Action.Signal perform(Action.Context<? extends Carrier> context) {
        var carrier = context.getActor();
        var worldState = context.getWorldState();
        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);

        faceTargetIfPresent(carrier, worldState.getOrDefault(GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(), Option.<LivingEntity>none()));
        carrier.getNavigation().stop();

        var attackStarted = blackboard.getOrDefault(KEY_ATTACK_STARTED, false);

        if (!attackStarted) {
            carrier.screamReleaseFacehuggers();
            blackboard.set(KEY_DURATION, carrier.attackDurationInTicks.get());
            blackboard.set(KEY_ELAPSED_TICKS, 0);
            blackboard.set(KEY_ATTACK_STARTED, true);
            blackboard.set(KEY_HAS_RELEASED, false);
            return Action.Signal.CONTINUE;
        }

        var elapsedTicks = blackboard.getOrDefault(KEY_ELAPSED_TICKS, 0) + 1;
        var duration = blackboard.getOrDefault(KEY_DURATION, 0);

        blackboard.set(KEY_ELAPSED_TICKS, elapsedTicks);

        var releaseTickThreshold = (int) (duration * RELEASE_POINT_PERCENT);
        var hasReleased = blackboard.getOrDefault(KEY_HAS_RELEASED, false);

        if (!hasReleased && elapsedTicks >= releaseTickThreshold) {
            carrier.releaseAllFacehuggers();
            blackboard.set(KEY_HAS_RELEASED, true);
        }

        if (elapsedTicks >= duration) {
            blackboard.set(KEY_ATTACK_STARTED, false);
            return Action.Signal.ABORT;
        }

        return Action.Signal.CONTINUE;
    }

    private static void faceTargetIfPresent(Carrier carrier, Option<LivingEntity> targetOption) {
        if (targetOption.isNone()) {
            return;
        }

        var target = targetOption.unwrap();
        carrier.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
        carrier.getLookControl().setLookAt(target);
    }

    private PanicReleaseActions() {
        throw new UnsupportedOperationException();
    }
}

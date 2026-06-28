package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai.hibernation;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;
import net.minecraft.world.phys.Vec3;

/**
 * Action for the hibernation hold (Stage 3a). While she is in HIBERNATION it pins her in place at the anchor — she
 * sleeps. The hibernate pose itself is driven client-side by {@code QueenAnimator} off the synced {@code isHibernating}
 * flag (set by the phase manager), not here, since this perform runs server-side and AzCommand dispatch is client-only.
 * Holds the MOVE+LOOK masks with a negative cost so the planner keeps her
 * asleep over idle wandering. The sleep clock and the wake-to-found transition live on the phase manager, not here; this
 * action only owns posture and animation.
 * <p>
 * Stage 3b will add the disturbance branch (damage above a threshold releases the hold so she can defend, then returns).
 */
public final class HibernationActions {

    /** Negative so the planner prefers holding her asleep over idle wandering. */
    private static final float COST = -1.0F;

    public static final Action<Xenomorph> HIBERNATE_HOLD = BLibAction.<Xenomorph>builder("HibernateHoldAction")
            .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
            .addPrecondition(HibernationSensors.IS_HIBERNATING.key(), Expressions.Boolean.isTrue())
            .addEffect(HibernationSensors.IS_HIBERNATING.key().asDerived(), false)
            .withCost(COST)
            .withPerformCallback(context -> {
                var actor = context.getActor();
                actor.setDeltaMovement(Vec3.ZERO);
                actor.getNavigation().stop();
                actor.setTarget(null);
                // Animation is driven client-side by QueenAnimator off the synced isHibernating flag — never dispatched
                // here, since this perform runs on the server and AzCommand dispatch is client-only.
                return Action.Signal.CONTINUE;
            })
            .build();

    private HibernationActions() {
        throw new UnsupportedOperationException();
    }
}
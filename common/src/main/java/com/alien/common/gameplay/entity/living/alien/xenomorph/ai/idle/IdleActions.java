package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.idle;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.blib.api.common.goap.v1.action.impl.NeoWanderAction;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorUser;
import com.blib.api.common.pathfinding.v1.terrain.TerrainType;
import com.just.goap.action.Action;
import com.just.goap.condition.expression.Expressions;

import java.util.EnumSet;

public class IdleActions {

    public static final Action<Xenomorph> WANDER = BLibAction.<Xenomorph>builder("WanderAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(IdleSensors.IS_BORED.key(), Expressions.Boolean.isTrue())
        .addEffect(IdleSensors.IS_BORED.key().asDerived(), false)
        .withPerformCallback(context -> {
            if (context.getActor() instanceof PathNavigatorUser navigatorUser) {
                navigatorUser.getPathNavigator()
                    .setExcludedTerrains(EnumSet.of(TerrainType.BREAKABLE, TerrainType.WATER));
            }

            return NeoWanderAction.perform(
                context,
                10,
                7,
                0.5,
                ctx -> ctx.getActor().getXenomorphData().resetTicksUntilBored()
            );
        })
        .withFinishCallback(context -> {
            if (context.getActor() instanceof PathNavigatorUser navigatorUser) {
                navigatorUser.getPathNavigator().setExcludedTerrains(null);
            }

            NeoWanderAction.onFinish(context);
        })
        .build();

    private IdleActions() {
        throw new UnsupportedOperationException();
    }
}

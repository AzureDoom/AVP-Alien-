package com.alien.common.gameplay.entity.living.alien.xenomorph.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatGoals;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.dig.DigActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.dig.DigSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.idle.IdleActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.idle.IdleGoals;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.idle.IdleSensors;
import com.alien.common.util.AlienPredicates;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;
import com.just.goap.plan.ReplanPolicies;

public class XenomorphGOAP {

    public static <T extends Xenomorph> Graph.Builder<T> applyBaseGraph(Graph.Builder<T> graphBuilder) {
        return graphBuilder
            .apply(XenomorphGOAP::addSensorsPackage)
            .apply(XenomorphGOAP::addCombatPackage)
            .apply(XenomorphGOAP::addDigPackage)
            .apply(XenomorphGOAP::addIdlePackage);
    }

    public static <T extends Xenomorph> Agent.Builder<T> applyBaseAgentProperties(Agent.Builder<T> agentBuilder) {
        return agentBuilder.withReplanPolicy(
            ReplanPolicies.anyOf(
                ReplanPolicies.ifNoActivePlans(),
                ReplanPolicies.custom(context -> context.agent().getActor().tickCount % 20 == 0),
                ReplanPolicies.custom(context -> {
                    var isOnFire = context.worldState().getOrDefault(GOAPSensors.IS_ON_FIRE.key(), false);
                    var wasOnFire = context.previousWorldState().getOrDefault(GOAPSensors.IS_ON_FIRE.key(), false);
                    return !wasOnFire && isOnFire;
                }),
                ReplanPolicies.custom(context -> {
                    var currentHealthRatio = context.worldState().getOrDefault(GOAPSensors.HEALTH_RATIO.key(), 0F);
                    var previousHealthRatio = context.previousWorldState().getOrDefault(GOAPSensors.HEALTH_RATIO.key(), 0F);
                    return currentHealthRatio < previousHealthRatio;
                })
            )
        );
    }

    private static <T extends Xenomorph> Graph.Builder<T> addSensorsPackage(Graph.Builder<T> graphBuilder) {
        graphBuilder.addSensor(GOAPSensors.IS_ON_GROUND);
        graphBuilder.addSensor(GOAPSensors.IS_ON_FIRE);
        graphBuilder.addSensor(GOAPSensors.HEALTH_RATIO);

        return graphBuilder;
    }

    private static <T extends Xenomorph> Graph.Builder<T> addCombatPackage(Graph.Builder<T> graphBuilder) {
        graphBuilder.addGoal(CombatGoals.KILL_TARGET);

        graphBuilder.addAction(CombatActions.MOVE_TO_TARGET);
        graphBuilder.addAction(CombatActions.MELEE_ATTACK);

        graphBuilder.addSensor(
            GOAPSensors.nearbyAttackableTargetsFactory(AlienPredicates::canTarget)
        );
        graphBuilder.addSensor(GOAPSensors.NEAREST_ATTACKABLE_TARGETS);
        graphBuilder.addSensor(GOAPSensors.NEAREST_ATTACKABLE_TARGET);
        graphBuilder.addSensor(GOAPSensors.HAS_ATTACK_TARGET);
        graphBuilder.addSensor(CombatSensors.IS_TARGET_IN_MELEE_RANGE);

        return graphBuilder;
    }

    private static <T extends Xenomorph> Graph.Builder<T> addDigPackage(Graph.Builder<T> graphBuilder) {
        graphBuilder.addAction(DigActions.DIG_TO_TARGET);

        graphBuilder.addSensor(DigSensors.IS_PATH_TO_TARGET_BLOCKED);

        return graphBuilder;
    }

    private static <T extends Xenomorph> Graph.Builder<T> addIdlePackage(Graph.Builder<T> graphBuilder) {
        graphBuilder.addGoal(IdleGoals.SATISFY_BOREDOM);

        graphBuilder.addAction(IdleActions.WANDER);

        graphBuilder.addSensor(IdleSensors.IS_BORED);

        return graphBuilder;
    }

    private XenomorphGOAP() {
        throw new UnsupportedOperationException();
    }
}

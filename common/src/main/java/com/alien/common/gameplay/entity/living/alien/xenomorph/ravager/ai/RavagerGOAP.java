package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack.RavagerSpecialAttackActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack.RavagerSpecialAttackConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack.RavagerSpecialAttackSensors;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.pathfinding.v1.navigator.PathNavigatorUser;
import com.just.ai.goap.Agent;
import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.goal.Goal;
import com.just.ai.goap.graph.Graph;
import com.just.ai.goap.plan.ReplanPolicies;

public class RavagerGOAP {

    public static final Graph<Ravager> GRAPH = Graph.<Ravager>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(RavagerGOAP::addSpecialAttackPackage)
        .build();

    public static Agent.Builder<Ravager> applyAgentProperties(Agent.Builder<Ravager> agentBuilder) {
        return agentBuilder.withReplanPolicy(
            ReplanPolicies.anyOf(
                ReplanPolicies.custom(context -> !context.agent().getActor().isUsingSpecialAttack() && !context.agent().hasPlan()),
                ReplanPolicies.custom(context -> {
                    var actor = context.agent().getActor();

                    if (actor.isUsingSpecialAttack()) {
                        return false;
                    }

                    if (actor instanceof PathNavigatorUser user && user.getPathNavigator().isWaitingForBlockBreak()) {
                        return false;
                    }

                    return actor.tickCount % 20 == 0;
                }),
                ReplanPolicies.custom(context -> {
                    if (context.agent().getActor().isUsingSpecialAttack()) {
                        return false;
                    }

                    var isOnFire = context.worldState().getOrDefault(GOAPSensors.IS_ON_FIRE.key(), false);
                    var wasOnFire = context.previousWorldState().getOrDefault(GOAPSensors.IS_ON_FIRE.key(), false);
                    return !wasOnFire && isOnFire;
                }),
                ReplanPolicies.custom(context -> {
                    if (context.agent().getActor().isUsingSpecialAttack()) {
                        return false;
                    }

                    var currentHealthRatio = context.worldState().getOrDefault(GOAPSensors.HEALTH_RATIO.key(), 0F);
                    var previousHealthRatio = context.previousWorldState().getOrDefault(GOAPSensors.HEALTH_RATIO.key(), 0F);
                    return currentHealthRatio < previousHealthRatio;
                })
            )
        );
    }

    private static Graph.Builder<Ravager> addSpecialAttackPackage(Graph.Builder<Ravager> graphBuilder) {
        graphBuilder.addGoal(
            Goal.builder("RavagerSpecialAttackGoal")
                .addPrecondition(RavagerSpecialAttackSensors.CAN_SPECIAL_ATTACK.key(), Expressions.Boolean.isTrue())
                .addDesiredCondition(RavagerSpecialAttackSensors.CAN_SPECIAL_ATTACK.key().asDerived(), Expressions.Boolean.isFalse())
                .build()
        );
        graphBuilder.addAction(RavagerSpecialAttackActions.createSpecialAttack(RavagerSpecialAttackConfig.DEFAULT));
        graphBuilder.addSensor(RavagerSpecialAttackSensors.IS_SPECIAL_ATTACK_COOLDOWN_READY);
        graphBuilder.addSensor(RavagerSpecialAttackSensors.CAN_SPECIAL_ATTACK);
        return graphBuilder;
    }

    private RavagerGOAP() {
        throw new UnsupportedOperationException();
    }
}

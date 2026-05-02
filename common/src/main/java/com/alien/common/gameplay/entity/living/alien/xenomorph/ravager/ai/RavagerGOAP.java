package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack.RavagerSpecialAttackActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack.RavagerSpecialAttackConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack.RavagerSpecialAttackSensors;
import com.just.ai.goap.Agent;
import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.goal.Goal;
import com.just.ai.goap.graph.Graph;

public class RavagerGOAP {

    public static final Graph<Ravager> GRAPH = Graph.<Ravager>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(RavagerGOAP::addSpecialAttackPackage)
        .build();

    public static Agent.Builder<Ravager> applyAgentProperties(Agent.Builder<Ravager> agentBuilder) {
        return XenomorphGOAP.applySpecialAttackAgentProperties(agentBuilder);
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

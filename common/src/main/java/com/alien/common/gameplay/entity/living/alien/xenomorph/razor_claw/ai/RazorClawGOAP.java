package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.RazorClaw;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai.special_attack.RazorClawSpecialAttackActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai.special_attack.RazorClawSpecialAttackConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai.special_attack.RazorClawSpecialAttackSensors;
import com.just.ai.goap.Agent;
import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.goal.Goal;
import com.just.ai.goap.graph.Graph;

public class RazorClawGOAP {

    public static final Graph<RazorClaw> GRAPH = Graph.<RazorClaw>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(RazorClawGOAP::addSpecialAttackPackage)
        .build();

    public static Agent.Builder<RazorClaw> applyAgentProperties(Agent.Builder<RazorClaw> agentBuilder) {
        return XenomorphGOAP.applySpecialAttackAgentProperties(agentBuilder);
    }

    private static Graph.Builder<RazorClaw> addSpecialAttackPackage(Graph.Builder<RazorClaw> graphBuilder) {
        graphBuilder.addGoal(
            Goal.builder("RazorClawSpecialAttackGoal")
                .addPrecondition(RazorClawSpecialAttackSensors.CAN_SPECIAL_ATTACK.key(), Expressions.Boolean.isTrue())
                .addDesiredCondition(RazorClawSpecialAttackSensors.CAN_SPECIAL_ATTACK.key().asDerived(), Expressions.Boolean.isFalse())
                .build()
        );
        graphBuilder.addAction(RazorClawSpecialAttackActions.createSpecialAttack(RazorClawSpecialAttackConfig.DEFAULT));
        graphBuilder.addSensor(RazorClawSpecialAttackSensors.IS_SPECIAL_ATTACK_COOLDOWN_READY);
        graphBuilder.addSensor(RazorClawSpecialAttackSensors.CAN_SPECIAL_ATTACK);
        return graphBuilder;
    }

    private RazorClawGOAP() {
        throw new UnsupportedOperationException();
    }
}

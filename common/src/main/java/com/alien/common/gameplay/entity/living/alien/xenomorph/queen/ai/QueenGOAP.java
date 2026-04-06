package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class QueenGOAP {

    public static final Graph<Queen> GRAPH = Graph.<Queen>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(QueenGOAP::addEggLayingPackage)
        .build();

    public static Agent.Builder<Queen> applyAgentProperties(Agent.Builder<Queen> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Queen> addEggLayingPackage(Graph.Builder<Queen> graphBuilder) {
        graphBuilder.addGoal(EggLayingGoals.LAY_EGG);

        graphBuilder.addAction(EggLayingActions.LAY_EGG);

        graphBuilder.addSensor(EggLayingSensors.CAN_LAY_EGG);

        return graphBuilder;
    }

    private QueenGOAP() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying.EggLayingActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying.EggLayingGoals;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying.EggLayingSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

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

        graphBuilder.addAction(EggLayingActions.layEgg());

        graphBuilder.addSensor(EggLayingSensors.canLayEgg());

        return graphBuilder;
    }

    private QueenGOAP() {
        throw new UnsupportedOperationException();
    }
}

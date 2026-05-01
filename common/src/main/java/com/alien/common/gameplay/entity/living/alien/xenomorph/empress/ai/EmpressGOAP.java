package com.alien.common.gameplay.entity.living.alien.xenomorph.empress.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying.EggLayingActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying.EggLayingGoals;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying.EggLayingSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.empress.Empress;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class EmpressGOAP {

    public static final Graph<Empress> GRAPH = Graph.<Empress>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(EmpressGOAP::addEggLayingPackage)
        .build();

    public static Agent.Builder<Empress> applyAgentProperties(Agent.Builder<Empress> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Empress> addEggLayingPackage(Graph.Builder<Empress> graphBuilder) {
        graphBuilder.addGoal(EggLayingGoals.LAY_EGG);

        graphBuilder.addAction(EggLayingActions.layEgg());

        graphBuilder.addSensor(EggLayingSensors.canLayEgg());

        return graphBuilder;
    }

    private EmpressGOAP() {
        throw new UnsupportedOperationException();
    }
}

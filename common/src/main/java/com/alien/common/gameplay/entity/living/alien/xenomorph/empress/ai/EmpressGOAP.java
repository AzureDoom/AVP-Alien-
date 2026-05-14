package com.alien.common.gameplay.entity.living.alien.xenomorph.empress.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.empress.Empress;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

public class EmpressGOAP {

    public static final Graph<Empress> GRAPH = Graph.<Empress>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(XenomorphGOAP::addEggLayingPackage)
        .build();

    public static final Graph<Empress> OVIPOSITOR_GRAPH = Graph.<Empress>builder()
        .apply(XenomorphGOAP::applyEggLayingOnlyGraph)
        .build();

    public static Agent.Builder<Empress> applyAgentProperties(Agent.Builder<Empress> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private EmpressGOAP() {
        throw new UnsupportedOperationException();
    }
}

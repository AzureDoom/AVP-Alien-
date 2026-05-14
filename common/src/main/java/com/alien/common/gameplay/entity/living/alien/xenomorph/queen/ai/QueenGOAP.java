package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

public class QueenGOAP {

    public static final Graph<Queen> GRAPH = Graph.<Queen>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(XenomorphGOAP::addEggLayingPackage)
        .build();

    public static final Graph<Queen> OVIPOSITOR_GRAPH = Graph.<Queen>builder()
        .apply(XenomorphGOAP::applyEggLayingOnlyGraph)
        .build();

    public static Agent.Builder<Queen> applyAgentProperties(Agent.Builder<Queen> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private QueenGOAP() {
        throw new UnsupportedOperationException();
    }
}

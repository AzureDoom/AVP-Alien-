package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class RavagerGOAP {

    public static final Graph<Ravager> GRAPH = Graph.<Ravager>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .build();

    public static Agent.Builder<Ravager> applyAgentProperties(Agent.Builder<Ravager> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private RavagerGOAP() {
        throw new UnsupportedOperationException();
    }
}

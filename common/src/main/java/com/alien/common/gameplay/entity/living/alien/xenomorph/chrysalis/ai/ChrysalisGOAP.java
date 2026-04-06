package com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.Chrysalis;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class ChrysalisGOAP {

    public static final Graph<Chrysalis> GRAPH = Graph.<Chrysalis>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .build();

    public static Agent.Builder<Chrysalis> applyAgentProperties(Agent.Builder<Chrysalis> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private ChrysalisGOAP() {
        throw new UnsupportedOperationException();
    }
}

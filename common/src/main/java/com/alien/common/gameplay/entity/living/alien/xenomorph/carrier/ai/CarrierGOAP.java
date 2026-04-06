package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class CarrierGOAP {

    public static final Graph<Carrier> GRAPH = Graph.<Carrier>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .build();

    public static Agent.Builder<Carrier> applyAgentProperties(Agent.Builder<Carrier> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private CarrierGOAP() {
        throw new UnsupportedOperationException();
    }
}

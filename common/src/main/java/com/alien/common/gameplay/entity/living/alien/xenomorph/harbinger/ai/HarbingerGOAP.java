package com.alien.common.gameplay.entity.living.alien.xenomorph.harbinger.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.harbinger.Harbinger;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class HarbingerGOAP {

    public static final Graph<Harbinger> GRAPH = Graph.<Harbinger>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .build();

    public static Agent.Builder<Harbinger> applyAgentProperties(Agent.Builder<Harbinger> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private HarbingerGOAP() {
        throw new UnsupportedOperationException();
    }
}

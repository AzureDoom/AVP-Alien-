package com.alien.common.gameplay.entity.living.alien.xenomorph.predalien.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.predalien.Predalien;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class PredalienGOAP {

    public static final Graph<Predalien> GRAPH = Graph.<Predalien>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .build();

    public static Agent.Builder<Predalien> applyAgentProperties(Agent.Builder<Predalien> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private PredalienGOAP() {
        throw new UnsupportedOperationException();
    }
}

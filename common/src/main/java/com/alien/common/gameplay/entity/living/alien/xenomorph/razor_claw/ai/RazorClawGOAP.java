package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.RazorClaw;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class RazorClawGOAP {

    public static final Graph<RazorClaw> GRAPH = Graph.<RazorClaw>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .build();

    public static Agent.Builder<RazorClaw> applyAgentProperties(Agent.Builder<RazorClaw> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private RazorClawGOAP() {
        throw new UnsupportedOperationException();
    }
}

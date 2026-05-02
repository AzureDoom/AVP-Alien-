package com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.TriggeredAttackGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.RazorClaw;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.RazorClawSweepAttack;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

public class RazorClawGOAP {

    public static final Graph<RazorClaw> GRAPH = Graph.<RazorClaw>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(graph -> TriggeredAttackGOAP.applyTriggeredAttack(graph, RazorClawSweepAttack.ATTACK))
        .build();

    public static Agent.Builder<RazorClaw> applyAgentProperties(Agent.Builder<RazorClaw> agentBuilder) {
        return XenomorphGOAP.applySpecialAttackAgentProperties(agentBuilder);
    }

    private RazorClawGOAP() {
        throw new UnsupportedOperationException();
    }
}

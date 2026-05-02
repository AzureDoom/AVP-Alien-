package com.alien.common.gameplay.entity.living.alien.xenomorph.prowler.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.prowler.Prowler;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

public class ProwlerGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 12, 20 * 7);

    public static final Graph<Prowler> GRAPH = Graph.<Prowler>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(b -> XenomorphGOAP.addLungePackage(b, LUNGE_CONFIG))
        .build();

    public static Agent.Builder<Prowler> applyAgentProperties(Agent.Builder<Prowler> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private ProwlerGOAP() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.burster.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.burster.Burster;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

public class BursterGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 12, 20 * 7);

    public static final Graph<Burster> GRAPH = Graph.<Burster>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(b -> XenomorphGOAP.addLungePackage(b, LUNGE_CONFIG))
        .build();

    public static Agent.Builder<Burster> applyAgentProperties(Agent.Builder<Burster> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private BursterGOAP() {
        throw new UnsupportedOperationException();
    }
}

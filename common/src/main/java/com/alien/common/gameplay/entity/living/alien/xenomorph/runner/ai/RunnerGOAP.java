package com.alien.common.gameplay.entity.living.alien.xenomorph.runner.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.runner.Runner;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

public class RunnerGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 12, 20 * 7);

    public static final Graph<Runner> GRAPH = Graph.<Runner>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(b -> XenomorphGOAP.addLungePackage(b, LUNGE_CONFIG))
        .apply(XenomorphGOAP::addEggPackage)
        .apply(XenomorphGOAP::addVentPackage)
        .apply(XenomorphGOAP::addResinPackage)
        .build();

    public static Agent.Builder<Runner> applyAgentProperties(Agent.Builder<Runner> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private RunnerGOAP() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.Warrior;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

public class WarriorGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 15, 20 * 5);

    public static final Graph<Warrior> GRAPH = Graph.<Warrior>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(b -> XenomorphGOAP.addLungePackage(b, LUNGE_CONFIG))
        .build();

    public static Agent.Builder<Warrior> applyAgentProperties(Agent.Builder<Warrior> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private WarriorGOAP() {
        throw new UnsupportedOperationException();
    }
}

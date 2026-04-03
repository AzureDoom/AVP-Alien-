package com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.Warrior;
import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.ai.lunge.LungeActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.ai.lunge.LungeSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class WarriorGOAP {

    public static final Graph<Warrior> GRAPH = Graph.<Warrior>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(WarriorGOAP::addLungePackage)
        .build();

    public static Agent.Builder<Warrior> applyAgentProperties(Agent.Builder<Warrior> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Warrior> addLungePackage(Graph.Builder<Warrior> graphBuilder) {
        graphBuilder.addAction(LungeActions.LUNGE_AT_TARGET);

        graphBuilder.addSensor(LungeSensors.IS_TARGET_IN_LUNGE_RANGE);

        return graphBuilder;
    }

    private WarriorGOAP() {
        throw new UnsupportedOperationException();
    }
}

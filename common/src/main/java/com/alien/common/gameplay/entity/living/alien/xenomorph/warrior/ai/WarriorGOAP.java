package com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.Warrior;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class WarriorGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 15, 20 * 5);

    public static final Graph<Warrior> GRAPH = Graph.<Warrior>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(WarriorGOAP::addLungePackage)
        .build();

    public static Agent.Builder<Warrior> applyAgentProperties(Agent.Builder<Warrior> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Warrior> addLungePackage(Graph.Builder<Warrior> graphBuilder) {
        var lungeSensor = LungeSensors.<Warrior>createLungeRangeSensor(LUNGE_CONFIG);

        graphBuilder.addAction(LungeActions.createLungeAtTarget(lungeSensor.key()));
        graphBuilder.addSensor(lungeSensor);

        return graphBuilder;
    }

    private WarriorGOAP() {
        throw new UnsupportedOperationException();
    }
}

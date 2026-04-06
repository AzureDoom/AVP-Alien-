package com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.Spitter;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class SpitterGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 12, 20 * 7);

    public static final Graph<Spitter> GRAPH = Graph.<Spitter>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(SpitterGOAP::addLungePackage)
        .build();

    public static Agent.Builder<Spitter> applyAgentProperties(Agent.Builder<Spitter> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Spitter> addLungePackage(Graph.Builder<Spitter> graphBuilder) {
        var lungeSensor = LungeSensors.<Spitter>createLungeRangeSensor(LUNGE_CONFIG);

        graphBuilder.addAction(LungeActions.createLungeAtTarget(lungeSensor.key()));
        graphBuilder.addSensor(lungeSensor);

        return graphBuilder;
    }

    private SpitterGOAP() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.crusher.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.crusher.Crusher;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class CrusherGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 12, 20 * 7);

    public static final Graph<Crusher> GRAPH = Graph.<Crusher>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(CrusherGOAP::addLungePackage)
        .build();

    public static Agent.Builder<Crusher> applyAgentProperties(Agent.Builder<Crusher> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Crusher> addLungePackage(Graph.Builder<Crusher> graphBuilder) {
        var lungeSensor = LungeSensors.<Crusher>createLungeRangeSensor(LUNGE_CONFIG);

        graphBuilder.addAction(LungeActions.createLungeAtTarget(lungeSensor.key()));
        graphBuilder.addSensor(lungeSensor);

        return graphBuilder;
    }

    private CrusherGOAP() {
        throw new UnsupportedOperationException();
    }
}

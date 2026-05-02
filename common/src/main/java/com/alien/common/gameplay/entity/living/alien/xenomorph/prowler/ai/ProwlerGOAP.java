package com.alien.common.gameplay.entity.living.alien.xenomorph.prowler.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.prowler.Prowler;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

public class ProwlerGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 12, 20 * 7);

    public static final Graph<Prowler> GRAPH = Graph.<Prowler>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(ProwlerGOAP::addLungePackage)
        .build();

    public static Agent.Builder<Prowler> applyAgentProperties(Agent.Builder<Prowler> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Prowler> addLungePackage(Graph.Builder<Prowler> graphBuilder) {
        var lungeSensor = LungeSensors.<Prowler>createLungeRangeSensor(LUNGE_CONFIG);

        graphBuilder.addAction(LungeActions.createLungeAtTarget(lungeSensor.key()));
        graphBuilder.addSensor(lungeSensor);

        return graphBuilder;
    }

    private ProwlerGOAP() {
        throw new UnsupportedOperationException();
    }
}

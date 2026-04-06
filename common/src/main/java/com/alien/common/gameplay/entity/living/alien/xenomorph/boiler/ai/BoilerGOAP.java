package com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.Boiler;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class BoilerGOAP {

    public static final Graph<Boiler> GRAPH = Graph.<Boiler>builder()
        .apply(XenomorphGOAP::applyBaseGraphWithoutDig)
        .apply(BoilerGOAP::addVibrationPackage)
        .build();

    public static Agent.Builder<Boiler> applyAgentProperties(Agent.Builder<Boiler> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Boiler> addVibrationPackage(Graph.Builder<Boiler> graphBuilder) {
        graphBuilder.addGoal(VibrationGoals.INVESTIGATE_VIBRATION);

        graphBuilder.addAction(VibrationActions.INVESTIGATE_VIBRATION);

        graphBuilder.addSensor(VibrationSensors.HAS_VIBRATION_TO_INVESTIGATE);

        return graphBuilder;
    }

    private BoilerGOAP() {
        throw new UnsupportedOperationException();
    }
}

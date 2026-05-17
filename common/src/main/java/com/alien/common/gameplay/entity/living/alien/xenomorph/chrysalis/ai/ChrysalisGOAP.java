package com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.Chrysalis;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ai.roll.RollActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ai.roll.RollConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ai.roll.RollSensors;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

public class ChrysalisGOAP {

    public static final Graph<Chrysalis> GRAPH = Graph.<Chrysalis>builder()
        .apply(XenomorphGOAP::addSensorsPackage)
        .apply(XenomorphGOAP::addCombatPackageWithoutMove)
        .apply(XenomorphGOAP::addIdlePackage)
        .apply(ChrysalisGOAP::addRollPackage)
        .apply(ChrysalisGOAP::addChrysalisMovePackage)
        .build();

    public static Agent.Builder<Chrysalis> applyAgentProperties(Agent.Builder<Chrysalis> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Chrysalis> addRollPackage(Graph.Builder<Chrysalis> graphBuilder) {
        var rollSensor = RollSensors.createRollRangeSensor(RollConfig.DEFAULT);

        graphBuilder.addAction(RollActions.createRollAtTarget(rollSensor.key()));
        graphBuilder.addSensor(rollSensor);

        return graphBuilder;
    }

    private static Graph.Builder<Chrysalis> addChrysalisMovePackage(Graph.Builder<Chrysalis> graphBuilder) {
        graphBuilder.addAction(ChrysalisCombatActions.MOVE_TO_TARGET);
        return graphBuilder;
    }

    private ChrysalisGOAP() {
        throw new UnsupportedOperationException();
    }
}

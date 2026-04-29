package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai.throw_attack.ThrowAttackActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.ai.throw_attack.ThrowAttackSensors;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class CarrierGOAP {

    public static final Graph<Carrier> GRAPH = Graph.<Carrier>builder()
        .apply(XenomorphGOAP::addSensorsPackage)
        .apply(CarrierGOAP::addCombatPackage)
        .apply(XenomorphGOAP::addDigPackage)
        .apply(XenomorphGOAP::addIdlePackage)
        .apply(CarrierGOAP::addThrowPackage)
        .build();

    public static Agent.Builder<Carrier> applyAgentProperties(Agent.Builder<Carrier> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Carrier> addCombatPackage(Graph.Builder<Carrier> graphBuilder) {
        XenomorphGOAP.addCombatPackageWithoutMove(graphBuilder);
        graphBuilder.addAction(CarrierCombatActions.MOVE_TO_TARGET);
        return graphBuilder;
    }

    private static Graph.Builder<Carrier> addThrowPackage(Graph.Builder<Carrier> graphBuilder) {
        graphBuilder.addAction(ThrowAttackActions.THROW_FACEHUGGER);
        graphBuilder.addSensor(ThrowAttackSensors.HAS_RIDING_FACEHUGGER);
        graphBuilder.addSensor(ThrowAttackSensors.IS_THROW_COOLDOWN_READY);
        graphBuilder.addSensor(ThrowAttackSensors.CAN_THROW_FACEHUGGER);
        return graphBuilder;
    }

    private CarrierGOAP() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.drone.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg.EggActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg.EggGoals;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg.EggSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent.VentActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent.VentGoals;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent.VentSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.drone.Drone;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class DroneGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 12, 20 * 7);

    public static final Graph<Drone> GRAPH = Graph.<Drone>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(DroneGOAP::addLungePackage)
        .apply(DroneGOAP::addEggPackage)
        .apply(DroneGOAP::addVentPackage)
        .build();

    public static Agent.Builder<Drone> applyAgentProperties(Agent.Builder<Drone> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Drone> addLungePackage(Graph.Builder<Drone> graphBuilder) {
        var lungeSensor = LungeSensors.<Drone>createLungeRangeSensor(LUNGE_CONFIG);

        graphBuilder.addAction(LungeActions.createLungeAtTarget(lungeSensor.key()));
        graphBuilder.addSensor(lungeSensor);

        return graphBuilder;
    }

    private static Graph.Builder<Drone> addEggPackage(Graph.Builder<Drone> graphBuilder) {
        graphBuilder.addGoal(EggGoals.FETCH_EGG);
        graphBuilder.addGoal(EggGoals.DELIVER_EGG);

        graphBuilder.addAction(EggActions.PICK_UP_EGG);
        graphBuilder.addAction(EggActions.DROP_OFF_EGG);

        graphBuilder.addSensor(EggSensors.HAS_TARGET_OVOMORPH);
        graphBuilder.addSensor(EggSensors.IS_CARRYING_OVOMORPH);

        return graphBuilder;
    }

    private static Graph.Builder<Drone> addVentPackage(Graph.Builder<Drone> graphBuilder) {
        graphBuilder.addGoal(VentGoals.CREATE_VENT);

        graphBuilder.addAction(VentActions.CREATE_VENT);

        graphBuilder.addSensor(VentSensors.CAN_CREATE_VENT);

        return graphBuilder;
    }

    private DroneGOAP() {
        throw new UnsupportedOperationException();
    }
}

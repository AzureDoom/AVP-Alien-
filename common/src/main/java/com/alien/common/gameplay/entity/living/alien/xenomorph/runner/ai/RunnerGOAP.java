package com.alien.common.gameplay.entity.living.alien.xenomorph.runner.ai;

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
import com.alien.common.gameplay.entity.living.alien.xenomorph.runner.Runner;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class RunnerGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 12, 20 * 7);

    public static final Graph<Runner> GRAPH = Graph.<Runner>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(RunnerGOAP::addLungePackage)
        .apply(RunnerGOAP::addEggPackage)
        .apply(RunnerGOAP::addVentPackage)
        .build();

    public static Agent.Builder<Runner> applyAgentProperties(Agent.Builder<Runner> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Runner> addLungePackage(Graph.Builder<Runner> graphBuilder) {
        var lungeSensor = LungeSensors.<Runner>createLungeRangeSensor(LUNGE_CONFIG);

        graphBuilder.addAction(LungeActions.createLungeAtTarget(lungeSensor.key()));
        graphBuilder.addSensor(lungeSensor);

        return graphBuilder;
    }

    private static Graph.Builder<Runner> addEggPackage(Graph.Builder<Runner> graphBuilder) {
        graphBuilder.addGoal(EggGoals.FETCH_EGG);
        graphBuilder.addGoal(EggGoals.DELIVER_EGG);

        graphBuilder.addAction(EggActions.PICK_UP_EGG);
        graphBuilder.addAction(EggActions.DROP_OFF_EGG);

        graphBuilder.addSensor(EggSensors.HAS_TARGET_OVOMORPH);
        graphBuilder.addSensor(EggSensors.IS_CARRYING_OVOMORPH);

        return graphBuilder;
    }

    private static Graph.Builder<Runner> addVentPackage(Graph.Builder<Runner> graphBuilder) {
        graphBuilder.addGoal(VentGoals.CREATE_VENT);

        graphBuilder.addAction(VentActions.CREATE_VENT);

        graphBuilder.addSensor(VentSensors.CAN_CREATE_VENT);

        return graphBuilder;
    }

    private RunnerGOAP() {
        throw new UnsupportedOperationException();
    }
}

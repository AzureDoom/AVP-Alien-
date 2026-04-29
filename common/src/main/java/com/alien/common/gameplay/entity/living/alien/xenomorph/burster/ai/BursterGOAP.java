package com.alien.common.gameplay.entity.living.alien.xenomorph.burster.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg.EggActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg.EggGoals;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg.EggSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeConfig;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.lunge.LungeSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.resin.ResinActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.resin.ResinGoals;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.resin.ResinSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent.VentActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent.VentGoals;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent.VentSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.burster.Burster;
import com.just.goap.Agent;
import com.just.goap.graph.Graph;

public class BursterGOAP {

    private static final LungeConfig LUNGE_CONFIG = new LungeConfig(6, 12, 20 * 7);

    public static final Graph<Burster> GRAPH = Graph.<Burster>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(BursterGOAP::addLungePackage)
        .apply(BursterGOAP::addEggPackage)
        .apply(BursterGOAP::addVentPackage)
        .apply(BursterGOAP::addResinPackage)
        .build();

    public static Agent.Builder<Burster> applyAgentProperties(Agent.Builder<Burster> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private static Graph.Builder<Burster> addLungePackage(Graph.Builder<Burster> graphBuilder) {
        var lungeSensor = LungeSensors.<Burster>createLungeRangeSensor(LUNGE_CONFIG);

        graphBuilder.addAction(LungeActions.createLungeAtTarget(lungeSensor.key()));
        graphBuilder.addSensor(lungeSensor);

        return graphBuilder;
    }

    private static Graph.Builder<Burster> addEggPackage(Graph.Builder<Burster> graphBuilder) {
        graphBuilder.addGoal(EggGoals.FETCH_EGG);
        graphBuilder.addGoal(EggGoals.DELIVER_EGG);

        graphBuilder.addAction(EggActions.PICK_UP_EGG);
        graphBuilder.addAction(EggActions.DROP_OFF_EGG);

        graphBuilder.addSensor(EggSensors.HAS_TARGET_OVOMORPH);
        graphBuilder.addSensor(EggSensors.IS_CARRYING_OVOMORPH);

        return graphBuilder;
    }

    private static Graph.Builder<Burster> addVentPackage(Graph.Builder<Burster> graphBuilder) {
        graphBuilder.addGoal(VentGoals.CREATE_VENT);

        graphBuilder.addAction(VentActions.CREATE_VENT);

        graphBuilder.addSensor(VentSensors.CAN_CREATE_VENT);

        return graphBuilder;
    }

    private static Graph.Builder<Burster> addResinPackage(Graph.Builder<Burster> graphBuilder) {
        graphBuilder.addGoal(ResinGoals.SPREAD_RESIN);

        graphBuilder.addAction(ResinActions.SPREAD_RESIN);

        graphBuilder.addSensor(ResinSensors.CAN_SPREAD_RESIN);

        return graphBuilder;
    }

    private BursterGOAP() {
        throw new UnsupportedOperationException();
    }
}

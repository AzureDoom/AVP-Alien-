package com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai;

import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.attach_to_host.AttachToHostActions;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.attach_to_host.AttachToHostGoals;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.attach_to_host.AttachToHostSensors;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.idle.IdleActions;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.idle.IdleGoals;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.idle.IdleSensors;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.seek_carrier.SeekCarrierActions;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.seek_carrier.SeekCarrierGoals;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.ai.seek_carrier.SeekCarrierSensors;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;
import com.just.ai.goap.plan.ReplanPolicies;

public class FacehuggerGOAP {

    public static final Graph<Facehugger> GRAPH = Graph.<Facehugger>builder()
        .apply(FacehuggerGOAP::addSensorsPackage)
        .apply(FacehuggerGOAP::addCombatPackage)
        .apply(FacehuggerGOAP::addSeekCarrierPackage)
        .apply(FacehuggerGOAP::addIdlePackage)
        .build();

    public static Agent.Builder<Facehugger> applyAgentProperties(Agent.Builder<Facehugger> agentBuilder) {
        return agentBuilder.withReplanPolicy(
            ReplanPolicies.anyOf(
                ReplanPolicies.ifNoActivePlans(),
                // Replan every 20 ticks (every 1 second).
                ReplanPolicies.custom(context -> context.agent().getActor().tickCount % 20 == 0),
                ReplanPolicies.custom(context -> {
                    var isOnFire = context.worldState().getOrDefault(GOAPSensors.IS_ON_FIRE.key(), false);
                    var wasOnFire = context.previousWorldState().getOrDefault(GOAPSensors.IS_ON_FIRE.key(), false);
                    // If the facehugger was previously not on fire, and is now on fire, then replan.
                    return !wasOnFire && isOnFire;
                }),
                ReplanPolicies.custom(context -> {
                    var currentHealthRatio = context.worldState().getOrDefault(GOAPSensors.HEALTH_RATIO.key(), 0F);
                    var previousHealthRatio = context.previousWorldState().getOrDefault(GOAPSensors.HEALTH_RATIO.key(), 0F);
                    // If the facehugger's health decreased, then replan.
                    return currentHealthRatio < previousHealthRatio;
                })
            )
        );
    }

    private static Graph.Builder<Facehugger> addSensorsPackage(Graph.Builder<Facehugger> graphBuilder) {
        graphBuilder.addSensor(GOAPSensors.IS_ON_GROUND);
        graphBuilder.addSensor(GOAPSensors.IS_ON_FIRE);
        graphBuilder.addSensor(GOAPSensors.HEALTH_RATIO);

        return graphBuilder;
    }

    private static Graph.Builder<Facehugger> addCombatPackage(Graph.Builder<Facehugger> graphBuilder) {
        // The goal we want to complete.
        graphBuilder.addGoal(AttachToHostGoals.ATTACH_TO_HOST_GOAL);

        // Actions that can complete the goal.
        graphBuilder.addAction(AttachToHostActions.MOVE_TO_HOST);
        graphBuilder.addAction(AttachToHostActions.LUNGE_AT_HOST);

        // Used for sensing attackable targets.
        graphBuilder.addSensor(
            GOAPSensors.nearbyAttackableTargetsFactory(
                (facehugger, livingEntity) -> facehugger.isValidHost(livingEntity)
                    && facehugger.getSensing().hasLineOfSight(livingEntity)
            )
        );
        // Used for sensing attackable targets in a sorted order based on distance.
        graphBuilder.addSensor(GOAPSensors.NEAREST_ATTACKABLE_TARGETS);
        // Used for picking out the closest attackable target.
        graphBuilder.addSensor(GOAPSensors.NEAREST_ATTACKABLE_TARGET);
        // Used for checking if entity has an attack target.
        graphBuilder.addSensor(GOAPSensors.HAS_ATTACK_TARGET);
        // Used for checking if the attack target is in range.
        graphBuilder.addSensor(AttachToHostSensors.IS_ATTACK_TARGET_IN_LUNGE_RANGE);

        return graphBuilder;
    }

    private static Graph.Builder<Facehugger> addSeekCarrierPackage(Graph.Builder<Facehugger> graphBuilder) {
        graphBuilder.addGoal(SeekCarrierGoals.SEEK_CARRIER);
        graphBuilder.addAction(SeekCarrierActions.MOVE_TO_CARRIER);
        graphBuilder.addSensor(SeekCarrierSensors.NEAREST_AVAILABLE_CARRIER);
        graphBuilder.addSensor(SeekCarrierSensors.SHOULD_SEEK_CARRIER);
        return graphBuilder;
    }

    private static Graph.Builder<Facehugger> addIdlePackage(Graph.Builder<Facehugger> graphBuilder) {
        // The goal we want to complete.
        graphBuilder.addGoal(IdleGoals.SATISFY_BOREDOM);

        // Actions that can complete the goal.
        graphBuilder.addAction(IdleActions.WANDER);

        // Used for determining when the facehugger should wander around.
        graphBuilder.addSensor(IdleSensors.IS_BORED);

        return graphBuilder;
    }

    private FacehuggerGOAP() {
        throw new UnsupportedOperationException();
    }
}

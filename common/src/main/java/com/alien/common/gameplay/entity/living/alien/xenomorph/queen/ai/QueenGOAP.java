package com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.XenomorphGOAP;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai.founding_move.FoundingMoveActions;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai.founding_move.FoundingMoveGoals;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.ai.founding_move.FoundingMoveSensors;
import com.just.ai.goap.Agent;
import com.just.ai.goap.graph.Graph;

public class QueenGOAP {

    public static final Graph<Queen> GRAPH = Graph.<Queen>builder()
        .apply(XenomorphGOAP::applyBaseGraph)
        .apply(XenomorphGOAP::addResinPackage)
        .apply(XenomorphGOAP::addEggLayingPackage)
        .apply(QueenGOAP::addFoundingMovePackage)
        .build();

    public static final Graph<Queen> OVIPOSITOR_GRAPH = Graph.<Queen>builder()
        .apply(XenomorphGOAP::applyEggLayingOnlyGraph)
        .build();

    /**
     * Founding navigate-to-center (Option B). Queen-specific so drones and the empress are unaffected, and attached
     * only to the normal {@link #GRAPH} — never the egg-laying-only {@link #OVIPOSITOR_GRAPH} — so it cannot interfere
     * with the working laying flow once she is reproductive. The behaviour as a whole is gated by
     * {@link FoundingMoveSensors#isEnabled()}; when disabled, these nodes simply never get selected.
     */
    public static Graph.Builder<Queen> addFoundingMovePackage(Graph.Builder<Queen> graphBuilder) {
        graphBuilder.addGoal(FoundingMoveGoals.BE_AT_CENTER);

        graphBuilder.addAction(FoundingMoveActions.GO_TO_CENTER);

        graphBuilder.addSensor(FoundingMoveSensors.IS_FOUNDING);
        graphBuilder.addSensor(FoundingMoveSensors.IS_AT_CENTER);

        return graphBuilder;
    }

    public static Agent.Builder<Queen> applyAgentProperties(Agent.Builder<Queen> agentBuilder) {
        return XenomorphGOAP.applyBaseAgentProperties(agentBuilder);
    }

    private QueenGOAP() {
        throw new UnsupportedOperationException();
    }
}

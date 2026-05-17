package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.cocoon;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.just.ai.goap.graph.Graph;

public class CocoonGOAP {

    public static final Graph<Xenomorph> GRAPH = Graph.<Xenomorph>builder()
        .addGoal(CocoonGoals.COCOON)
        .addAction(CocoonActions.COCOON)
        .addSensor(CocoonSensors.SHOULD_COCOON)
        .build();

    private CocoonGOAP() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.alien.common.gameplay.ai.path.CrawlPathNodeEvaluator;
import com.blib.api.common.entity.v1.ai.goal.WaterMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;

public class XenomorphNavigationManager {

    private final GroundPathNavigation groundNavigation;

    private final MoveControl groundMoveControl;

    private final WaterBoundPathNavigation waterNavigation;

    private final WaterMoveControl waterMoveControl;

    public XenomorphNavigationManager(Xenomorph xenomorph, MoveControl moveControl) {
        this.groundMoveControl = moveControl;
        this.groundNavigation = new GroundPathNavigation(xenomorph, xenomorph.level()) {

            @Override
            protected @NotNull PathFinder createPathFinder(int i) {
                this.nodeEvaluator = new CrawlPathNodeEvaluator();
                this.nodeEvaluator.setCanPassDoors(true);
                this.nodeEvaluator.setCanOpenDoors(true);
                this.nodeEvaluator.setCanWalkOverFences(true);
                return new PathFinder(this.nodeEvaluator, i);
            }
        };

        xenomorph.setPathfindingMalus(PathType.WATER, 0.0F);
        this.waterMoveControl = new WaterMoveControl(xenomorph);
        this.waterNavigation = new WaterBoundPathNavigation(xenomorph, xenomorph.level());
    }

    public void switchToGround(Xenomorph xenomorph) {
        xenomorph.setMoveControl(groundMoveControl);
        xenomorph.setNavigation(groundNavigation);
    }

    public void switchToWater(Xenomorph xenomorph) {
        xenomorph.setMoveControl(waterMoveControl);
        xenomorph.setNavigation(waterNavigation);
    }
}

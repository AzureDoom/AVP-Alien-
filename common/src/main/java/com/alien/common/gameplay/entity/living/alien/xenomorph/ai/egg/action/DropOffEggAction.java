package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg.action;

import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.registry.init.AlienSoundEvents;
import com.blib.api.common.goap.v1.action.impl.NeoMoveToPosAction;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.state.Blackboard;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class DropOffEggAction {

    private static final StateKey<Vec3> KEY_TARGET_POS = StateKey.sensed("egg_drop_target_pos");

    private static final StateKey<Boolean> KEY_HAS_SEARCHED = StateKey.sensed("egg_drop_has_searched");

    private static final StateKey<List<BlockPos>> KEY_FAILED_SPOTS = StateKey.sensed("egg_drop_failed_spots");

    private static final StateKey<Integer> KEY_NEXT_SEARCH_TICK = StateKey.sensed("egg_drop_next_search_tick");

    private static final double DROP_OFF_RANGE_SQUARED = 2.0 * 2.0;

    private static final int SEARCH_RETRY_DELAY_TICKS = 20;

    private static final int MAX_REMEMBERED_FAILED_SPOTS = 32;

    private static final int MAX_PATH_VALIDATION_ATTEMPTS = 24;

    private static final List<BlockPos> EGG_GRID_POS_OFFSETS = generateSpiralOffsets(16);

    public static Action.Signal perform(Action.Context<? extends Xenomorph> context) {
        var xenomorph = context.getActor();
        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);

        if (!isCarryingOvomorph(xenomorph)) {
            return Action.Signal.CONTINUE;
        }

        var hasSearched = blackboard.getOrDefault(KEY_HAS_SEARCHED, false);
        var targetPos = blackboard.getOrDefault(KEY_TARGET_POS, (Vec3) null);

        if (!hasSearched || targetPos == null) {
            var nextSearchTick = blackboard.getOrDefault(KEY_NEXT_SEARCH_TICK, 0);

            if (xenomorph.tickCount < nextSearchTick) {
                return Action.Signal.CONTINUE;
            }

            blackboard.set(KEY_HAS_SEARCHED, true);

            var failedSpots = getFailedSpots(blackboard);
            var freeSpot = findFreeEggSpot(
                xenomorph,
                xenomorph.level(),
                xenomorph.blockPosition(),
                pos -> xenomorph.level().getBlockState(pos).entityCanStandOn(xenomorph.level(), pos, xenomorph),
                failedSpots
            );
            setFailedSpots(blackboard, failedSpots);

            if (freeSpot.isEmpty()) {
                scheduleSearchRetry(blackboard, xenomorph.tickCount);
                return Action.Signal.CONTINUE;
            }

            targetPos = freeSpot.get().getCenter();
            blackboard.set(KEY_TARGET_POS, targetPos);
        }

        if (targetPos == null) {
            return Action.Signal.ABORT;
        }

        var result = NeoMoveToPosAction.perform(context, targetPos, 0.5);

        return switch (result) {
            case FINISHED, MOVING -> {
                if (xenomorph.distanceToSqr(targetPos) <= DROP_OFF_RANGE_SQUARED) {
                    placeEggs(xenomorph, targetPos);
                    yield Action.Signal.CONTINUE;
                }

                yield Action.Signal.CONTINUE;
            }
            case WAITING_FOR_BLOCK_BREAK -> Action.Signal.CONTINUE;
            case NO_PATH -> {
                rememberFailedSpot(blackboard, BlockPos.containing(targetPos));
                scheduleSearchRetry(blackboard, xenomorph.tickCount);
                NeoMoveToPosAction.onFinish(context);
                yield Action.Signal.CONTINUE;
            }
        };
    }

    public static void onFinish(Action.Context<? extends Xenomorph> context) {
        NeoMoveToPosAction.onFinish(context);
    }

    private static void placeEggs(Xenomorph xenomorph, Vec3 center) {
        getPassengerOvomorphs(xenomorph).forEach(ovomorph -> {
            xenomorph.level()
                .playSound(null, ovomorph, AlienSoundEvents.ENTITY_OVOMORPH_ROOT.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
            ovomorph.isRooted.set(true);
            ovomorph.stopRiding();
            ovomorph.setPos(center.x, center.y, center.z);

            var randomYaw = xenomorph.getRandom().nextFloat() * 360.0F;

            ovomorph.setYRot(randomYaw);
            ovomorph.setYHeadRot(randomYaw);
            ovomorph.yBodyRot = randomYaw;
            ovomorph.yRotO = randomYaw;
            ovomorph.yHeadRotO = randomYaw;
            ovomorph.yBodyRotO = randomYaw;
        });
    }

    private static boolean isCarryingOvomorph(Xenomorph xenomorph) {
        return !getPassengerOvomorphs(xenomorph).isEmpty();
    }

    private static List<Ovomorph> getPassengerOvomorphs(Xenomorph xenomorph) {
        return xenomorph.getPassengers()
            .stream()
            .filter(passenger -> passenger instanceof Ovomorph)
            .map(passenger -> (Ovomorph) passenger)
            .toList();
    }

    private static Optional<BlockPos> findFreeEggSpot(
        Xenomorph xenomorph,
        Level level,
        BlockPos center,
        Predicate<BlockPos> isWalkable,
        Set<BlockPos> failedSpots
    ) {
        var centerIsOdd = (center.getX() & 1) != 0 && (center.getZ() & 1) != 0;

        var result = tryFindWithParity(xenomorph, level, center, isWalkable, centerIsOdd, failedSpots);

        if (result.isEmpty()) {
            result = tryFindWithParity(xenomorph, level, center, isWalkable, !centerIsOdd, failedSpots);
        }

        return result;
    }

    private static Optional<BlockPos> tryFindWithParity(
        Xenomorph xenomorph,
        Level level,
        BlockPos center,
        Predicate<BlockPos> isWalkable,
        boolean useOdd,
        Set<BlockPos> failedSpots
    ) {
        var baseX = (center.getX() & ~1) + (useOdd ? 1 : 0);
        var baseZ = (center.getZ() & ~1) + (useOdd ? 1 : 0);
        var gridAlignedCenter = new BlockPos(baseX, center.getY(), baseZ);

        var viable = new ArrayList<BlockPos>();

        for (var offset : EGG_GRID_POS_OFFSETS) {
            var pos = gridAlignedCenter.offset(offset);

            if (((pos.getX() & 1) == 0) == useOdd) {
                continue;
            }

            if (((pos.getZ() & 1) == 0) == useOdd) {
                continue;
            }

            var verticalSearchRange = 4;

            for (var dy = -verticalSearchRange; dy <= verticalSearchRange; dy++) {
                var adjustedPos = pos.above(dy);

                if (failedSpots.contains(adjustedPos)) {
                    continue;
                }

                var below = adjustedPos.below();

                if (!isWalkable.test(below)) {
                    continue;
                }

                var state = level.getBlockState(adjustedPos);
                var aboveState = level.getBlockState(adjustedPos.above());

                if (
                    (state.isAir() || state.canBeReplaced())
                        && (aboveState.isAir() || aboveState.canBeReplaced())
                        && level.getEntities(null, new AABB(adjustedPos)).isEmpty()
                ) {
                    viable.add(adjustedPos.immutable());
                }
            }
        }

        viable.sort(Comparator.comparingDouble(pos -> pos.distSqr(gridAlignedCenter)));

        for (var i = 0; i < Math.min(viable.size(), MAX_PATH_VALIDATION_ATTEMPTS); i++) {
            var pos = viable.get(i);
            var path = xenomorph.getNavigation().createPath(pos, 0);

            if (path != null && path.canReach()) {
                return Optional.of(pos);
            } else {
                failedSpots.add(pos);
            }
        }

        return Optional.empty();
    }

    private static Set<BlockPos> getFailedSpots(Blackboard blackboard) {
        return new HashSet<>(blackboard.getOrDefault(KEY_FAILED_SPOTS, List.<BlockPos>of()));
    }

    private static void rememberFailedSpot(Blackboard blackboard, BlockPos blockPos) {
        var failedSpots = getFailedSpots(blackboard);
        failedSpots.add(blockPos.immutable());
        setFailedSpots(blackboard, failedSpots);
    }

    private static void setFailedSpots(Blackboard blackboard, Set<BlockPos> failedSpots) {
        blackboard.set(
            KEY_FAILED_SPOTS,
            failedSpots.stream()
                .limit(MAX_REMEMBERED_FAILED_SPOTS)
                .toList()
        );
    }

    private static void scheduleSearchRetry(Blackboard blackboard, int currentTick) {
        blackboard.set(KEY_HAS_SEARCHED, false);
        blackboard.set(KEY_NEXT_SEARCH_TICK, currentTick + SEARCH_RETRY_DELAY_TICKS);
    }

    private static List<BlockPos> generateSpiralOffsets(int maxDist) {
        var step = 2;
        var offsets = new ArrayList<BlockPos>();

        for (var dist = 0; dist <= maxDist; dist += step) {
            for (var dx = -dist; dx <= dist; dx += step) {
                var dz = dist - Math.abs(dx);

                if ((dz & 1) != 0) {
                    continue;
                }

                offsets.add(new BlockPos(dx, 0, dz));

                if (dz != 0) {
                    offsets.add(new BlockPos(dx, 0, -dz));
                }
            }
        }

        return offsets;
    }

    private DropOffEggAction() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.vent.action;

import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.entity.living.alien.xenomorph.VentBuilder;
import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.model.alien.variant.AlienVariantType;
import com.alien.common.registry.tag.AlienBlockTags;
import com.blib.api.common.goap.v1.action.impl.NeoMoveToPosAction;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.state.Blackboard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CreateVentAction {

    private static final Direction[] ALL_DIRECTIONS = Direction.values();

    private static final int WALL_DEPTH = 3;

    private static final int WALL_TOTAL_DEPTH = 4;

    private static final StateKey<BlockPos> KEY_WALL_START = StateKey.sensed("vent_wall_start");

    private static final StateKey<Integer> KEY_DIRECTION_ORDINAL = StateKey.sensed("vent_direction");

    private static final StateKey<Boolean> KEY_HAS_DRILLED = StateKey.sensed("vent_has_drilled");

    public static boolean hasVentTarget(Xenomorph xenomorph) {
        return findVentTarget(xenomorph, (wallStart, direction) -> {});
    }

    public static Action.Signal perform(Action.Context<? extends Xenomorph> context) {
        var xenomorph = context.getActor();
        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);

        var wallStart = blackboard.getOrDefault(KEY_WALL_START, (BlockPos) null);
        var directionOrdinal = blackboard.getOrDefault(KEY_DIRECTION_ORDINAL, -1);

        if (wallStart == null) {
            var found = findVentTarget(xenomorph, blackboard);

            if (!found) {
                recordVentTargetSearchFailure(xenomorph);
                return Action.Signal.ABORT;
            }

            wallStart = blackboard.getOrDefault(KEY_WALL_START, (BlockPos) null);
            directionOrdinal = blackboard.getOrDefault(KEY_DIRECTION_ORDINAL, -1);
        }

        if (wallStart == null || directionOrdinal < 0) {
            return Action.Signal.ABORT;
        }

        var direction = Direction.from3DDataValue(directionOrdinal);
        var pathTarget = wallStart.relative(direction, WALL_TOTAL_DEPTH - 1);
        var targetPos = Vec3.atBottomCenterOf(pathTarget);

        var hasDrilled = blackboard.getOrDefault(KEY_HAS_DRILLED, false);

        if (hasDrilled) {
            return Action.Signal.CONTINUE;
        }

        var result = NeoMoveToPosAction.perform(context, targetPos, 0.5);

        return switch (result) {
            case FINISHED -> {
                drillVent(xenomorph, wallStart, direction);

                if (xenomorph instanceof VentBuilder ventBuilder) {
                    ventBuilder.getVentData().setLastVentCreationTick(xenomorph.tickCount);
                    ventBuilder.getVentData().clearVentTargetSearchFailure();
                }

                blackboard.set(KEY_HAS_DRILLED, true);
                yield Action.Signal.CONTINUE;
            }
            case MOVING -> Action.Signal.CONTINUE;
            case WAITING_FOR_BLOCK_BREAK -> Action.Signal.CONTINUE;
            case NO_PATH -> {
                recordVentTargetSearchFailure(xenomorph);
                yield Action.Signal.ABORT;
            }
        };
    }

    public static void onFinish(Action.Context<? extends Xenomorph> context) {
        NeoMoveToPosAction.onFinish(context);
    }

    private static boolean findVentTarget(Xenomorph xenomorph, Blackboard blackboard) {
        return findVentTarget(xenomorph, (hitPos, direction) -> {
            blackboard.set(KEY_WALL_START, hitPos);
            blackboard.set(KEY_DIRECTION_ORDINAL, direction.get3DDataValue());
        });
    }

    private static boolean findVentTarget(Xenomorph xenomorph, VentTargetConsumer targetConsumer) {
        var footPos = xenomorph.blockPosition();

        for (var direction : Direction.Plane.HORIZONTAL) {
            var hit = rayTraceToSolid(xenomorph, footPos, direction);

            if (!(hit instanceof BlockHitResult blockHitResult)) {
                continue;
            }

            var hitPos = blockHitResult.getBlockPos();

            // Reject if a vent already exists in the hive2 location that owns this chunk. (If no location owns it,
            // there's nothing to dedupe against — let the build proceed.)
            var owningLocation = com.alien.common.gameplay.hive2.location.HiveLocationRegistry.INSTANCE.getByChunk(
                xenomorph.level().dimension(),
                new net.minecraft.world.level.ChunkPos(hitPos)
            );
            if (owningLocation != null && !owningLocation.ventManager().getVentsWithinSection(hitPos).isEmpty()) {
                continue;
            }

            if (!isValidWall(xenomorph, hitPos, direction)) {
                continue;
            }

            var pathTarget = hitPos.relative(direction, WALL_TOTAL_DEPTH - 1);
            var path = xenomorph.getNavigation().createPath(pathTarget, 1);

            if (path != null && path.canReach()) {
                targetConsumer.accept(hitPos, direction);
                return true;
            }
        }

        return false;
    }

    private static void recordVentTargetSearchFailure(Xenomorph xenomorph) {
        if (xenomorph instanceof VentBuilder ventBuilder) {
            ventBuilder.getVentData().recordVentTargetSearchFailure(xenomorph.tickCount);
        }
    }

    private static void drillVent(Xenomorph xenomorph, BlockPos wallStart, Direction direction) {
        var level = xenomorph.level();
        var alienVariantType = AlienVariantTypes.getFor(xenomorph);
        var resinHolder = alienVariantType.resin();
        var resinVentHolder = alienVariantType.resinVent();
        var resinWebHolder = alienVariantType.resinWeb();

        for (var i = 0; i < WALL_DEPTH; i++) {
            var tunnelPos = wallStart.relative(direction, i);

            level.setBlock(tunnelPos, resinWebHolder.get().defaultBlockState(), Block.UPDATE_ALL);

            for (var relativeDirection : ALL_DIRECTIONS) {
                if (relativeDirection == direction || relativeDirection == direction.getOpposite()) {
                    continue;
                }

                var tunnelWallPos = tunnelPos.relative(relativeDirection);

                level.setBlock(tunnelWallPos, resinHolder.get().defaultBlockState(), Block.UPDATE_ALL);
            }
        }

        var ventPos = wallStart.relative(direction, WALL_DEPTH);

        level.setBlock(ventPos, resinVentHolder.get().defaultBlockState(), Block.UPDATE_ALL);
    }

    private static HitResult rayTraceToSolid(Xenomorph xenomorph, BlockPos start, Direction direction) {
        var from = Vec3.atCenterOf(start);
        var to = from.add(Vec3.atLowerCornerOf(direction.getNormal()).scale(16));

        return xenomorph.level()
            .clip(
                new ClipContext(
                    from,
                    to,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    xenomorph
                )
            );
    }

    private static boolean isValidWall(Xenomorph xenomorph, BlockPos start, Direction direction) {
        var level = xenomorph.level();
        var alienVariantType = AlienVariantTypes.getFor(xenomorph);

        for (var i = 0; i < WALL_DEPTH; i++) {
            var tunnelPos = start.relative(direction, i);

            if (!isTunnelPosClear(level, tunnelPos, alienVariantType)) {
                return false;
            }

            for (var adjacentDirection : ALL_DIRECTIONS) {
                if (adjacentDirection == direction || adjacentDirection == direction.getOpposite()) {
                    continue;
                }

                var tunnelWallPos = tunnelPos.relative(adjacentDirection);

                if (!isTunnelWallReplaceable(level, tunnelWallPos, alienVariantType)) {
                    return false;
                }
            }
        }

        var ventPos = start.relative(direction, WALL_TOTAL_DEPTH - 1);

        return isTunnelPosClear(level, ventPos, alienVariantType);
    }

    private static boolean isTunnelPosClear(Level level, BlockPos pos, AlienVariantType alienVariantType) {
        var blockState = level.getBlockState(pos);

        return blockState.isAir() || isTunnelWallReplaceable(level, pos, alienVariantType);
    }

    private static boolean isTunnelWallReplaceable(Level level, BlockPos pos, AlienVariantType alienVariantType) {
        var blockState = level.getBlockState(pos);

        if (blockState.is(AlienBlockTags.XENOMORPH_IMMUNE)) {
            return false;
        }

        return blockState.is(alienVariantType.resin().get())
            || blockState.is(alienVariantType.resinReplaceableTag());
    }

    @FunctionalInterface
    private interface VentTargetConsumer {

        void accept(BlockPos wallStart, Direction direction);
    }

    private CreateVentAction() {
        throw new UnsupportedOperationException();
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.dig.action;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.registry.tag.AlienBlockTags;
import com.blib.api.common.block.v1.BlockBreakProgressManager;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.state.Blackboard;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DigToTargetAction {

    private static final StateKey<List<BlockPos>> KEY_TARGET_BLOCKS = StateKey.sensed("dig_target_blocks");

    private static final float DESTROY_TIME_LIMIT = 6F;

    private static final float BREAKING_SPEED = 50F;

    private static final double REACH_DISTANCE = 4.0;

    public static Action.Signal perform(Action.Context<? extends Xenomorph> context) {
        var xenomorph = context.getActor();
        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);
        var target = xenomorph.getTarget();

        if (target == null || !target.isAlive()) {
            return Action.Signal.ABORT;
        }

        if (xenomorph.getHealth() < xenomorph.getMaxHealth() * 0.5f) {
            return Action.Signal.ABORT;
        }

        var targetBlocks = blackboard.getOrDefault(KEY_TARGET_BLOCKS, List.<BlockPos>of());

        if (targetBlocks.isEmpty()) {
            targetBlocks = gatherTargetBlocks(xenomorph);

            if (targetBlocks.isEmpty()) {
                return Action.Signal.ABORT;
            }

            blackboard.set(KEY_TARGET_BLOCKS, targetBlocks);
        }

        if (xenomorph.tickCount % 4 != 0) {
            return Action.Signal.CONTINUE;
        }

        if (!xenomorph.isAttacking()) {
            xenomorph.runDigAnimation();
        }

        var blocksToRemove = new ArrayList<BlockPos>();

        var parallelDigCount = xenomorph.getXenomorphData().getParallelDigCount();

        for (int i = 0; i < targetBlocks.size() && i < parallelDigCount; i++) {
            var blockPos = targetBlocks.get(i);
            var blockState = xenomorph.level().getBlockState(blockPos);

            if (blockState.isAir()) {
                blocksToRemove.add(blockPos);
                continue;
            }

            if (blockPos.distSqr(xenomorph.blockPosition()) > REACH_DISTANCE * REACH_DISTANCE) {
                blocksToRemove.add(blockPos);
                continue;
            }

            xenomorph.getLookControl().setLookAt(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);

            BlockBreakProgressManager.damage(xenomorph.level(), blockPos, BREAKING_SPEED);

            var soundType = blockState.getSoundType();

            xenomorph.level()
                .playSound(
                    null,
                    blockPos,
                    soundType.getHitSound(),
                    SoundSource.BLOCKS,
                    (soundType.getVolume() + 1.0F) / 8.0F,
                    soundType.getPitch() * 0.5F
                );

            if (xenomorph.level().getBlockState(blockPos).isAir()) {
                blocksToRemove.add(blockPos);
            }
        }

        targetBlocks.removeAll(blocksToRemove);
        blackboard.set(KEY_TARGET_BLOCKS, targetBlocks);

        if (targetBlocks.isEmpty()) {
            xenomorph.getXenomorphData().clearPathFailure();
            return Action.Signal.CONTINUE;
        }

        return Action.Signal.CONTINUE;
    }

    public static void onFinish(Action.Context<? extends Xenomorph> context) {
        context.getActor().getXenomorphData().clearPathFailure();
    }

    private static List<BlockPos> gatherTargetBlocks(Xenomorph xenomorph) {
        var target = xenomorph.getTarget();

        if (target == null) {
            return List.of();
        }

        var mobWidth = Mth.ceil(xenomorph.getBbWidth());
        var mobHeight = Mth.ceil(xenomorph.getBbHeight());
        var reachDistanceSquared = REACH_DISTANCE * REACH_DISTANCE;
        var result = new ArrayList<BlockPos>();

        for (var i = 0; i < mobHeight; i++) {
            for (var j = -mobWidth / 2; j <= mobWidth / 2; j++) {
                for (var k = -mobWidth / 2; k <= mobWidth / 2; k++) {
                    var from = xenomorph.position().add(j, i + 0.5, k);
                    var to = target.getEyePosition(1f).add(j, i, k);
                    var clipContext = new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, xenomorph);
                    var rayTraceResult = xenomorph.level().clip(clipContext);

                    if (rayTraceResult.getType() == HitResult.Type.MISS || result.contains(rayTraceResult.getBlockPos())) {
                        continue;
                    }

                    var hitPos = rayTraceResult.getBlockPos();
                    var distance = xenomorph.distanceToSqr(rayTraceResult.getLocation());

                    if (distance > reachDistanceSquared) {
                        continue;
                    }

                    if (!isBreakable(xenomorph, hitPos)) {
                        continue;
                    }

                    result.add(hitPos);
                }
            }
        }

        Collections.reverse(result);
        return result;
    }

    private static boolean isBreakable(Xenomorph xenomorph, BlockPos blockPos) {
        var blockState = xenomorph.level().getBlockState(blockPos);

        return !blockState.hasBlockEntity()
            && blockState.getDestroySpeed(xenomorph.level(), blockPos) != -1
            && blockState.getBlock().defaultDestroyTime() < DESTROY_TIME_LIMIT
            && !blockState.is(AlienBlockTags.XENOMORPH_IMMUNE);
    }

    private DigToTargetAction() {
        throw new UnsupportedOperationException();
    }
}

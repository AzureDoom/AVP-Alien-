package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.idle;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.hive.location.HiveLocation;
import com.alien.common.gameplay.hive.location.HiveLocationRegistry;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.blib.api.common.goap.v1.action.impl.NeoMoveToPosAction;
import com.blib.api.common.goap.v1.action.impl.NeoWanderAction;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;
import com.just.ai.goap.state.Blackboard;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class IdleActions {

    private static final StateKey<Vec3> KEY_HIVE_WANDER_TARGET = StateKey.sensed("hive_wander_target");

    private static final int WANDER_HORIZONTAL_RANGE = 10;

    private static final int WANDER_VERTICAL_RANGE = 7;

    private static final double WANDER_SPEED = 0.5;

    private static final int HIVE_BOUND_WANDER_ATTEMPTS = 12;

    public static final Action<Xenomorph> WANDER = BLibAction.<Xenomorph>builder("WanderAction")
        .addMasks(ActionMasks.MOVE)
        .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isFalse())
        .addPrecondition(IdleSensors.IS_BORED.key(), Expressions.Boolean.isTrue())
        .addEffect(IdleSensors.IS_BORED.key().asDerived(), false)
        .withPerformCallback(context -> {
            if (isHiveBoundIdleWanderer(context.getActor())) {
                return performHiveBoundWander(context);
            }

            return NeoWanderAction.perform(
                context,
                WANDER_HORIZONTAL_RANGE,
                WANDER_VERTICAL_RANGE,
                WANDER_SPEED,
                ctx -> ctx.getActor().getXenomorphData().resetTicksUntilBored()
            );
        })
        .withFinishCallback(context -> {
            if (isHiveBoundIdleWanderer(context.getActor())) {
                NeoMoveToPosAction.onFinish(context);
                context.getBlackboard(Blackboard.Scope.ACTION).clear();
            } else {
                NeoWanderAction.onFinish(context);
            }
        })
        .build();

    private static Action.Signal performHiveBoundWander(Action.Context<? extends Xenomorph> context) {
        var actor = context.getActor();
        var location = HiveLocationRegistry.INSTANCE.getByChunk(actor.level().dimension(), actor.chunkPosition());
        if (location == null || !location.isAlive()) {
            return Action.Signal.ABORT;
        }

        var blackboard = context.getBlackboard(Blackboard.Scope.ACTION);
        var target = blackboard.getOrDefault(KEY_HIVE_WANDER_TARGET, (Vec3) null);
        if (target == null || !isInsideLocation(location, target)) {
            target = pickHiveBoundWanderTarget(actor, location);
            if (target == null) {
                actor.getXenomorphData().resetTicksUntilBored();
                return Action.Signal.CONTINUE;
            }
            blackboard.set(KEY_HIVE_WANDER_TARGET, target);
        }

        var result = NeoMoveToPosAction.perform(context, target, WANDER_SPEED);
        return switch (result) {
            case FINISHED -> {
                actor.getXenomorphData().resetTicksUntilBored();
                blackboard.set(KEY_HIVE_WANDER_TARGET, null);
                yield Action.Signal.CONTINUE;
            }
            case MOVING -> Action.Signal.CONTINUE;
            case NO_PATH -> {
                blackboard.set(KEY_HIVE_WANDER_TARGET, null);
                yield Action.Signal.ABORT;
            }
            default -> {
                blackboard.set(KEY_HIVE_WANDER_TARGET, null);
                yield Action.Signal.ABORT;
            }
        };
    }

    private static @Nullable Vec3 pickHiveBoundWanderTarget(Xenomorph actor, HiveLocation location) {
        for (var attempt = 0; attempt < HIVE_BOUND_WANDER_ATTEMPTS; attempt++) {
            var candidate = LandRandomPos.getPos(actor, WANDER_HORIZONTAL_RANGE, WANDER_VERTICAL_RANGE);
            if (candidate != null && isInsideLocation(location, candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private static boolean isHiveBoundIdleWanderer(Xenomorph actor) {
        return actor.getType().is(AlienEntityTypeTags.QUEENS)
            || actor.getType().is(AlienEntityTypeTags.EMPRESSES)
            || actor.getType().is(AlienEntityTypeTags.HARBINGERS);
    }

    private static boolean isInsideLocation(HiveLocation location, Vec3 pos) {
        return location.claimedChunks().contains(new ChunkPos((int) Math.floor(pos.x) >> 4, (int) Math.floor(pos.z) >> 4));
    }

    private IdleActions() {
        throw new UnsupportedOperationException();
    }
}

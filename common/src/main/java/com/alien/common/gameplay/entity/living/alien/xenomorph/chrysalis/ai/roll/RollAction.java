package com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.ai.roll;

import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.Chrysalis;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.just.ai.goap.action.Action;
import com.just.core.functional.option.Option;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class RollAction {

    public static Action.Signal perform(Action.Context<? extends Chrysalis> context) {
        var chrysalis = context.getActor();

        if (chrysalis.isRolling.get()) {
            return Action.Signal.CONTINUE;
        }

        if (!chrysalis.isRollCooldownReady()) {
            return Action.Signal.ABORT;
        }

        var worldState = context.getWorldState();
        var attackTargetOption = worldState.getOrDefault(GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(), Option.<LivingEntity>none());

        if (attackTargetOption.isNone()) {
            return Action.Signal.ABORT;
        }

        var attackTarget = attackTargetOption.unwrap();
        var yaw = computeYawTowards(chrysalis, attackTarget);

        chrysalis.startRoll(yaw);
        return Action.Signal.CONTINUE;
    }

    private static float computeYawTowards(Chrysalis chrysalis, LivingEntity target) {
        var dx = target.getX() - chrysalis.getX();
        var dz = target.getZ() - chrysalis.getZ();
        return (float) (Mth.atan2(-dx, dz) * Mth.RAD_TO_DEG);
    }

    private RollAction() {
        throw new UnsupportedOperationException();
    }
}

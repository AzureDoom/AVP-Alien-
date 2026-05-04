package com.alien.common.gameplay.entity.dismemberment;

import com.blib.api.common.dismemberment.v1.Dismemberable;
import com.blib.api.common.dismemberment.v1.LimbDefinitionRegistry;
import com.blib.api.common.dismemberment.v1.LimbDismemberer;
import net.minecraft.world.entity.LivingEntity;

/**
 * Server-side glue for the Ravager's one-shot charge attack.
 * <p>
 * For any {@link Dismemberable} target with registered limbs, asks BLib to detach each one. BLib captures the source's
 * NBT at dismemberment time and the client renders fragments through the source mob's own renderer, so this code
 * doesn't need to know anything about textures, models, or per-mob mappings.
 */
public final class RavagerHeadDismemberment {

    private RavagerHeadDismemberment() {}

    public static void tryDismemberHead(LivingEntity target) {
        if (target.level().isClientSide) {
            return;
        }

        if (!(target instanceof Dismemberable)) {
            return;
        }

        // TEMP: detach every registered limb so we can eyeball each one in-game. Revert to
        // detachFirstOfCategory(HEAD, ...) once the per-limb visuals are dialed in.
        for (var definition : LimbDefinitionRegistry.getDefinitions(target.getType())) {
            LimbDismemberer.detach(target, definition.id());
        }
    }
}

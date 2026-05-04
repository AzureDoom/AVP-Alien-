package com.alien.common.gameplay.entity.dismemberment;

import com.blib.api.common.dismemberment.v1.Dismemberable;
import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.LimbDismemberer;
import com.blib.api.common.dismemberment.v1.entity.DismemberedLimbEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Server-side glue for ravager attacks that decapitate. Detaches the first registered head-category limb on the target,
 * so any mob the consumer has wired up via a {@code HEAD}-categorized limb definition (drone variants, vanilla
 * humanoids, vanilla quadrupeds, ...) gets its head taken off uniformly.
 */
public final class RavagerHeadDismemberment {

    /**
     * Horizontal nudge, in blocks/tick, applied to the spawned head entity so it travels a few blocks past the body
     * before settling. The default tumble velocity from BLib is tiny on its own.
     */
    private static final double HORIZONTAL_KNOCKBACK = 0.15;

    /**
     * Vertical nudge, in blocks/tick, applied on top of the horizontal knockback so the head arcs rather than skidding
     * flat across the ground.
     */
    private static final double VERTICAL_KNOCKBACK = 0.05;

    private RavagerHeadDismemberment() {}

    public static void tryDismemberHead(LivingEntity target) {
        tryDismemberHead(target, null);
    }

    public static void tryDismemberHead(LivingEntity target, @Nullable Consumer<DismemberedLimbEntity> configurer) {
        if (target.level().isClientSide) {
            return;
        }

        if (!(target instanceof Dismemberable)) {
            return;
        }

        LimbDismemberer.detachFirstOfCategory(target, LimbCategories.HEAD, configurer);
    }

    /**
     * Builds a limb configurer that nudges the spawned head away from {@code from} (typically the attacking ravager) on
     * top of BLib's default random tumble velocity, so the fragment lands a few blocks away from both the body it came
     * off and the attacker.
     */
    public static Consumer<DismemberedLimbEntity> knockbackAwayFrom(Entity from) {
        return limb -> {
            var dx = limb.getX() - from.getX();
            var dz = limb.getZ() - from.getZ();
            var distSq = dx * dx + dz * dz;

            if (distSq < 1.0e-4) {
                return;
            }

            var dist = Math.sqrt(distSq);
            var ux = dx / dist;
            var uz = dz / dist;

            var current = limb.getDeltaMovement();
            limb.setDeltaMovement(
                current.x + ux * HORIZONTAL_KNOCKBACK,
                current.y + VERTICAL_KNOCKBACK,
                current.z + uz * HORIZONTAL_KNOCKBACK
            );
        };
    }
}

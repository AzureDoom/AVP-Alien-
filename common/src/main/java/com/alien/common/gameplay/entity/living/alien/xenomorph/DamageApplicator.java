package com.alien.common.gameplay.entity.living.alien.xenomorph;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface DamageApplicator {

    DamageApplicator DEFAULT = (xenomorph, target) -> {
        var attackRange = xenomorph.getBbWidth() + 1.0;

        if (xenomorph.distanceTo(target) > attackRange || !xenomorph.getSensing().hasLineOfSight(target)) {
            return;
        }

        xenomorph.swing(InteractionHand.MAIN_HAND);
        xenomorph.doHurtTarget(target);
    };

    DamageApplicator NOOP = (xenomorph, target) -> {};

    void apply(Xenomorph xenomorph, LivingEntity target);
}

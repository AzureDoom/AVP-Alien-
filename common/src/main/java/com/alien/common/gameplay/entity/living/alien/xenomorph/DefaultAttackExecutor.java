package com.alien.common.gameplay.entity.living.alien.xenomorph;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class DefaultAttackExecutor implements AttackExecutor {

    private int totalTicks;

    private int ticksRemaining;

    private boolean damageDealt;

    private @Nullable LivingEntity target;

    @Override
    public int totalDurationInTicks(AttackType attack) {
        return attack.defaultDurationInTicks();
    }

    @Override
    public void onStart(Xenomorph entity, AttackType attack, @Nullable LivingEntity target) {
        this.totalTicks = attack.defaultDurationInTicks();
        this.ticksRemaining = totalTicks;
        this.damageDealt = false;
        this.target = target;
    }

    @Override
    public boolean onTick(Xenomorph entity, AttackType attack) {
        ticksRemaining--;

        if (!damageDealt && target != null && target.isAlive()) {
            var elapsed = totalTicks - ticksRemaining;
            var damageTickThreshold = (int) (totalTicks * attack.damageThresholdPercent());

            if (elapsed >= damageTickThreshold) {
                attack.damageApplicator().apply(entity, target);
                damageDealt = true;
            }
        }

        return ticksRemaining > 0;
    }

    @Override
    public void onComplete(Xenomorph entity, AttackType attack) {
        target = null;
    }
}

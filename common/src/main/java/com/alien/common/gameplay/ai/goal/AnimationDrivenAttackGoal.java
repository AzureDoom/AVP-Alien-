package com.alien.common.gameplay.ai.goal;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class AnimationDrivenAttackGoal extends MeleeAttackGoal {

    private final Xenomorph xenomorph;

    private final float damagePointPercent;

    private final double attackRange;

    private int attackDurationInTicks;

    private int elapsedAttackTicks;

    private boolean dealtDamage;

    public AnimationDrivenAttackGoal(
        Xenomorph xenomorph,
        double speedModifier,
        boolean followTargetEvenIfNotSeen,
        float damagePointPercent,
        double attackRange
    ) {
        super(xenomorph, speedModifier, followTargetEvenIfNotSeen);
        this.xenomorph = xenomorph;
        this.damagePointPercent = damagePointPercent;
        this.attackRange = attackRange;

        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canContinueToUse() {
        if (xenomorph.isAttacking()) {
            return true;
        }

        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (!xenomorph.isAttacking()) {
            return;
        }

        elapsedAttackTicks++;

        var damageTickThreshold = (int) (attackDurationInTicks * damagePointPercent);

        if (dealtDamage || elapsedAttackTicks < damageTickThreshold) {
            return;
        }

        var target = mob.getTarget();

        if (
            target != null
                && mob.distanceTo(target) <= mob.getBbWidth() + attackRange
                && mob.getSensing().hasLineOfSight(target)
        ) {
            mob.swing(InteractionHand.MAIN_HAND);
            mob.doHurtTarget(target);
        }

        resetAttackCooldown();
        dealtDamage = true;
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity target) {
        if (xenomorph.isAttacking() || !canPerformAttack(target)) {
            return;
        }

        xenomorph.runAttackAnimations();
        attackDurationInTicks = xenomorph.attackDurationInTicks.get();
        elapsedAttackTicks = 0;
        dealtDamage = false;
    }

    @Override
    public void stop() {
        super.stop();
        elapsedAttackTicks = 0;
        dealtDamage = false;
    }
}

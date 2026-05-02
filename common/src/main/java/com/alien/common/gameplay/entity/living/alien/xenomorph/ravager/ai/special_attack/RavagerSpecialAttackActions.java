package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ai.combat.CombatSensors;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.RavagerAreaAttackUtil;
import com.alien.common.registry.key.AlienDamageTypeKeys;
import com.blib.api.common.goap.v1.GOAPSensors;
import com.blib.api.common.goap.v1.action.ActionMasks;
import com.blib.api.common.goap.v1.action.BLibAction;
import com.just.ai.goap.StateKey;
import com.just.ai.goap.action.Action;
import com.just.ai.goap.condition.expression.Expressions;
import com.just.core.functional.option.Option;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;

public class RavagerSpecialAttackActions {

    private static final StateKey<Boolean> KEY_ATTACK_STARTED = StateKey.sensed("ravager_special_attack_started");

    public static Action<Ravager> createSpecialAttack(RavagerSpecialAttackConfig config) {
        return BLibAction.<Ravager>builder("RavagerSpecialAttackAction")
            .addMasks(ActionMasks.MOVE, ActionMasks.LOOK)
            .addPrecondition(GOAPSensors.HAS_ATTACK_TARGET.key(), Expressions.Boolean.isTrue())
            .addPrecondition(CombatSensors.IS_TARGET_IN_MELEE_RANGE.key(), Expressions.Boolean.isTrue())
            .addPrecondition(RavagerSpecialAttackSensors.CAN_SPECIAL_ATTACK.key(), Expressions.Boolean.isTrue())
            .addEffect(RavagerSpecialAttackSensors.CAN_SPECIAL_ATTACK.key().asDerived(), false)
            .withCost(-2.0F)
            .withPerformCallback(context -> perform(context, config))
            .build();
    }

    private static Action.Signal perform(Action.Context<? extends Ravager> context, RavagerSpecialAttackConfig config) {
        var ravager = context.getActor();
        var worldState = context.getWorldState();
        var blackboard = context.getBlackboard(com.just.ai.goap.state.Blackboard.Scope.ACTION);
        var attackTargetOption = worldState.getOrDefault(GOAPSensors.NEAREST_ATTACKABLE_TARGET.key(), Option.<LivingEntity>none());
        var attackStarted = blackboard.getOrDefault(KEY_ATTACK_STARTED, false);

        if (attackStarted) {
            if (ravager.isUsingSpecialAttack()) {
                return Action.Signal.CONTINUE;
            }

            blackboard.set(KEY_ATTACK_STARTED, false);
            return Action.Signal.ABORT;
        }

        if (attackTargetOption.isNone()) {
            return Action.Signal.ABORT;
        }

        var target = attackTargetOption.unwrap();

        ravager.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
        ravager.getLookControl().setLookAt(target);
        ravager.getNavigation().stop();

        if (!ravager.getRavagerData().isSpecialAttackCooldownReady()) {
            return Action.Signal.ABORT;
        }

        ravager.startSpecialAttackWindup(config.getRandomWindupDurationInTicks(ravager.getRandom()), target);
        ravager.getRavagerData().resetSpecialAttackCooldown(config.cooldownInTicks());
        blackboard.set(KEY_ATTACK_STARTED, true);
        return Action.Signal.CONTINUE;
    }

    public static void damageEntitiesInFront(Ravager ravager, RavagerSpecialAttackConfig config) {
        var damageSource = ravager.damageSources().source(AlienDamageTypeKeys.RAVAGER_SPECIAL, ravager);
        var targets = RavagerAreaAttackUtil.getEntitiesInFront(ravager, config.rangeInBlocks(), config.coneAngleInDegrees());

        for (var target : targets) {
            if (target.isInvulnerableTo(damageSource)) {
                continue;
            }

            if (isSmallerThanRavager(ravager, target)) {
                target.hurt(damageSource, Float.MAX_VALUE);
            } else {
                target.hurt(damageSource, target.getMaxHealth() * 0.25F);
            }
        }
    }

    private static boolean isSmallerThanRavager(Ravager ravager, LivingEntity target) {
        return getSize(target) < getSize(ravager);
    }

    private static double getSize(LivingEntity entity) {
        return entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight();
    }

    private RavagerSpecialAttackActions() {
        throw new UnsupportedOperationException();
    }
}

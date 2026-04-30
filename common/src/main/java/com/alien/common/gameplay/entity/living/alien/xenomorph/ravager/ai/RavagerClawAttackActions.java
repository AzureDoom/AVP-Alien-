package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai.special_attack.RavagerSpecialAttackConfig;
import com.alien.common.registry.key.AlienDamageTypeKeys;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class RavagerClawAttackActions {

    public static void damageEntitiesInFront(Ravager ravager) {
        var config = RavagerSpecialAttackConfig.DEFAULT;
        var damageSource = ravager.damageSources().source(AlienDamageTypeKeys.RAVAGER_CLAW, ravager);
        var damage = (float) ravager.getAttributeValue(Attributes.ATTACK_DAMAGE);
        var targets = RavagerAreaAttackUtil.getEntitiesInFront(ravager, config.rangeInBlocks(), config.coneAngleInDegrees());

        for (var target : targets) {
            if (!target.isInvulnerableTo(damageSource)) {
                target.hurt(damageSource, damage);
            }
        }
    }

    private RavagerClawAttackActions() {
        throw new UnsupportedOperationException();
    }
}

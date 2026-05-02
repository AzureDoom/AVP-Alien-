package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.alien.common.registry.key.AlienDamageTypeKeys;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class RavagerClawAttackActions {

    public static void damageEntitiesInFront(Ravager ravager) {
        var damageSource = ravager.damageSources().source(AlienDamageTypeKeys.RAVAGER_CLAW, ravager);
        var damage = (float) ravager.getAttributeValue(Attributes.ATTACK_DAMAGE);
        var targets = RavagerAreaAttackUtil.getEntitiesInFront(
            ravager,
            Ravager.FRONT_AOE_RANGE_IN_BLOCKS,
            Ravager.FRONT_AOE_CONE_ANGLE_DEGREES
        );

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

package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.ai;

import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RavagerAreaAttackUtil {

    public static List<LivingEntity> getEntitiesInFront(Ravager ravager, double rangeInBlocks, double coneAngleInDegrees) {
        var searchBox = ravager.getBoundingBox()
            .expandTowards(getForward(ravager).scale(rangeInBlocks))
            .inflate(ravager.getBbWidth(), ravager.getBbHeight() * 0.5, ravager.getBbWidth());

        return ravager.level()
            .getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                target -> target != ravager && target.isAlive() && isInAttackCone(ravager, target, rangeInBlocks, coneAngleInDegrees)
            );
    }

    private static boolean isInAttackCone(Ravager ravager, LivingEntity target, double rangeInBlocks, double coneAngleInDegrees) {
        var forward = getForward(ravager);
        var toTarget = target.position().subtract(ravager.position());
        var horizontalToTarget = new Vec3(toTarget.x, 0, toTarget.z);
        var distance = horizontalToTarget.length();

        if (distance <= 0.001 || distance > rangeInBlocks + target.getBbWidth() * 0.5) {
            return false;
        }

        var dot = forward.dot(horizontalToTarget.normalize());
        var minDot = Math.cos(Math.toRadians(coneAngleInDegrees * 0.5));

        return dot >= minDot && ravager.getSensing().hasLineOfSight(target);
    }

    private static Vec3 getForward(Ravager ravager) {
        var forward = ravager.getViewVector(1.0F);
        return new Vec3(forward.x, 0, forward.z).normalize();
    }

    /**
     * True when {@code target}'s bounding-box height is less than the ravager's. Used as the gate for
     * dismemberment-on-strike behaviours so the ravager can only tear limbs off things shorter than itself; volume was
     * misleading for stockier mobs (cow, sheep) that read as "small" to the eye but had similar mass to the ravager
     * once width was squared.
     */
    public static boolean isSmallerThanRavager(Ravager ravager, LivingEntity target) {
        return target.getBbHeight() < ravager.getBbHeight();
    }

    private RavagerAreaAttackUtil() {
        throw new UnsupportedOperationException();
    }
}

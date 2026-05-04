package com.alien.common.gameplay.entity.dismemberment;

import com.alien.AlienResources;
import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.LimbDefinition;
import com.blib.api.common.dismemberment.v1.LimbDefinitionRegistry;
import com.blib.api.common.registry.v1.BLibHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

/**
 * Shared registration helper for adult bipedal xenomorphs whose geo models follow the standard bone naming used by the
 * drone (gHead, gLeft/gRightShoulder, gLeft/gRightLeg, gTail1). Render offsets and rotations are tuned for the drone
 * bind pose and reused since all of these xenomorphs share the same skeleton; spawn offsets scale with the entity's
 * hitbox so larger forms (queen, empress, harbinger) settle correctly.
 */
public final class XenomorphLimbs {

    private XenomorphLimbs() {}

    @SafeVarargs
    public static void register(String idPrefix, BLibHolder<? extends EntityType<?>>... entityTypes) {
        var head = head(idPrefix);
        var leftArm = leftArm(idPrefix);
        var rightArm = rightArm(idPrefix);
        var leftLeg = leftLeg(idPrefix);
        var rightLeg = rightLeg(idPrefix);
        var tail = tail(idPrefix);

        for (var entityType : entityTypes) {
            LimbDefinitionRegistry.register(entityType, head);
            LimbDefinitionRegistry.register(entityType, leftArm);
            LimbDefinitionRegistry.register(entityType, rightArm);
            LimbDefinitionRegistry.register(entityType, leftLeg);
            LimbDefinitionRegistry.register(entityType, rightLeg);
            LimbDefinitionRegistry.register(entityType, tail);
        }
    }

    private static LimbDefinition head(String prefix) {
        return LimbDefinition.builder(
            AlienResources.location(prefix + "_head"),
            "gHead",
            LimbCategories.HEAD
        )
            .renderOffset(0.0, 0.125, 0.0)
            .spawnAtEyeHeight()
            .build();
    }

    private static LimbDefinition leftArm(String prefix) {
        return LimbDefinition.builder(
            AlienResources.location(prefix + "_left_arm"),
            "gLeftShoulder",
            LimbCategories.ARM
        )
            .renderOffset(0.0, 0.25, 0.0)
            .renderRotation(-135.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.75, 0.0))
            .build();
    }

    private static LimbDefinition rightArm(String prefix) {
        return LimbDefinition.builder(
            AlienResources.location(prefix + "_right_arm"),
            "gRightShoulder",
            LimbCategories.ARM
        )
            .renderOffset(0.0, 0.25, 0.0)
            .renderRotation(-135.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.75, 0.0))
            .build();
    }

    private static LimbDefinition leftLeg(String prefix) {
        return LimbDefinition.builder(
            AlienResources.location(prefix + "_left_leg"),
            "gLeftLeg",
            LimbCategories.LEG
        )
            .renderOffset(0.0, 0.5, 0.0)
            .renderRotation(-90.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
            .build();
    }

    private static LimbDefinition rightLeg(String prefix) {
        return LimbDefinition.builder(
            AlienResources.location(prefix + "_right_leg"),
            "gRightLeg",
            LimbCategories.LEG
        )
            .renderOffset(0.0, 0.5, 0.0)
            .renderRotation(-90.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
            .build();
    }

    private static LimbDefinition tail(String prefix) {
        return LimbDefinition.builder(
            AlienResources.location(prefix + "_tail"),
            "gTail1",
            LimbCategories.TAIL
        )
            .build();
    }
}

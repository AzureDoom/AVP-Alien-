package com.alien.common.gameplay.entity.dismemberment;

import com.alien.AlienResources;
import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.LimbDefinition;
import com.blib.api.common.registry.v1.BLibHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

/**
 * Shared registration helper for adult bipedal xenomorphs whose geo models follow the standard bone naming used by the
 * drone (gHead, gLeft/gRightShoulder, gLeft/gRightLeg, gTail1). Render offsets and rotations are tuned for the drone
 * bind pose and reused since all of these xenomorphs share the same skeleton; spawn offsets scale with the entity's
 * hitbox so larger forms (queen, empress, harbinger) settle correctly.
 * <p>
 * {@link LimbDefinition.Builder#build()} self-registers each definition with BLib's registries against the supplied
 * entity-type holder, so we build one set of limb definitions per entity type rather than once-and-share. The
 * {@link LimbDefinition#builder(BLibHolder, net.minecraft.resources.ResourceLocation, String, com.blib.api.common.dismemberment.v1.LimbCategory)}
 * overload is bind-free — registration happens at mod init before holders resolve their backing entries.
 */
public final class XenomorphLimbs {

    private XenomorphLimbs() {}

    @SafeVarargs
    public static void register(String idPrefix, BLibHolder<? extends EntityType<?>>... entityTypes) {
        for (var holder : entityTypes) {
            head(holder, idPrefix);
            leftArm(holder, idPrefix);
            rightArm(holder, idPrefix);
            leftLeg(holder, idPrefix);
            rightLeg(holder, idPrefix);
            tail(holder, idPrefix);
        }
    }

    private static LimbDefinition head(BLibHolder<? extends EntityType<?>> entityType, String prefix) {
        return LimbDefinition.builder(entityType, AlienResources.location(prefix + "_head"), "gHead", LimbCategories.HEAD)
            .renderOffset(0.0, 0.125, 0.0)
            .spawnAtEyeHeight()
            .fatal()
            .build();
    }

    private static LimbDefinition leftArm(BLibHolder<? extends EntityType<?>> entityType, String prefix) {
        return LimbDefinition.builder(entityType, AlienResources.location(prefix + "_left_arm"), "gLeftShoulder", LimbCategories.ARM)
            .renderOffset(0.0, 0.25, 0.0)
            .renderRotation(-135.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.75, 0.0))
            .build();
    }

    private static LimbDefinition rightArm(BLibHolder<? extends EntityType<?>> entityType, String prefix) {
        return LimbDefinition.builder(entityType, AlienResources.location(prefix + "_right_arm"), "gRightShoulder", LimbCategories.ARM)
            .renderOffset(0.0, 0.25, 0.0)
            .renderRotation(-135.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.75, 0.0))
            .build();
    }

    private static LimbDefinition leftLeg(BLibHolder<? extends EntityType<?>> entityType, String prefix) {
        return LimbDefinition.builder(entityType, AlienResources.location(prefix + "_left_leg"), "gLeftLeg", LimbCategories.LEG)
            .renderOffset(0.0, 0.5, 0.0)
            .renderRotation(-90.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
            .build();
    }

    private static LimbDefinition rightLeg(BLibHolder<? extends EntityType<?>> entityType, String prefix) {
        return LimbDefinition.builder(entityType, AlienResources.location(prefix + "_right_leg"), "gRightLeg", LimbCategories.LEG)
            .renderOffset(0.0, 0.5, 0.0)
            .renderRotation(-90.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
            .build();
    }

    private static LimbDefinition tail(BLibHolder<? extends EntityType<?>> entityType, String prefix) {
        return LimbDefinition.builder(entityType, AlienResources.location(prefix + "_tail"), "gTail1", LimbCategories.TAIL).build();
    }
}

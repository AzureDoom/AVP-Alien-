package com.alien.common.gameplay.entity.dismemberment;

import com.alien.AlienResources;
import com.alien.common.registry.init.AlienEntityTypes;
import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.LimbDefinition;
import com.blib.api.common.registry.v1.BLibHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

/**
 * Queen-variant limb definitions. Geo bones differ from the standard xenomorph skeleton only in render offsets (the
 * queen's larger head needs a 0.375 lift and a -60° forward tilt to sit naturally), so the queen + her variants get
 * their own helper rather than going through {@link XenomorphLimbs}.
 * <p>
 * Uses the bind-free
 * {@link LimbDefinition#builder(BLibHolder, net.minecraft.resources.ResourceLocation, String, com.blib.api.common.dismemberment.v1.LimbCategory)}
 * overload so registration runs at mod-init before {@link BLibHolder}s resolve their backing entries.
 */
public final class QueenLimbDefinitions {

    private QueenLimbDefinitions() {}

    public static void initialize() {
        registerFor(AlienEntityTypes.QUEEN);
        registerFor(AlienEntityTypes.ABERRANT_QUEEN);
        registerFor(AlienEntityTypes.NETHER_QUEEN);
        registerFor(AlienEntityTypes.IRRADIATED_QUEEN);
    }

    private static void registerFor(BLibHolder<? extends EntityType<?>> entityType) {
        head(entityType);
        leftArm(entityType);
        rightArm(entityType);
        leftLeg(entityType);
        rightLeg(entityType);
        tail(entityType);
    }

    private static LimbDefinition head(BLibHolder<? extends EntityType<?>> entityType) {
        return LimbDefinition.builder(entityType, AlienResources.location("queen_head"), "gHead", LimbCategories.HEAD)
            .renderOffset(0.0, 0.375, 0.0)
            .renderRotation(-60.0, 0.0, 0.0)
            .spawnAtEyeHeight()
            .fatal()
            .build();
    }

    private static LimbDefinition leftArm(BLibHolder<? extends EntityType<?>> entityType) {
        return LimbDefinition.builder(entityType, AlienResources.location("queen_left_arm"), "gLeftShoulder", LimbCategories.ARM)
            .renderOffset(0.0, 0.25, 0.0)
            .renderRotation(-135.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.75, 0.0))
            .build();
    }

    private static LimbDefinition rightArm(BLibHolder<? extends EntityType<?>> entityType) {
        return LimbDefinition.builder(entityType, AlienResources.location("queen_right_arm"), "gRightShoulder", LimbCategories.ARM)
            .renderOffset(0.0, 0.25, 0.0)
            .renderRotation(-135.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.75, 0.0))
            .build();
    }

    private static LimbDefinition leftLeg(BLibHolder<? extends EntityType<?>> entityType) {
        return LimbDefinition.builder(entityType, AlienResources.location("queen_left_leg"), "gLeftLeg", LimbCategories.LEG)
            .renderOffset(0.0, 0.5, 0.0)
            .renderRotation(-90.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
            .build();
    }

    private static LimbDefinition rightLeg(BLibHolder<? extends EntityType<?>> entityType) {
        return LimbDefinition.builder(entityType, AlienResources.location("queen_right_leg"), "gRightLeg", LimbCategories.LEG)
            .renderOffset(0.0, 0.5, 0.0)
            .renderRotation(-90.0, 0.0, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
            .build();
    }

    private static LimbDefinition tail(BLibHolder<? extends EntityType<?>> entityType) {
        return LimbDefinition.builder(entityType, AlienResources.location("queen_tail"), "gTail1", LimbCategories.TAIL).build();
    }
}

package com.alien.common.gameplay.entity.dismemberment;

import com.alien.AlienResources;
import com.alien.common.registry.init.AlienEntityTypes;
import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.LimbDefinition;
import com.blib.api.common.dismemberment.v1.LimbDefinitionRegistry;
import com.blib.api.common.registry.v1.BLibHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public final class AlienLimbDefinitions {

    public static final LimbDefinition DRONE_HEAD = LimbDefinition.builder(
        AlienResources.location("drone_head"),
        "gHead",
        LimbCategories.HEAD
    )
        // Lift the rendered head so it sits inside the limb entity's hitbox
        // rather than penetrating the ground.
        .renderOffset(0.0, 0.125, 0.0)
        // Spawn at the drone's eye height so the head pops off where it
        // visually was on the body, not the entity's centre.
        .spawnAtEyeHeight()
        .build();

    public static final LimbDefinition DRONE_LEFT_ARM = LimbDefinition.builder(
        AlienResources.location("drone_left_arm"),
        "gLeftShoulder",
        LimbCategories.ARM
    )
        .renderOffset(0.0, 0.25, 0.0)
        // Arms hang downward in bind pose; pitch them backward so they lie
        // along the ground instead of standing up from the shoulder.
        .renderRotation(-135.0, 0.0, 0.0)
        .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.75, 0.0))
        .build();

    public static final LimbDefinition DRONE_RIGHT_ARM = LimbDefinition.builder(
        AlienResources.location("drone_right_arm"),
        "gRightShoulder",
        LimbCategories.ARM
    )
        .renderOffset(0.0, 0.25, 0.0)
        .renderRotation(-135.0, 0.0, 0.0)
        .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.75, 0.0))
        .build();

    public static final LimbDefinition DRONE_LEFT_LEG = LimbDefinition.builder(
        AlienResources.location("drone_left_leg"),
        "gLeftLeg",
        LimbCategories.LEG
    )
        .renderOffset(0.0, 0.5, 0.0)
        .renderRotation(-90.0, 0.0, 0.0)
        .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
        .build();

    public static final LimbDefinition DRONE_RIGHT_LEG = LimbDefinition.builder(
        AlienResources.location("drone_right_leg"),
        "gRightLeg",
        LimbCategories.LEG
    )
        .renderOffset(0.0, 0.5, 0.0)
        .renderRotation(-90.0, 0.0, 0.0)
        .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
        .build();

    public static final LimbDefinition DRONE_TAIL = LimbDefinition.builder(
        AlienResources.location("drone_tail"),
        "gTail1",
        LimbCategories.TAIL
    )
        .build();

    public static void initialize() {
        registerDroneLimbs(AlienEntityTypes.DRONE);
        registerDroneLimbs(AlienEntityTypes.NETHER_DRONE);
        registerDroneLimbs(AlienEntityTypes.ABERRANT_DRONE);
        registerDroneLimbs(AlienEntityTypes.IRRADIATED_DRONE);
    }

    private static void registerDroneLimbs(BLibHolder<? extends EntityType<?>> entityType) {
        LimbDefinitionRegistry.register(entityType, DRONE_HEAD);
        LimbDefinitionRegistry.register(entityType, DRONE_LEFT_ARM);
        LimbDefinitionRegistry.register(entityType, DRONE_RIGHT_ARM);
        LimbDefinitionRegistry.register(entityType, DRONE_LEFT_LEG);
        LimbDefinitionRegistry.register(entityType, DRONE_RIGHT_LEG);
        LimbDefinitionRegistry.register(entityType, DRONE_TAIL);
    }

    private AlienLimbDefinitions() {}
}

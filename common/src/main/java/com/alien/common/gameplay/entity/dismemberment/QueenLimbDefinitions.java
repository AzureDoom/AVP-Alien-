package com.alien.common.gameplay.entity.dismemberment;

import com.alien.AlienResources;
import com.alien.common.registry.init.AlienEntityTypes;
import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.LimbDefinition;
import com.blib.api.common.dismemberment.v1.LimbDefinitionRegistry;
import com.blib.api.common.registry.v1.BLibHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public final class QueenLimbDefinitions {

    public static final LimbDefinition HEAD = LimbDefinition.builder(
        AlienResources.location("queen_head"),
        "gHead",
        LimbCategories.HEAD
    )
        .renderOffset(0.0, 0.375, 0.0)
        .renderRotation(-60.0, 0.0, 0.0)
        .spawnAtEyeHeight()
        .fatal()
        .build();

    public static final LimbDefinition LEFT_ARM = LimbDefinition.builder(
        AlienResources.location("queen_left_arm"),
        "gLeftShoulder",
        LimbCategories.ARM
    )
        .renderOffset(0.0, 0.25, 0.0)
        .renderRotation(-135.0, 0.0, 0.0)
        .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.75, 0.0))
        .build();

    public static final LimbDefinition RIGHT_ARM = LimbDefinition.builder(
        AlienResources.location("queen_right_arm"),
        "gRightShoulder",
        LimbCategories.ARM
    )
        .renderOffset(0.0, 0.25, 0.0)
        .renderRotation(-135.0, 0.0, 0.0)
        .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.75, 0.0))
        .build();

    public static final LimbDefinition LEFT_LEG = LimbDefinition.builder(
        AlienResources.location("queen_left_leg"),
        "gLeftLeg",
        LimbCategories.LEG
    )
        .renderOffset(0.0, 0.5, 0.0)
        .renderRotation(-90.0, 0.0, 0.0)
        .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
        .build();

    public static final LimbDefinition RIGHT_LEG = LimbDefinition.builder(
        AlienResources.location("queen_right_leg"),
        "gRightLeg",
        LimbCategories.LEG
    )
        .renderOffset(0.0, 0.5, 0.0)
        .renderRotation(-90.0, 0.0, 0.0)
        .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
        .build();

    public static final LimbDefinition TAIL = LimbDefinition.builder(
        AlienResources.location("queen_tail"),
        "gTail1",
        LimbCategories.TAIL
    )
        .build();

    public static void initialize() {
        registerFor(AlienEntityTypes.QUEEN);
        registerFor(AlienEntityTypes.ABERRANT_QUEEN);
        registerFor(AlienEntityTypes.NETHER_QUEEN);
        registerFor(AlienEntityTypes.IRRADIATED_QUEEN);
    }

    private static void registerFor(BLibHolder<? extends EntityType<?>> entityType) {
        LimbDefinitionRegistry.register(entityType, HEAD);
        LimbDefinitionRegistry.register(entityType, LEFT_ARM);
        LimbDefinitionRegistry.register(entityType, RIGHT_ARM);
        LimbDefinitionRegistry.register(entityType, LEFT_LEG);
        LimbDefinitionRegistry.register(entityType, RIGHT_LEG);
        LimbDefinitionRegistry.register(entityType, TAIL);
    }

    private QueenLimbDefinitions() {}
}

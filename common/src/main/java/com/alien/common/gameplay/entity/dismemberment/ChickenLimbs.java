package com.alien.common.gameplay.entity.dismemberment;

import com.alien.AlienResources;
import com.blib.api.common.dismemberment.v1.LimbCategories;
import com.blib.api.common.dismemberment.v1.LimbDefinition;
import com.blib.api.common.dismemberment.v1.LimbDefinitionRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

/**
 * Registration helper for the vanilla chicken. ChickenModel exposes head/body, two legs, and two wings as named
 * children of the root. Wings are categorized as ARM since they're the chicken's upper-limb analog.
 */
public final class ChickenLimbs {

    private ChickenLimbs() {}

    public static void register() {
        var typeId = BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.CHICKEN);

        LimbDefinitionRegistry.register(typeId, head());
        LimbDefinitionRegistry.register(typeId, body());
        LimbDefinitionRegistry.register(typeId, leg("right_leg"));
        LimbDefinitionRegistry.register(typeId, leg("left_leg"));
        LimbDefinitionRegistry.register(typeId, wing("right_wing"));
        LimbDefinitionRegistry.register(typeId, wing("left_wing"));
    }

    private static LimbDefinition head() {
        return LimbDefinition.builder(
            AlienResources.location("chicken_head"),
            "head",
            LimbCategories.HEAD
        )
            .spawnAtEyeHeight()
            .build();
    }

    private static LimbDefinition body() {
        // No torso category yet — re-categorize once one exists in BLib.
        return LimbDefinition.builder(
            AlienResources.location("chicken_body"),
            "body",
            LimbCategories.HEAD
        )
            .renderRotation(90.0, 0.0, 0.0)
            .renderOffset(0.0, 0.25, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.5, 0.0))
            .build();
    }

    private static LimbDefinition leg(String partName) {
        return LimbDefinition.builder(
            AlienResources.location("chicken_" + partName),
            partName,
            LimbCategories.LEG
        )
            .renderRotation(90.0, 0.0, 0.0)
            .renderOffset(0.0, 0.125, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.3, 0.0))
            .build();
    }

    private static LimbDefinition wing(String partName) {
        return LimbDefinition.builder(
            AlienResources.location("chicken_" + partName),
            partName,
            LimbCategories.ARM
        )
            .renderRotation(90.0, 0.0, 0.0)
            .renderOffset(0.0, 0.125, 0.0)
            .spawnOffset(entity -> new Vec3(0.0, entity.getBbHeight() * 0.6, 0.0))
            .build();
    }
}

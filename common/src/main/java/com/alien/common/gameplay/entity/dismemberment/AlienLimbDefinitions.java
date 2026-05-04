package com.alien.common.gameplay.entity.dismemberment;

import net.minecraft.world.entity.EntityType;

/**
 * Aggregates per-mob limb definition registrations. Inline {@link HumanoidLimbs}/{@link QuadrupedLimbs} calls cover any
 * vanilla mob whose limbs follow a standard pattern; mobs with bespoke geometry (e.g. the drone) keep their own
 * dedicated definition class.
 */
public final class AlienLimbDefinitions {

    public static void initialize() {
        // AVP entities
        DroneLimbDefinitions.initialize();

        // Vanilla humanoid hostile mobs
        HumanoidLimbs.register(EntityType.ZOMBIE, "zombie");
        HumanoidLimbs.register(EntityType.ZOMBIE_VILLAGER, "zombie_villager");
        HumanoidLimbs.register(EntityType.HUSK, "husk");
        HumanoidLimbs.register(EntityType.DROWNED, "drowned");
        HumanoidLimbs.register(EntityType.SKELETON, "skeleton");
        HumanoidLimbs.register(EntityType.STRAY, "stray");
        HumanoidLimbs.register(EntityType.WITHER_SKELETON, "wither_skeleton");
        HumanoidLimbs.register(EntityType.PIGLIN, "piglin");
        HumanoidLimbs.register(EntityType.PIGLIN_BRUTE, "piglin_brute");
        HumanoidLimbs.register(EntityType.ZOMBIFIED_PIGLIN, "zombified_piglin");
        HumanoidLimbs.register(EntityType.PILLAGER, "pillager");
        HumanoidLimbs.register(EntityType.VINDICATOR, "vindicator");
        HumanoidLimbs.register(EntityType.EVOKER, "evoker");
        HumanoidLimbs.register(EntityType.ILLUSIONER, "illusioner");

        // Vanilla quadrupeds (and creeper, which shares the QuadrupedModel-style part naming)
        QuadrupedLimbs.register(EntityType.COW, "cow");
        QuadrupedLimbs.register(EntityType.PIG, "pig");
        QuadrupedLimbs.register(EntityType.CREEPER, "creeper");
    }

    private AlienLimbDefinitions() {}
}

package com.alien.common.gameplay.entity.dismemberment;

import com.alien.common.registry.init.AlienEntityTypes;
import net.minecraft.world.entity.EntityType;

/**
 * Aggregates per-mob limb definition registrations. Inline {@link HumanoidLimbs}/{@link QuadrupedLimbs} calls cover any
 * vanilla mob whose limbs follow a standard pattern; mobs with bespoke geometry (e.g. the drone) keep their own
 * dedicated definition class.
 */
public final class AlienLimbDefinitions {

    public static void initialize() {
        // AVP entities
        QueenLimbDefinitions.initialize();
        registerXenomorphs();

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
        HumanoidLimbs.register(EntityType.ENDERMAN, "enderman");
        HumanoidLimbs.register(EntityType.BOGGED, "bogged");

        // IronGolemModel and WardenModel both expose head/body/arms/legs by name, so HumanoidLimbs is sufficient on
        // the def side. Iron golem's parts are direct children of the model root (resolved by BLib's HierarchicalModel
        // resolver out of the box); warden parts are nested under bone/body and need the custom resolver registered
        // client-side in AlienLimbModelResolvers.
        HumanoidLimbs.register(EntityType.IRON_GOLEM, "iron_golem");
        HumanoidLimbs.register(EntityType.WARDEN, "warden");

        // VillagerModel-based mobs use a unified `arms` ModelPart, not Steve-style separate arms — needs its own
        // helper so the ARM limb resolves at render time. Witch and wandering trader inherit VillagerModel.
        VillagerLimbs.register(EntityType.VILLAGER, "villager");
        VillagerLimbs.register(EntityType.WITCH, "witch");
        VillagerLimbs.register(EntityType.WANDERING_TRADER, "wandering_trader");

        // Mobs without legs / with non-standard part names get bespoke helpers.
        SnowGolemLimbs.register();
        AllayLimbs.register();

        // Spider/cave-spider share SpiderModel (8 legs + head).
        SpiderLimbs.register(EntityType.SPIDER, "spider");
        SpiderLimbs.register(EntityType.CAVE_SPIDER, "cave_spider");

        // Wolf/horse-family/fox/chicken/rabbit need the reflective resolver registered client-side; their named parts
        // live in private camelCase fields rather than being reachable through a model root.
        WolfLimbs.register();
        FoxLimbs.register();
        ChickenLimbs.register();
        RabbitLimbs.register();
        HorseLimbs.register(EntityType.HORSE, "horse");
        HorseLimbs.register(EntityType.DONKEY, "donkey");
        HorseLimbs.register(EntityType.MULE, "mule");
        HorseLimbs.register(EntityType.SKELETON_HORSE, "skeleton_horse");
        HorseLimbs.register(EntityType.ZOMBIE_HORSE, "zombie_horse");

        // Vanilla quadrupeds (and creeper, which shares the QuadrupedModel-style part naming)
        QuadrupedLimbs.register(EntityType.COW, "cow");
        QuadrupedLimbs.register(EntityType.PIG, "pig");
        QuadrupedLimbs.register(EntityType.CREEPER, "creeper");
        QuadrupedLimbs.register(EntityType.SHEEP, "sheep");
        QuadrupedLimbs.register(EntityType.MOOSHROOM, "mooshroom");
        QuadrupedLimbs.register(EntityType.PANDA, "panda");
        QuadrupedLimbs.register(EntityType.POLAR_BEAR, "polar_bear");
        QuadrupedLimbs.register(EntityType.GOAT, "goat");
        QuadrupedLimbs.register(EntityType.TURTLE, "turtle");
    }

    private static void registerXenomorphs() {
        XenomorphLimbs.register(
            "drone",
            AlienEntityTypes.DRONE,
            AlienEntityTypes.ABERRANT_DRONE,
            AlienEntityTypes.NETHER_DRONE,
            AlienEntityTypes.IRRADIATED_DRONE
        );
        XenomorphLimbs.register(
            "warrior",
            AlienEntityTypes.WARRIOR,
            AlienEntityTypes.ABERRANT_WARRIOR,
            AlienEntityTypes.NETHER_WARRIOR,
            AlienEntityTypes.IRRADIATED_WARRIOR
        );
        XenomorphLimbs.register(
            "runner",
            AlienEntityTypes.RUNNER,
            AlienEntityTypes.ABERRANT_RUNNER,
            AlienEntityTypes.NETHER_RUNNER,
            AlienEntityTypes.IRRADIATED_RUNNER
        );
        XenomorphLimbs.register(
            "spitter",
            AlienEntityTypes.SPITTER,
            AlienEntityTypes.ABERRANT_SPITTER,
            AlienEntityTypes.NETHER_SPITTER
        );
        XenomorphLimbs.register(
            "praetorian",
            AlienEntityTypes.PRAETORIAN,
            AlienEntityTypes.ABERRANT_PRAETORIAN,
            AlienEntityTypes.NETHER_PRAETORIAN,
            AlienEntityTypes.IRRADIATED_PRAETORIAN
        );
        XenomorphLimbs.register(
            "crusher",
            AlienEntityTypes.CRUSHER,
            AlienEntityTypes.ABERRANT_CRUSHER,
            AlienEntityTypes.NETHER_CRUSHER,
            AlienEntityTypes.IRRADIATED_CRUSHER
        );
        XenomorphLimbs.register(
            "boiler",
            AlienEntityTypes.BOILER,
            AlienEntityTypes.ABERRANT_BOILER,
            AlienEntityTypes.NETHER_BOILER
        );
        XenomorphLimbs.register(
            "razor_claw",
            AlienEntityTypes.RAZOR_CLAW,
            AlienEntityTypes.ABERRANT_RAZOR_CLAW,
            AlienEntityTypes.NETHER_RAZOR_CLAW,
            AlienEntityTypes.IRRADIATED_RAZOR_CLAW
        );
        XenomorphLimbs.register(
            "ravager",
            AlienEntityTypes.RAVAGER,
            AlienEntityTypes.ABERRANT_RAVAGER,
            AlienEntityTypes.NETHER_RAVAGER,
            AlienEntityTypes.IRRADIATED_RAVAGER
        );
        XenomorphLimbs.register(
            "prowler",
            AlienEntityTypes.PROWLER,
            AlienEntityTypes.ABERRANT_PROWLER,
            AlienEntityTypes.NETHER_PROWLER,
            AlienEntityTypes.IRRADIATED_PROWLER
        );
        XenomorphLimbs.register(
            "carrier",
            AlienEntityTypes.CARRIER,
            AlienEntityTypes.ABERRANT_CARRIER,
            AlienEntityTypes.NETHER_CARRIER,
            AlienEntityTypes.IRRADIATED_CARRIER
        );
        XenomorphLimbs.register(
            "chrysalis",
            AlienEntityTypes.CHRYSALIS,
            AlienEntityTypes.ABERRANT_CHRYSALIS,
            AlienEntityTypes.NETHER_CHRYSALIS,
            AlienEntityTypes.IRRADIATED_CHRYSALIS
        );
        XenomorphLimbs.register(
            "predalien",
            AlienEntityTypes.PREDALIEN,
            AlienEntityTypes.ABERRANT_PREDALIEN,
            AlienEntityTypes.NETHER_PREDALIEN,
            AlienEntityTypes.IRRADIATED_PREDALIEN
        );
        XenomorphLimbs.register(
            "burster",
            AlienEntityTypes.BURSTER,
            AlienEntityTypes.ABERRANT_BURSTER,
            AlienEntityTypes.NETHER_BURSTER,
            AlienEntityTypes.IRRADIATED_BURSTER
        );
        XenomorphLimbs.register(
            "empress",
            AlienEntityTypes.EMPRESS,
            AlienEntityTypes.ABERRANT_EMPRESS,
            AlienEntityTypes.NETHER_EMPRESS,
            AlienEntityTypes.IRRADIATED_EMPRESS
        );
        XenomorphLimbs.register(
            "harbinger",
            AlienEntityTypes.HARBINGER,
            AlienEntityTypes.ABERRANT_HARBINGER,
            AlienEntityTypes.NETHER_HARBINGER,
            AlienEntityTypes.IRRADIATED_HARBINGER
        );
    }

    private AlienLimbDefinitions() {}
}

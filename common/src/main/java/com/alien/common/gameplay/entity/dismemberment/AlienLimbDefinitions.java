package com.alien.common.gameplay.entity.dismemberment;

import com.alien.common.registry.init.AlienEntityTypes;

/**
 * Aggregates AVP-Alien limb definition registrations. Vanilla mob limb defs (zombie/skeleton/cow/wolf/etc.) are
 * provided by BLib's {@code BuiltInLimbDefinitions} and registered automatically during BLib's mod init — this class
 * only has to wire up our custom xenomorph entities.
 */
public final class AlienLimbDefinitions {

    public static void initialize() {
        QueenLimbDefinitions.initialize();
        registerXenomorphs();
    }

    private static void registerXenomorphs() {
        XenomorphLimbs.registerSpawnOffsets("drone");
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

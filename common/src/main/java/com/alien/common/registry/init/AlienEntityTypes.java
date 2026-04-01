package com.alien.common.registry.init;

import com.alien.Alien;
import com.alien.common.gameplay.entity.acid.Acid;
import com.alien.common.gameplay.entity.living.alien.adolescent.Adolescent;
import com.alien.common.gameplay.entity.living.alien.chestburster.Chestburster;
import com.alien.common.gameplay.entity.living.alien.ovipositor.Ovipositor;
import com.alien.common.gameplay.entity.living.alien.ovomorph.Ovomorph;
import com.alien.common.gameplay.entity.living.alien.parasite.facehugger.Facehugger;
import com.alien.common.gameplay.entity.living.alien.predalien_adolescent.PredalienAdolescent;
import com.alien.common.gameplay.entity.living.alien.predalien_chestburster.PredalienChestburster;
import com.alien.common.gameplay.entity.living.alien.xenomorph.boiler.Boiler;
import com.alien.common.gameplay.entity.living.alien.xenomorph.carrier.Carrier;
import com.alien.common.gameplay.entity.living.alien.xenomorph.chrysalis.Chrysalis;
import com.alien.common.gameplay.entity.living.alien.xenomorph.crusher.Crusher;
import com.alien.common.gameplay.entity.living.alien.xenomorph.drone.Drone;
import com.alien.common.gameplay.entity.living.alien.xenomorph.harbinger.Harbinger;
import com.alien.common.gameplay.entity.living.alien.xenomorph.praetorian.Praetorian;
import com.alien.common.gameplay.entity.living.alien.xenomorph.predalien.Predalien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.prowler.Prowler;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.entity.living.alien.xenomorph.ravager.Ravager;
import com.alien.common.gameplay.entity.living.alien.xenomorph.razor_claw.RazorClaw;
import com.alien.common.gameplay.entity.living.alien.xenomorph.runner.Runner;
import com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.Spitter;
import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.Warrior;
import com.blib.api.common.entity.v1.SilencedEntityTypeBuilder;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;
import com.blib.api.common.registry.v1.impl.BLibEntityAttributeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class AlienEntityTypes {

    private static final BLibEntityAttributeRegistry ATTRIBUTE_REGISTRY = Alien.MOD.registries().createEntityAttributeRegistry();

    private static final BLibRegistry<EntityType<?>> TYPE_REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.ENTITY_TYPE);

    public static final BLibHolder<EntityType<Adolescent>> ABERRANT_ADOLESCENT = create(
        "aberrant_adolescent",
        EntityType.Builder.of(Adolescent::new, MobCategory.MONSTER)
            .sized(0.5f, 0.5f)
    );

    public static final BLibHolder<EntityType<Boiler>> ABERRANT_BOILER = create(
        "aberrant_boiler",
        EntityType.Builder.of(Boiler::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Chestburster>> ABERRANT_CHESTBURSTER = create(
        "aberrant_chestburster",
        EntityType.Builder.of(Chestburster::new, MobCategory.MONSTER)
            .sized(0.35f, 0.35f)
    );

    public static final BLibHolder<EntityType<Carrier>> ABERRANT_CARRIER = create(
        "aberrant_carrier",
        EntityType.Builder.of(Carrier::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Chrysalis>> ABERRANT_CHRYSALIS = create(
        "aberrant_chrysalis",
        EntityType.Builder.of(Chrysalis::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Crusher>> ABERRANT_CRUSHER = create(
        "aberrant_crusher",
        EntityType.Builder.of(Crusher::new, MobCategory.MONSTER)
            .sized(1.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Drone>> ABERRANT_DRONE = create(
        "aberrant_drone",
        EntityType.Builder.of(Drone::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Facehugger>> ABERRANT_FACEHUGGER = create(
        "aberrant_facehugger",
        EntityType.Builder.of(Facehugger::new, MobCategory.MONSTER)
            .sized(0.8f, 0.25f)
    );

    public static final BLibHolder<EntityType<Harbinger>> ABERRANT_HARBINGER = create(
        "aberrant_harbinger",
        EntityType.Builder.of(Harbinger::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Ovomorph>> ABERRANT_OVOMORPH = create(
        "aberrant_ovomorph",
        EntityType.Builder.of(Ovomorph::new, MobCategory.MISC)
            .sized(0.65f, 0.8f)
    );

    public static final BLibHolder<EntityType<Praetorian>> ABERRANT_PRAETORIAN = create(
        "aberrant_praetorian",
        EntityType.Builder.of(Praetorian::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Predalien>> ABERRANT_PREDALIEN = create(
        "aberrant_predalien",
        EntityType.Builder.of(Predalien::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<PredalienAdolescent>> ABERRANT_PREDALIEN_ADOLESCENT = create(
        "aberrant_predalien_adolescent",
        EntityType.Builder.of(PredalienAdolescent::new, MobCategory.MONSTER)
            .sized(0.5f, 0.5f)
    );

    public static final BLibHolder<EntityType<PredalienChestburster>> ABERRANT_PREDALIEN_CHESTBURSTER = create(
        "aberrant_predalien_chestburster",
        EntityType.Builder.of(PredalienChestburster::new, MobCategory.MONSTER)
            .sized(0.35f, 0.35f)
    );

    public static final BLibHolder<EntityType<Prowler>> ABERRANT_PROWLER = create(
        "aberrant_prowler",
        EntityType.Builder.of(Prowler::new, MobCategory.MONSTER)
            .sized(0.8f, 0.98f)
    );

    public static final BLibHolder<EntityType<RazorClaw>> ABERRANT_RAZOR_CLAW = create(
        "aberrant_razor_claw",
        EntityType.Builder.of(RazorClaw::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Ravager>> ABERRANT_RAVAGER = create(
        "aberrant_ravager",
        EntityType.Builder.of(Ravager::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Queen>> ABERRANT_QUEEN = create(
        "aberrant_queen",
        EntityType.Builder.of(Queen::new, MobCategory.MONSTER)
            .sized(1.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Runner>> ABERRANT_RUNNER = create(
        "aberrant_runner",
        EntityType.Builder.of(Runner::new, MobCategory.MONSTER)
            .sized(0.8f, 0.98f)
    );

    public static final BLibHolder<EntityType<Spitter>> ABERRANT_SPITTER = create(
        "aberrant_spitter",
        EntityType.Builder.of(Spitter::new, MobCategory.MONSTER)
            .sized(0.8f, 2.5f)
    );

    public static final BLibHolder<EntityType<Warrior>> ABERRANT_WARRIOR = create(
        "aberrant_warrior",
        EntityType.Builder.of(Warrior::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Acid>> ACID = create(
        "acid",
        EntityType.Builder.of(Acid::new, MobCategory.MISC)
            .sized(0.66F, 0.05F)
    );

    public static final BLibHolder<EntityType<Adolescent>> ADOLESCENT = create(
        "adolescent",
        EntityType.Builder.of(Adolescent::new, MobCategory.MONSTER)
            .sized(0.5f, 0.5f)
    );

    public static final BLibHolder<EntityType<Boiler>> BOILER = create(
        "boiler",
        EntityType.Builder.of(Boiler::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Chestburster>> CHESTBURSTER = create(
        "chestburster",
        EntityType.Builder.of(Chestburster::new, MobCategory.MONSTER)
            .sized(0.35f, 0.35f)
    );

    public static final BLibHolder<EntityType<Carrier>> CARRIER = create(
        "carrier",
        EntityType.Builder.of(Carrier::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Chrysalis>> CHRYSALIS = create(
        "chrysalis",
        EntityType.Builder.of(Chrysalis::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Crusher>> CRUSHER = create(
        "crusher",
        EntityType.Builder.of(Crusher::new, MobCategory.MONSTER)
            .sized(1.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Drone>> DRONE = create(
        "drone",
        EntityType.Builder.of(Drone::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Harbinger>> HARBINGER = create(
        "harbinger",
        EntityType.Builder.of(Harbinger::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Facehugger>> FACEHUGGER = create(
        "facehugger",
        EntityType.Builder.of(Facehugger::new, MobCategory.MONSTER)
            .sized(0.8f, 0.25f)
    );

    public static final BLibHolder<EntityType<Carrier>> IRRADIATED_CARRIER = create(
        "irradiated_carrier",
        EntityType.Builder.of(Carrier::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Chrysalis>> IRRADIATED_CHRYSALIS = create(
        "irradiated_chrysalis",
        EntityType.Builder.of(Chrysalis::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Crusher>> IRRADIATED_CRUSHER = create(
        "irradiated_crusher",
        EntityType.Builder.of(Crusher::new, MobCategory.MONSTER)
            .sized(1.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Drone>> IRRADIATED_DRONE = create(
        "irradiated_drone",
        EntityType.Builder.of(Drone::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Praetorian>> IRRADIATED_PRAETORIAN = create(
        "irradiated_praetorian",
        EntityType.Builder.of(Praetorian::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Predalien>> IRRADIATED_PREDALIEN = create(
        "irradiated_predalien",
        EntityType.Builder.of(Predalien::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Prowler>> IRRADIATED_PROWLER = create(
        "irradiated_prowler",
        EntityType.Builder.of(Prowler::new, MobCategory.MONSTER)
            .sized(0.8f, 0.98f)
    );

    public static final BLibHolder<EntityType<Harbinger>> IRRADIATED_HARBINGER = create(
        "irradiated_harbinger",
        EntityType.Builder.of(Harbinger::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<RazorClaw>> IRRADIATED_RAZOR_CLAW = create(
        "irradiated_razor_claw",
        EntityType.Builder.of(RazorClaw::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Ravager>> IRRADIATED_RAVAGER = create(
        "irradiated_ravager",
        EntityType.Builder.of(Ravager::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Queen>> IRRADIATED_QUEEN = create(
        "irradiated_queen",
        EntityType.Builder.of(Queen::new, MobCategory.MONSTER)
            .sized(1.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Runner>> IRRADIATED_RUNNER = create(
        "irradiated_runner",
        EntityType.Builder.of(Runner::new, MobCategory.MONSTER)
            .sized(0.8f, 0.98f)
    );

    public static final BLibHolder<EntityType<Warrior>> IRRADIATED_WARRIOR = create(
        "irradiated_warrior",
        EntityType.Builder.of(Warrior::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Adolescent>> NETHER_ADOLESCENT = create(
        "nether_adolescent",
        EntityType.Builder.of(Adolescent::new, MobCategory.MONSTER)
            .sized(0.5f, 0.5f)
    );

    public static final BLibHolder<EntityType<Boiler>> NETHER_BOILER = create(
        "nether_boiler",
        EntityType.Builder.of(Boiler::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Chestburster>> NETHER_CHESTBURSTER = create(
        "nether_chestburster",
        EntityType.Builder.of(Chestburster::new, MobCategory.MONSTER)
            .sized(0.35f, 0.35f)
    );

    public static final BLibHolder<EntityType<Carrier>> NETHER_CARRIER = create(
        "nether_carrier",
        EntityType.Builder.of(Carrier::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Chrysalis>> NETHER_CHRYSALIS = create(
        "nether_chrysalis",
        EntityType.Builder.of(Chrysalis::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Crusher>> NETHER_CRUSHER = create(
        "nether_crusher",
        EntityType.Builder.of(Crusher::new, MobCategory.MONSTER)
            .sized(1.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Drone>> NETHER_DRONE = create(
        "nether_drone",
        EntityType.Builder.of(Drone::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Facehugger>> NETHER_FACEHUGGER = create(
        "nether_facehugger",
        EntityType.Builder.of(Facehugger::new, MobCategory.MONSTER)
            .sized(0.8f, 0.25f)
    );

    public static final BLibHolder<EntityType<Harbinger>> NETHER_HARBINGER = create(
        "nether_harbinger",
        EntityType.Builder.of(Harbinger::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Ovomorph>> NETHER_OVOMORPH = create(
        "nether_ovomorph",
        EntityType.Builder.of(Ovomorph::new, MobCategory.MISC)
            .sized(0.65f, 0.8f)
    );

    public static final BLibHolder<EntityType<Praetorian>> NETHER_PRAETORIAN = create(
        "nether_praetorian",
        EntityType.Builder.of(Praetorian::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Predalien>> NETHER_PREDALIEN = create(
        "nether_predalien",
        EntityType.Builder.of(Predalien::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<PredalienAdolescent>> NETHER_PREDALIEN_ADOLESCENT = create(
        "nether_predalien_adolescent",
        EntityType.Builder.of(PredalienAdolescent::new, MobCategory.MONSTER)
            .sized(0.5f, 0.5f)
    );

    public static final BLibHolder<EntityType<PredalienChestburster>> NETHER_PREDALIEN_CHESTBURSTER = create(
        "nether_predalien_chestburster",
        EntityType.Builder.of(PredalienChestburster::new, MobCategory.MONSTER)
            .sized(0.35f, 0.35f)
    );

    public static final BLibHolder<EntityType<Prowler>> NETHER_PROWLER = create(
        "nether_prowler",
        EntityType.Builder.of(Prowler::new, MobCategory.MONSTER)
            .sized(0.8f, 0.98f)
    );

    public static final BLibHolder<EntityType<RazorClaw>> NETHER_RAZOR_CLAW = create(
        "nether_razor_claw",
        EntityType.Builder.of(RazorClaw::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Ravager>> NETHER_RAVAGER = create(
        "nether_ravager",
        EntityType.Builder.of(Ravager::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Queen>> NETHER_QUEEN = create(
        "nether_queen",
        EntityType.Builder.of(Queen::new, MobCategory.MONSTER)
            .sized(1.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Runner>> NETHER_RUNNER = create(
        "nether_runner",
        EntityType.Builder.of(Runner::new, MobCategory.MONSTER)
            .sized(0.8f, 0.98f)
    );

    public static final BLibHolder<EntityType<Spitter>> NETHER_SPITTER = create(
        "nether_spitter",
        EntityType.Builder.of(Spitter::new, MobCategory.MONSTER)
            .sized(0.8f, 2.5f)
    );

    public static final BLibHolder<EntityType<Warrior>> NETHER_WARRIOR = create(
        "nether_warrior",
        EntityType.Builder.of(Warrior::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static final BLibHolder<EntityType<Ovipositor>> OVIPOSITOR = create(
        "ovipositor",
        EntityType.Builder.of(Ovipositor::new, MobCategory.MONSTER)
            .sized(5.0f, 3.25f)
    );

    public static final BLibHolder<EntityType<Ovomorph>> OVOMORPH = create(
        "ovomorph",
        EntityType.Builder.of(Ovomorph::new, MobCategory.MISC)
            .sized(0.65f, 0.8f)
    );

    public static final BLibHolder<EntityType<Praetorian>> PRAETORIAN = create(
        "praetorian",
        EntityType.Builder.of(Praetorian::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Predalien>> PREDALIEN = create(
        "predalien",
        EntityType.Builder.of(Predalien::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<PredalienAdolescent>> PREDALIEN_ADOLESCENT = create(
        "predalien_adolescent",
        EntityType.Builder.of(PredalienAdolescent::new, MobCategory.MONSTER)
            .sized(0.5f, 0.5f)
    );

    public static final BLibHolder<EntityType<PredalienChestburster>> PREDALIEN_CHESTBURSTER = create(
        "predalien_chestburster",
        EntityType.Builder.of(PredalienChestburster::new, MobCategory.MONSTER)
            .sized(0.35f, 0.35f)
    );

    public static final BLibHolder<EntityType<Prowler>> PROWLER = create(
        "prowler",
        EntityType.Builder.of(Prowler::new, MobCategory.MONSTER)
            .sized(0.8f, 0.98f)
    );

    public static final BLibHolder<EntityType<RazorClaw>> RAZOR_CLAW = create(
        "razor_claw",
        EntityType.Builder.of(RazorClaw::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Ravager>> RAVAGER = create(
        "ravager",
        EntityType.Builder.of(Ravager::new, MobCategory.MONSTER)
            .sized(0.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Queen>> QUEEN = create(
        "queen",
        EntityType.Builder.of(Queen::new, MobCategory.MONSTER)
            .sized(1.98f, 3.98f)
    );

    public static final BLibHolder<EntityType<Adolescent>> ROYAL_ABERRANT_ADOLESCENT = create(
        "royal_aberrant_adolescent",
        EntityType.Builder.of(Adolescent::new, MobCategory.MONSTER)
            .sized(0.5f, 0.5f)
    );

    public static final BLibHolder<EntityType<Chestburster>> ROYAL_ABERRANT_CHESTBURSTER = create(
        "royal_aberrant_chestburster",
        EntityType.Builder.of(Chestburster::new, MobCategory.MONSTER)
            .sized(0.35f, 0.35f)
    );

    public static final BLibHolder<EntityType<Facehugger>> ROYAL_ABERRANT_FACEHUGGER = create(
        "royal_aberrant_facehugger",
        EntityType.Builder.of(Facehugger::new, MobCategory.MONSTER)
            .sized(0.8f, 0.25f)
    );

    public static final BLibHolder<EntityType<Ovomorph>> ROYAL_ABERRANT_OVOMORPH = create(
        "royal_aberrant_ovomorph",
        EntityType.Builder.of(Ovomorph::new, MobCategory.MISC)
            .sized(0.65f, 0.8f)
    );

    public static final BLibHolder<EntityType<Adolescent>> ROYAL_ADOLESCENT = create(
        "royal_adolescent",
        EntityType.Builder.of(Adolescent::new, MobCategory.MONSTER)
            .sized(0.5f, 0.5f)
    );

    public static final BLibHolder<EntityType<Chestburster>> ROYAL_CHESTBURSTER = create(
        "royal_chestburster",
        EntityType.Builder.<Chestburster>of(Chestburster::new, MobCategory.MONSTER)
            .sized(0.35f, 0.35f)
    );

    public static final BLibHolder<EntityType<Facehugger>> ROYAL_FACEHUGGER = create(
        "royal_facehugger",
        EntityType.Builder.of(Facehugger::new, MobCategory.MONSTER)
            .sized(0.8f, 0.25f)
    );

    public static final BLibHolder<EntityType<Ovomorph>> ROYAL_OVOMORPH = create(
        "royal_ovomorph",
        EntityType.Builder.of(Ovomorph::new, MobCategory.MISC)
            .sized(0.65f, 0.8f)
    );

    public static final BLibHolder<EntityType<Adolescent>> ROYAL_NETHER_ADOLESCENT = create(
        "royal_nether_adolescent",
        EntityType.Builder.of(Adolescent::new, MobCategory.MONSTER)
            .sized(0.5f, 0.5f)
    );

    public static final BLibHolder<EntityType<Chestburster>> ROYAL_NETHER_CHESTBURSTER = create(
        "royal_nether_chestburster",
        EntityType.Builder.of(Chestburster::new, MobCategory.MONSTER)
            .sized(0.35f, 0.35f)
    );

    public static final BLibHolder<EntityType<Facehugger>> ROYAL_NETHER_FACEHUGGER = create(
        "royal_nether_facehugger",
        EntityType.Builder.of(Facehugger::new, MobCategory.MONSTER)
            .sized(0.8f, 0.25f)
    );

    public static final BLibHolder<EntityType<Ovomorph>> ROYAL_NETHER_OVOMORPH = create(
        "royal_nether_ovomorph",
        EntityType.Builder.of(Ovomorph::new, MobCategory.MISC)
            .sized(0.65f, 0.8f)
    );

    public static final BLibHolder<EntityType<Runner>> RUNNER = create(
        "runner",
        EntityType.Builder.of(Runner::new, MobCategory.MONSTER)
            .sized(0.8f, 0.98f)
    );

    public static final BLibHolder<EntityType<Spitter>> SPITTER = create(
        "spitter",
        EntityType.Builder.of(Spitter::new, MobCategory.MONSTER)
            .sized(0.8f, 2.5f)
    );

    public static final BLibHolder<EntityType<Warrior>> WARRIOR = create(
        "warrior",
        EntityType.Builder.of(Warrior::new, MobCategory.MONSTER)
            .sized(0.8f, 1.98f)
    );

    public static <T extends Entity> BLibHolder<EntityType<T>> create(String path, EntityType.Builder<T> builder) {
        return TYPE_REGISTRY.createHolder(
            path,
            () -> ((SilencedEntityTypeBuilder) builder).blib$buildWithoutDataFixerCheck()
        );
    }

    public static void initialize() {
        TYPE_REGISTRY.registerAll();
        ATTRIBUTE_REGISTRY.register(ABERRANT_ADOLESCENT, Adolescent::createAdolescentAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_BOILER, Boiler::createBoilerAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_CARRIER, Carrier::createCarrierAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_CHRYSALIS, Chrysalis::createChrysalisAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_CRUSHER, Crusher::createCrusherAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_DRONE, Drone::createDroneAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_HARBINGER, Harbinger::createHarbingerAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_OVOMORPH, Ovomorph::createOvomorphAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_PRAETORIAN, Praetorian::createPraetorianAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_PREDALIEN, Predalien::createPredalienAttributes);
        ATTRIBUTE_REGISTRY.register(
            ABERRANT_PREDALIEN_ADOLESCENT,
            PredalienAdolescent::createPredalienAdolescentAttributes
        );
        ATTRIBUTE_REGISTRY.register(
            ABERRANT_PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        ATTRIBUTE_REGISTRY.register(ABERRANT_PROWLER, Prowler::createProwlerAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_RAZOR_CLAW, RazorClaw::createRazorClawAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_RAVAGER, Ravager::createRavagerAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_QUEEN, Queen::createQueenAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_RUNNER, Runner::createRunnerAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_SPITTER, Spitter::createSpitterAttributes);
        ATTRIBUTE_REGISTRY.register(ABERRANT_WARRIOR, Warrior::createWarriorAttributes);
        ATTRIBUTE_REGISTRY.register(CHESTBURSTER, Chestburster::createChestbursterAttributes);
        ATTRIBUTE_REGISTRY.register(CARRIER, Carrier::createCarrierAttributes);
        ATTRIBUTE_REGISTRY.register(CHRYSALIS, Chrysalis::createChrysalisAttributes);
        ATTRIBUTE_REGISTRY.register(ADOLESCENT, Adolescent::createAdolescentAttributes);
        ATTRIBUTE_REGISTRY.register(BOILER, Boiler::createBoilerAttributes);
        ATTRIBUTE_REGISTRY.register(CRUSHER, Crusher::createCrusherAttributes);
        ATTRIBUTE_REGISTRY.register(DRONE, Drone::createDroneAttributes);
        ATTRIBUTE_REGISTRY.register(HARBINGER, Harbinger::createHarbingerAttributes);
        ATTRIBUTE_REGISTRY.register(FACEHUGGER, Facehugger::createFacehuggerAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_CARRIER, Carrier::createCarrierAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_CHRYSALIS, Chrysalis::createChrysalisAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_CRUSHER, Crusher::createCrusherAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_DRONE, Drone::createDroneAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_PRAETORIAN, Praetorian::createPraetorianAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_PREDALIEN, Predalien::createPredalienAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_PROWLER, Prowler::createProwlerAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_HARBINGER, Harbinger::createHarbingerAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_RAZOR_CLAW, RazorClaw::createRazorClawAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_RAVAGER, Ravager::createRavagerAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_QUEEN, Queen::createQueenAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_RUNNER, Runner::createRunnerAttributes);
        ATTRIBUTE_REGISTRY.register(IRRADIATED_WARRIOR, Warrior::createWarriorAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_ADOLESCENT, Adolescent::createAdolescentAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_BOILER, Boiler::createBoilerAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_CARRIER, Carrier::createCarrierAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_CHRYSALIS, Chrysalis::createChrysalisAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_CRUSHER, Crusher::createCrusherAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_DRONE, Drone::createDroneAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_HARBINGER, Harbinger::createHarbingerAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_OVOMORPH, Ovomorph::createOvomorphAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_PRAETORIAN, Praetorian::createPraetorianAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_PREDALIEN, Predalien::createPredalienAttributes);
        ATTRIBUTE_REGISTRY.register(
            NETHER_PREDALIEN_ADOLESCENT,
            PredalienAdolescent::createPredalienAdolescentAttributes
        );
        ATTRIBUTE_REGISTRY.register(
            NETHER_PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        ATTRIBUTE_REGISTRY.register(NETHER_PROWLER, Prowler::createProwlerAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_RAZOR_CLAW, RazorClaw::createRazorClawAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_RAVAGER, Ravager::createRavagerAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_QUEEN, Queen::createQueenAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_RUNNER, Runner::createRunnerAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_SPITTER, Spitter::createSpitterAttributes);
        ATTRIBUTE_REGISTRY.register(NETHER_WARRIOR, Warrior::createWarriorAttributes);
        ATTRIBUTE_REGISTRY.register(OVIPOSITOR, Ovipositor::createOvipositorAttributes);
        ATTRIBUTE_REGISTRY.register(OVOMORPH, Ovomorph::createOvomorphAttributes);
        ATTRIBUTE_REGISTRY.register(PRAETORIAN, Praetorian::createPraetorianAttributes);
        ATTRIBUTE_REGISTRY.register(PREDALIEN, Predalien::createPredalienAttributes);
        ATTRIBUTE_REGISTRY.register(PREDALIEN_ADOLESCENT, PredalienAdolescent::createPredalienAdolescentAttributes);
        ATTRIBUTE_REGISTRY.register(
            PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        ATTRIBUTE_REGISTRY.register(PROWLER, Prowler::createProwlerAttributes);
        ATTRIBUTE_REGISTRY.register(RAZOR_CLAW, RazorClaw::createRazorClawAttributes);
        ATTRIBUTE_REGISTRY.register(RAVAGER, Ravager::createRavagerAttributes);
        ATTRIBUTE_REGISTRY.register(QUEEN, Queen::createQueenAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_ABERRANT_ADOLESCENT, Adolescent::createAdolescentAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_ABERRANT_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_ABERRANT_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_ABERRANT_OVOMORPH, Ovomorph::createOvomorphAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_ADOLESCENT, Adolescent::createAdolescentAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_NETHER_ADOLESCENT, Adolescent::createAdolescentAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_NETHER_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_NETHER_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_NETHER_OVOMORPH, Ovomorph::createOvomorphAttributes);
        ATTRIBUTE_REGISTRY.register(ROYAL_OVOMORPH, Ovomorph::createOvomorphAttributes);
        ATTRIBUTE_REGISTRY.register(RUNNER, Runner::createRunnerAttributes);
        ATTRIBUTE_REGISTRY.register(SPITTER, Spitter::createSpitterAttributes);
        ATTRIBUTE_REGISTRY.register(WARRIOR, Warrior::createWarriorAttributes);
    }
}

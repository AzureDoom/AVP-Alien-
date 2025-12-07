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
import com.alien.common.gameplay.entity.living.alien.xenomorph.crusher.Crusher;
import com.alien.common.gameplay.entity.living.alien.xenomorph.drone.Drone;
import com.alien.common.gameplay.entity.living.alien.xenomorph.praetorian.Praetorian;
import com.alien.common.gameplay.entity.living.alien.xenomorph.predalien.Predalien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.prowler.Prowler;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.entity.living.alien.xenomorph.runner.Runner;
import com.alien.common.gameplay.entity.living.alien.xenomorph.spitter.Spitter;
import com.alien.common.gameplay.entity.living.alien.xenomorph.warrior.Warrior;
import com.avp.common.registry.init.entity_type.SilencedEntityTypeBuilder;
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
import com.blib.service.BLibServices;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class AlienEntityTypes {

    public static final BLibRegistry<EntityType<?>> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.ENTITY_TYPE);

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

    public static final BLibHolder<EntityType<Facehugger>> FACEHUGGER = create(
        "facehugger",
        EntityType.Builder.of(Facehugger::new, MobCategory.MONSTER)
            .sized(0.8f, 0.25f)
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
        return REGISTRY.createHolder(
            path,
            () -> ((SilencedEntityTypeBuilder) builder).avp$buildWithoutDataFixerCheck()
        );
    }

    public static void initialize() {
        REGISTRY.registerAll();
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_ADOLESCENT, Adolescent::createAdolescentAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_BOILER, Boiler::createBoilerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_CRUSHER, Crusher::createCrusherAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_DRONE, Drone::createDroneAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_OVOMORPH, Ovomorph::createOvomorphAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_PRAETORIAN, Praetorian::createPraetorianAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_PREDALIEN, Predalien::createPredalienAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(
            ABERRANT_PREDALIEN_ADOLESCENT,
            PredalienAdolescent::createPredalienAdolescentAttributes
        );
        BLibServices.REGISTRY.registerEntityAttributes(
            ABERRANT_PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_PROWLER, Prowler::createProwlerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_QUEEN, Queen::createQueenAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_RUNNER, Runner::createRunnerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_SPITTER, Spitter::createSpitterAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ABERRANT_WARRIOR, Warrior::createWarriorAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(CHESTBURSTER, Chestburster::createChestbursterAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ADOLESCENT, Adolescent::createAdolescentAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(BOILER, Boiler::createBoilerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(CRUSHER, Crusher::createCrusherAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(DRONE, Drone::createDroneAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(FACEHUGGER, Facehugger::createFacehuggerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(IRRADIATED_CRUSHER, Crusher::createCrusherAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(IRRADIATED_DRONE, Drone::createDroneAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(IRRADIATED_PRAETORIAN, Praetorian::createPraetorianAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(IRRADIATED_PREDALIEN, Predalien::createPredalienAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(IRRADIATED_PROWLER, Prowler::createProwlerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(IRRADIATED_QUEEN, Queen::createQueenAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(IRRADIATED_RUNNER, Runner::createRunnerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(IRRADIATED_WARRIOR, Warrior::createWarriorAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_ADOLESCENT, Adolescent::createAdolescentAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_BOILER, Boiler::createBoilerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_CRUSHER, Crusher::createCrusherAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_DRONE, Drone::createDroneAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_OVOMORPH, Ovomorph::createOvomorphAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_PRAETORIAN, Praetorian::createPraetorianAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_PREDALIEN, Predalien::createPredalienAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(
            NETHER_PREDALIEN_ADOLESCENT,
            PredalienAdolescent::createPredalienAdolescentAttributes
        );
        BLibServices.REGISTRY.registerEntityAttributes(
            NETHER_PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_PROWLER, Prowler::createProwlerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_QUEEN, Queen::createQueenAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_RUNNER, Runner::createRunnerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_SPITTER, Spitter::createSpitterAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(NETHER_WARRIOR, Warrior::createWarriorAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(OVIPOSITOR, Ovipositor::createOvipositorAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(OVOMORPH, Ovomorph::createOvomorphAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(PRAETORIAN, Praetorian::createPraetorianAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(PREDALIEN, Predalien::createPredalienAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(PREDALIEN_ADOLESCENT, PredalienAdolescent::createPredalienAdolescentAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(
            PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        BLibServices.REGISTRY.registerEntityAttributes(PROWLER, Prowler::createProwlerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(QUEEN, Queen::createQueenAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_ABERRANT_ADOLESCENT, Adolescent::createAdolescentAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_ABERRANT_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_ABERRANT_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_ABERRANT_OVOMORPH, Ovomorph::createOvomorphAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_ADOLESCENT, Adolescent::createAdolescentAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_NETHER_ADOLESCENT, Adolescent::createAdolescentAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_NETHER_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_NETHER_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_NETHER_OVOMORPH, Ovomorph::createOvomorphAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(ROYAL_OVOMORPH, Ovomorph::createOvomorphAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(RUNNER, Runner::createRunnerAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(SPITTER, Spitter::createSpitterAttributes);
        BLibServices.REGISTRY.registerEntityAttributes(WARRIOR, Warrior::createWarriorAttributes);
    }
}

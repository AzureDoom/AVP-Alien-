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
import com.blib.BLibHolder;
import com.blib.common.registry.SilencedEntityTypeBuilder;
import com.blib.common.registry.impl.BLibEntityTypeRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class AlienEntityTypes {

    public static final BLibEntityTypeRegistry REGISTRY = Alien.MOD.createEntityTypeRegistry();

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
            () -> ((SilencedEntityTypeBuilder) builder).blib$buildWithoutDataFixerCheck()
        );
    }

    public static void initialize() {
        REGISTRY.registerAll();
        REGISTRY.registerAttributes(ABERRANT_ADOLESCENT, Adolescent::createAdolescentAttributes);
        REGISTRY.registerAttributes(ABERRANT_BOILER, Boiler::createBoilerAttributes);
        REGISTRY.registerAttributes(ABERRANT_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        REGISTRY.registerAttributes(ABERRANT_CRUSHER, Crusher::createCrusherAttributes);
        REGISTRY.registerAttributes(ABERRANT_DRONE, Drone::createDroneAttributes);
        REGISTRY.registerAttributes(ABERRANT_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        REGISTRY.registerAttributes(ABERRANT_OVOMORPH, Ovomorph::createOvomorphAttributes);
        REGISTRY.registerAttributes(ABERRANT_PRAETORIAN, Praetorian::createPraetorianAttributes);
        REGISTRY.registerAttributes(ABERRANT_PREDALIEN, Predalien::createPredalienAttributes);
        REGISTRY.registerAttributes(
            ABERRANT_PREDALIEN_ADOLESCENT,
            PredalienAdolescent::createPredalienAdolescentAttributes
        );
        REGISTRY.registerAttributes(
            ABERRANT_PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        REGISTRY.registerAttributes(ABERRANT_PROWLER, Prowler::createProwlerAttributes);
        REGISTRY.registerAttributes(ABERRANT_QUEEN, Queen::createQueenAttributes);
        REGISTRY.registerAttributes(ABERRANT_RUNNER, Runner::createRunnerAttributes);
        REGISTRY.registerAttributes(ABERRANT_SPITTER, Spitter::createSpitterAttributes);
        REGISTRY.registerAttributes(ABERRANT_WARRIOR, Warrior::createWarriorAttributes);
        REGISTRY.registerAttributes(CHESTBURSTER, Chestburster::createChestbursterAttributes);
        REGISTRY.registerAttributes(ADOLESCENT, Adolescent::createAdolescentAttributes);
        REGISTRY.registerAttributes(BOILER, Boiler::createBoilerAttributes);
        REGISTRY.registerAttributes(CRUSHER, Crusher::createCrusherAttributes);
        REGISTRY.registerAttributes(DRONE, Drone::createDroneAttributes);
        REGISTRY.registerAttributes(FACEHUGGER, Facehugger::createFacehuggerAttributes);
        REGISTRY.registerAttributes(IRRADIATED_CRUSHER, Crusher::createCrusherAttributes);
        REGISTRY.registerAttributes(IRRADIATED_DRONE, Drone::createDroneAttributes);
        REGISTRY.registerAttributes(IRRADIATED_PRAETORIAN, Praetorian::createPraetorianAttributes);
        REGISTRY.registerAttributes(IRRADIATED_PREDALIEN, Predalien::createPredalienAttributes);
        REGISTRY.registerAttributes(IRRADIATED_PROWLER, Prowler::createProwlerAttributes);
        REGISTRY.registerAttributes(IRRADIATED_QUEEN, Queen::createQueenAttributes);
        REGISTRY.registerAttributes(IRRADIATED_RUNNER, Runner::createRunnerAttributes);
        REGISTRY.registerAttributes(IRRADIATED_WARRIOR, Warrior::createWarriorAttributes);
        REGISTRY.registerAttributes(NETHER_ADOLESCENT, Adolescent::createAdolescentAttributes);
        REGISTRY.registerAttributes(NETHER_BOILER, Boiler::createBoilerAttributes);
        REGISTRY.registerAttributes(NETHER_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        REGISTRY.registerAttributes(NETHER_CRUSHER, Crusher::createCrusherAttributes);
        REGISTRY.registerAttributes(NETHER_DRONE, Drone::createDroneAttributes);
        REGISTRY.registerAttributes(NETHER_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        REGISTRY.registerAttributes(NETHER_OVOMORPH, Ovomorph::createOvomorphAttributes);
        REGISTRY.registerAttributes(NETHER_PRAETORIAN, Praetorian::createPraetorianAttributes);
        REGISTRY.registerAttributes(NETHER_PREDALIEN, Predalien::createPredalienAttributes);
        REGISTRY.registerAttributes(
            NETHER_PREDALIEN_ADOLESCENT,
            PredalienAdolescent::createPredalienAdolescentAttributes
        );
        REGISTRY.registerAttributes(
            NETHER_PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        REGISTRY.registerAttributes(NETHER_PROWLER, Prowler::createProwlerAttributes);
        REGISTRY.registerAttributes(NETHER_QUEEN, Queen::createQueenAttributes);
        REGISTRY.registerAttributes(NETHER_RUNNER, Runner::createRunnerAttributes);
        REGISTRY.registerAttributes(NETHER_SPITTER, Spitter::createSpitterAttributes);
        REGISTRY.registerAttributes(NETHER_WARRIOR, Warrior::createWarriorAttributes);
        REGISTRY.registerAttributes(OVIPOSITOR, Ovipositor::createOvipositorAttributes);
        REGISTRY.registerAttributes(OVOMORPH, Ovomorph::createOvomorphAttributes);
        REGISTRY.registerAttributes(PRAETORIAN, Praetorian::createPraetorianAttributes);
        REGISTRY.registerAttributes(PREDALIEN, Predalien::createPredalienAttributes);
        REGISTRY.registerAttributes(PREDALIEN_ADOLESCENT, PredalienAdolescent::createPredalienAdolescentAttributes);
        REGISTRY.registerAttributes(
            PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        REGISTRY.registerAttributes(PROWLER, Prowler::createProwlerAttributes);
        REGISTRY.registerAttributes(QUEEN, Queen::createQueenAttributes);
        REGISTRY.registerAttributes(ROYAL_ABERRANT_ADOLESCENT, Adolescent::createAdolescentAttributes);
        REGISTRY.registerAttributes(ROYAL_ABERRANT_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        REGISTRY.registerAttributes(ROYAL_ABERRANT_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        REGISTRY.registerAttributes(ROYAL_ABERRANT_OVOMORPH, Ovomorph::createOvomorphAttributes);
        REGISTRY.registerAttributes(ROYAL_ADOLESCENT, Adolescent::createAdolescentAttributes);
        REGISTRY.registerAttributes(ROYAL_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        REGISTRY.registerAttributes(ROYAL_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        REGISTRY.registerAttributes(ROYAL_NETHER_ADOLESCENT, Adolescent::createAdolescentAttributes);
        REGISTRY.registerAttributes(ROYAL_NETHER_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        REGISTRY.registerAttributes(ROYAL_NETHER_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        REGISTRY.registerAttributes(ROYAL_NETHER_OVOMORPH, Ovomorph::createOvomorphAttributes);
        REGISTRY.registerAttributes(ROYAL_OVOMORPH, Ovomorph::createOvomorphAttributes);
        REGISTRY.registerAttributes(RUNNER, Runner::createRunnerAttributes);
        REGISTRY.registerAttributes(SPITTER, Spitter::createSpitterAttributes);
        REGISTRY.registerAttributes(WARRIOR, Warrior::createWarriorAttributes);
    }
}

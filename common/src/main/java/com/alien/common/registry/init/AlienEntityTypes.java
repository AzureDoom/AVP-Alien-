package com.alien.common.registry.init;

import com.alien.AlienResources;
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
import com.avp.common.registry.AVPDeferredHolder;
import com.avp.common.registry.init.entity_type.SilencedEntityTypeBuilder;
import com.avp.service.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlienEntityTypes {

    public static final MobCategory ALIEN_CATEGORY = Services.BRIDGE.getAlienMobCategory();

    public static final MobCategory OVOMORPH_CATEGORY = Services.BRIDGE.getOvomorphMobCategory();

    private static final List<AVPDeferredHolder<? extends EntityType<?>>> ENTITY_TYPE_HOLDERS = new ArrayList<>();

    public static List<AVPDeferredHolder<? extends EntityType<?>>> getAll() {
        return Collections.unmodifiableList(ENTITY_TYPE_HOLDERS);
    }

    public static final AVPDeferredHolder<EntityType<Adolescent>> ABERRANT_ADOLESCENT = register(
        "aberrant_adolescent",
        EntityType.Builder.of(Adolescent::new, ALIEN_CATEGORY)
            .sized(0.5f, 0.5f)
    );

    public static final AVPDeferredHolder<EntityType<Boiler>> ABERRANT_BOILER = register(
        "aberrant_boiler",
        EntityType.Builder.of(Boiler::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Chestburster>> ABERRANT_CHESTBURSTER = register(
        "aberrant_chestburster",
        EntityType.Builder.of(Chestburster::new, ALIEN_CATEGORY)
            .sized(0.35f, 0.35f)
    );

    public static final AVPDeferredHolder<EntityType<Crusher>> ABERRANT_CRUSHER = register(
        "aberrant_crusher",
        EntityType.Builder.of(Crusher::new, ALIEN_CATEGORY)
            .sized(1.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Drone>> ABERRANT_DRONE = register(
        "aberrant_drone",
        EntityType.Builder.of(Drone::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Facehugger>> ABERRANT_FACEHUGGER = register(
        "aberrant_facehugger",
        EntityType.Builder.of(Facehugger::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.25f)
    );

    public static final AVPDeferredHolder<EntityType<Ovomorph>> ABERRANT_OVOMORPH = register(
        "aberrant_ovomorph",
        EntityType.Builder.of(Ovomorph::new, OVOMORPH_CATEGORY)
            .sized(0.65f, 0.8f)
    );

    public static final AVPDeferredHolder<EntityType<Praetorian>> ABERRANT_PRAETORIAN = register(
        "aberrant_praetorian",
        EntityType.Builder.of(Praetorian::new, ALIEN_CATEGORY)
            .sized(0.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<Predalien>> ABERRANT_PREDALIEN = register(
        "aberrant_predalien",
        EntityType.Builder.of(Predalien::new, ALIEN_CATEGORY)
            .sized(0.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<PredalienAdolescent>> ABERRANT_PREDALIEN_ADOLESCENT = register(
        "aberrant_predalien_adolescent",
        EntityType.Builder.of(PredalienAdolescent::new, ALIEN_CATEGORY)
            .sized(0.5f, 0.5f)
    );

    public static final AVPDeferredHolder<EntityType<PredalienChestburster>> ABERRANT_PREDALIEN_CHESTBURSTER = register(
        "aberrant_predalien_chestburster",
        EntityType.Builder.of(PredalienChestburster::new, ALIEN_CATEGORY)
            .sized(0.35f, 0.35f)
    );

    public static final AVPDeferredHolder<EntityType<Prowler>> ABERRANT_PROWLER = register(
        "aberrant_prowler",
        EntityType.Builder.of(Prowler::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.98f)
    );

    public static final AVPDeferredHolder<EntityType<Queen>> ABERRANT_QUEEN = register(
        "aberrant_queen",
        EntityType.Builder.of(Queen::new, ALIEN_CATEGORY)
            .sized(1.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<Runner>> ABERRANT_RUNNER = register(
        "aberrant_runner",
        EntityType.Builder.of(Runner::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.98f)
    );

    public static final AVPDeferredHolder<EntityType<Spitter>> ABERRANT_SPITTER = register(
        "aberrant_spitter",
        EntityType.Builder.of(Spitter::new, ALIEN_CATEGORY)
            .sized(0.8f, 2.5f)
    );

    public static final AVPDeferredHolder<EntityType<Warrior>> ABERRANT_WARRIOR = register(
        "aberrant_warrior",
        EntityType.Builder.of(Warrior::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Acid>> ACID = register(
        "acid",
        EntityType.Builder.of(Acid::new, MobCategory.MISC)
            .sized(0.66F, 0.05F)
    );

    public static final AVPDeferredHolder<EntityType<Adolescent>> ADOLESCENT = register(
        "adolescent",
        EntityType.Builder.of(Adolescent::new, ALIEN_CATEGORY)
            .sized(0.5f, 0.5f)
    );

    public static final AVPDeferredHolder<EntityType<Boiler>> BOILER = register(
        "boiler",
        EntityType.Builder.of(Boiler::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Chestburster>> CHESTBURSTER = register(
        "chestburster",
        EntityType.Builder.of(Chestburster::new, ALIEN_CATEGORY)
            .sized(0.35f, 0.35f)
    );

    public static final AVPDeferredHolder<EntityType<Crusher>> CRUSHER = register(
        "crusher",
        EntityType.Builder.of(Crusher::new, ALIEN_CATEGORY)
            .sized(1.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Drone>> DRONE = register(
        "drone",
        EntityType.Builder.of(Drone::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Facehugger>> FACEHUGGER = register(
        "facehugger",
        EntityType.Builder.of(Facehugger::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.25f)
    );

    public static final AVPDeferredHolder<EntityType<Crusher>> IRRADIATED_CRUSHER = register(
        "irradiated_crusher",
        EntityType.Builder.of(Crusher::new, ALIEN_CATEGORY)
            .sized(1.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Drone>> IRRADIATED_DRONE = register(
        "irradiated_drone",
        EntityType.Builder.of(Drone::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Praetorian>> IRRADIATED_PRAETORIAN = register(
        "irradiated_praetorian",
        EntityType.Builder.of(Praetorian::new, ALIEN_CATEGORY)
            .sized(0.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<Predalien>> IRRADIATED_PREDALIEN = register(
        "irradiated_predalien",
        EntityType.Builder.of(Predalien::new, ALIEN_CATEGORY)
            .sized(0.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<Prowler>> IRRADIATED_PROWLER = register(
        "irradiated_prowler",
        EntityType.Builder.of(Prowler::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.98f)
    );

    public static final AVPDeferredHolder<EntityType<Queen>> IRRADIATED_QUEEN = register(
        "irradiated_queen",
        EntityType.Builder.of(Queen::new, ALIEN_CATEGORY)
            .sized(1.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<Runner>> IRRADIATED_RUNNER = register(
        "irradiated_runner",
        EntityType.Builder.of(Runner::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.98f)
    );

    public static final AVPDeferredHolder<EntityType<Warrior>> IRRADIATED_WARRIOR = register(
        "irradiated_warrior",
        EntityType.Builder.of(Warrior::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Adolescent>> NETHER_ADOLESCENT = register(
        "nether_adolescent",
        EntityType.Builder.of(Adolescent::new, ALIEN_CATEGORY)
            .sized(0.5f, 0.5f)
    );

    public static final AVPDeferredHolder<EntityType<Boiler>> NETHER_BOILER = register(
        "nether_boiler",
        EntityType.Builder.of(Boiler::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Chestburster>> NETHER_CHESTBURSTER = register(
        "nether_chestburster",
        EntityType.Builder.of(Chestburster::new, ALIEN_CATEGORY)
            .sized(0.35f, 0.35f)
    );

    public static final AVPDeferredHolder<EntityType<Crusher>> NETHER_CRUSHER = register(
        "nether_crusher",
        EntityType.Builder.of(Crusher::new, ALIEN_CATEGORY)
            .sized(1.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Drone>> NETHER_DRONE = register(
        "nether_drone",
        EntityType.Builder.of(Drone::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Facehugger>> NETHER_FACEHUGGER = register(
        "nether_facehugger",
        EntityType.Builder.of(Facehugger::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.25f)
    );

    public static final AVPDeferredHolder<EntityType<Ovomorph>> NETHER_OVOMORPH = register(
        "nether_ovomorph",
        EntityType.Builder.of(Ovomorph::new, OVOMORPH_CATEGORY)
            .sized(0.65f, 0.8f)
    );

    public static final AVPDeferredHolder<EntityType<Praetorian>> NETHER_PRAETORIAN = register(
        "nether_praetorian",
        EntityType.Builder.of(Praetorian::new, ALIEN_CATEGORY)
            .sized(0.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<Predalien>> NETHER_PREDALIEN = register(
        "nether_predalien",
        EntityType.Builder.of(Predalien::new, ALIEN_CATEGORY)
            .sized(0.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<PredalienAdolescent>> NETHER_PREDALIEN_ADOLESCENT = register(
        "nether_predalien_adolescent",
        EntityType.Builder.of(PredalienAdolescent::new, ALIEN_CATEGORY)
            .sized(0.5f, 0.5f)
    );

    public static final AVPDeferredHolder<EntityType<PredalienChestburster>> NETHER_PREDALIEN_CHESTBURSTER = register(
        "nether_predalien_chestburster",
        EntityType.Builder.of(PredalienChestburster::new, ALIEN_CATEGORY)
            .sized(0.35f, 0.35f)
    );

    public static final AVPDeferredHolder<EntityType<Prowler>> NETHER_PROWLER = register(
        "nether_prowler",
        EntityType.Builder.of(Prowler::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.98f)
    );

    public static final AVPDeferredHolder<EntityType<Queen>> NETHER_QUEEN = register(
        "nether_queen",
        EntityType.Builder.of(Queen::new, ALIEN_CATEGORY)
            .sized(1.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<Runner>> NETHER_RUNNER = register(
        "nether_runner",
        EntityType.Builder.of(Runner::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.98f)
    );

    public static final AVPDeferredHolder<EntityType<Spitter>> NETHER_SPITTER = register(
        "nether_spitter",
        EntityType.Builder.of(Spitter::new, ALIEN_CATEGORY)
            .sized(0.8f, 2.5f)
    );

    public static final AVPDeferredHolder<EntityType<Warrior>> NETHER_WARRIOR = register(
        "nether_warrior",
        EntityType.Builder.of(Warrior::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static final AVPDeferredHolder<EntityType<Ovipositor>> OVIPOSITOR = register(
        "ovipositor",
        EntityType.Builder.of(Ovipositor::new, ALIEN_CATEGORY)
            .sized(5.0f, 3.25f)
    );

    public static final AVPDeferredHolder<EntityType<Ovomorph>> OVOMORPH = register(
        "ovomorph",
        EntityType.Builder.of(Ovomorph::new, OVOMORPH_CATEGORY)
            .sized(0.65f, 0.8f)
    );

    public static final AVPDeferredHolder<EntityType<Praetorian>> PRAETORIAN = register(
        "praetorian",
        EntityType.Builder.of(Praetorian::new, ALIEN_CATEGORY)
            .sized(0.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<Predalien>> PREDALIEN = register(
        "predalien",
        EntityType.Builder.of(Predalien::new, ALIEN_CATEGORY)
            .sized(0.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<PredalienAdolescent>> PREDALIEN_ADOLESCENT = register(
        "predalien_adolescent",
        EntityType.Builder.of(PredalienAdolescent::new, ALIEN_CATEGORY)
            .sized(0.5f, 0.5f)
    );

    public static final AVPDeferredHolder<EntityType<PredalienChestburster>> PREDALIEN_CHESTBURSTER = register(
        "predalien_chestburster",
        EntityType.Builder.of(PredalienChestburster::new, ALIEN_CATEGORY)
            .sized(0.35f, 0.35f)
    );

    public static final AVPDeferredHolder<EntityType<Prowler>> PROWLER = register(
        "prowler",
        EntityType.Builder.of(Prowler::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.98f)
    );

    public static final AVPDeferredHolder<EntityType<Queen>> QUEEN = register(
        "queen",
        EntityType.Builder.of(Queen::new, ALIEN_CATEGORY)
            .sized(1.98f, 3.98f)
    );

    public static final AVPDeferredHolder<EntityType<Adolescent>> ROYAL_ABERRANT_ADOLESCENT = register(
        "royal_aberrant_adolescent",
        EntityType.Builder.of(Adolescent::new, ALIEN_CATEGORY)
            .sized(0.5f, 0.5f)
    );

    public static final AVPDeferredHolder<EntityType<Chestburster>> ROYAL_ABERRANT_CHESTBURSTER = register(
        "royal_aberrant_chestburster",
        EntityType.Builder.of(Chestburster::new, ALIEN_CATEGORY)
            .sized(0.35f, 0.35f)
    );

    public static final AVPDeferredHolder<EntityType<Facehugger>> ROYAL_ABERRANT_FACEHUGGER = register(
        "royal_aberrant_facehugger",
        EntityType.Builder.of(Facehugger::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.25f)
    );

    public static final AVPDeferredHolder<EntityType<Ovomorph>> ROYAL_ABERRANT_OVOMORPH = register(
        "royal_aberrant_ovomorph",
        EntityType.Builder.of(Ovomorph::new, OVOMORPH_CATEGORY)
            .sized(0.65f, 0.8f)
    );

    public static final AVPDeferredHolder<EntityType<Adolescent>> ROYAL_ADOLESCENT = register(
        "royal_adolescent",
        EntityType.Builder.of(Adolescent::new, ALIEN_CATEGORY)
            .sized(0.5f, 0.5f)
    );

    public static final AVPDeferredHolder<EntityType<Chestburster>> ROYAL_CHESTBURSTER = register(
        "royal_chestburster",
        EntityType.Builder.<Chestburster>of(Chestburster::new, ALIEN_CATEGORY)
            .sized(0.35f, 0.35f)
    );

    public static final AVPDeferredHolder<EntityType<Facehugger>> ROYAL_FACEHUGGER = register(
        "royal_facehugger",
        EntityType.Builder.of(Facehugger::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.25f)
    );

    public static final AVPDeferredHolder<EntityType<Ovomorph>> ROYAL_OVOMORPH = register(
        "royal_ovomorph",
        EntityType.Builder.of(Ovomorph::new, OVOMORPH_CATEGORY)
            .sized(0.65f, 0.8f)
    );

    public static final AVPDeferredHolder<EntityType<Adolescent>> ROYAL_NETHER_ADOLESCENT = register(
        "royal_nether_adolescent",
        EntityType.Builder.of(Adolescent::new, ALIEN_CATEGORY)
            .sized(0.5f, 0.5f)
    );

    public static final AVPDeferredHolder<EntityType<Chestburster>> ROYAL_NETHER_CHESTBURSTER = register(
        "royal_nether_chestburster",
        EntityType.Builder.of(Chestburster::new, ALIEN_CATEGORY)
            .sized(0.35f, 0.35f)
    );

    public static final AVPDeferredHolder<EntityType<Facehugger>> ROYAL_NETHER_FACEHUGGER = register(
        "royal_nether_facehugger",
        EntityType.Builder.of(Facehugger::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.25f)
    );

    public static final AVPDeferredHolder<EntityType<Ovomorph>> ROYAL_NETHER_OVOMORPH = register(
        "royal_nether_ovomorph",
        EntityType.Builder.of(Ovomorph::new, OVOMORPH_CATEGORY)
            .sized(0.65f, 0.8f)
    );

    public static final AVPDeferredHolder<EntityType<Runner>> RUNNER = register(
        "runner",
        EntityType.Builder.of(Runner::new, ALIEN_CATEGORY)
            .sized(0.8f, 0.98f)
    );

    public static final AVPDeferredHolder<EntityType<Spitter>> SPITTER = register(
        "spitter",
        EntityType.Builder.of(Spitter::new, ALIEN_CATEGORY)
            .sized(0.8f, 2.5f)
    );

    public static final AVPDeferredHolder<EntityType<Warrior>> WARRIOR = register(
        "warrior",
        EntityType.Builder.of(Warrior::new, ALIEN_CATEGORY)
            .sized(0.8f, 1.98f)
    );

    public static <T extends Entity> AVPDeferredHolder<EntityType<T>> register(String id, EntityType.Builder<T> builder) {
        var holder = Services.REGISTRY.register(
            BuiltInRegistries.ENTITY_TYPE,
            AlienResources.location(id),
            () -> ((SilencedEntityTypeBuilder) builder).<T>avp$buildWithoutDataFixerCheck()
        );

        ENTITY_TYPE_HOLDERS.add(holder);

        return holder;
    }

    public static void initialize() {
        Services.REGISTRY.registerEntityAttributes(ABERRANT_ADOLESCENT, Adolescent::createAdolescentAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_BOILER, Boiler::createBoilerAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_CRUSHER, Crusher::createCrusherAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_DRONE, Drone::createDroneAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_OVOMORPH, Ovomorph::createOvomorphAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_PRAETORIAN, Praetorian::createPraetorianAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_PREDALIEN, Predalien::createPredalienAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_PREDALIEN_ADOLESCENT, PredalienAdolescent::createPredalienAdolescentAttributes);
        Services.REGISTRY.registerEntityAttributes(
            ABERRANT_PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        Services.REGISTRY.registerEntityAttributes(ABERRANT_PROWLER, Prowler::createProwlerAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_QUEEN, Queen::createQueenAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_RUNNER, Runner::createRunnerAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_SPITTER, Spitter::createSpitterAttributes);
        Services.REGISTRY.registerEntityAttributes(ABERRANT_WARRIOR, Warrior::createWarriorAttributes);
        Services.REGISTRY.registerEntityAttributes(CHESTBURSTER, Chestburster::createChestbursterAttributes);
        Services.REGISTRY.registerEntityAttributes(ADOLESCENT, Adolescent::createAdolescentAttributes);
        Services.REGISTRY.registerEntityAttributes(BOILER, Boiler::createBoilerAttributes);
        Services.REGISTRY.registerEntityAttributes(CRUSHER, Crusher::createCrusherAttributes);
        Services.REGISTRY.registerEntityAttributes(DRONE, Drone::createDroneAttributes);
        Services.REGISTRY.registerEntityAttributes(FACEHUGGER, Facehugger::createFacehuggerAttributes);
        Services.REGISTRY.registerEntityAttributes(IRRADIATED_CRUSHER, Crusher::createCrusherAttributes);
        Services.REGISTRY.registerEntityAttributes(IRRADIATED_DRONE, Drone::createDroneAttributes);
        Services.REGISTRY.registerEntityAttributes(IRRADIATED_PRAETORIAN, Praetorian::createPraetorianAttributes);
        Services.REGISTRY.registerEntityAttributes(IRRADIATED_PREDALIEN, Predalien::createPredalienAttributes);
        Services.REGISTRY.registerEntityAttributes(IRRADIATED_PROWLER, Prowler::createProwlerAttributes);
        Services.REGISTRY.registerEntityAttributes(IRRADIATED_QUEEN, Queen::createQueenAttributes);
        Services.REGISTRY.registerEntityAttributes(IRRADIATED_RUNNER, Runner::createRunnerAttributes);
        Services.REGISTRY.registerEntityAttributes(IRRADIATED_WARRIOR, Warrior::createWarriorAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_ADOLESCENT, Adolescent::createAdolescentAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_BOILER, Boiler::createBoilerAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_CRUSHER, Crusher::createCrusherAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_DRONE, Drone::createDroneAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_OVOMORPH, Ovomorph::createOvomorphAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_PRAETORIAN, Praetorian::createPraetorianAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_PREDALIEN, Predalien::createPredalienAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_PREDALIEN_ADOLESCENT, PredalienAdolescent::createPredalienAdolescentAttributes);
        Services.REGISTRY.registerEntityAttributes(
            NETHER_PREDALIEN_CHESTBURSTER,
            PredalienChestburster::createPredalienChestbursterAttributes
        );
        Services.REGISTRY.registerEntityAttributes(NETHER_PROWLER, Prowler::createProwlerAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_QUEEN, Queen::createQueenAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_RUNNER, Runner::createRunnerAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_SPITTER, Spitter::createSpitterAttributes);
        Services.REGISTRY.registerEntityAttributes(NETHER_WARRIOR, Warrior::createWarriorAttributes);
        Services.REGISTRY.registerEntityAttributes(OVIPOSITOR, Ovipositor::createOvipositorAttributes);
        Services.REGISTRY.registerEntityAttributes(OVOMORPH, Ovomorph::createOvomorphAttributes);
        Services.REGISTRY.registerEntityAttributes(PRAETORIAN, Praetorian::createPraetorianAttributes);
        Services.REGISTRY.registerEntityAttributes(PREDALIEN, Predalien::createPredalienAttributes);
        Services.REGISTRY.registerEntityAttributes(PREDALIEN_ADOLESCENT, PredalienAdolescent::createPredalienAdolescentAttributes);
        Services.REGISTRY.registerEntityAttributes(PREDALIEN_CHESTBURSTER, PredalienChestburster::createPredalienChestbursterAttributes);
        Services.REGISTRY.registerEntityAttributes(PROWLER, Prowler::createProwlerAttributes);
        Services.REGISTRY.registerEntityAttributes(QUEEN, Queen::createQueenAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_ABERRANT_ADOLESCENT, Adolescent::createAdolescentAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_ABERRANT_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_ABERRANT_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_ABERRANT_OVOMORPH, Ovomorph::createOvomorphAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_ADOLESCENT, Adolescent::createAdolescentAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_NETHER_ADOLESCENT, Adolescent::createAdolescentAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_NETHER_CHESTBURSTER, Chestburster::createChestbursterAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_NETHER_FACEHUGGER, Facehugger::createFacehuggerAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_NETHER_OVOMORPH, Ovomorph::createOvomorphAttributes);
        Services.REGISTRY.registerEntityAttributes(ROYAL_OVOMORPH, Ovomorph::createOvomorphAttributes);
        Services.REGISTRY.registerEntityAttributes(RUNNER, Runner::createRunnerAttributes);
        Services.REGISTRY.registerEntityAttributes(SPITTER, Spitter::createSpitterAttributes);
        Services.REGISTRY.registerEntityAttributes(WARRIOR, Warrior::createWarriorAttributes);
    }
}

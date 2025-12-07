package com.alien.fabric.data.tag;

import com.alien.common.registry.init.AlienEntityTypes;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.alien.compat.gigeresque.common.registry.tag.GigeresqueEntityTypeTags;
import com.alien.fabric.data.compatibility.stellaris.StellarisConstants;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class AlienEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {

    public AlienEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        addAberrantAliens();
        addAcidImmune();
        addPredalienAdolescents();
        addAdolescents();
        addAliens();
        addAnswersXenomorphCriesForHelp();
        addPredalienChestbursters();
        addChestbursters();
        addCrushers();
        addDrones();
        addFacehuggers();
        addHatedByXenomorphs();
        addHiveAliens();
        addHiveLayerSpawns();
        addRunnerHosts();
        addHosts();
        addIrradiatedAliens();
        addNetherAliens();
        addNormalAliens();
        addOvomorphs();
        addParasites();
        addPraetorians();
        addPredaliens();
        addProwlers();
        addQueens();
        addRadiationResistant();
        addRoyalAliens();
        addRoyalXenomorphs();
        addRunners();
        addSpitters();
        addWarriors();
        addXenomorphs();

        addCompatibilityTags();
    }

    private void addAberrantAliens() {
        getOrCreateTagBuilder(AlienEntityTypeTags.ABERRANT_ALIENS)
            .add(
                AlienEntityTypes.ABERRANT_ADOLESCENT.get(),
                AlienEntityTypes.ABERRANT_BOILER.get(),
                AlienEntityTypes.ABERRANT_CHESTBURSTER.get(),
                AlienEntityTypes.ABERRANT_CRUSHER.get(),
                AlienEntityTypes.ABERRANT_DRONE.get(),
                AlienEntityTypes.ABERRANT_FACEHUGGER.get(),
                AlienEntityTypes.ABERRANT_OVOMORPH.get(),
                AlienEntityTypes.ABERRANT_PRAETORIAN.get(),
                AlienEntityTypes.ABERRANT_PREDALIEN.get(),
                AlienEntityTypes.ABERRANT_PREDALIEN_ADOLESCENT.get(),
                AlienEntityTypes.ABERRANT_PREDALIEN_CHESTBURSTER.get(),
                AlienEntityTypes.ABERRANT_PROWLER.get(),
                AlienEntityTypes.ABERRANT_QUEEN.get(),
                AlienEntityTypes.ABERRANT_RUNNER.get(),
                AlienEntityTypes.ABERRANT_SPITTER.get(),
                AlienEntityTypes.ABERRANT_WARRIOR.get(),
                AlienEntityTypes.ROYAL_ABERRANT_ADOLESCENT.get(),
                AlienEntityTypes.ROYAL_ABERRANT_CHESTBURSTER.get(),
                AlienEntityTypes.ROYAL_ABERRANT_FACEHUGGER.get(),
                AlienEntityTypes.ROYAL_ABERRANT_OVOMORPH.get()
            );
    }

    private void addAcidImmune() {
        getOrCreateTagBuilder(AlienEntityTypeTags.ACID_IMMUNE)
            .addTag(AlienEntityTypeTags.ALIENS);
    }

    private void addAdolescents() {
        getOrCreateTagBuilder(AlienEntityTypeTags.ADOLESCENTS)
            .addTag(AlienEntityTypeTags.PREDALIEN_ADOLESCENTS)
            .add(
                AlienEntityTypes.ABERRANT_ADOLESCENT.get(),
                AlienEntityTypes.ADOLESCENT.get(),
                AlienEntityTypes.NETHER_ADOLESCENT.get(),
                AlienEntityTypes.ROYAL_ABERRANT_ADOLESCENT.get(),
                AlienEntityTypes.ROYAL_ADOLESCENT.get(),
                AlienEntityTypes.ROYAL_NETHER_ADOLESCENT.get()
            );
    }

    private void addAliens() {
        getOrCreateTagBuilder(AlienEntityTypeTags.ALIENS)
            .addTag(AlienEntityTypeTags.IRRADIATED_ALIENS)
            .addTag(AlienEntityTypeTags.ABERRANT_ALIENS)
            .addTag(AlienEntityTypeTags.NORMAL_ALIENS)
            .addTag(AlienEntityTypeTags.NETHER_ALIENS)
            .addTag(AlienEntityTypeTags.ROYAL_ALIENS);
    }

    private void addAnswersXenomorphCriesForHelp() {
        getOrCreateTagBuilder(AlienEntityTypeTags.ANSWERS_XENOMORPH_CRIES_FOR_HELP)
            .addTag(AlienEntityTypeTags.DRONES)
            .addTag(AlienEntityTypeTags.PROWLERS)
            .addTag(AlienEntityTypeTags.RUNNERS)
            .addTag(AlienEntityTypeTags.SPITTERS)
            .addTag(AlienEntityTypeTags.WARRIORS);
    }

    private void addChestbursters() {
        getOrCreateTagBuilder(AlienEntityTypeTags.CHESTBURSTERS)
            .addTag(AlienEntityTypeTags.PREDALIEN_CHESTBURSTERS)
            .add(
                AlienEntityTypes.ABERRANT_CHESTBURSTER.get(),
                AlienEntityTypes.CHESTBURSTER.get(),
                AlienEntityTypes.NETHER_CHESTBURSTER.get(),
                AlienEntityTypes.ROYAL_ABERRANT_CHESTBURSTER.get(),
                AlienEntityTypes.ROYAL_CHESTBURSTER.get(),
                AlienEntityTypes.ROYAL_NETHER_CHESTBURSTER.get()
            );
    }

    private void addCrushers() {
        getOrCreateTagBuilder(AlienEntityTypeTags.CRUSHERS)
            .add(
                AlienEntityTypes.ABERRANT_CRUSHER.get(),
                AlienEntityTypes.CRUSHER.get(),
                AlienEntityTypes.IRRADIATED_CRUSHER.get(),
                AlienEntityTypes.NETHER_CRUSHER.get()
            );
    }

    private void addDrones() {
        getOrCreateTagBuilder(AlienEntityTypeTags.DRONES)
            .add(
                AlienEntityTypes.ABERRANT_DRONE.get(),
                AlienEntityTypes.DRONE.get(),
                AlienEntityTypes.IRRADIATED_DRONE.get(),
                AlienEntityTypes.NETHER_DRONE.get()
            );
    }

    private void addFacehuggers() {
        getOrCreateTagBuilder(AlienEntityTypeTags.FACEHUGGERS)
            .add(
                AlienEntityTypes.ABERRANT_FACEHUGGER.get(),
                AlienEntityTypes.FACEHUGGER.get(),
                AlienEntityTypes.NETHER_FACEHUGGER.get(),
                AlienEntityTypes.ROYAL_ABERRANT_FACEHUGGER.get(),
                AlienEntityTypes.ROYAL_FACEHUGGER.get(),
                AlienEntityTypes.ROYAL_NETHER_FACEHUGGER.get()
            );
    }

    private void addHatedByXenomorphs() {
        getOrCreateTagBuilder(AlienEntityTypeTags.HATED_BY_XENOMORPHS)
            .add(EntityType.PLAYER);
        // TODO: Add a "humans" tag here that includes the marine.
        // FIXME:
        // .add(HumanEntityTypes.MARINE.get());
    }

    private void addHiveAliens() {
        getOrCreateTagBuilder(AlienEntityTypeTags.HIVE_ALIENS)
            .addTag(AlienEntityTypeTags.XENOMORPHS);
    }

    private void addHiveLayerSpawns() {
        getOrCreateTagBuilder(AlienEntityTypeTags.SPAWNS_IN_HIVE_WARRIOR_LAYER)
            .addTag(AlienEntityTypeTags.PROWLERS)
            .addTag(AlienEntityTypeTags.SPITTERS)
            .addTag(AlienEntityTypeTags.WARRIORS);

        getOrCreateTagBuilder(AlienEntityTypeTags.SPAWNS_IN_HIVE_DRONE_LAYER)
            .addTag(AlienEntityTypeTags.SPAWNS_IN_HIVE_WARRIOR_LAYER)
            .addTag(AlienEntityTypeTags.ADOLESCENTS)
            .addTag(AlienEntityTypeTags.CHESTBURSTERS)
            .addTag(AlienEntityTypeTags.DRONES)
            .addTag(AlienEntityTypeTags.RUNNERS)
            .addTag(AlienEntityTypeTags.OVOMORPHS);

        getOrCreateTagBuilder(AlienEntityTypeTags.SPAWNS_IN_HIVE_PRAETORIAN_LAYER)
            .addTag(AlienEntityTypeTags.SPAWNS_IN_HIVE_DRONE_LAYER)
            .addTag(AlienEntityTypeTags.CRUSHERS)
            .addTag(AlienEntityTypeTags.PRAETORIANS)
            .addTag(AlienEntityTypeTags.PREDALIENS);

        getOrCreateTagBuilder(AlienEntityTypeTags.SPAWNS_IN_HIVE_QUEEN_LAYER)
            .addTag(AlienEntityTypeTags.SPAWNS_IN_HIVE_PRAETORIAN_LAYER)
            .addTag(AlienEntityTypeTags.QUEENS);
    }

    private void addHosts() {
        getOrCreateTagBuilder(AlienEntityTypeTags.HOSTS)
            .addTag(AlienEntityTypeTags.RUNNER_HOSTS)
            .addOptionalTag(EntityTypeTags.ILLAGER)
            .add(
                EntityType.LLAMA,
                EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE,
                EntityType.PLAYER,
                EntityType.TRADER_LLAMA,
                EntityType.VILLAGER,
                EntityType.WANDERING_TRADER,
                EntityType.WITCH
                // FIXME:
                // HumanEntityTypes.MARINE.get()
            );
    }

    private void addIrradiatedAliens() {
        getOrCreateTagBuilder(AlienEntityTypeTags.IRRADIATED_ALIENS)
            .add(
                AlienEntityTypes.IRRADIATED_CRUSHER.get(),
                AlienEntityTypes.IRRADIATED_DRONE.get(),
                AlienEntityTypes.IRRADIATED_PRAETORIAN.get(),
                AlienEntityTypes.IRRADIATED_PREDALIEN.get(),
                AlienEntityTypes.IRRADIATED_PROWLER.get(),
                AlienEntityTypes.IRRADIATED_QUEEN.get(),
                AlienEntityTypes.IRRADIATED_RUNNER.get(),
                AlienEntityTypes.IRRADIATED_WARRIOR.get()
            );
    }

    private void addNetherAliens() {
        getOrCreateTagBuilder(AlienEntityTypeTags.NETHER_ALIENS)
            .add(
                AlienEntityTypes.NETHER_ADOLESCENT.get(),
                AlienEntityTypes.NETHER_BOILER.get(),
                AlienEntityTypes.NETHER_CHESTBURSTER.get(),
                AlienEntityTypes.NETHER_CRUSHER.get(),
                AlienEntityTypes.NETHER_DRONE.get(),
                AlienEntityTypes.NETHER_FACEHUGGER.get(),
                AlienEntityTypes.NETHER_OVOMORPH.get(),
                AlienEntityTypes.NETHER_PRAETORIAN.get(),
                AlienEntityTypes.NETHER_PREDALIEN.get(),
                AlienEntityTypes.NETHER_PREDALIEN_ADOLESCENT.get(),
                AlienEntityTypes.NETHER_PREDALIEN_CHESTBURSTER.get(),
                AlienEntityTypes.NETHER_PROWLER.get(),
                AlienEntityTypes.NETHER_QUEEN.get(),
                AlienEntityTypes.NETHER_RUNNER.get(),
                AlienEntityTypes.NETHER_SPITTER.get(),
                AlienEntityTypes.NETHER_WARRIOR.get(),
                AlienEntityTypes.ROYAL_NETHER_ADOLESCENT.get(),
                AlienEntityTypes.ROYAL_NETHER_CHESTBURSTER.get(),
                AlienEntityTypes.ROYAL_NETHER_OVOMORPH.get(),
                AlienEntityTypes.ROYAL_NETHER_FACEHUGGER.get()
            );
    }

    private void addNormalAliens() {
        getOrCreateTagBuilder(AlienEntityTypeTags.NORMAL_ALIENS)
            .add(
                AlienEntityTypes.ADOLESCENT.get(),
                AlienEntityTypes.BOILER.get(),
                AlienEntityTypes.CHESTBURSTER.get(),
                AlienEntityTypes.CRUSHER.get(),
                AlienEntityTypes.DRONE.get(),
                AlienEntityTypes.FACEHUGGER.get(),
                AlienEntityTypes.OVOMORPH.get(),
                AlienEntityTypes.PRAETORIAN.get(),
                AlienEntityTypes.PREDALIEN.get(),
                AlienEntityTypes.PREDALIEN_ADOLESCENT.get(),
                AlienEntityTypes.PREDALIEN_CHESTBURSTER.get(),
                AlienEntityTypes.PROWLER.get(),
                AlienEntityTypes.QUEEN.get(),
                AlienEntityTypes.ROYAL_ADOLESCENT.get(),
                AlienEntityTypes.ROYAL_CHESTBURSTER.get(),
                AlienEntityTypes.ROYAL_FACEHUGGER.get(),
                AlienEntityTypes.ROYAL_OVOMORPH.get(),
                AlienEntityTypes.RUNNER.get(),
                AlienEntityTypes.SPITTER.get(),
                AlienEntityTypes.WARRIOR.get()
            );
    }

    private void addOvomorphs() {
        getOrCreateTagBuilder(AlienEntityTypeTags.OVOMORPHS)
            .add(
                AlienEntityTypes.ABERRANT_OVOMORPH.get(),
                AlienEntityTypes.NETHER_OVOMORPH.get(),
                AlienEntityTypes.OVOMORPH.get(),
                AlienEntityTypes.ROYAL_ABERRANT_OVOMORPH.get(),
                AlienEntityTypes.ROYAL_NETHER_OVOMORPH.get(),
                AlienEntityTypes.ROYAL_OVOMORPH.get()
            );
    }

    private void addParasites() {
        getOrCreateTagBuilder(AlienEntityTypeTags.PARASITES)
            .addTag(AlienEntityTypeTags.FACEHUGGERS);
    }

    private void addPraetorians() {
        getOrCreateTagBuilder(AlienEntityTypeTags.PRAETORIANS)
            .add(
                AlienEntityTypes.ABERRANT_PRAETORIAN.get(),
                AlienEntityTypes.IRRADIATED_PRAETORIAN.get(),
                AlienEntityTypes.NETHER_PRAETORIAN.get(),
                AlienEntityTypes.PRAETORIAN.get()
            );
    }

    private void addPredalienAdolescents() {
        getOrCreateTagBuilder(AlienEntityTypeTags.PREDALIEN_ADOLESCENTS)
            .add(
                AlienEntityTypes.ABERRANT_PREDALIEN_ADOLESCENT.get(),
                AlienEntityTypes.NETHER_PREDALIEN_ADOLESCENT.get(),
                AlienEntityTypes.PREDALIEN_ADOLESCENT.get()
            );
    }

    private void addPredalienChestbursters() {
        getOrCreateTagBuilder(AlienEntityTypeTags.PREDALIEN_CHESTBURSTERS)
            .add(
                AlienEntityTypes.ABERRANT_PREDALIEN_CHESTBURSTER.get(),
                AlienEntityTypes.NETHER_PREDALIEN_CHESTBURSTER.get(),
                AlienEntityTypes.PREDALIEN_CHESTBURSTER.get()
            );
    }

    private void addPredaliens() {
        getOrCreateTagBuilder(AlienEntityTypeTags.PREDALIENS)
            .add(
                AlienEntityTypes.ABERRANT_PREDALIEN.get(),
                AlienEntityTypes.IRRADIATED_PREDALIEN.get(),
                AlienEntityTypes.NETHER_PREDALIEN.get(),
                AlienEntityTypes.PREDALIEN.get()
            );
    }

    private void addProwlers() {
        getOrCreateTagBuilder(AlienEntityTypeTags.PROWLERS)
            .add(
                AlienEntityTypes.ABERRANT_PROWLER.get(),
                AlienEntityTypes.IRRADIATED_PROWLER.get(),
                AlienEntityTypes.NETHER_PROWLER.get(),
                AlienEntityTypes.PROWLER.get()
            );
    }

    private void addQueens() {
        getOrCreateTagBuilder(AlienEntityTypeTags.QUEENS)
            .add(
                AlienEntityTypes.ABERRANT_QUEEN.get(),
                AlienEntityTypes.IRRADIATED_QUEEN.get(),
                AlienEntityTypes.NETHER_QUEEN.get(),
                AlienEntityTypes.QUEEN.get()
            );
    }

    private void addRadiationResistant() {
        // FIXME:
        // getOrCreateTagBuilder(AVPEntityTypeTags.RADIATION_RESISTANT)
        // .addTag(AlienEntityTypeTags.XENOMORPHS);
    }

    private void addRoyalAliens() {
        getOrCreateTagBuilder(AlienEntityTypeTags.ROYAL_ALIENS)
            .addTag(AlienEntityTypeTags.ROYAL_XENOMORPHS)
            .addTag(AlienEntityTypeTags.PREDALIEN_ADOLESCENTS)
            .addTag(AlienEntityTypeTags.PREDALIEN_CHESTBURSTERS)
            .add(
                AlienEntityTypes.ROYAL_ABERRANT_ADOLESCENT.get(),
                AlienEntityTypes.ROYAL_ABERRANT_CHESTBURSTER.get(),
                AlienEntityTypes.ROYAL_ABERRANT_FACEHUGGER.get(),
                AlienEntityTypes.ROYAL_ABERRANT_OVOMORPH.get(),
                AlienEntityTypes.ROYAL_ADOLESCENT.get(),
                AlienEntityTypes.ROYAL_CHESTBURSTER.get(),
                AlienEntityTypes.ROYAL_FACEHUGGER.get(),
                AlienEntityTypes.ROYAL_NETHER_ADOLESCENT.get(),
                AlienEntityTypes.ROYAL_NETHER_CHESTBURSTER.get(),
                AlienEntityTypes.ROYAL_NETHER_FACEHUGGER.get(),
                AlienEntityTypes.ROYAL_NETHER_OVOMORPH.get(),
                AlienEntityTypes.ROYAL_OVOMORPH.get()
            );
    }

    private void addRoyalXenomorphs() {
        getOrCreateTagBuilder(AlienEntityTypeTags.ROYAL_XENOMORPHS)
            .addTag(AlienEntityTypeTags.PRAETORIANS)
            .addTag(AlienEntityTypeTags.PREDALIENS)
            .addTag(AlienEntityTypeTags.QUEENS);
    }

    private void addRunnerHosts() {
        // NOTE: Llamas are deliberately excluded here.
        getOrCreateTagBuilder(AlienEntityTypeTags.RUNNER_HOSTS)
            .add(
                EntityType.CAMEL,
                EntityType.COW,
                EntityType.DONKEY,
                EntityType.FOX,
                EntityType.GOAT,
                EntityType.HORSE,
                EntityType.MOOSHROOM,
                EntityType.MULE,
                EntityType.PANDA,
                EntityType.PIG,
                EntityType.POLAR_BEAR,
                EntityType.RAVAGER,
                EntityType.SHEEP,
                EntityType.SNIFFER,
                EntityType.WOLF
            );
    }

    private void addRunners() {
        getOrCreateTagBuilder(AlienEntityTypeTags.RUNNERS)
            .add(
                AlienEntityTypes.ABERRANT_RUNNER.get(),
                AlienEntityTypes.IRRADIATED_RUNNER.get(),
                AlienEntityTypes.NETHER_RUNNER.get(),
                AlienEntityTypes.RUNNER.get()
            );
    }

    private void addSpitters() {
        getOrCreateTagBuilder(AlienEntityTypeTags.SPITTERS)
            .add(
                AlienEntityTypes.ABERRANT_SPITTER.get(),
                AlienEntityTypes.NETHER_SPITTER.get(),
                AlienEntityTypes.SPITTER.get()
            );
    }

    private void addWarriors() {
        getOrCreateTagBuilder(AlienEntityTypeTags.WARRIORS)
            .add(
                AlienEntityTypes.ABERRANT_WARRIOR.get(),
                AlienEntityTypes.IRRADIATED_WARRIOR.get(),
                AlienEntityTypes.NETHER_WARRIOR.get(),
                AlienEntityTypes.WARRIOR.get()
            );
    }

    private void addXenomorphs() {
        getOrCreateTagBuilder(AlienEntityTypeTags.XENOMORPHS)
            .addTag(AlienEntityTypeTags.CRUSHERS)
            .addTag(AlienEntityTypeTags.DRONES)
            .addTag(AlienEntityTypeTags.PRAETORIANS)
            .addTag(AlienEntityTypeTags.PREDALIENS)
            .addTag(AlienEntityTypeTags.PROWLERS)
            .addTag(AlienEntityTypeTags.QUEENS)
            .addTag(AlienEntityTypeTags.RUNNERS)
            .addTag(AlienEntityTypeTags.SPITTERS)
            .addTag(AlienEntityTypeTags.WARRIORS);
    }

    private void addCompatibilityTags() {
        getOrCreateTagBuilder(AlienEntityTypeTags.ACID_IMMUNE)
            .addOptionalTag(GigeresqueEntityTypeTags.ACID_RESISTANT);

        getOrCreateTagBuilder(StellarisConstants.NO_OXYGEN_NEEDED)
            .setReplace(false)
            .addTag(AlienEntityTypeTags.ALIENS);
    }
}

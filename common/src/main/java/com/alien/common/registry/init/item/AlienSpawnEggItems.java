package com.alien.common.registry.init.item;

import com.alien.Alien;
import com.alien.common.registry.init.AlienEntityTypes;
import com.blib.common.registry.BLibHolder;
import com.blib.common.registry.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public class AlienSpawnEggItems {

    public static final BLibRegistry<Item> REGISTRY = Alien.MOD.registries().create(BuiltInRegistries.ITEM);

    public static final BLibHolder<SpawnEggItem> ABERRANT_ADOLESCENT_SPAWN_EGG = create(
        "aberrant_adolescent",
        AlienEntityTypes.ABERRANT_ADOLESCENT
    );

    public static final BLibHolder<SpawnEggItem> ABERRANT_BOILER_SPAWN_EGG = create("aberrant_boiler", AlienEntityTypes.ABERRANT_BOILER);

    public static final BLibHolder<SpawnEggItem> ABERRANT_CHESTBURSTER_SPAWN_EGG = create(
        "aberrant_chestburster",
        AlienEntityTypes.ABERRANT_CHESTBURSTER
    );

    public static final BLibHolder<SpawnEggItem> ABERRANT_CRUSHER_SPAWN_EGG = create("aberrant_crusher", AlienEntityTypes.ABERRANT_CRUSHER);

    public static final BLibHolder<SpawnEggItem> ABERRANT_DRONE_SPAWN_EGG = create("aberrant_drone", AlienEntityTypes.ABERRANT_DRONE);

    public static final BLibHolder<SpawnEggItem> ABERRANT_FACEHUGGER_SPAWN_EGG = create(
        "aberrant_facehugger",
        AlienEntityTypes.ABERRANT_FACEHUGGER
    );

    public static final BLibHolder<SpawnEggItem> ABERRANT_OVOMORPH_SPAWN_EGG = create(
        "aberrant_ovomorph",
        AlienEntityTypes.ABERRANT_OVOMORPH
    );

    public static final BLibHolder<SpawnEggItem> ABERRANT_PRAETORIAN_SPAWN_EGG = create(
        "aberrant_praetorian",
        AlienEntityTypes.ABERRANT_PRAETORIAN
    );

    public static final BLibHolder<SpawnEggItem> ABERRANT_PREDALIEN_ADOLESCENT_SPAWN_EGG = create(
        "aberrant_predalien_adolescent",
        AlienEntityTypes.ABERRANT_PREDALIEN_ADOLESCENT
    );

    public static final BLibHolder<SpawnEggItem> ABERRANT_PREDALIEN_CHESTBURSTER_SPAWN_EGG = create(
        "aberrant_predalien_chestburster",
        AlienEntityTypes.ABERRANT_PREDALIEN_CHESTBURSTER
    );

    public static final BLibHolder<SpawnEggItem> ABERRANT_PREDALIEN_SPAWN_EGG = create(
        "aberrant_predalien",
        AlienEntityTypes.ABERRANT_PREDALIEN
    );

    public static final BLibHolder<SpawnEggItem> ABERRANT_PROWLER_SPAWN_EGG = create("aberrant_prowler", AlienEntityTypes.ABERRANT_PROWLER);

    public static final BLibHolder<SpawnEggItem> ABERRANT_RUNNER_SPAWN_EGG = create("aberrant_runner", AlienEntityTypes.ABERRANT_RUNNER);

    public static final BLibHolder<SpawnEggItem> ABERRANT_SPITTER_SPAWN_EGG = create("aberrant_spitter", AlienEntityTypes.ABERRANT_SPITTER);

    public static final BLibHolder<SpawnEggItem> ABERRANT_WARRIOR_SPAWN_EGG = create("aberrant_warrior", AlienEntityTypes.ABERRANT_WARRIOR);

    public static final BLibHolder<SpawnEggItem> ABERRANT_QUEEN_SPAWN_EGG = create("aberrant_queen", AlienEntityTypes.ABERRANT_QUEEN);

    public static final BLibHolder<SpawnEggItem> ADOLESCENT_SPAWN_EGG = create("adolescent", AlienEntityTypes.ADOLESCENT);

    public static final BLibHolder<SpawnEggItem> BOILER_SPAWN_EGG = create("boiler", AlienEntityTypes.BOILER);

    public static final BLibHolder<SpawnEggItem> CHESTBURSTER_SPAWN_EGG = create("chestburster", AlienEntityTypes.CHESTBURSTER);

    public static final BLibHolder<SpawnEggItem> CRUSHER_SPAWN_EGG = create("crusher", AlienEntityTypes.CRUSHER);

    public static final BLibHolder<SpawnEggItem> DRONE_SPAWN_EGG = create("drone", AlienEntityTypes.DRONE);

    public static final BLibHolder<SpawnEggItem> FACEHUGGER_SPAWN_EGG = create("facehugger", AlienEntityTypes.FACEHUGGER);

    public static final BLibHolder<SpawnEggItem> IRRADIATED_CRUSHER_SPAWN_EGG = create(
        "irradiated_crusher",
        AlienEntityTypes.IRRADIATED_CRUSHER
    );

    public static final BLibHolder<SpawnEggItem> IRRADIATED_DRONE_SPAWN_EGG = create("irradiated_drone", AlienEntityTypes.IRRADIATED_DRONE);

    public static final BLibHolder<SpawnEggItem> IRRADIATED_PRAETORIAN_SPAWN_EGG = create(
        "irradiated_praetorian",
        AlienEntityTypes.IRRADIATED_PRAETORIAN
    );

    public static final BLibHolder<SpawnEggItem> IRRADIATED_PREDALIEN_SPAWN_EGG = create(
        "irradiated_predalien",
        AlienEntityTypes.IRRADIATED_PREDALIEN
    );

    public static final BLibHolder<SpawnEggItem> IRRADIATED_PROWLER_SPAWN_EGG = create(
        "irradiated_prowler",
        AlienEntityTypes.IRRADIATED_PROWLER
    );

    public static final BLibHolder<SpawnEggItem> IRRADIATED_QUEEN_SPAWN_EGG = create("irradiated_queen", AlienEntityTypes.IRRADIATED_QUEEN);

    public static final BLibHolder<SpawnEggItem> IRRADIATED_RUNNER_SPAWN_EGG = create(
        "irradiated_runner",
        AlienEntityTypes.IRRADIATED_RUNNER
    );

    public static final BLibHolder<SpawnEggItem> IRRADIATED_WARRIOR_SPAWN_EGG = create(
        "irradiated_warrior",
        AlienEntityTypes.IRRADIATED_WARRIOR
    );

    public static final BLibHolder<SpawnEggItem> NETHER_ADOLESCENT_SPAWN_EGG = create(
        "nether_adolescent",
        AlienEntityTypes.NETHER_ADOLESCENT
    );

    public static final BLibHolder<SpawnEggItem> NETHER_BOILER_SPAWN_EGG = create("nether_boiler", AlienEntityTypes.NETHER_BOILER);

    public static final BLibHolder<SpawnEggItem> NETHER_CHESTBURSTER_SPAWN_EGG = create(
        "nether_chestburster",
        AlienEntityTypes.NETHER_CHESTBURSTER
    );

    public static final BLibHolder<SpawnEggItem> NETHER_CRUSHER_SPAWN_EGG = create("nether_crusher", AlienEntityTypes.NETHER_CRUSHER);

    public static final BLibHolder<SpawnEggItem> NETHER_DRONE_SPAWN_EGG = create("nether_drone", AlienEntityTypes.NETHER_DRONE);

    public static final BLibHolder<SpawnEggItem> NETHER_FACEHUGGER_SPAWN_EGG = create(
        "nether_facehugger",
        AlienEntityTypes.NETHER_FACEHUGGER
    );

    public static final BLibHolder<SpawnEggItem> NETHER_OVOMORPH_SPAWN_EGG = create("nether_ovomorph", AlienEntityTypes.NETHER_OVOMORPH);

    public static final BLibHolder<SpawnEggItem> NETHER_PRAETORIAN_SPAWN_EGG = create(
        "nether_praetorian",
        AlienEntityTypes.NETHER_PRAETORIAN
    );

    public static final BLibHolder<SpawnEggItem> NETHER_PREDALIEN_ADOLESCENT_SPAWN_EGG = create(
        "nether_predalien_adolescent",
        AlienEntityTypes.NETHER_PREDALIEN_ADOLESCENT
    );

    public static final BLibHolder<SpawnEggItem> NETHER_PREDALIEN_CHESTBURSTER_SPAWN_EGG = create(
        "nether_predalien_chestburster",
        AlienEntityTypes.NETHER_PREDALIEN_CHESTBURSTER
    );

    public static final BLibHolder<SpawnEggItem> NETHER_PREDALIEN_SPAWN_EGG = create("nether_predalien", AlienEntityTypes.NETHER_PREDALIEN);

    public static final BLibHolder<SpawnEggItem> NETHER_PROWLER_SPAWN_EGG = create("nether_prowler", AlienEntityTypes.NETHER_PROWLER);

    public static final BLibHolder<SpawnEggItem> NETHER_RUNNER_SPAWN_EGG = create("nether_runner", AlienEntityTypes.NETHER_RUNNER);

    public static final BLibHolder<SpawnEggItem> NETHER_SPITTER_SPAWN_EGG = create("nether_spitter", AlienEntityTypes.NETHER_SPITTER);

    public static final BLibHolder<SpawnEggItem> NETHER_WARRIOR_SPAWN_EGG = create("nether_warrior", AlienEntityTypes.NETHER_WARRIOR);

    public static final BLibHolder<SpawnEggItem> NETHER_QUEEN_SPAWN_EGG = create("nether_queen", AlienEntityTypes.NETHER_QUEEN);

    public static final BLibHolder<SpawnEggItem> OVOMORPH_SPAWN_EGG = create("ovomorph", AlienEntityTypes.OVOMORPH);

    public static final BLibHolder<SpawnEggItem> PRAETORIAN_SPAWN_EGG = create("praetorian", AlienEntityTypes.PRAETORIAN);

    public static final BLibHolder<SpawnEggItem> PREDALIEN_ADOLESCENT_SPAWN_EGG = create(
        "predalien_adolescent",
        AlienEntityTypes.PREDALIEN_ADOLESCENT
    );

    public static final BLibHolder<SpawnEggItem> PREDALIEN_CHESTBURSTER_SPAWN_EGG = create(
        "predalien_chestburster",
        AlienEntityTypes.PREDALIEN_CHESTBURSTER
    );

    public static final BLibHolder<SpawnEggItem> PREDALIEN_SPAWN_EGG = create("predalien", AlienEntityTypes.PREDALIEN);

    public static final BLibHolder<SpawnEggItem> PROWLER_SPAWN_EGG = create("prowler", AlienEntityTypes.PROWLER);

    public static final BLibHolder<SpawnEggItem> QUEEN_SPAWN_EGG = create("queen", AlienEntityTypes.QUEEN);

    public static final BLibHolder<SpawnEggItem> ROYAL_ABERRANT_ADOLESCENT_SPAWN_EGG = create(
        "royal_aberrant_adolescent",
        AlienEntityTypes.ROYAL_ABERRANT_ADOLESCENT
    );

    public static final BLibHolder<SpawnEggItem> ROYAL_ABERRANT_CHESTBURSTER_SPAWN_EGG = create(
        "royal_aberrant_chestburster",
        AlienEntityTypes.ROYAL_ABERRANT_CHESTBURSTER
    );

    public static final BLibHolder<SpawnEggItem> ROYAL_ABERRANT_FACEHUGGER_SPAWN_EGG = create(
        "royal_aberrant_facehugger",
        AlienEntityTypes.ROYAL_ABERRANT_FACEHUGGER
    );

    public static final BLibHolder<SpawnEggItem> ROYAL_ABERRANT_OVOMORPH_SPAWN_EGG = create(
        "royal_aberrant_ovomorph",
        AlienEntityTypes.ROYAL_ABERRANT_OVOMORPH
    );

    public static final BLibHolder<SpawnEggItem> ROYAL_ADOLESCENT_SPAWN_EGG = create("royal_adolescent", AlienEntityTypes.ROYAL_ADOLESCENT);

    public static final BLibHolder<SpawnEggItem> ROYAL_CHESTBURSTER_SPAWN_EGG = create(
        "royal_chestburster",
        AlienEntityTypes.ROYAL_CHESTBURSTER
    );

    public static final BLibHolder<SpawnEggItem> ROYAL_NETHER_ADOLESCENT_SPAWN_EGG = create(
        "royal_nether_adolescent",
        AlienEntityTypes.ROYAL_NETHER_ADOLESCENT
    );

    public static final BLibHolder<SpawnEggItem> ROYAL_NETHER_CHESTBURSTER_SPAWN_EGG = create(
        "royal_nether_chestburster",
        AlienEntityTypes.ROYAL_NETHER_CHESTBURSTER
    );

    public static final BLibHolder<SpawnEggItem> ROYAL_FACEHUGGER_SPAWN_EGG = create("royal_facehugger", AlienEntityTypes.ROYAL_FACEHUGGER);

    public static final BLibHolder<SpawnEggItem> ROYAL_NETHER_FACEHUGGER_SPAWN_EGG = create(
        "royal_nether_facehugger",
        AlienEntityTypes.ROYAL_NETHER_FACEHUGGER
    );

    public static final BLibHolder<SpawnEggItem> ROYAL_NETHER_OVOMORPH_SPAWN_EGG = create(
        "royal_nether_ovomorph",
        AlienEntityTypes.ROYAL_NETHER_OVOMORPH
    );

    public static final BLibHolder<SpawnEggItem> ROYAL_OVOMORPH_SPAWN_EGG = create("royal_ovomorph", AlienEntityTypes.ROYAL_OVOMORPH);

    public static final BLibHolder<SpawnEggItem> RUNNER_SPAWN_EGG = create("runner", AlienEntityTypes.RUNNER);

    public static final BLibHolder<SpawnEggItem> SPITTER_SPAWN_EGG = create("spitter", AlienEntityTypes.SPITTER);

    public static final BLibHolder<SpawnEggItem> WARRIOR_SPAWN_EGG = create("warrior", AlienEntityTypes.WARRIOR);

    private static <E extends Mob> BLibHolder<SpawnEggItem> create(
        String path,
        Supplier<EntityType<E>> entityTypeSupplier
    ) {
        return REGISTRY.createHolder(
            path + "_spawn_egg",
            Alien.MOD.factories().createSpawnEggSupplier(entityTypeSupplier, 0xFFFFFF, 0xFFFFFF, new Item.Properties())
        );
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

package com.alien.fabric.data.lang.en_us.provider;

import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.function.Consumer;

public class EnUsEntityTypeTagProvider {

    public static final Consumer<FabricLanguageProvider.TranslationBuilder> CONSUMER = builder -> {
        builder.add(AlienEntityTypeTags.ABERRANT_ALIENS, "Aberrant Aliens");
        builder.add(AlienEntityTypeTags.ACID_IMMUNE, "Acid Immune");
        builder.add(AlienEntityTypeTags.ALIENS, "Aliens");
        builder.add(AlienEntityTypeTags.CHESTBURSTERS, "Chestbursters");
        builder.add(AlienEntityTypeTags.DRONES, "Drones");
        builder.add(AlienEntityTypeTags.FACEHUGGERS, "Facehuggers");
        builder.add(AlienEntityTypeTags.HATED_BY_XENOMORPHS, "Hated By Xenomorphs");
        builder.add(AlienEntityTypeTags.HIVE_ALIENS, "Hive Aliens");
        builder.add(AlienEntityTypeTags.HOSTS, "Hosts");
        builder.add(AlienEntityTypeTags.IRRADIATED_ALIENS, "Irradiated Aliens");
        builder.add(AlienEntityTypeTags.NETHER_ALIENS, "Nether Aliens");
        builder.add(AlienEntityTypeTags.NORMAL_ALIENS, "Aliens");
        builder.add(AlienEntityTypeTags.OVOMORPHS, "Ovomorphs");
        builder.add(AlienEntityTypeTags.PARASITES, "Parasites");
        builder.add(AlienEntityTypeTags.PRAETORIANS, "Praetorians");
        builder.add(AlienEntityTypeTags.PREDALIENS, "Predaliens");
        builder.add(AlienEntityTypeTags.QUEENS, "Queens");
        builder.add(AlienEntityTypeTags.ROYAL_ALIENS, "Royal Aliens");
        builder.add(AlienEntityTypeTags.ROYAL_XENOMORPHS, "Royal Xenomorphs");
        builder.add(AlienEntityTypeTags.SPAWNS_IN_HIVE_DRONE_LAYER, "Spawns In Hive Drone Layer");
        builder.add(AlienEntityTypeTags.SPAWNS_IN_HIVE_PRAETORIAN_LAYER, "Spawns In Hive Praetorian Layer");
        builder.add(AlienEntityTypeTags.SPAWNS_IN_HIVE_QUEEN_LAYER, "Spawns In Hive Queen Layer");
        builder.add(AlienEntityTypeTags.SPAWNS_IN_HIVE_WARRIOR_LAYER, "Spawns In Hive Warrior Layer");
        builder.add(AlienEntityTypeTags.WARRIORS, "Warriors");
        builder.add(AlienEntityTypeTags.XENOMORPHS, "Xenomorphs");
    };
}

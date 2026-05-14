package com.alien.common.registry.init;

import com.alien.Alien;
import com.alien.common.gameplay.hive.HiveFactionData;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.faction.LocationFactionData;
import com.alien.common.gameplay.hive2.faction.VariantFactionData;
import com.blib.api.common.faction.v1.FactionData;
import com.blib.api.common.faction.v1.FactionDataType;
import com.blib.api.common.registry.v1.BLibBuiltInRegistries;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;

import java.util.function.Supplier;

public class AlienFactionDataTypes {

    private static final BLibRegistry<FactionDataType<?>> REGISTRY = Alien.MOD.registries()
        .create(BLibBuiltInRegistries.FACTION_DATA_TYPES);

    public static final BLibHolder<FactionDataType<HiveFactionData>> HIVE = register(
        "hive",
        () -> new FactionDataType<>(HiveFactionData::new)
    );

    public static final BLibHolder<FactionDataType<VariantFactionData>> VARIANT = register(
        "variant",
        () -> new FactionDataType<>(VariantFactionData::new)
    );

    public static final BLibHolder<FactionDataType<LineageFactionData>> LINEAGE = register(
        "lineage",
        () -> new FactionDataType<>(LineageFactionData::new)
    );

    public static final BLibHolder<FactionDataType<LocationFactionData>> LOCATION = register(
        "location",
        () -> new FactionDataType<>(LocationFactionData::new)
    );

    private static <T extends FactionData> BLibHolder<FactionDataType<T>> register(
        String path,
        Supplier<FactionDataType<T>> supplier
    ) {
        return REGISTRY.createHolder(path, supplier);
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

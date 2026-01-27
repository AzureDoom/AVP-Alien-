package com.alien.common.registry.init;

import com.alien.Alien;
import com.alien.AlienResources;
import com.blib.api.common.registry.v1.BLibHolder;
import com.blib.api.common.registry.v1.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;

public class AlienDecoratedPotPatterns {

    private static final BLibRegistry<DecoratedPotPattern> REGISTRY = Alien.MOD.registries()
        .create(BuiltInRegistries.DECORATED_POT_PATTERN);

    public static final BLibHolder<DecoratedPotPattern> OVOID = create("ovoid_pottery_pattern");

    public static final BLibHolder<DecoratedPotPattern> PARASITE = create("parasite_pottery_pattern");

    public static final BLibHolder<DecoratedPotPattern> ROYALTY = create("royalty_pottery_pattern");

    public static final BLibHolder<DecoratedPotPattern> VECTOR = create("vector_pottery_pattern");

    private static BLibHolder<DecoratedPotPattern> create(String path) {
        return REGISTRY.createHolder(
            path,
            () -> new DecoratedPotPattern(AlienResources.location(path))
        );
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

package com.alien.common.registry.init;

import com.alien.Alien;
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.gameevent.GameEvent;

public class AlienGameEvents {

    private static final BLibRegistry<GameEvent> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.GAME_EVENT);

    public static final BLibHolder<GameEvent> EGG_ABERRANT_PICKUP_REQUEST = register("egg_aberrant_pickup_request");

    public static final BLibHolder<GameEvent> EGG_NETHER_PICKUP_REQUEST = register("egg_nether_pickup_request");

    public static final BLibHolder<GameEvent> EGG_PICKUP_REQUEST = register("egg_pickup_request");

    public static final BLibHolder<GameEvent> XENOMORPH_ABERRANT_RESIN_SPREAD = register("aberrant_resin_spread");

    public static final BLibHolder<GameEvent> XENOMORPH_IRRADIATED_RESIN_SPREAD = register("irradiated_resin_spread");

    public static final BLibHolder<GameEvent> XENOMORPH_NETHER_RESIN_SPREAD = register("nether_resin_spread");

    public static final BLibHolder<GameEvent> XENOMORPH_RESIN_SPREAD = register("resin_spread");

    public static final BLibHolder<GameEvent> XENOMORPH_ABERRANT_CRY_FOR_HELP = register("aberrant_cry_for_help");

    public static final BLibHolder<GameEvent> XENOMORPH_IRRADIATED_CRY_FOR_HELP = register("irradiated_cry_for_help");

    public static final BLibHolder<GameEvent> XENOMORPH_NETHER_CRY_FOR_HELP = register("nether_cry_for_help");

    public static final BLibHolder<GameEvent> XENOMORPH_CRY_FOR_HELP = register("cry_for_help");

    private static BLibHolder<GameEvent> register(String path) {
        return register(path, 16);
    }

    private static BLibHolder<GameEvent> register(String path, int radius) {
        return REGISTRY.createHolder(path, () -> new GameEvent(radius));
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

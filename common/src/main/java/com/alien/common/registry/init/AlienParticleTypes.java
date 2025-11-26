package com.alien.common.registry.init;

import com.alien.Alien;
import com.blib.BLibHolder;
import com.blib.BLibRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class AlienParticleTypes {

    private static final BLibRegistry<SimpleParticleType> REGISTRY = Alien.MOD.createRegistry(BuiltInRegistries.PARTICLE_TYPE);

    public static final BLibHolder<SimpleParticleType> ACID = create("acid");

    public static final BLibHolder<SimpleParticleType> BLUE_ACID = create("blue_acid");

    public static final BLibHolder<SimpleParticleType> IRRADIATED_ACID = create("irradiated_acid");

    private static BLibHolder<SimpleParticleType> create(String path) {
        return REGISTRY.createHolder(path, () -> new SimpleParticleType(false) {});
    }

    public static void initialize() {
        REGISTRY.registerAll();
    }
}

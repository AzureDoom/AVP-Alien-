package com.alien.fabric.data.damage_type;

import com.alien.common.registry.key.AlienDamageTypeKeys;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;

import com.avp.common.registry.key.AVPDamageTypeKeys;

public class DamageTypeBootstrapper {

    public static void bootstrap(BootstrapContext<DamageType> registry) {
        registry.register(AlienDamageTypeKeys.ACID, new DamageType("acid", 0.1F));
        registry.register(AlienDamageTypeKeys.CHESTBURSTING, new DamageType("chestbursting", 0.1F));
        registry.register(AlienDamageTypeKeys.SMOTHERING, new DamageType("smothering", 0.1F));
    }
}

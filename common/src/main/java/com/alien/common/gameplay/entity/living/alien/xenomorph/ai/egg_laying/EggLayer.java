package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying;

import com.alien.common.gameplay.entity.living.alien.HiveManager;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.compatibility.avp_human.GeneManagerProxy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface EggLayer {

    // Do NOT declare vanilla LivingEntity/Entity methods (isAlive, level, getRandom, ...) on this
    // interface. They are satisfied only by inheritance from the Minecraft entity, whose names are
    // remapped in the production jar (e.g. isAlive -> method_5805), which leaves the interface method
    // abstract at runtime -> AbstractMethodError. Reach them through asEntity() instead
    // (e.g. asEntity().isAlive(), asEntity().level(), asEntity().getRandom()).
    Entity asEntity();

    AlienVariant getVariant();

    HiveManager getHiveManager();

    GeneManagerProxy getGeneManager();

    boolean isEggLayCooldownReady();

    void resetEggLayCooldown();

    boolean hasOvipositor();

    Vec3 getEggLayingPosition();
}

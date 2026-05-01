package com.alien.common.gameplay.entity.living.alien.xenomorph.ai.egg_laying;

import com.alien.common.gameplay.entity.living.alien.HiveManager;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.compatibility.avp_human.GeneManagerProxy;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface EggLayer {

    boolean isAlive();

    Entity asEntity();

    Level level();

    AlienVariant getVariant();

    HiveManager getHiveManager();

    GeneManagerProxy getGeneManager();

    RandomSource getRandom();

    boolean isEggLayCooldownReady();

    void resetEggLayCooldown();

    boolean hasOvipositor();

    Vec3 getEggLayingPosition();
}

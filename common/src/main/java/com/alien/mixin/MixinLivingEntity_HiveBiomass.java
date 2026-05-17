package com.alien.mixin;

import com.alien.common.gameplay.hive2.economy.HiveBiomassEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Routes every server-side living-entity death through {@link HiveBiomassEvents} so xenomorph kills credit biomass to
 * the killer's home location. Injected at HEAD of {@code die} so the lookup uses the live entity state.
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity_HiveBiomass {

    @Inject(method = "die", at = @At("HEAD"))
    private void avp_alien$onDie(DamageSource damageSource, CallbackInfo ci) {
        var self = (LivingEntity) (Object) this;
        if (!(self.level() instanceof ServerLevel)) {
            return;
        }
        HiveBiomassEvents.onLivingEntityDeath(self, damageSource.getEntity());
    }
}

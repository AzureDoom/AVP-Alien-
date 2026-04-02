package com.alien.mixin;

import com.alien.common.gameplay.effect.BloodLossStatusEffect;
import com.alien.common.registry.init.AlienMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity_BloodLoss {

    @Unique
    private float avp_alien$healthBeforeHurt;

    @Inject(
        method = "hurt",
        at = @At("HEAD")
    )
    private void onHurtHead(DamageSource damageSource, float damageAmount, CallbackInfoReturnable<Boolean> callbackInfo) {
        var self = (LivingEntity) (Object) this;
        this.avp_alien$healthBeforeHurt = self.getHealth();
    }

    @Inject(
        method = "hurt",
        at = @At("RETURN")
    )
    private void onHurtReturn(DamageSource damageSource, float damageAmount, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!callbackInfo.getReturnValue()) {
            return;
        }

        var self = (LivingEntity) (Object) this;

        if (!self.hasEffect(AlienMobEffects.getBloodLossHolder())) {
            return;
        }

        var actualDamage = this.avp_alien$healthBeforeHurt - self.getHealth();

        if (actualDamage > 0) {
            BloodLossStatusEffect.applyMaxHealthReduction(self, actualDamage);
        }
    }

    @Inject(
        method = "onEffectRemoved",
        at = @At("TAIL")
    )
    private void onBloodLossEffectRemoved(MobEffectInstance effectInstance, CallbackInfo callbackInfo) {
        if (!effectInstance.is(AlienMobEffects.getBloodLossHolder())) {
            return;
        }

        var self = (LivingEntity) (Object) this;
        BloodLossStatusEffect.removeMaxHealthReduction(self);
    }
}

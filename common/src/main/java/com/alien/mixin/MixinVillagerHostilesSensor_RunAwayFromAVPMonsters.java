package com.alien.mixin;

import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public abstract class MixinVillagerHostilesSensor_RunAwayFromAVPMonsters extends NearestVisibleLivingEntitySensor {

    @Inject(at = @At("HEAD"), method = "isClose", cancellable = true)
    void isClose(LivingEntity livingEntity, LivingEntity livingEntity2, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!livingEntity2.getType().is(AlienEntityTypeTags.XENOMORPHS)) {
            return;
        }

        var distance = 12F;
        var returnValue = livingEntity2.distanceToSqr(livingEntity) <= (distance * distance);
        callbackInfoReturnable.setReturnValue(returnValue);
    }

    @Inject(at = @At("HEAD"), method = "isHostile", cancellable = true)
    void isHostile(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (livingEntity.getType().is(AlienEntityTypeTags.XENOMORPHS)) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}

package com.alien.common.gameplay.block.resin.web;

import com.alien.compatibility.avp_human.AVPHuman;
import com.human.common.gameplay.effect.RadiationStatusEffect;
import com.human.common.registry.init.HumanMobEffects;
import com.human.util.HumanPredicates;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class IrradiatedResinWebBlock extends ResinWebBlock {

    public IrradiatedResinWebBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull Entity entity) {
        if (!AVPHuman.MOD.isLoaded()) {
            return;
        }

        if (HumanPredicates.canBeIrradiated(entity) && entity instanceof LivingEntity livingEntity) {
            // Apply radiation effect.
            livingEntity.addEffect(
                new MobEffectInstance(HumanMobEffects.getRadiationHolder(), RadiationStatusEffect.SHORT_EFFECT_DURATION_IN_TICKS, 0)
            );
        }

        super.stepOn(level, blockPos, blockState, entity);
    }
}

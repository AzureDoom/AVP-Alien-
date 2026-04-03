package com.alien.common.model.lifecycle.growth;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public sealed interface GrowthRequirement {

    Codec<GrowthRequirement> CODEC = Codec.STRING.dispatch(
        "type",
        requirement -> switch (requirement) {
            case MobEffectRequirement ignored -> "mob_effect";
        },
        type -> switch (type) {
            case "mob_effect" -> MobEffectRequirement.CODEC;
            default -> throw new IllegalArgumentException("Unknown growth requirement type: " + type);
        }
    );

    boolean test(LivingEntity entity);

    record MobEffectRequirement(Holder<MobEffect> effect, int minAmplifier) implements GrowthRequirement {

        public static final MapCodec<MobEffectRequirement> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(MobEffectRequirement::effect),
                Codec.INT.optionalFieldOf("min_amplifier", 0).forGetter(MobEffectRequirement::minAmplifier)
            ).apply(instance, MobEffectRequirement::new)
        );

        @Override
        public boolean test(LivingEntity entity) {
            var effectInstance = entity.getEffect(effect);

            return effectInstance != null && effectInstance.getAmplifier() >= minAmplifier;
        }

        public boolean isInGrowthWindow(LivingEntity entity, int windowInTicks) {
            var effectInstance = entity.getEffect(effect);

            if (effectInstance == null || effectInstance.getAmplifier() < minAmplifier) {
                return false;
            }

            return effectInstance.getDuration() <= windowInTicks;
        }
    }
}

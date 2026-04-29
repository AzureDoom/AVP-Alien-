package com.alien.common.gameplay.entity.living.alien.xenomorph.carrier;

import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

public class CarrierData implements NBTSerializable {

    private static final String NBT_THROW_COOLDOWN_IN_TICKS = "throwCooldownInTicks";

    private static final int MIN_THROW_COOLDOWN_IN_TICKS = 20 * 5;

    private static final int MAX_THROW_COOLDOWN_IN_TICKS = 20 * 10;

    private int throwCooldownInTicks;

    public void tick() {
        if (throwCooldownInTicks > 0) {
            throwCooldownInTicks--;
        }
    }

    public boolean isThrowCooldownReady() {
        return throwCooldownInTicks <= 0;
    }

    public void resetThrowCooldown(RandomSource random) {
        this.throwCooldownInTicks = random.nextIntBetweenInclusive(
            MIN_THROW_COOLDOWN_IN_TICKS,
            MAX_THROW_COOLDOWN_IN_TICKS
        );
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(NBT_THROW_COOLDOWN_IN_TICKS)) {
            this.throwCooldownInTicks = compoundTag.getInt(NBT_THROW_COOLDOWN_IN_TICKS);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putInt(NBT_THROW_COOLDOWN_IN_TICKS, throwCooldownInTicks);
    }
}

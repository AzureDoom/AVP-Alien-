package com.alien.common.gameplay.entity.living.alien.xenomorph.empress;

import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.nbt.CompoundTag;

import java.util.concurrent.TimeUnit;

public class EmpressData implements NBTSerializable {

    private static final String NBT_EGG_LAY_COOLDOWN = "eggLayCooldownInTicks";

    private static final int MAX_EGG_LAY_COOLDOWN_IN_TICKS = (int) TimeUnit.MINUTES.toSeconds(1) * 20;

    private int eggLayCooldownInTicks;

    public EmpressData() {
        this.eggLayCooldownInTicks = MAX_EGG_LAY_COOLDOWN_IN_TICKS;
    }

    public void tick() {
        if (eggLayCooldownInTicks > 0) {
            eggLayCooldownInTicks--;
        }
    }

    public boolean isEggLayCooldownReady() {
        return eggLayCooldownInTicks <= 0;
    }

    public void resetEggLayCooldown() {
        this.eggLayCooldownInTicks = MAX_EGG_LAY_COOLDOWN_IN_TICKS;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(NBT_EGG_LAY_COOLDOWN)) {
            this.eggLayCooldownInTicks = compoundTag.getInt(NBT_EGG_LAY_COOLDOWN);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putInt(NBT_EGG_LAY_COOLDOWN, eggLayCooldownInTicks);
    }
}

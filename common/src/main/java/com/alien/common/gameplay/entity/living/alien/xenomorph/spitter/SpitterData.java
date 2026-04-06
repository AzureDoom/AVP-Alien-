package com.alien.common.gameplay.entity.living.alien.xenomorph.spitter;

import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.nbt.CompoundTag;

public class SpitterData implements NBTSerializable {

    private static final String NBT_LAST_SPIT_TICK = "lastSpitTick";

    private static final int SPIT_COOLDOWN_IN_TICKS = 20 * 3;

    private int lastSpitTick = -SPIT_COOLDOWN_IN_TICKS;

    public boolean isCooldownReady(int currentTick) {
        if (currentTick < lastSpitTick) {
            return true;
        }

        return currentTick - lastSpitTick >= SPIT_COOLDOWN_IN_TICKS;
    }

    public int getLastSpitTick() {
        return lastSpitTick;
    }

    public void setLastSpitTick(int tick) {
        this.lastSpitTick = tick;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(NBT_LAST_SPIT_TICK)) {
            this.lastSpitTick = compoundTag.getInt(NBT_LAST_SPIT_TICK);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putInt(NBT_LAST_SPIT_TICK, lastSpitTick);
    }
}

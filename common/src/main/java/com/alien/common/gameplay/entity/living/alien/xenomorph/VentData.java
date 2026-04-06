package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.nbt.CompoundTag;

public class VentData implements NBTSerializable {

    private static final String NBT_LAST_VENT_CREATION_TICK = "lastVentCreationTick";

    private int lastVentCreationTick;

    public int getLastVentCreationTick() {
        return lastVentCreationTick;
    }

    public void setLastVentCreationTick(int tick) {
        this.lastVentCreationTick = tick;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(NBT_LAST_VENT_CREATION_TICK)) {
            this.lastVentCreationTick = compoundTag.getInt(NBT_LAST_VENT_CREATION_TICK);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putInt(NBT_LAST_VENT_CREATION_TICK, lastVentCreationTick);
    }
}

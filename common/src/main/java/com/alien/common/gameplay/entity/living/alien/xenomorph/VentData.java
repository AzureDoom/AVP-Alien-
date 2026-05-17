package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.nbt.CompoundTag;

public class VentData implements NBTSerializable {

    private static final String NBT_LAST_VENT_CREATION_TICK = "lastVentCreationTick";

    private int lastVentCreationTick;

    private boolean hasVentTargetSearchFailure;

    private int lastVentTargetSearchFailureTick;

    public int getLastVentCreationTick() {
        return lastVentCreationTick;
    }

    public void setLastVentCreationTick(int tick) {
        this.lastVentCreationTick = tick;
    }

    public boolean canRetryVentTargetSearch(int currentTick, int retryCooldownInTicks) {
        return !hasVentTargetSearchFailure
            || currentTick - lastVentTargetSearchFailureTick >= retryCooldownInTicks;
    }

    public void recordVentTargetSearchFailure(int tick) {
        this.hasVentTargetSearchFailure = true;
        this.lastVentTargetSearchFailureTick = tick;
    }

    public void clearVentTargetSearchFailure() {
        this.hasVentTargetSearchFailure = false;
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

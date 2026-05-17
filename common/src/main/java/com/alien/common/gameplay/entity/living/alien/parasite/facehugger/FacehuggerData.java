package com.alien.common.gameplay.entity.living.alien.parasite.facehugger;

import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.nbt.CompoundTag;

public class FacehuggerData implements NBTSerializable {

    public static final int MAX_IDLE_TIME_IN_TICKS = 12 * 20;

    public static final int MIN_IDLE_TIME_IN_TICKS = 7 * 20;

    private static final String NBT_TICKS_UNTIL_BORED = "ticksUntilBored";

    private final Facehugger facehugger;

    private int ticksUntilBored;

    public FacehuggerData(Facehugger facehugger) {
        this.facehugger = facehugger;
        this.ticksUntilBored = facehugger.getRandom().nextIntBetweenInclusive(MIN_IDLE_TIME_IN_TICKS, MAX_IDLE_TIME_IN_TICKS);
    }

    public void tick() {
        this.ticksUntilBored = Math.max(ticksUntilBored - 1, 0);
    }

    public int getTicksUntilBored() {
        return ticksUntilBored;
    }

    public void resetTicksUntilBored() {
        this.ticksUntilBored = facehugger.getRandom().nextIntBetweenInclusive(MIN_IDLE_TIME_IN_TICKS, MAX_IDLE_TIME_IN_TICKS);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(NBT_TICKS_UNTIL_BORED)) {
            this.ticksUntilBored = compoundTag.getInt(NBT_TICKS_UNTIL_BORED);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putInt(NBT_TICKS_UNTIL_BORED, ticksUntilBored);
    }
}

package com.alien.common.gameplay.entity.living.alien.xenomorph.ravager;

import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.nbt.CompoundTag;

public class RavagerData implements NBTSerializable {

    private static final String NBT_SPECIAL_ATTACK_COOLDOWN_IN_TICKS = "specialAttackCooldownInTicks";

    private int specialAttackCooldownInTicks;

    public void tick() {
        if (specialAttackCooldownInTicks > 0) {
            specialAttackCooldownInTicks--;
        }
    }

    public boolean isSpecialAttackCooldownReady() {
        return specialAttackCooldownInTicks <= 0;
    }

    public void resetSpecialAttackCooldown(int cooldownInTicks) {
        this.specialAttackCooldownInTicks = cooldownInTicks;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (compoundTag.contains(NBT_SPECIAL_ATTACK_COOLDOWN_IN_TICKS)) {
            this.specialAttackCooldownInTicks = compoundTag.getInt(NBT_SPECIAL_ATTACK_COOLDOWN_IN_TICKS);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.putInt(NBT_SPECIAL_ATTACK_COOLDOWN_IN_TICKS, specialAttackCooldownInTicks);
    }
}

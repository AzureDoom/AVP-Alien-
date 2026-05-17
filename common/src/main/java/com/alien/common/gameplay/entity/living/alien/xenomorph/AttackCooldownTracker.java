package com.alien.common.gameplay.entity.living.alien.xenomorph;

import com.blib.api.common.nbt.v1.model.NBTSerializable;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

public class AttackCooldownTracker implements NBTSerializable {

    private static final String NBT_TAG = "AttackCooldowns";

    private final Map<String, Integer> ticksRemainingById = new HashMap<>();

    public void tick() {
        var iterator = ticksRemainingById.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            var next = entry.getValue() - 1;

            if (next <= 0) {
                iterator.remove();
            } else {
                entry.setValue(next);
            }
        }
    }

    public boolean isReady(AttackType attack) {
        return !ticksRemainingById.containsKey(attack.id());
    }

    public void start(AttackType attack) {
        if (attack.cooldownInTicks() > 0) {
            ticksRemainingById.put(attack.id(), attack.cooldownInTicks());
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        ticksRemainingById.clear();

        if (!compoundTag.contains(NBT_TAG)) {
            return;
        }

        var subTag = compoundTag.getCompound(NBT_TAG);

        for (var key : subTag.getAllKeys()) {
            ticksRemainingById.put(key, subTag.getInt(key));
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        if (ticksRemainingById.isEmpty()) {
            return;
        }

        var subTag = new CompoundTag();
        ticksRemainingById.forEach(subTag::putInt);
        compoundTag.put(NBT_TAG, subTag);
    }
}

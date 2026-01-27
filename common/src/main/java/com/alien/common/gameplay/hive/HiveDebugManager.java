package com.alien.common.gameplay.hive;

import com.alien.common.property.AlienProperties;
import com.alien.common.property.AlienPropertyAccess;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class HiveDebugManager {

    private final Hive hive;

    public HiveDebugManager(Hive hive) {
        this.hive = hive;
    }

    public void tick() {
        var debugManager = hive.getDebugManager();

        if (debugManager.isDebugEnabled()) {
            runDebugRoutines();
        }
    }

    public void onHiveRemoved() { /* NO-OP */ }

    private void runDebugRoutines() {
        var debugManager = hive.getDebugManager();

        var hiveLeader = hive.getLeadershipManager().getLeaderOrNull();

        if (hiveLeader != null && debugManager.isDebugLeaderHighlightEnabled()) {
            if (hive.ageInTicks() % 20 == 0) {
                var effect = new MobEffectInstance(MobEffects.GLOWING, 40, 3, true, false, true);

                hiveLeader.addEffect(effect);
            }
        }

        if (debugManager.isDebugHiveMemberHighlightEnabled()) {
            var effect = new MobEffectInstance(MobEffects.GLOWING, 40, 3, true, false, true);

            hive.getMembershipManager()
                .getLoadedMembers()
                .forEach(entity -> {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.addEffect(effect);
                    }
                });
        }
    }

    public boolean isDebugEnabled() {
        return AlienPropertyAccess.INSTANCE.getOrThrow(AlienProperties.Hive.Debug.ENABLED);
    }

    public boolean isDebugHiveMemberHighlightEnabled() {
        return AlienPropertyAccess.INSTANCE.getOrThrow(AlienProperties.Hive.Debug.HIGHLIGHT_ALL_MEMBERS);
    }

    public boolean isDebugLeaderHighlightEnabled() {
        return AlienPropertyAccess.INSTANCE.getOrThrow(AlienProperties.Hive.Debug.HIGHLIGHT_LEADER);
    }
}

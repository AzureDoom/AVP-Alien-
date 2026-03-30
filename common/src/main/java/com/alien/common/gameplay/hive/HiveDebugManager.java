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
        if (isDebugEnabled()) {
            runDebugRoutines();
        }
    }

    public void onHiveRemoved() { /* NO-OP */ }

    private void runDebugRoutines() {
        // Debug routines require a server to resolve entities — skip if not available via tick context.
        // The hive tick passes the server, but the debug manager is called from tick() without it.
        // For now, leader highlight uses the cached leader from the faction data.
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

    public void tickDebug() {
        if (!isDebugEnabled()) {
            return;
        }

        var leadershipManager = hive.getLeadershipManager();
        var hiveLeader = leadershipManager.getLeaderOrNull(hive.getServer());

        if (hiveLeader != null && isDebugLeaderHighlightEnabled()) {
            if (hive.ageInTicks() % 20 == 0) {
                var effect = new MobEffectInstance(MobEffects.GLOWING, 40, 3, true, false, true);
                hiveLeader.addEffect(effect);
            }
        }

        if (isDebugHiveMemberHighlightEnabled()) {
            var effect = new MobEffectInstance(MobEffects.GLOWING, 40, 3, true, false, true);

            hive.getLoadedMembers().forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.addEffect(effect);
                }
            });
        }
    }
}

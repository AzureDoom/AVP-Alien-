package com.alien.common.gameplay.hive.ai.task.impl;

import com.alien.common.gameplay.hive.Hive;
import com.alien.common.gameplay.hive.ai.task.HiveTask;
import com.alien.common.gameplay.hive.util.HiveLeaderDispositionUtil;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.UUID;

public class PickBestLeaderTask extends HiveTask {

    private static final int TWENTY_SECONDS_IN_TICKS = 20 * 10;

    public PickBestLeaderTask(Hive hive) {
        super(hive);
    }

    @Override
    public boolean canRun() {
        return hive.ageInTicks() % TWENTY_SECONDS_IN_TICKS == 0;
    }

    @Override
    public void run() {
        var leadershipManager = hive.getLeadershipManager();
        var loadedByType = hive.getFactionData().getLoadedMembersByType();

        UUID candidateUUID = leadershipManager.getLeaderIdOrNull();
        EntityType<?> candidateType = findEntityType(candidateUUID, loadedByType);

        for (var entry : loadedByType.entrySet()) {
            var entityType = entry.getKey();

            if (!entityType.is(AlienEntityTypeTags.XENOMORPHS)) {
                continue;
            }

            for (var contestantUUID : entry.getValue()) {
                if (candidateType == null) {
                    candidateUUID = contestantUUID;
                    candidateType = entityType;
                    continue;
                }

                if (HiveLeaderDispositionUtil.isLeftLowerDisposition(candidateType, entityType)) {
                    candidateUUID = contestantUUID;
                    candidateType = entityType;
                }
            }
        }

        leadershipManager.setLeaderId(candidateUUID);
    }

    private EntityType<?> findEntityType(
        UUID uuid,
        java.util.Map<EntityType<?>, java.util.Set<UUID>> loadedByType
    ) {
        if (uuid == null) {
            return null;
        }

        for (var entry : loadedByType.entrySet()) {
            if (entry.getValue().contains(uuid)) {
                return entry.getKey();
            }
        }

        return null;
    }
}

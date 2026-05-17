package com.alien.common.gameplay.hive2.location;

import com.alien.common.gameplay.hive2.util.HiveLeaderDispositionUtil;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.nbt.v1.CompoundTagUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Per-location leader picker. Unlike the legacy {@code HiveLeadershipManager}, the candidate set is drawn from
 * xenomorphs <em>physically inside this location's claimed chunks</em> — the queen of location A no longer wins the
 * leader race in location B just because they share a lineage faction. See {@code HIVE_REDESIGN_07_LEADERSHIP.md} § 2.
 * <p>
 * The empress (rank 5 via {@link HiveLeaderDispositionUtil}) outranks a queen (rank 4) when both stand in the same
 * territory. If no xenomorph is present the location is dormant and {@link #getLeaderIdOrNull()} returns null —
 * {@code HIVE_REDESIGN_07_LEADERSHIP.md} § 3.
 */
public final class HiveLocationLeadership {

    private static final String NBT_LEADER_ID = "LeaderId";

    private @Nullable UUID leaderId;

    public @Nullable UUID getLeaderIdOrNull() {
        return leaderId;
    }

    public boolean isLeader(Entity entity) {
        return leaderId != null && leaderId.equals(entity.getUUID());
    }

    public boolean isLeader(UUID uuid) {
        return leaderId != null && leaderId.equals(uuid);
    }

    public void setLeaderId(@Nullable UUID leaderId) {
        this.leaderId = leaderId;
    }

    public void clearLeader() {
        this.leaderId = null;
    }

    /**
     * Recomputes the leader from the location's per-chunk loaded membership. Picks the highest-disposition xenomorph;
     * ties go to the current leader if still eligible, otherwise to the first scanned candidate.
     */
    public void pickBestLeader(Map<EntityType<?>, Set<UUID>> loadedMembersByType) {
        UUID bestUuid = null;
        EntityType<?> bestType = null;
        var keepCurrent = false;

        for (var entry : loadedMembersByType.entrySet()) {
            var entityType = entry.getKey();

            if (!entityType.is(AlienEntityTypeTags.XENOMORPHS)) {
                continue;
            }

            for (var contestantUuid : entry.getValue()) {
                if (bestType == null) {
                    bestUuid = contestantUuid;
                    bestType = entityType;

                    if (leaderId != null && leaderId.equals(contestantUuid)) {
                        keepCurrent = true;
                    }
                    continue;
                }

                if (HiveLeaderDispositionUtil.isLeftLowerDisposition(bestType, entityType)) {
                    bestUuid = contestantUuid;
                    bestType = entityType;
                    keepCurrent = leaderId != null && leaderId.equals(contestantUuid);
                } else if (
                    !keepCurrent
                        && leaderId != null
                        && leaderId.equals(contestantUuid)
                        && HiveLeaderDispositionUtil.getDispositionForEntityType(entityType) == HiveLeaderDispositionUtil
                            .getDispositionForEntityType(bestType)
                ) {
                    bestUuid = contestantUuid;
                    keepCurrent = true;
                }
            }
        }

        this.leaderId = bestUuid;
    }

    public void save(CompoundTag tag) {
        if (leaderId != null) {
            tag.putUUID(NBT_LEADER_ID, leaderId);
        }
    }

    public void load(CompoundTag tag) {
        this.leaderId = CompoundTagUtil.getUUIDOrNull(tag, NBT_LEADER_ID);
    }
}

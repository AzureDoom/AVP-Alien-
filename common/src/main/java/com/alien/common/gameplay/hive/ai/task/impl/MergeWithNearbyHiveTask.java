package com.alien.common.gameplay.hive.ai.task.impl;

import com.alien.common.gameplay.hive.Hive;
import com.alien.common.gameplay.hive.HiveRegistry;
import com.alien.common.gameplay.hive.HiveRemovalReason;
import com.alien.common.gameplay.hive.ai.task.HiveTask;
import com.alien.common.gameplay.hive.util.HiveLeaderDispositionUtil;
import com.blib.api.common.faction.v1.FactionMember;

import java.util.Objects;
import java.util.Set;

public class MergeWithNearbyHiveTask extends HiveTask {

    public MergeWithNearbyHiveTask(Hive hive) {
        super(hive);
    }

    @Override
    public boolean canRun() {
        return super.canRun()
            && !hive.isAngry()
            && hive.ageInTicks() % (20 * 60) == 0;
    }

    @Override
    public void run() {
        var dimension = hive.getFactionData().getDimension();

        var nearestHive = HiveRegistry.INSTANCE.findNearestHive(
            hive.centerPosition(),
            dimension,
            candidate -> !Objects.equals(candidate.getFactionId(), hive.getFactionId())
                && Objects.equals(candidate.getVariant(), hive.getVariant())
        );

        if (nearestHive == null) {
            return;
        }

        if (!hive.getSpaceManager().isBlockPosLeashedToHive(nearestHive.centerPosition())) {
            return;
        }

        var strongerHive = getStrongerHive(hive, nearestHive);
        var weakerHive = Objects.equals(strongerHive.getFactionId(), hive.getFactionId())
            ? nearestHive
            : hive;

        mergeLeftHiveIntoRight(weakerHive, strongerHive);
    }

    private Hive getStrongerHive(Hive left, Hive right) {
        var leftLeader = left.getLeadershipManager().getLeaderOrNull(hive.getServer());
        var rightLeader = right.getLeadershipManager().getLeaderOrNull(hive.getServer());

        if (leftLeader != null && rightLeader != null) {
            var leftDisposition = HiveLeaderDispositionUtil.getDispositionForEntityType(leftLeader.getType());
            var rightDisposition = HiveLeaderDispositionUtil.getDispositionForEntityType(rightLeader.getType());

            if (leftDisposition > rightDisposition) {
                return left;
            } else if (leftDisposition < rightDisposition) {
                return right;
            }
        }

        var leftMemberCount = left.getRelationships().getMembers().size();
        var rightMemberCount = right.getRelationships().getMembers().size();

        if (leftMemberCount > rightMemberCount) {
            return left;
        } else if (leftMemberCount < rightMemberCount) {
            return right;
        }

        return left.ageInTicks() >= right.ageInTicks() ? left : right;
    }

    private void mergeLeftHiveIntoRight(Hive left, Hive right) {
        var leftMembers = Set.copyOf(left.getRelationships().getMembers());
        var memberUuids = new java.util.HashSet<java.util.UUID>();

        for (var member : leftMembers) {
            if (member instanceof FactionMember.Entity(var uuid)) {
                left.getRelationships().removeEntity(uuid);
                right.getRelationships().addEntity(uuid);
                memberUuids.add(uuid);
            }
        }

        left.getFactionData().transferMemberTypes(right.getFactionData(), memberUuids);
        left.getLeadershipManager().setLeaderId(null);
        left.remove(HiveRemovalReason.DISCARDED);
    }
}

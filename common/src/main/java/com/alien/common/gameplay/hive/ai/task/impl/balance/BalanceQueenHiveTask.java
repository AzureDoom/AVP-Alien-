package com.alien.common.gameplay.hive.ai.task.impl.balance;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.hive.Hive;
import com.alien.common.registry.tag.AlienEntityTypeTags;

public class BalanceQueenHiveTask extends BalanceHiveTask {

    public BalanceQueenHiveTask(Hive hive) {
        super(hive);
    }

    @Override
    public void run() {
        var loadedByType = hive.getFactionData().getLoadedMembersByType();
        var hasQueenOrEmpress = loadedByType.entrySet()
            .stream()
            .anyMatch(entry -> entry.getKey().is(AlienEntityTypeTags.QUEENS) && !entry.getValue().isEmpty());

        if (hasQueenOrEmpress) {
            return;
        }

        hive.getLeadershipManager().getLeader(hive.getServer()).ifSome(hiveLeader -> {
            if (!(hiveLeader instanceof Xenomorph xenomorph)) {
                return;
            }

            if (!hive.getMembership().hasMember(com.blib.api.common.faction.v1.FactionMember.entity(xenomorph))) {
                return;
            }

            growXenomorph(xenomorph);
        });
    }
}

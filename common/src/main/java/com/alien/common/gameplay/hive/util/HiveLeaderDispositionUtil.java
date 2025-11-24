package com.alien.common.gameplay.hive.util;

import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.world.entity.EntityType;

public class HiveLeaderDispositionUtil {

    public static boolean isLeftLowerDisposition(EntityType<?> current, EntityType<?> other) {
        var currentDisposition = getDispositionForEntityType(current);
        var contestantDisposition = getDispositionForEntityType(other);
        return currentDisposition < contestantDisposition;
    }

    public static int getDispositionForEntityType(EntityType<?> entityType) {
        if (entityType.is(AlienEntityTypeTags.DRONES) || entityType.is(AlienEntityTypeTags.RUNNERS)) {
            return 0;
        } else if (entityType.is(AlienEntityTypeTags.WARRIORS) || entityType.is(AlienEntityTypeTags.PROWLERS)) {
            return 1;
        } else if (entityType.is(AlienEntityTypeTags.PRAETORIANS) || entityType.is(AlienEntityTypeTags.CRUSHERS)) {
            return 2;
        } else if (entityType.is(AlienEntityTypeTags.PREDALIENS)) {
            return 3;
        } else if (entityType.is(AlienEntityTypeTags.QUEENS)) {
            return 4;
        }

        return -1;
    }
}

package com.alien.common.gameplay.hive.convoy;

import com.blib.api.common.entity.v1.EntityReserves;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

final class ConvoyMaterializedMembers {

    private final Map<UUID, EntityType<?>> members;

    private final EntityReserves composition;

    ConvoyMaterializedMembers(Map<UUID, EntityType<?>> members) {
        this.members = new HashMap<>(members);
        this.composition = new EntityReserves();
        for (var entityType : members.values()) {
            this.composition.add(entityType, 1);
        }
    }

    Map<UUID, EntityType<?>> members() {
        return members;
    }

    void track(UUID memberId, EntityType<?> entityType) {
        var previous = members.put(memberId, entityType);
        if (previous != null) {
            composition.add(previous, -1);
        }
        composition.add(entityType, 1);
    }

    EntityType<?> untrack(UUID memberId) {
        var previous = members.remove(memberId);
        if (previous != null) {
            composition.add(previous, -1);
        }
        return previous;
    }

    int countMatching(Predicate<EntityType<?>> predicate) {
        return composition.getCountMatching(predicate);
    }
}

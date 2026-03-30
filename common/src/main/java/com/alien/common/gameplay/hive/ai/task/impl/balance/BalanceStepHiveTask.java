package com.alien.common.gameplay.hive.ai.task.impl.balance;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.hive.Hive;
import com.alien.common.property.AlienProperties;
import com.alien.common.property.AlienPropertyAccess;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BalanceStepHiveTask extends BalanceHiveTask {

    private static final Predicate<EntityType<?>> XENOMORPH_PREDICATE = entityType -> entityType.is(AlienEntityTypeTags.XENOMORPHS);

    private final Supplier<EntityType<?>> baseUnitTypeSupplier;

    private final Supplier<EntityType<?>> desiredUnitTypeSupplier;

    public BalanceStepHiveTask(
        Hive hive,
        Supplier<EntityType<?>> baseUnitTypeSupplier,
        Supplier<EntityType<?>> desiredUnitTypeSupplier
    ) {
        super(hive);
        this.baseUnitTypeSupplier = baseUnitTypeSupplier;
        this.desiredUnitTypeSupplier = desiredUnitTypeSupplier;
    }

    @Override
    public void run() {
        balanceReserveUnits();
        balanceLoadedUnits();
    }

    private void balanceReserveUnits() {
        var reserveManager = hive.getReserveManager();
        var reserveXenomorphCount = reserveManager.getCountMatching(XENOMORPH_PREDICATE);
        var baseUnitType = baseUnitTypeSupplier.get();
        var desiredUnitType = desiredUnitTypeSupplier.get();
        var currentBaseUnitCount = reserveManager.getCount(baseUnitType);
        var currentDesiredUnitCount = reserveManager.getCount(desiredUnitType);

        var desiredUnitCount = computeDesiredUnitCount(reserveXenomorphCount, currentDesiredUnitCount);
        desiredUnitCount = Math.min(currentBaseUnitCount, desiredUnitCount);

        if (desiredUnitCount == 0) {
            return;
        }

        reserveManager.add(baseUnitType, -desiredUnitCount);
        reserveManager.add(desiredUnitType, desiredUnitCount);
    }

    private void balanceLoadedUnits() {
        var loadedByType = hive.getFactionData().getLoadedMembersByType();
        var xenomorphCount = hive.getFactionData().getLoadedMemberCount(XENOMORPH_PREDICATE);
        var baseEntityType = baseUnitTypeSupplier.get();
        var desiredEntityType = desiredUnitTypeSupplier.get();
        var baseUuids = loadedByType.getOrDefault(baseEntityType, Set.of());
        var baseUnitCount = baseUuids.size();
        var desiredUuids = loadedByType.getOrDefault(desiredEntityType, Set.of());
        var currentDesiredUnitCount = desiredUuids.size();

        var desiredUnitCount = computeDesiredUnitCount(xenomorphCount, currentDesiredUnitCount);

        if (desiredUnitCount == 0) {
            return;
        }

        var uuidsToGrow = baseUuids.stream()
            .limit(desiredUnitCount)
            .toList();

        var loadedMembers = hive.getLoadedMembers();

        for (var uuid : uuidsToGrow) {
            loadedMembers.stream()
                .filter(entity -> entity.getUUID().equals(uuid) && entity instanceof Xenomorph)
                .findFirst()
                .ifPresent(entity -> growXenomorph((Xenomorph) entity));
        }
    }

    private int computeDesiredUnitCount(int xenomorphHiveMemberCount, int desiredUnitCount) {
        var hiveMembersRequiredForPraetorian = AlienPropertyAccess.INSTANCE.getOrThrow(
            AlienProperties.Hive.MEMBERS_REQUIRED_FOR_PRAETORIAN
        );
        var maxPraetorianCount = AlienPropertyAccess.INSTANCE.getOrThrow(AlienProperties.Hive.MAX_PRAETORIAN_COUNT);

        return hiveMembersRequiredForPraetorian > 0
            ? Math.max(
                0,
                Math.clamp(xenomorphHiveMemberCount / hiveMembersRequiredForPraetorian, 0, maxPraetorianCount) - desiredUnitCount
            )
            : 0;
    }
}

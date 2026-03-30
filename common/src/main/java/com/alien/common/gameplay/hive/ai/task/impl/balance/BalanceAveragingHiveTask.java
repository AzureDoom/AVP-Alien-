package com.alien.common.gameplay.hive.ai.task.impl.balance;

import com.alien.common.gameplay.entity.living.alien.xenomorph.Xenomorph;
import com.alien.common.gameplay.hive.Hive;
import net.minecraft.world.entity.EntityType;

import java.util.Set;
import java.util.function.Supplier;

public class BalanceAveragingHiveTask extends BalanceHiveTask {

    private final Supplier<EntityType<?>> baseUnitTypeSupplier;

    private final Supplier<EntityType<?>> desiredUnitTypeSupplier;

    public BalanceAveragingHiveTask(
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
        var baseUnitType = baseUnitTypeSupplier.get();
        var desiredUnitType = desiredUnitTypeSupplier.get();
        var baseUnitCount = reserveManager.getCount(baseUnitType);
        var currentDesiredUnitCount = reserveManager.getCount(desiredUnitType);

        var desiredUnitCount = computeDesiredUnitCount(baseUnitCount, currentDesiredUnitCount);

        if (desiredUnitCount == 0) {
            return;
        }

        reserveManager.add(baseUnitType, -desiredUnitCount);
        reserveManager.add(desiredUnitType, desiredUnitCount);
    }

    private void balanceLoadedUnits() {
        var loadedByType = hive.getFactionData().getLoadedMembersByType();
        var baseEntityType = baseUnitTypeSupplier.get();
        var desiredEntityType = desiredUnitTypeSupplier.get();
        var baseUuids = loadedByType.getOrDefault(baseEntityType, Set.of());
        var baseUnitCount = baseUuids.size();
        var desiredUuids = loadedByType.getOrDefault(desiredEntityType, Set.of());

        var desiredUnitCount = computeDesiredUnitCount(baseUnitCount, desiredUuids.size());

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

    private int computeDesiredUnitCount(int baseUnitCount, int desiredUnitCount) {
        return Math.max(0, (baseUnitCount - desiredUnitCount) / 2);
    }
}

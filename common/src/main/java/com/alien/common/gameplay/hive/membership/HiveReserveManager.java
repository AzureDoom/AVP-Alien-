package com.alien.common.gameplay.hive.membership;

import com.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.burster.Burster;
import com.alien.common.gameplay.entity.living.alien.xenomorph.drone.Drone;
import com.alien.common.gameplay.entity.living.alien.xenomorph.runner.Runner;
import com.alien.common.gameplay.hive.HiveSpaceManager;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.codec.v1.BLibCodecs;
import com.blib.api.common.entity.v1.EntityReserves;
import com.blib.api.common.nbt.v1.model.NBTSerializable;
import com.blib.api.common.spatial.v1.block.BlockPosVec3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class HiveReserveManager implements NBTSerializable {

    private static final String NBT_HIVE_MEMBER_RESERVES = "hiveMemberReserves";

    private static final int FIVE_MINUTES_IN_TICKS = 5 * 60 * 20;

    private final EntityReserves hiveMemberReserves;

    public HiveReserveManager() {
        this.hiveMemberReserves = new EntityReserves();
    }

    public void tick(
        int hiveAgeInTicks,
        AlienVariant variant,
        RandomSource randomSource,
        HiveSpaceManager spaceManager,
        Collection<? extends Entity> loadedMembers
    ) {
        if (hiveAgeInTicks % FIVE_MINUTES_IN_TICKS != 0) {
            return;
        }

        var warriorLayer = HiveSpaceManager.HiveLayer.WARRIOR.getSphereLayer();

        var numberOfXenomorphsInOuterEdges = (int) loadedMembers.stream()
            .filter(
                entity -> entity.getType().is(AlienEntityTypeTags.XENOMORPHS)
                    && !spaceManager.isWithinLayerOrBelow(warriorLayer, new BlockPosVec3(entity.blockPosition()))
            )
            .count();

        if (numberOfXenomorphsInOuterEdges <= 0) {
            return;
        }

        var half = numberOfXenomorphsInOuterEdges / 2;
        var remainder = numberOfXenomorphsInOuterEdges % 2;
        var droneType = Drone.getType(variant);
        var runnerType = Runner.getType(variant);
        var bursterType = Burster.getType(variant);

        hiveMemberReserves.add(droneType, half);
        hiveMemberReserves.add(runnerType, half);
        hiveMemberReserves.add(bursterType, half);

        if (remainder > 0) {
            var extraType = randomSource.nextBoolean() ? droneType : runnerType;
            hiveMemberReserves.add(extraType, 1);
        }
    }

    public boolean canSpawn(EntityType<?> entityType) {
        return hiveMemberReserves.getCount(entityType) > 0;
    }

    public void add(EntityType<?> entityType, int count) {
        hiveMemberReserves.add(entityType, count);
    }

    public int getCount(EntityType<?> entityType) {
        return hiveMemberReserves.getCount(entityType);
    }

    public int getCountMatching(Predicate<EntityType<?>> predicate) {
        return hiveMemberReserves.getCountMatching(predicate);
    }

    public List<EntityType<?>> getAvailableEntityTypes() {
        return hiveMemberReserves.getAvailableEntityTypes();
    }

    @Override
    public void load(CompoundTag compoundTag) {
        EntityReserves.CODEC.decode(BLibCodecs.Schema.NBT, compoundTag.getCompound(NBT_HIVE_MEMBER_RESERVES))
            .inspectErr(tag -> Alien.LOGGER.error("Failed to load tag '{}'. Tag: {}", NBT_HIVE_MEMBER_RESERVES, tag))
            .ifOk(loadedEntityReserves -> hiveMemberReserves.putAll(loadedEntityReserves.getBackingMap()));
    }

    @Override
    public void save(CompoundTag compoundTag) {
        compoundTag.put(NBT_HIVE_MEMBER_RESERVES, EntityReserves.CODEC.encode(BLibCodecs.Schema.NBT, hiveMemberReserves));
    }
}

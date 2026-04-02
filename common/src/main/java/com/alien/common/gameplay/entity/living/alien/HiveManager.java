package com.alien.common.gameplay.entity.living.alien;

import com.alien.common.gameplay.hive.Hive;
import com.alien.common.gameplay.hive.HiveRegistry;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.nbt.v1.model.NBTSerializable;
import com.just.core.functional.option.Option;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HiveManager implements NBTSerializable {

    private static final String HIVE_FACTION_ID_KEY = "HiveFactionId";

    private final Alien alien;

    private Option<ResourceLocation> hiveFactionId;

    public HiveManager(Alien alien) {
        this.alien = alien;
        this.hiveFactionId = Option.none();
    }

    public void tick() {
        var level = alien.level();

        if (level.isClientSide) {
            return;
        }

        if (hiveFactionId.isNone() && alien.tickCount % (20 * 10) == 0) {
            tryFindOrCreateHive();
        }

        hiveFactionId.andThen(id -> Option.ofNullable(HiveRegistry.INSTANCE.getHive(id)))
            .ifSome(this::tickWithHive);

        hiveFactionId.ifSome(id -> {
            var hive = HiveRegistry.INSTANCE.getHive(id);

            if (hive == null || !hive.getMembership().hasMember(com.blib.api.common.faction.v1.FactionMember.entity(alien))) {
                hiveFactionId = Option.none();
            }
        });
    }

    private void tryFindOrCreateHive() {
        if (!(alien.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        var dimension = serverLevel.dimension();
        var nearestHive = HiveRegistry.INSTANCE.findNearestHive(
            alien.blockPosition(),
            dimension,
            hive -> Objects.equals(alien.getVariant(), hive.getVariant())
        );

        if (nearestHive != null) {
            var joinedSuccessfully = tryJoinHive(nearestHive);

            if (!joinedSuccessfully) {
                tryCreateAndAssignHive(nearestHive);
            }
        } else {
            tryCreateAndAssignHive(null);
        }
    }

    private void tickWithHive(Hive hive) {
        if (!hive.isAlive() || !Objects.equals(alien.getVariant(), hive.getVariant())) {
            hive.removeHiveMember(alien);
            this.hiveFactionId = Option.none();
            return;
        }

        if (alien.tickCount % 20 == 0) {
            hive.ping(alien);

            if (hive.getLeadershipManager().isLeader(alien)) {
                hive.moveCenter(alien.blockPosition());
            }
        }
    }

    public boolean tryJoinHive(Hive hive) {
        var joinedSuccessfully = hive.requestToJoin(alien);

        if (joinedSuccessfully) {
            this.hiveFactionId = Option.some(hive.getFactionId());
        }

        return joinedSuccessfully;
    }

    private void tryCreateAndAssignHive(@Nullable Hive nearestHive) {
        if (!alien.getType().is(AlienEntityTypeTags.XENOMORPHS)) {
            return;
        }

        if (nearestHive != null && nearestHive.getSpaceManager().isEntityWithinHiveBuffer(alien)) {
            return;
        }

        if (!(alien.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        var newHive = HiveRegistry.INSTANCE.createHive(
            serverLevel.getServer(),
            alien.blockPosition(),
            serverLevel.dimension(),
            alien.getVariant()
        );

        newHive.requestToJoin(alien);
        newHive.getLeadershipManager().setLeaderId(alien.getUUID());
        this.hiveFactionId = Option.some(newHive.getFactionId());
    }

    @Override
    public void load(CompoundTag compoundTag) {
        if (!compoundTag.contains(HIVE_FACTION_ID_KEY)) {
            return;
        }

        var factionIdString = compoundTag.getString(HIVE_FACTION_ID_KEY);
        var factionId = ResourceLocation.tryParse(factionIdString);

        if (factionId != null && HiveRegistry.INSTANCE.getHive(factionId) != null) {
            this.hiveFactionId = Option.some(factionId);
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        hiveFactionId.ifSome(id -> compoundTag.putString(HIVE_FACTION_ID_KEY, id.toString()));
    }

    public Option<Hive> hive() {
        return hiveFactionId.andThen(id -> Option.ofNullable(HiveRegistry.INSTANCE.getHive(id)));
    }

    public Option<ResourceLocation> signature() {
        return hiveFactionId;
    }
}

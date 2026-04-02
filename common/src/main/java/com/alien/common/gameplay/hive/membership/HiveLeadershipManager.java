package com.alien.common.gameplay.hive.membership;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.blib.api.common.faction.v1.FactionMember;
import com.blib.api.common.faction.v1.FactionMembership;
import com.blib.api.common.nbt.v1.CompoundTagUtil;
import com.blib.api.common.nbt.v1.model.NBTSerializable;
import com.just.core.functional.option.Option;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class HiveLeadershipManager implements NBTSerializable {

    private static final String LEADER_ID_KEY = "HiveLeaderId";

    private Option<UUID> leaderIdOption;

    public HiveLeadershipManager() {
        this.leaderIdOption = Option.none();
    }

    public void tick(FactionMembership membership) {
        var leaderId = getLeaderIdOrNull();

        if (leaderId != null && !membership.hasMember(FactionMember.entity(leaderId))) {
            setLeaderId(null);
        }
    }

    public void removeLeadership(@NotNull Entity entity) {
        removeLeadership(entity.getUUID());
    }

    public void removeLeadership(@NotNull UUID uuid) {
        if (leaderIdOption.contains(uuid)) {
            // If the entity being removed is the leader, then set the leader ID to none.
            this.leaderIdOption = Option.none();
        }
    }

    public boolean isLeader(Entity entity) {
        return leaderIdOption.contains(entity.getUUID());
    }

    public @Nullable UUID getLeaderIdOrNull() {
        return leaderIdOption.unwrapOr(null);
    }

    public Option<Alien> getLeader(MinecraftServer server) {
        return Option.ofNullable(getLeaderOrNull(server));
    }

    public @Nullable Alien getLeaderOrNull(MinecraftServer server) {
        return leaderIdOption.map(leaderId -> {
            for (var level : server.getAllLevels()) {
                var entity = level.getEntity(leaderId);

                if (entity instanceof Alien alien) {
                    return alien;
                }
            }

            return null;
        }).unwrapOr(null);
    }

    public void setLeaderId(@Nullable UUID id) {
        this.leaderIdOption = Option.ofNullable(id);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        this.leaderIdOption = Option.ofNullable(CompoundTagUtil.getUUIDOrNull(compoundTag, LEADER_ID_KEY));
    }

    @Override
    public void save(CompoundTag compoundTag) {
        leaderIdOption.ifSome(leaderId -> compoundTag.putUUID(LEADER_ID_KEY, leaderId));
    }
}

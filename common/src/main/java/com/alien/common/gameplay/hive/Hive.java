package com.alien.common.gameplay.hive;

import com.alien.common.gameplay.entity.living.alien.Alien;
import com.alien.common.gameplay.hive.ai.task.Task;
import com.alien.common.gameplay.hive.ai.task.impl.MergeWithNearbyHiveTask;
import com.alien.common.gameplay.hive.ai.task.impl.PickBestLeaderTask;
import com.alien.common.gameplay.hive.membership.HiveLeadershipManager;
import com.alien.common.gameplay.hive.membership.HiveReserveManager;
import com.alien.common.gameplay.hive.vent.HiveVentManager;
import com.alien.common.gameplay.level.saveddata.QueenSpawnChunkData;
import com.alien.common.model.alien.variant.AlienVariant;
import com.alien.common.property.AlienProperties;
import com.alien.common.property.AlienPropertyAccess;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.faction.v1.FactionMember;
import com.blib.api.common.faction.v1.FactionMembership;
import com.blib.api.common.spatial.v1.chunk.ChunkPosUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hive {

    private final ResourceLocation factionId;

    private final FactionMembership membership;

    private final HiveFactionData factionData;

    private final MinecraftServer server;

    private final HiveBossBarManager bossBarManager;

    private final HiveSpaceManager spaceManager;

    private final HiveVentManager ventManager;

    private final RandomSource randomSource;

    private final List<Task> tasks;

    public Hive(
        MinecraftServer server,
        ResourceLocation factionId,
        FactionMembership membership,
        HiveFactionData factionData
    ) {
        this.factionId = factionId;
        this.membership = membership;
        this.factionData = factionData;
        this.server = server;
        this.bossBarManager = new HiveBossBarManager(this);
        this.randomSource = RandomSource.create();
        this.spaceManager = new HiveSpaceManager(this);
        this.ventManager = new HiveVentManager();
        this.tasks = new ArrayList<>();

        tasks.add(new PickBestLeaderTask(this));
        tasks.add(new MergeWithNearbyHiveTask(this));
    }

    public void tick() {
        if (!isActive()) {
            return;
        }

        var leadershipManager = factionData.getLeadershipManager();
        var reserveManager = factionData.getReserveManager();

        bossBarManager.tick();
        leadershipManager.tick(membership);
        reserveManager.tick(
            factionData.getAgeInTicks(),
            factionData.getVariant(),
            randomSource,
            spaceManager,
            getLoadedMembers()
        );

        tasks.stream()
            .filter(Task::canRun)
            .forEach(Task::run);

        if (!hasXenomorphs()) {
            remove(HiveRemovalReason.KILLED);
        }

        factionData.incrementAge();
    }

    public boolean requestToJoin(Entity requestingEntity) {
        if (
            !(requestingEntity instanceof Alien alien)
                || !Objects.equals(alien.getVariant(), factionData.getVariant())
        ) {
            return false;
        }

        if (!spaceManager.isEntityLeashedToHive(requestingEntity)) {
            return false;
        }

        membership.addEntity(requestingEntity);

        return true;
    }

    public void ping(@NotNull Entity entity) {
        if (
            !entity.isAlive()
                || (membership.hasMember(FactionMember.entity(entity))
                    && !spaceManager.isEntityLeashedToHive(entity))
        ) {
            removeHiveMember(entity);
            return;
        }

        membership.addEntity(entity);
    }

    public void removeHiveMember(@NotNull Entity entity) {
        factionData.getLeadershipManager().removeLeadership(entity);
        membership.removeEntity(entity.getUUID());
    }

    public boolean isActive() {
        return bossBarManager.isTrackingPlayers() || isChunkLoaded();
    }

    public boolean isAlive() {
        return factionData.getRemovalReason() == null;
    }

    public boolean hasXenomorphs() {
        return factionData.hasMemberMatching(
            entityType -> entityType.is(AlienEntityTypeTags.XENOMORPHS)
        );
    }

    public boolean isAngry() {
        return bossBarManager.isTrackingPlayers();
    }

    public void remove(HiveRemovalReason removalReason) {
        factionData.setRemovalReason(removalReason);
    }

    public void onRemove() {
        bossBarManager.onHiveRemoved();

        var level = server.getLevel(factionData.getDimension());

        if (level != null && level.getDifficulty() != Difficulty.PEACEFUL) {
            QueenSpawnChunkData.getOrCreate(level)
                .ifSome(queenSpawnChunkData -> {
                    var chunkRadiusToBlacklist = AlienPropertyAccess.INSTANCE.getOrThrow(
                        AlienProperties.Hive.MINIMUM_DISTANCE_BETWEEN_NATURAL_QUEEN_SPAWNS_IN_CHUNKS
                    );
                    var nearbyChunkPositions = ChunkPosUtil.getChunksAround(centerPosition(), chunkRadiusToBlacklist);

                    nearbyChunkPositions.forEach(queenSpawnChunkData::addChunkToBlacklist);
                });
        }
    }

    public void moveCenter(BlockPos newCenterPos) {
        factionData.setCenterPos(newCenterPos);
    }

    public List<Entity> getLoadedMembers() {
        var loadedMembers = new ArrayList<Entity>();

        for (var member : membership.getMembers()) {
            if (member instanceof FactionMember.Entity(var uuid)) {
                for (var level : server.getAllLevels()) {
                    var entity = level.getEntity(uuid);

                    if (entity != null) {
                        loadedMembers.add(entity);
                        break;
                    }
                }
            }
        }

        return loadedMembers;
    }

    private boolean isChunkLoaded() {
        var level = server.getLevel(factionData.getDimension());

        if (level == null) {
            return false;
        }

        var center = factionData.getCenterPos();

        return level.getChunkSource().getChunkNow(center.getX() >> 4, center.getZ() >> 4) != null;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public ResourceLocation getFactionId() {
        return factionId;
    }

    public FactionMembership getMembership() {
        return membership;
    }

    public HiveFactionData getFactionData() {
        return factionData;
    }

    public HiveBossBarManager getBossBarManager() {
        return bossBarManager;
    }

    public HiveLeadershipManager getLeadershipManager() {
        return factionData.getLeadershipManager();
    }

    public HiveReserveManager getReserveManager() {
        return factionData.getReserveManager();
    }

    public RandomSource getRandom() {
        return randomSource;
    }

    public HiveSpaceManager getSpaceManager() {
        return spaceManager;
    }

    public HiveVentManager getVentManager() {
        return ventManager;
    }

    public AlienVariant getVariant() {
        return factionData.getVariant();
    }

    public void setVariant(AlienVariant variant) {
        factionData.setVariant(variant);
    }

    public int ageInTicks() {
        return factionData.getAgeInTicks();
    }

    public BlockPos centerPosition() {
        return factionData.getCenterPos();
    }
}

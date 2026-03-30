package com.alien.common.gameplay.hive;

import com.alien.common.gameplay.hive.membership.HiveLeadershipManager;
import com.alien.common.gameplay.hive.membership.HiveReserveManager;
import com.alien.common.model.alien.variant.AlienVariant;
import com.blib.api.common.faction.v1.FactionData;
import com.blib.api.common.faction.v1.FactionMember;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class HiveFactionData extends FactionData {

    private static final String NBT_AGE_IN_TICKS = "AgeInTicks";

    private static final String NBT_CENTER_POS = "CenterPos";

    private static final String NBT_DIMENSION = "Dimension";

    private static final String NBT_VARIANT_ID = "VariantId";

    private static final String NBT_REMOVAL_REASON = "RemovalReason";

    private static final String NBT_ALL_MEMBERS_BY_TYPE = "AllMembersByType";

    private static final AlienVariant DEFAULT_VARIANT = AlienVariant.NORMAL;

    private final HiveLeadershipManager leadershipManager;

    private final HiveReserveManager reserveManager;

    private final Map<EntityType<?>, Set<UUID>> allMembersByType;

    private final Map<EntityType<?>, Set<UUID>> loadedMembersByType;

    private BlockPos centerPos;

    private ResourceKey<Level> dimension;

    private int ageInTicks;

    private AlienVariant variant;

    private @Nullable HiveRemovalReason removalReason;

    public HiveFactionData() {
        this.leadershipManager = new HiveLeadershipManager();
        this.reserveManager = new HiveReserveManager();
        this.allMembersByType = new HashMap<>();
        this.loadedMembersByType = new HashMap<>();
        this.centerPos = BlockPos.ZERO;
        this.dimension = Level.OVERWORLD;
        this.ageInTicks = 0;
        this.variant = DEFAULT_VARIANT;
    }

    @Override
    public void onMemberAdded(FactionMember member) {
        markDirty();
    }

    @Override
    public void onMemberAdded(FactionMember member, Entity entity) {
        allMembersByType
            .computeIfAbsent(entity.getType(), $ -> new HashSet<>())
            .add(entity.getUUID());

        markDirty();
    }

    @Override
    public void onMemberRemoved(FactionMember member) {
        if (member instanceof FactionMember.Entity(var uuid)) {
            removeFromMap(allMembersByType, uuid);
            removeFromMap(loadedMembersByType, uuid);
        }

        markDirty();
    }

    @Override
    public void onMemberLoaded(Entity entity) {
        loadedMembersByType
            .computeIfAbsent(entity.getType(), $ -> new HashSet<>())
            .add(entity.getUUID());
    }

    @Override
    public void onMemberUnloaded(Entity entity) {
        removeFromMap(loadedMembersByType, entity.getType(), entity.getUUID());
    }

    public void transferMemberTypes(HiveFactionData target, Set<UUID> uuids) {
        for (var entry : allMembersByType.entrySet()) {
            for (var uuid : entry.getValue()) {
                if (uuids.contains(uuid)) {
                    target.allMembersByType
                        .computeIfAbsent(entry.getKey(), $ -> new HashSet<>())
                        .add(uuid);
                }
            }
        }

        target.markDirty();
    }

    private void removeFromMap(Map<EntityType<?>, Set<UUID>> map, UUID uuid) {
        var iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            entry.getValue().remove(uuid);

            if (entry.getValue().isEmpty()) {
                iterator.remove();
            }
        }
    }

    private void removeFromMap(Map<EntityType<?>, Set<UUID>> map, EntityType<?> entityType, UUID uuid) {
        var uuids = map.get(entityType);

        if (uuids != null) {
            uuids.remove(uuid);

            if (uuids.isEmpty()) {
                map.remove(entityType);
            }
        }
    }

    public boolean hasMemberOfType(EntityType<?> entityType) {
        var uuids = allMembersByType.get(entityType);
        return uuids != null && !uuids.isEmpty();
    }

    public int getMemberCountOfType(EntityType<?> entityType) {
        var uuids = allMembersByType.get(entityType);
        return uuids == null ? 0 : uuids.size();
    }

    public int getMemberCountMatching(Predicate<EntityType<?>> predicate) {
        var count = 0;

        for (var entry : allMembersByType.entrySet()) {
            if (predicate.test(entry.getKey())) {
                count += entry.getValue().size();
            }
        }

        return count;
    }

    public boolean hasMemberMatching(Predicate<EntityType<?>> predicate) {
        for (var entry : allMembersByType.entrySet()) {
            if (predicate.test(entry.getKey()) && !entry.getValue().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public Map<EntityType<?>, Set<UUID>> getAllMembersByType() {
        return allMembersByType;
    }

    public int getLoadedMemberCount(Predicate<EntityType<?>> predicate) {
        var count = 0;

        for (var entry : loadedMembersByType.entrySet()) {
            if (predicate.test(entry.getKey())) {
                count += entry.getValue().size();
            }
        }

        return count;
    }

    public boolean hasLoadedXenomorphs(Predicate<EntityType<?>> xenomorphPredicate) {
        for (var entry : loadedMembersByType.entrySet()) {
            if (xenomorphPredicate.test(entry.getKey()) && !entry.getValue().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public Map<EntityType<?>, Set<UUID>> getLoadedMembersByType() {
        return loadedMembersByType;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        leadershipManager.load(compoundTag);
        reserveManager.load(compoundTag);

        this.ageInTicks = compoundTag.getInt(NBT_AGE_IN_TICKS);

        if (compoundTag.contains(NBT_CENTER_POS)) {
            var centerPosComponents = compoundTag.getIntArray(NBT_CENTER_POS);
            this.centerPos = new BlockPos(centerPosComponents[0], centerPosComponents[1], centerPosComponents[2]);
        }

        if (compoundTag.contains(NBT_DIMENSION)) {
            var dimensionString = compoundTag.getString(NBT_DIMENSION);
            this.dimension = ResourceKey.create(
                net.minecraft.core.registries.Registries.DIMENSION,
                net.minecraft.resources.ResourceLocation.parse(dimensionString)
            );
        }

        if (compoundTag.contains(NBT_VARIANT_ID)) {
            this.variant = AlienVariant.getById(compoundTag.getByte(NBT_VARIANT_ID)).unwrapOr(DEFAULT_VARIANT);
        }

        if (compoundTag.contains(NBT_REMOVAL_REASON)) {
            this.removalReason = HiveRemovalReason.values()[compoundTag.getByte(NBT_REMOVAL_REASON)];
        }

        if (compoundTag.contains(NBT_ALL_MEMBERS_BY_TYPE)) {
            loadAllMembersByType(compoundTag.getCompound(NBT_ALL_MEMBERS_BY_TYPE));
        }
    }

    @Override
    public void save(CompoundTag compoundTag) {
        leadershipManager.save(compoundTag);
        reserveManager.save(compoundTag);

        compoundTag.putInt(NBT_AGE_IN_TICKS, ageInTicks);

        var centerPosComponents = new int[] { centerPos.getX(), centerPos.getY(), centerPos.getZ() };
        compoundTag.putIntArray(NBT_CENTER_POS, centerPosComponents);

        compoundTag.putString(NBT_DIMENSION, dimension.location().toString());
        compoundTag.putByte(NBT_VARIANT_ID, (byte) variant.getId());

        if (removalReason != null) {
            compoundTag.putByte(NBT_REMOVAL_REASON, (byte) removalReason.ordinal());
        }

        compoundTag.put(NBT_ALL_MEMBERS_BY_TYPE, saveAllMembersByType());
    }

    private void loadAllMembersByType(CompoundTag tag) {
        allMembersByType.clear();

        for (var key : tag.getAllKeys()) {
            var entityType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(key));

            if (entityType == null) {
                continue;
            }

            var uuidArray = tag.getIntArray(key);
            var uuids = new HashSet<UUID>();

            for (var i = 0; i + 3 < uuidArray.length; i += 4) {
                var msb = ((long) uuidArray[i] << 32) | (uuidArray[i + 1] & 0xFFFFFFFFL);
                var lsb = ((long) uuidArray[i + 2] << 32) | (uuidArray[i + 3] & 0xFFFFFFFFL);
                uuids.add(new UUID(msb, lsb));
            }

            if (!uuids.isEmpty()) {
                allMembersByType.put(entityType, uuids);
            }
        }
    }

    private CompoundTag saveAllMembersByType() {
        var tag = new CompoundTag();

        for (var entry : allMembersByType.entrySet()) {
            var typeId = BuiltInRegistries.ENTITY_TYPE.getKey(entry.getKey());
            var uuids = entry.getValue();
            var uuidArray = new int[uuids.size() * 4];
            var index = 0;

            for (var uuid : uuids) {
                uuidArray[index++] = (int) (uuid.getMostSignificantBits() >> 32);
                uuidArray[index++] = (int) uuid.getMostSignificantBits();
                uuidArray[index++] = (int) (uuid.getLeastSignificantBits() >> 32);
                uuidArray[index++] = (int) uuid.getLeastSignificantBits();
            }

            tag.putIntArray(typeId.toString(), uuidArray);
        }

        return tag;
    }

    public BlockPos getCenterPos() {
        return centerPos;
    }

    public void setCenterPos(BlockPos centerPos) {
        this.centerPos = centerPos;
        markDirty();
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public void setDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
        markDirty();
    }

    public int getAgeInTicks() {
        return ageInTicks;
    }

    public void incrementAge() {
        this.ageInTicks++;
    }

    public AlienVariant getVariant() {
        return variant;
    }

    public void setVariant(AlienVariant variant) {
        this.variant = variant;
        markDirty();
    }

    public @Nullable HiveRemovalReason getRemovalReason() {
        return removalReason;
    }

    public void setRemovalReason(@Nullable HiveRemovalReason removalReason) {
        this.removalReason = removalReason;
        markDirty();
    }

    public HiveLeadershipManager getLeadershipManager() {
        return leadershipManager;
    }

    public HiveReserveManager getReserveManager() {
        return reserveManager;
    }
}

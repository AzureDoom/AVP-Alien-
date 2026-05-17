package com.alien.common.gameplay.hive2.convoy;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.blib.api.common.codec.v1.BLibCodecs;
import com.blib.api.common.entity.v1.EntityReserves;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * NBT serialization for the sealed {@link Convoy} interface. Uses a discriminator string ({@code "type"}) to dispatch
 * on subtype. Phase 8 ships the {@code "reinforcement"} discriminator only — Phase 8b will add {@code "migration"} and
 * {@code "raid"}.
 * <p>
 * Saves a {@link ListTag} of compound tags, one per convoy.
 */
public final class ConvoyCodec {

    private static final String NBT_TYPE = "Type";

    private static final String NBT_ID = "Id";

    private static final String NBT_LINEAGE_FACTION_ID = "LineageFactionId";

    private static final String NBT_DIMENSION = "Dimension";

    private static final String NBT_SOURCE_LOCATION_ID = "SourceLocationId";

    private static final String NBT_DESTINATION_LOCATION_ID = "DestinationLocationId";

    private static final String NBT_CURRENT_POS = "CurrentPos";

    private static final String NBT_DESTINATION_POS = "DestinationPos";

    private static final String NBT_COMPOSITION = "Composition";

    private static final String NBT_DISPATCHED_TICK = "DispatchedTick";

    private static final String TYPE_REINFORCEMENT = "reinforcement";

    private static final String TYPE_MIGRATION = "migration";

    private static final String TYPE_RAID = "raid";

    private static final String NBT_BIOMASS_PAYLOAD = "BiomassPayload";

    private static final String NBT_CARRIES_EMPRESS = "CarriesEmpress";

    private static final String NBT_TARGET_PLAYER_ID = "TargetPlayerId";

    private static final String NBT_LAST_KNOWN_TARGET_POS = "LastKnownTargetPos";

    private static final String NBT_EXPIRES_AT_TICK = "ExpiresAtTick";

    private static final String NBT_MATERIALIZED_MEMBERS = "MaterializedMembers";

    private static final String NBT_WARNING_ISSUED = "WarningIssued";

    private static final String NBT_RETURNING_HOME = "ReturningHome";

    private static final String NBT_RETURN_LOCATION_ID = "ReturnLocationId";

    private static final String NBT_RETURN_POS = "ReturnPos";

    private ConvoyCodec() {}

    public static ListTag saveAll(List<Convoy> convoys) {
        var listTag = new ListTag();
        for (var convoy : convoys) {
            listTag.add(saveOne(convoy));
        }
        return listTag;
    }

    public static List<Convoy> loadAll(ListTag listTag) {
        var convoys = new ArrayList<Convoy>();
        for (var i = 0; i < listTag.size(); i++) {
            var tag = listTag.getCompound(i);
            var convoy = loadOne(tag);
            if (convoy != null) {
                convoys.add(convoy);
            }
        }
        return convoys;
    }

    public static CompoundTag saveOne(Convoy convoy) {
        var tag = new CompoundTag();

        tag.putUUID(NBT_ID, convoy.id().value());
        tag.putString(NBT_LINEAGE_FACTION_ID, convoy.lineageFactionId().toString());
        tag.putString(NBT_DIMENSION, convoy.dimension().location().toString());

        var pos = convoy.currentPos();
        tag.put(NBT_CURRENT_POS, encodeVec3(pos));

        var composition = new CompoundTag();
        composition.put("reserves", EntityReserves.CODEC.encode(BLibCodecs.Schema.NBT, convoy.composition()));
        tag.put(NBT_COMPOSITION, composition);

        tag.putLong(NBT_DISPATCHED_TICK, convoy.dispatchedTick());

        if (convoy instanceof Convoy.Reinforcement reinforcement) {
            tag.putString(NBT_TYPE, TYPE_REINFORCEMENT);
            tag.putString(NBT_SOURCE_LOCATION_ID, reinforcement.sourceLocationId().value().toString());
            tag.putString(NBT_DESTINATION_LOCATION_ID, reinforcement.destinationLocationId().value().toString());
            tag.putIntArray(NBT_DESTINATION_POS, encodeBlockPos(reinforcement.destinationPos()));
        } else if (convoy instanceof Convoy.Migration migration) {
            tag.putString(NBT_TYPE, TYPE_MIGRATION);
            tag.putString(NBT_SOURCE_LOCATION_ID, migration.sourceLocationId().value().toString());
            tag.putString(NBT_DESTINATION_LOCATION_ID, migration.destinationLocationId().value().toString());
            tag.putIntArray(NBT_DESTINATION_POS, encodeBlockPos(migration.destinationPos()));
            tag.putInt(NBT_BIOMASS_PAYLOAD, migration.biomassPayload());
            tag.putBoolean(NBT_CARRIES_EMPRESS, migration.carriesEmpress());
        } else if (convoy instanceof Convoy.Raid raid) {
            tag.putString(NBT_TYPE, TYPE_RAID);
            tag.putString(NBT_SOURCE_LOCATION_ID, raid.sourceLocationId().value().toString());
            tag.putUUID(NBT_TARGET_PLAYER_ID, raid.targetPlayerId());
            tag.putIntArray(NBT_LAST_KNOWN_TARGET_POS, encodeBlockPos(raid.lastKnownTargetPos()));
            tag.putLong(NBT_EXPIRES_AT_TICK, raid.expiresAtTick());
            tag.put(NBT_MATERIALIZED_MEMBERS, encodeMaterializedMembers(raid.materializedMembers()));
            tag.putBoolean(NBT_WARNING_ISSUED, raid.warningIssued());
            tag.putBoolean(NBT_RETURNING_HOME, raid.returningHome());
            if (raid.returnLocationId() != null) {
                tag.putString(NBT_RETURN_LOCATION_ID, raid.returnLocationId().value().toString());
            }
            if (raid.returnPos() != null) {
                tag.putIntArray(NBT_RETURN_POS, encodeBlockPos(raid.returnPos()));
            }
        } else {
            throw new IllegalStateException("Unknown convoy subtype: " + convoy.getClass().getName());
        }

        return tag;
    }

    public static Convoy loadOne(CompoundTag tag) {
        var type = tag.getString(NBT_TYPE);

        var id = new ConvoyId(tag.getUUID(NBT_ID));
        var lineageFactionId = ResourceLocation.parse(tag.getString(NBT_LINEAGE_FACTION_ID));
        var dimension = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString(NBT_DIMENSION)));
        var currentPos = decodeVec3(tag.getList(NBT_CURRENT_POS, Tag.TAG_DOUBLE));
        var dispatchedTick = tag.getLong(NBT_DISPATCHED_TICK);

        var compositionTag = tag.getCompound(NBT_COMPOSITION).getCompound("reserves");
        var composition = new EntityReserves();
        EntityReserves.CODEC.decode(BLibCodecs.Schema.NBT, compositionTag)
            .inspectErr(failure -> Alien.LOGGER.error("Failed to load convoy composition: {}", failure))
            .ifOk(loaded -> composition.putAll(loaded.getBackingMap()));

        return switch (type) {
            case TYPE_REINFORCEMENT -> new Convoy.Reinforcement(
                id,
                lineageFactionId,
                dimension,
                new HiveLocationId(ResourceLocation.parse(tag.getString(NBT_SOURCE_LOCATION_ID))),
                new HiveLocationId(ResourceLocation.parse(tag.getString(NBT_DESTINATION_LOCATION_ID))),
                currentPos,
                decodeBlockPos(tag.getIntArray(NBT_DESTINATION_POS)),
                composition,
                dispatchedTick
            );
            case TYPE_MIGRATION -> new Convoy.Migration(
                id,
                lineageFactionId,
                dimension,
                new HiveLocationId(ResourceLocation.parse(tag.getString(NBT_SOURCE_LOCATION_ID))),
                new HiveLocationId(ResourceLocation.parse(tag.getString(NBT_DESTINATION_LOCATION_ID))),
                currentPos,
                decodeBlockPos(tag.getIntArray(NBT_DESTINATION_POS)),
                composition,
                tag.getInt(NBT_BIOMASS_PAYLOAD),
                tag.getBoolean(NBT_CARRIES_EMPRESS),
                dispatchedTick
            );
            case TYPE_RAID -> new Convoy.Raid(
                id,
                lineageFactionId,
                dimension,
                new HiveLocationId(ResourceLocation.parse(tag.getString(NBT_SOURCE_LOCATION_ID))),
                tag.getUUID(NBT_TARGET_PLAYER_ID),
                currentPos,
                decodeBlockPos(tag.getIntArray(NBT_LAST_KNOWN_TARGET_POS)),
                composition,
                decodeMaterializedMembers(tag.getList(NBT_MATERIALIZED_MEMBERS, Tag.TAG_COMPOUND)),
                tag.getBoolean(NBT_WARNING_ISSUED),
                tag.getBoolean(NBT_RETURNING_HOME),
                tag.contains(NBT_RETURN_LOCATION_ID)
                    ? new HiveLocationId(ResourceLocation.parse(tag.getString(NBT_RETURN_LOCATION_ID)))
                    : null,
                tag.contains(NBT_RETURN_POS) ? decodeBlockPos(tag.getIntArray(NBT_RETURN_POS)) : null,
                dispatchedTick,
                tag.getLong(NBT_EXPIRES_AT_TICK)
            );
            default -> {
                Alien.LOGGER.warn("Unknown convoy type discriminator '{}' — skipping", type);
                yield null;
            }
        };
    }

    private static ListTag encodeVec3(Vec3 vec) {
        var listTag = new ListTag();
        listTag.add(net.minecraft.nbt.DoubleTag.valueOf(vec.x));
        listTag.add(net.minecraft.nbt.DoubleTag.valueOf(vec.y));
        listTag.add(net.minecraft.nbt.DoubleTag.valueOf(vec.z));
        return listTag;
    }

    private static Vec3 decodeVec3(ListTag listTag) {
        if (listTag.size() < 3) {
            return Vec3.ZERO;
        }
        return new Vec3(listTag.getDouble(0), listTag.getDouble(1), listTag.getDouble(2));
    }

    private static int[] encodeBlockPos(BlockPos pos) {
        return new int[] { pos.getX(), pos.getY(), pos.getZ() };
    }

    private static BlockPos decodeBlockPos(int[] arr) {
        if (arr.length < 3) {
            return BlockPos.ZERO;
        }
        return new BlockPos(arr[0], arr[1], arr[2]);
    }

    private static ListTag encodeMaterializedMembers(Map<UUID, net.minecraft.world.entity.EntityType<?>> members) {
        var listTag = new ListTag();
        for (var entry : members.entrySet()) {
            var tag = new CompoundTag();
            tag.putUUID("EntityId", entry.getKey());
            tag.putString("EntityType", BuiltInRegistries.ENTITY_TYPE.getKey(entry.getValue()).toString());
            listTag.add(tag);
        }
        return listTag;
    }

    private static Map<UUID, net.minecraft.world.entity.EntityType<?>> decodeMaterializedMembers(ListTag listTag) {
        var members = new HashMap<UUID, net.minecraft.world.entity.EntityType<?>>();
        for (var i = 0; i < listTag.size(); i++) {
            var tag = listTag.getCompound(i);
            if (!tag.hasUUID("EntityId") || !tag.contains("EntityType")) {
                continue;
            }
            var entityType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(tag.getString("EntityType")));
            members.put(tag.getUUID("EntityId"), entityType);
        }
        return members;
    }
}

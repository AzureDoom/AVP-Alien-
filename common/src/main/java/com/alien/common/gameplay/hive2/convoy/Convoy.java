package com.alien.common.gameplay.hive2.convoy;

import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.blib.api.common.entity.v1.EntityReserves;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A traveling group of xenomorphs moving between locations or hunting a player. Sealed so codecs can dispatch on
 * subtype.
 * <p>
 * Phase 8 ships {@link Reinforcement}. Phase 8b adds {@link Migration} (whole-location evacuation) and {@link Raid}
 * (player-hunting counter-attack).
 * <p>
 * Common semantics across all subtypes:
 * <ul>
 * <li>{@link #composition} — the EntityReserves bag of "who's in the convoy." Decrements on combat losses and refunds
 * to a live hive location on disband when possible.</li>
 * <li>{@link #currentPos} — the abstract Vec3 position. Updated every tick by {@link ConvoyTravel}.</li>
 * <li>{@link #dispatchedTick} — when the convoy was first minted; used by raid expiry (Phase 8b) and just informational
 * for reinforcement.</li>
 * </ul>
 */
public sealed interface Convoy {

    ConvoyId id();

    ResourceLocation lineageFactionId();

    ResourceKey<Level> dimension();

    Vec3 currentPos();

    void setCurrentPos(Vec3 newPos);

    EntityReserves composition();

    long dispatchedTick();

    /**
     * Sister-to-sister rebalancing. The default convoy type. Source has surplus reserves; destination is low. The
     * empress dispatches; the convoy travels through abstract space; on arrival, composition pours into the
     * destination's local reserves.
     * <p>
     * See {@code HIVE_REDESIGN_06_CONVOYS.md} § 4.
     */
    final class Reinforcement implements Convoy {

        private final ConvoyId id;

        private final ResourceLocation lineageFactionId;

        private final ResourceKey<Level> dimension;

        private final HiveLocationId sourceLocationId;

        private final HiveLocationId destinationLocationId;

        private final BlockPos destinationPos;

        private final EntityReserves composition;

        private final long dispatchedTick;

        private Vec3 currentPos;

        public Reinforcement(
            ConvoyId id,
            ResourceLocation lineageFactionId,
            ResourceKey<Level> dimension,
            HiveLocationId sourceLocationId,
            HiveLocationId destinationLocationId,
            Vec3 currentPos,
            BlockPos destinationPos,
            EntityReserves composition,
            long dispatchedTick
        ) {
            this.id = id;
            this.lineageFactionId = lineageFactionId;
            this.dimension = dimension;
            this.sourceLocationId = sourceLocationId;
            this.destinationLocationId = destinationLocationId;
            this.currentPos = currentPos;
            this.destinationPos = destinationPos;
            this.composition = composition;
            this.dispatchedTick = dispatchedTick;
        }

        @Override
        public ConvoyId id() {
            return id;
        }

        @Override
        public ResourceLocation lineageFactionId() {
            return lineageFactionId;
        }

        @Override
        public ResourceKey<Level> dimension() {
            return dimension;
        }

        @Override
        public Vec3 currentPos() {
            return currentPos;
        }

        @Override
        public void setCurrentPos(Vec3 newPos) {
            this.currentPos = newPos;
        }

        @Override
        public EntityReserves composition() {
            return composition;
        }

        @Override
        public long dispatchedTick() {
            return dispatchedTick;
        }

        public HiveLocationId sourceLocationId() {
            return sourceLocationId;
        }

        public HiveLocationId destinationLocationId() {
            return destinationLocationId;
        }

        public BlockPos destinationPos() {
            return destinationPos;
        }
    }

    /**
     * A whole-location evacuation. The source location is doomed; its remaining members and biomass payload are folded
     * into a sister location. Per {@code HIVE_REDESIGN_06_CONVOYS.md} § 5.
     */
    final class Migration implements Convoy {

        private final ConvoyId id;

        private final ResourceLocation lineageFactionId;

        private final ResourceKey<Level> dimension;

        private final HiveLocationId sourceLocationId;

        private final HiveLocationId destinationLocationId;

        private final BlockPos destinationPos;

        private final EntityReserves composition;

        private final int biomassPayload;

        private final boolean carriesEmpress;

        private final long dispatchedTick;

        private Vec3 currentPos;

        public Migration(
            ConvoyId id,
            ResourceLocation lineageFactionId,
            ResourceKey<Level> dimension,
            HiveLocationId sourceLocationId,
            HiveLocationId destinationLocationId,
            Vec3 currentPos,
            BlockPos destinationPos,
            EntityReserves composition,
            int biomassPayload,
            boolean carriesEmpress,
            long dispatchedTick
        ) {
            this.id = id;
            this.lineageFactionId = lineageFactionId;
            this.dimension = dimension;
            this.sourceLocationId = sourceLocationId;
            this.destinationLocationId = destinationLocationId;
            this.currentPos = currentPos;
            this.destinationPos = destinationPos;
            this.composition = composition;
            this.biomassPayload = biomassPayload;
            this.carriesEmpress = carriesEmpress;
            this.dispatchedTick = dispatchedTick;
        }

        @Override
        public ConvoyId id() {
            return id;
        }

        @Override
        public ResourceLocation lineageFactionId() {
            return lineageFactionId;
        }

        @Override
        public ResourceKey<Level> dimension() {
            return dimension;
        }

        @Override
        public Vec3 currentPos() {
            return currentPos;
        }

        @Override
        public void setCurrentPos(Vec3 newPos) {
            this.currentPos = newPos;
        }

        @Override
        public EntityReserves composition() {
            return composition;
        }

        @Override
        public long dispatchedTick() {
            return dispatchedTick;
        }

        public HiveLocationId sourceLocationId() {
            return sourceLocationId;
        }

        public HiveLocationId destinationLocationId() {
            return destinationLocationId;
        }

        public BlockPos destinationPos() {
            return destinationPos;
        }

        public int biomassPayload() {
            return biomassPayload;
        }

        public boolean carriesEmpress() {
            return carriesEmpress;
        }
    }

    /**
     * A counter-attack against a player who recently attacked the lineage. The convoy's destination tracks the target
     * player's position over time (snapshotted into {@link #lastKnownTargetPos} each tick by
     * {@link com.alien.common.gameplay.hive2.tick.LineageConvoyTickTask}).
     * <p>
     * Per {@code HIVE_REDESIGN_06_CONVOYS.md} § 6.
     */
    final class Raid implements Convoy {

        private final ConvoyId id;

        private final ResourceLocation lineageFactionId;

        private final ResourceKey<Level> dimension;

        private final HiveLocationId sourceLocationId;

        private final UUID targetPlayerId;

        private final EntityReserves composition;

        private final Map<UUID, EntityType<?>> materializedMembers;

        private final long dispatchedTick;

        private final long expiresAtTick;

        private Vec3 currentPos;

        private BlockPos lastKnownTargetPos;

        public Raid(
            ConvoyId id,
            ResourceLocation lineageFactionId,
            ResourceKey<Level> dimension,
            HiveLocationId sourceLocationId,
            UUID targetPlayerId,
            Vec3 currentPos,
            BlockPos lastKnownTargetPos,
            EntityReserves composition,
            long dispatchedTick,
            long expiresAtTick
        ) {
            this(
                id,
                lineageFactionId,
                dimension,
                sourceLocationId,
                targetPlayerId,
                currentPos,
                lastKnownTargetPos,
                composition,
                new HashMap<>(),
                dispatchedTick,
                expiresAtTick
            );
        }

        public Raid(
            ConvoyId id,
            ResourceLocation lineageFactionId,
            ResourceKey<Level> dimension,
            HiveLocationId sourceLocationId,
            UUID targetPlayerId,
            Vec3 currentPos,
            BlockPos lastKnownTargetPos,
            EntityReserves composition,
            Map<UUID, EntityType<?>> materializedMembers,
            long dispatchedTick,
            long expiresAtTick
        ) {
            this.id = id;
            this.lineageFactionId = lineageFactionId;
            this.dimension = dimension;
            this.sourceLocationId = sourceLocationId;
            this.targetPlayerId = targetPlayerId;
            this.currentPos = currentPos;
            this.lastKnownTargetPos = lastKnownTargetPos;
            this.composition = composition;
            this.materializedMembers = new HashMap<>(materializedMembers);
            this.dispatchedTick = dispatchedTick;
            this.expiresAtTick = expiresAtTick;
        }

        @Override
        public ConvoyId id() {
            return id;
        }

        @Override
        public ResourceLocation lineageFactionId() {
            return lineageFactionId;
        }

        @Override
        public ResourceKey<Level> dimension() {
            return dimension;
        }

        @Override
        public Vec3 currentPos() {
            return currentPos;
        }

        @Override
        public void setCurrentPos(Vec3 newPos) {
            this.currentPos = newPos;
        }

        @Override
        public EntityReserves composition() {
            return composition;
        }

        public Map<UUID, EntityType<?>> materializedMembers() {
            return materializedMembers;
        }

        public void trackMaterializedMember(UUID memberId, EntityType<?> entityType) {
            materializedMembers.put(memberId, entityType);
        }

        public void untrackMaterializedMember(UUID memberId) {
            materializedMembers.remove(memberId);
        }

        @Override
        public long dispatchedTick() {
            return dispatchedTick;
        }

        public HiveLocationId sourceLocationId() {
            return sourceLocationId;
        }

        public UUID targetPlayerId() {
            return targetPlayerId;
        }

        public BlockPos lastKnownTargetPos() {
            return lastKnownTargetPos;
        }

        public void setLastKnownTargetPos(BlockPos newPos) {
            this.lastKnownTargetPos = newPos;
        }

        public long expiresAtTick() {
            return expiresAtTick;
        }
    }
}

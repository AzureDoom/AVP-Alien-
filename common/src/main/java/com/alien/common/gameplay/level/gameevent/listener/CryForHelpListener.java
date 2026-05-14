package com.alien.common.gameplay.level.gameevent.listener;

import com.alien.Alien;
import com.alien.common.data.AlienVariantTypes;
import com.alien.common.gameplay.block.entity.resin.vent.ResinVentBlockEntity;
import com.alien.common.gameplay.hive2.convoy.InPlacePoolReinforcement;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.spawning.ReserveSpawnUtil;
import com.alien.common.registry.tag.AlienBlockTags;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.spatial.v1.block.BlockPosUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Reacts to a xenomorph's "cry for help" game event by summoning a defender at a vent. In hive2 the responding hive is
 * the {@link HiveLocation} bound to the vent's chunk; defender supply comes from the location's local reserves first,
 * with empress-gated {@link InPlacePoolReinforcement} as a fallback when the local reserves can't satisfy the request.
 */
public class CryForHelpListener implements GameEventListener {

    private static final int MAXIMUM_SUMMONED_XENOMORPHS_PER_LOCATION = 60;

    private final PositionSource positionSource;

    public CryForHelpListener(PositionSource positionSource) {
        this.positionSource = positionSource;
    }

    @Override
    public @NotNull PositionSource getListenerSource() {
        return positionSource;
    }

    @Override
    public @NotNull GameEventListener.DeliveryMode getDeliveryMode() {
        return DeliveryMode.BY_DISTANCE;
    }

    @Override
    public int getListenerRadius() {
        return 16;
    }

    @Override
    public boolean handleGameEvent(
        @NotNull ServerLevel serverLevel,
        @NotNull Holder<GameEvent> holder,
        @NotNull GameEvent.Context context,
        @NotNull Vec3 vec3
    ) {
        var sourceEntity = context.sourceEntity();

        if (
            sourceEntity == null
                // If there is no alien variant type for given source entity OR if there is a cry-for-help event
                // type mismatch...
                || AlienVariantTypes.getFor(sourceEntity)
                    .isNoneOr(alienVariantType -> !holder.is(alienVariantType.cryForHelpEvent()))
        ) {
            return false;
        }

        var blockPos = positionSource.getPosition(serverLevel)
            .map(BlockPos::containing)
            .orElse(null);

        if (blockPos == null) {
            return false;
        }

        var blockEntity = serverLevel.getBlockEntity(blockPos);

        if (
            !(blockEntity instanceof ResinVentBlockEntity vent)
                || vent.getAlienSpawnCooldown().isActive()
        ) {
            return false;
        }

        var location = vent.getBoundLocation();
        if (location == null || !location.isAlive()) {
            return false;
        }

        // Cap concurrent helpers per location.
        var loadedXenomorphCount = location.loadedMembersByType()
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().is(AlienEntityTypeTags.XENOMORPHS))
            .mapToInt(entry -> entry.getValue().size())
            .sum();
        if (loadedXenomorphCount >= MAXIMUM_SUMMONED_XENOMORPHS_PER_LOCATION) {
            return false;
        }

        var lineageFaction = Alien.MOD.factions().get(location.lineageFactionId());
        if (lineageFaction == null || !(lineageFaction.data() instanceof LineageFactionData lineage)) {
            return false;
        }

        var basePos = vent.getBlockPos();
        var freeSpaces = BlockPosUtil.getNeighborsMatching(serverLevel, basePos, blockState -> blockState.is(AlienBlockTags.RESIN_WEBS));

        var spawnPos = freeSpaces.isEmpty()
            ? null
            : freeSpaces.get(sourceEntity.getRandom().nextInt(freeSpaces.size()));

        if (spawnPos == null) {
            return false;
        }

        // Pick a defender type from the location's local reserves first; pool-fallback (empress-gated) below.
        var localReserveTypes = location.localReserves()
            .getAvailableEntityTypes()
            .stream()
            .filter(type -> type.is(AlienEntityTypeTags.ANSWERS_XENOMORPH_CRIES_FOR_HELP))
            .toList();

        if (!localReserveTypes.isEmpty()) {
            var spawnedFromReserves = trySpawnFromReserves(serverLevel, location, sourceEntity, spawnPos, localReserveTypes, vent);
            if (spawnedFromReserves) {
                return true;
            }
        }

        // Fallback: try empress-gated pool reinforcement directly into the vent's spawn pos.
        return tryPoolFallback(serverLevel, location, lineage, sourceEntity, spawnPos, vent);
    }

    private static boolean trySpawnFromReserves(
        ServerLevel level,
        HiveLocation location,
        Entity sourceEntity,
        BlockPos spawnPos,
        List<EntityType<?>> reserveTypes,
        ResinVentBlockEntity vent
    ) {
        var randomType = reserveTypes.get(sourceEntity.getRandom().nextInt(reserveTypes.size()));
        if (!location.localReserves().canSpawn(randomType)) {
            return false;
        }

        var summoned = randomType.spawn(level, spawnPos, MobSpawnType.MOB_SUMMONED);
        if (summoned == null) {
            return false;
        }

        ReserveSpawnUtil.markSpawnedFromReserves(summoned);
        location.localReserves().trySpawn(randomType);
        vent.getAlienSpawnCooldown().reset();
        retargetIfPossible(sourceEntity, summoned);
        return true;
    }

    private static boolean tryPoolFallback(
        ServerLevel level,
        HiveLocation location,
        LineageFactionData lineage,
        Entity sourceEntity,
        BlockPos spawnPos,
        ResinVentBlockEntity vent
    ) {
        if (lineage.empressId() == null) {
            return false;
        }

        var poolTypes = lineage.lineagePool()
            .getAvailableEntityTypes()
            .stream()
            .filter(type -> type.is(AlienEntityTypeTags.ANSWERS_XENOMORPH_CRIES_FOR_HELP))
            .toList();
        if (poolTypes.isEmpty()) {
            return false;
        }

        var randomType = poolTypes.get(sourceEntity.getRandom().nextInt(poolTypes.size()));
        // We can't use InPlacePoolReinforcement directly because it spawns at location.centerPos(); we want the
        // vent's spawnPos. Inline the equivalent.
        if (lineage.lineagePool().getCount(randomType) <= 0) {
            return false;
        }
        var summoned = randomType.spawn(level, spawnPos, MobSpawnType.MOB_SUMMONED);
        if (summoned == null) {
            return false;
        }

        ReserveSpawnUtil.markSpawnedFromReserves(summoned);
        lineage.lineagePool().add(randomType, -1);
        lineage.markDirty();
        vent.getAlienSpawnCooldown().reset();
        retargetIfPossible(sourceEntity, summoned);

        // Reference InPlacePoolReinforcement so the doc-pointer in the class header stays valid for IDE Find Usages.
        @SuppressWarnings("unused")
        var ref = InPlacePoolReinforcement.class;

        return true;
    }

    private static void retargetIfPossible(Entity sourceEntity, Entity summoned) {
        if (sourceEntity instanceof Mob sourceMob && summoned instanceof Mob summonedMob) {
            summonedMob.setTarget(sourceMob.getTarget());
        }
    }
}

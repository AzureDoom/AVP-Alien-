package com.alien.common.gameplay.hive2.convoy;

import com.alien.Alien;
import com.alien.common.gameplay.hive2.config.HiveConfig;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.gameplay.hive2.location.HiveLocationReserves;
import com.alien.common.registry.HiveRecipeRegistry;
import com.alien.common.registry.RaidWaveProfileRegistry;
import com.alien.common.registry.tag.AlienEntityTypeTags;
import com.blib.api.common.entity.v1.EntityReserves;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Raid dispatch — counter-attacks against players who've racked up too many lineage-member kills inside the aggro
 * window.
 * <p>
 * Per {@code HIVE_REDESIGN_06_CONVOYS.md} § 6:
 * <ul>
 * <li>Empress-gated.</li>
 * <li>Triggered when a player has at least {@code raidThresholdKills} kills in the aggro window.</li>
 * <li>Source = the largest qualifying location ({@code claimedChunks ≥ raidMinLocationSizeChunks}) with a
 * harbinger.</li>
 * <li>Per-source cooldown so the same source doesn't spam raids.</li>
 * <li>Composition drained from source reserves using the active datapack raid wave profile.</li>
 * <li>Persists until the target player dies, then returns to a live lineage location when possible.</li>
 * </ul>
 * <p>
 * Phase 8b ships an "admin-trigger only or auto-trigger via kill threshold" path. The full design's complexities
 * (cross-dimension blocking, target-offline camping behaviors mid-flight) are handled by
 * {@link com.alien.common.gameplay.hive2.tick.LineageConvoyTickTask}'s tick-time updater.
 */
public final class RaidDispatch {

    private static final Map<HiveLocationId, Long> lastDispatchTickByLocation = new HashMap<>();

    private RaidDispatch() {}

    public static void clear() {
        lastDispatchTickByLocation.clear();
    }

    public static void scanAndDispatch(MinecraftServer server) {
        var currentTick = server.overworld().getGameTime();
        var config = HiveLocationRegistry.INSTANCE.config();

        for (var factionId : new ArrayList<>(Alien.MOD.factions().getAllIds())) {
            if (!LineageIds.isLineageId(factionId)) {
                continue;
            }
            var faction = Alien.MOD.factions().get(factionId);
            if (faction == null || !(faction.data() instanceof LineageFactionData lineage) || !lineage.isAlive()) {
                continue;
            }
            if (lineage.empressId() == null) {
                continue;
            }
            scanLineage(server, factionId, lineage, currentTick, config);
        }
    }

    /** Admin trigger entry. Forces a raid against {@code targetPlayer} from the lineage's largest location. */
    public static boolean forceRaid(
        MinecraftServer server,
        LineageFactionData lineage,
        ResourceLocation lineageFactionId,
        ServerPlayer targetPlayer
    ) {
        var currentTick = server.overworld().getGameTime();
        var config = HiveLocationRegistry.INSTANCE.config();
        return tryDispatchAgainstPlayer(
            server,
            lineage,
            lineageFactionId,
            targetPlayer.getUUID(),
            targetPlayer,
            currentTick,
            config,
            false,
            false
        );
    }

    private static void scanLineage(
        MinecraftServer server,
        ResourceLocation lineageFactionId,
        LineageFactionData lineage,
        long currentTick,
        HiveConfig config
    ) {
        // Snapshot since recordKillByPlayer mutates lists during iteration via prune.
        var attribution = new HashMap<>(lineage.killAttributionByPlayer());

        for (var entry : attribution.entrySet()) {
            var playerId = entry.getKey();
            var kills = lineage.countRecentKills(playerId, currentTick, config.raidAggroWindowTicks());

            if (kills < config.raidThresholdKills()) {
                continue;
            }

            var targetPlayer = server.getPlayerList().getPlayer(playerId);
            if (targetPlayer == null) {
                continue;
            }
            if (!targetPlayer.level().dimension().equals(lineage.dimension())) {
                continue;
            }

            tryDispatchAgainstPlayer(
                server,
                lineage,
                lineageFactionId,
                playerId,
                targetPlayer,
                currentTick,
                config,
                true,
                true
            );
        }
    }

    private static boolean tryDispatchAgainstPlayer(
        MinecraftServer server,
        LineageFactionData lineage,
        ResourceLocation lineageFactionId,
        UUID playerId,
        ServerPlayer targetPlayer,
        long currentTick,
        HiveConfig config,
        boolean consumeKillAttribution,
        boolean blockExistingTargetRaid
    ) {
        if (blockExistingTargetRaid && hasActiveOutboundRaidAgainst(lineage, playerId)) {
            return false;
        }

        var waveProfile = RaidWaveProfileRegistry.active();
        var source = pickLargestEligibleSource(lineage, currentTick, config, waveProfile);
        if (source == null) {
            return false;
        }

        var composition = drainComposition(source.localReserves(), waveProfile, server.overworld().random);
        if (composition.getCount() < waveProfile.totalSize()) {
            refundComposition(source.localReserves(), composition);
            return false;
        }

        var sourceCenter = new Vec3(
            source.centerPos().getX() + 0.5,
            source.centerPos().getY() + 0.5,
            source.centerPos().getZ() + 0.5
        );

        var raid = new Convoy.Raid(
            ConvoyId.fresh(),
            lineageFactionId,
            source.dimension(),
            source.id(),
            playerId,
            sourceCenter,
            targetPlayer.blockPosition(),
            composition,
            currentTick,
            Long.MAX_VALUE
        );

        lineage.convoys().add(raid);
        if (consumeKillAttribution) {
            lineage.clearKillAttributionForPlayer(playerId);
        }
        lineage.markDirty();
        lastDispatchTickByLocation.put(source.id(), currentTick);

        Alien.LOGGER.info(
            "Hive2: raid dispatched: {} → player {} from source {} ({} members, no expiry)",
            raid.id(),
            playerId,
            source.id(),
            composition.getCount()
        );

        return true;
    }

    private static boolean hasActiveOutboundRaidAgainst(LineageFactionData lineage, UUID playerId) {
        for (var convoy : lineage.convoys()) {
            if (
                convoy instanceof Convoy.Raid raid
                    && !raid.returningHome()
                    && raid.targetPlayerId().equals(playerId)
            ) {
                return true;
            }
        }
        return false;
    }

    private static @Nullable HiveLocation pickLargestEligibleSource(
        LineageFactionData lineage,
        long currentTick,
        HiveConfig config,
        RaidWaveProfile waveProfile
    ) {
        HiveLocation best = null;
        var bestSize = 0;

        for (var location : lineage.locationsById().values()) {
            if (!location.isAlive()) {
                continue;
            }
            if (location.claimedChunks().size() < config.raidMinLocationSizeChunks()) {
                continue;
            }
            if (location.localReserves().getCountMatching(type -> type.is(AlienEntityTypeTags.HARBINGERS)) <= 0) {
                continue;
            }
            if (!hasRaidCapacity(location.localReserves(), waveProfile)) {
                continue;
            }

            var lastDispatch = lastDispatchTickByLocation.get(location.id());
            if (lastDispatch != null && currentTick - lastDispatch < config.perSourceRaidCooldownTicks()) {
                continue;
            }

            if (location.claimedChunks().size() > bestSize) {
                bestSize = location.claimedChunks().size();
                best = location;
            }
        }

        return best;
    }

    private static boolean hasRaidCapacity(HiveLocationReserves reserves, RaidWaveProfile waveProfile) {
        return reserves.getCountMatching(type -> type.is(AlienEntityTypeTags.HARBINGERS)) > 0
            && eligibleNonHarbingerReserveCount(reserves) >= waveProfile.nonHarbingerSize();
    }

    private static EntityReserves drainComposition(
        HiveLocationReserves donorReserves,
        RaidWaveProfile waveProfile,
        RandomSource random
    ) {
        var composition = new EntityReserves();
        var harbingerType = drainOneHarbinger(donorReserves);
        if (harbingerType == null) {
            return composition;
        }

        composition.add(harbingerType, 1);

        for (var i = 0; i < waveProfile.waves().size(); i++) {
            var waveSize = waveProfile.wave(i).size();
            var nonHarbingerSlots = i == Convoy.Raid.WAVE_COUNT - 1 ? waveSize - 1 : waveSize;
            drainWave(donorReserves, composition, waveProfile.wave(i), nonHarbingerSlots, random);
        }

        return composition;
    }

    private static void drainWave(
        HiveLocationReserves donorReserves,
        EntityReserves composition,
        RaidWaveProfile.Wave wave,
        int count,
        RandomSource random
    ) {
        var selectedByPool = new HashMap<Integer, Integer>();
        var inventory = new RaidWaveSelection.Inventory() {
            @Override
            public Iterable<EntityType<?>> availableTypes() {
                return donorReserves.getAvailableEntityTypes();
            }

            @Override
            public int count(EntityType<?> type) {
                return donorReserves.getCount(type);
            }
        };

        for (var i = 0; i < count; i++) {
            var selectedType = RaidWaveSelection.chooseType(
                wave,
                inventory,
                RaidDispatch::isRaidEligible,
                false,
                selectedByPool,
                random
            );
            if (selectedType == null || !donorReserves.trySpawn(selectedType)) {
                return;
            }
            composition.add(selectedType, 1);
        }
    }

    private static void refundComposition(HiveLocationReserves reserves, EntityReserves composition) {
        for (var type : new ArrayList<>(composition.getAvailableEntityTypes())) {
            var count = composition.getCount(type);
            if (count <= 0) {
                continue;
            }
            reserves.addReturningMember(type, count);
            composition.add(type, -count);
        }
    }

    private static @Nullable EntityType<?> drainOneHarbinger(HiveLocationReserves reserves) {
        for (var type : reserves.getAvailableEntityTypes()) {
            if (type.is(AlienEntityTypeTags.HARBINGERS) && reserves.trySpawn(type)) {
                return type;
            }
        }
        return null;
    }

    private static int eligibleNonHarbingerReserveCount(HiveLocationReserves reserves) {
        var count = 0;
        for (var type : reserves.getAvailableEntityTypes()) {
            if (isRaidEligible(type) && !type.is(AlienEntityTypeTags.HARBINGERS)) {
                count += reserves.getCount(type);
            }
        }
        return count;
    }

    static boolean isRaidEligible(EntityType<?> type) {
        if (!type.is(AlienEntityTypeTags.XENOMORPHS)) {
            return false;
        }
        if (type.is(AlienEntityTypeTags.HARBINGERS)) {
            return true;
        }
        if (type.is(AlienEntityTypeTags.WARRIORS)) {
            return true;
        }
        if (type.is(AlienEntityTypeTags.PROWLERS)) {
            return true;
        }
        var recipe = HiveRecipeRegistry.forOutputEntity(type);
        return recipe != null && recipe.scourgeJelly() > 0;
    }
}

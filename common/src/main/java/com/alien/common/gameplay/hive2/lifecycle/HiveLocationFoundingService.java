package com.alien.common.gameplay.hive2.lifecycle;

import com.alien.Alien;
import com.alien.common.gameplay.entity.living.alien.xenomorph.queen.Queen;
import com.alien.common.gameplay.hive2.faction.LineageFactionData;
import com.alien.common.gameplay.hive2.faction.VariantFactionRegistry;
import com.alien.common.gameplay.hive2.id.HiveLocationId;
import com.alien.common.gameplay.hive2.id.HiveLocationIds;
import com.alien.common.gameplay.hive2.id.LineageIds;
import com.alien.common.gameplay.hive2.location.HiveLocation;
import com.alien.common.gameplay.hive2.location.HiveLocationRegistry;
import com.alien.common.registry.init.AlienFactionDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

/**
 * Production-path founding for the new hive system. Two entry points:
 * <ul>
 * <li>{@link #foundNewLineage} — the queen is a forager on fresh ground. Mints a new lineage faction (parented to her
 * variant faction) and her first location.</li>
 * <li>{@link #foundNewLocation} — the queen is already in a lineage and has settled within its spread zone. Just adds
 * another location to that existing lineage.</li>
 * </ul>
 * <p>
 * Either path: claims the queen's chunk, registers the location with {@link HiveLocationRegistry}, sets
 * {@code founderId = queen.uuid}, and adds the queen to the lineage's BLib membership.
 * <p>
 * Implements the rules from {@code HIVE_REDESIGN_02_FACTION_LIFECYCLES.md} § 2 and
 * {@code HIVE_REDESIGN_03_LOCATIONS.md} § 9. Empress emergence (if the lineage now hits 2+ locations) is parked for
 * Phase 10; this phase only sets the {@code pendingEmpressEmergence} flag so Phase 10's listener can pick it up.
 */
public final class HiveLocationFoundingService {

    private HiveLocationFoundingService() {}

    /**
     * Mints a new {@link LineageFactionData} (parented to the queen's variant faction) and her first
     * {@link HiveLocation} at {@code position}. Returns the new location id.
     */
    public static HiveLocationId foundNewLineage(Queen queen, BlockPos position) {
        var level = queen.level();
        var dimension = level.dimension();
        var variant = queen.getVariant();

        var variantFaction = VariantFactionRegistry.getOrCreate(variant);
        var lineageId = LineageIds.create();
        var lineageFaction = Alien.MOD.factions().getOrCreate(lineageId, AlienFactionDataTypes.LINEAGE);
        var lineageData = lineageFaction.data();

        if (lineageData == null) {
            throw new IllegalStateException("LineageFactionData was null after getOrCreate for " + lineageId);
        }

        lineageData.setVariant(variant);
        lineageData.setParentVariantFactionId(variantFaction.id());
        lineageData.setDimension(dimension);
        lineageData.setFounderId(queen.getUUID());

        var locationId = mintLocation(queen, lineageId, position, level.getGameTime(), lineageData);

        // Add the queen to the new lineage's membership. addEntity is idempotent.
        lineageFaction.membership().addEntity(queen);

        Alien.LOGGER.info(
            "Hive2: queen {} founded new lineage {} at {} (variant {}) with first location {}",
            queen.getUUID(),
            lineageId,
            position,
            variant,
            locationId
        );

        return locationId;
    }

    /**
     * Adds a new {@link HiveLocation} to an existing lineage at {@code position}. Returns the new location id. If the
     * lineage now has 2+ locations, sets the {@code pendingEmpressEmergence} flag for Phase 10.
     */
    public static HiveLocationId foundNewLocation(Queen queen, ResourceLocation lineageFactionId, BlockPos position) {
        var level = queen.level();
        var faction = Alien.MOD.factions().get(lineageFactionId);

        if (faction == null || !(faction.data() instanceof LineageFactionData lineageData)) {
            throw new IllegalStateException("Lineage " + lineageFactionId + " missing or wrong type at founding time");
        }

        var locationId = mintLocation(queen, lineageFactionId, position, level.getGameTime(), lineageData);

        // Idempotent — queen may or may not already be a member.
        faction.membership().addEntity(queen);

        if (lineageData.locationsById().size() >= 2 && lineageData.empressId() == null) {
            // Phase 10 will pick this up and run the empress emergence ritual.
            lineageData.setPendingEmpressEmergence(true);
        }

        Alien.LOGGER.info(
            "Hive2: queen {} founded location {} in existing lineage {} at {} (lineage now has {} locations)",
            queen.getUUID(),
            locationId,
            lineageFactionId,
            position,
            lineageData.locationsById().size()
        );

        return locationId;
    }

    private static HiveLocationId mintLocation(
        Queen queen,
        ResourceLocation lineageFactionId,
        BlockPos position,
        long currentGameTime,
        LineageFactionData lineageData
    ) {
        var locationId = HiveLocationIds.create();
        var centerChunk = new ChunkPos(position);
        var location = new HiveLocation(
            locationId,
            lineageFactionId,
            queen.level().dimension(),
            position,
            queen.getUUID()
        );

        location.claimedChunks().add(centerChunk);
        location.chunkClaimTicks().put(centerChunk, currentGameTime);

        lineageData.addLocation(location);
        HiveLocationRegistry.INSTANCE.register(location);

        return locationId;
    }

    /**
     * Convenience wrapper: looks at the {@link SpreadZoneResult} and runs the matching founding action. Returns the new
     * location id when something was minted, or null when blocked.
     */
    public static @Nullable HiveLocationId foundFromResult(Queen queen, BlockPos position, SpreadZoneResult result) {
        if (result instanceof SpreadZoneResult.NewLineage) {
            return foundNewLineage(queen, position);
        }

        if (result instanceof SpreadZoneResult.NewLocation newLocation) {
            return foundNewLocation(queen, newLocation.lineageFactionId(), position);
        }

        // Blocked — caller already knows why; nothing to do.
        return null;
    }
}
